package org.unibl.etf.sni.backend.waf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.authorization.AuthorizeRequests;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.code.Code;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.comment.CommentService;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.jwtconfig.TokenBlackListService;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.LogService;
import org.unibl.etf.sni.backend.log.Status;
import org.unibl.etf.sni.backend.permission.UserRoomPermissionEntity;
import org.unibl.etf.sni.backend.permission.UserRoomPermissionService;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.role.Role;
import org.unibl.etf.sni.backend.siem.SIEMService;
import org.unibl.etf.sni.backend.user.UserModel;
import org.unibl.etf.sni.backend.user.UserService;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

//waf checks for mysql injection, xss (these two based on keywords), buffer overflow
//if there is something malicious, token is blacklisted (no more access)
//it's checked whether user has right to update comment (role and if he modifies his own comment)
//it's checked whether user has right to allow registration, modify roles (can't do it for himself etc.)
@Service
public class WAFService {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 2147483645;

    public static Certificate accessControllerCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.acAlias);
    public static Certificate wafCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.wafAlias);
    public static Certificate siemCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.siemAlias);

    @Autowired
    private SIEMService siemService;

    @Autowired
    private UserRoomPermissionService userRoomPermissionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlackListService tokenBlackListService;

    private String token = "t";

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean checkNumberLength(Integer number, String route)  {

        if(number < MIN_LENGTH) {
            logService.insertNewLog("Tried to access id for less than " + MIN_LENGTH + " on route " + route, Status.DANGER);
            return false;
        } else if (number > MAX_LENGTH) {
            logService.insertNewLog("Tried to access id for more than " + MAX_LENGTH + " on route " + route, Status.DANGER);
            return false;
        }

        return true;
    }

    private void dangerousActionWritter(String message) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {
        byte[] mssg = createDigitalSignature(message, CertificateAliasResolver.wafAlias);
        siemService.writeDangerousAction(message, mssg);
    }



    public byte[] authorizeCommentsEnablingDisabling(Integer userId, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(userId.toString(), route, numberByte);
        if (x != null) return returnBadMessage();

        //System.out.println("In enabling disabling");

        Boolean userAuthorization = authorizeUserIdInternal(userId);
        //can't modify your own permission
        if(!userAuthorization) {
            //System.out.println("Doesn't work");


            String message = "User with id " + userId + " tried to enable his own comment!";
            blackListToken();
            dangerousActionWritter(message);
            return returnBadMessage();
        }
        return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
    }

    private void blackListToken() {
        if (!tokenBlackListService.isTokenBlacklisted(token)) {
            tokenBlackListService.addToBlacklist(token);
        }
    }

    public byte[] authorizeUserId(Integer userId, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(userId.toString(), route, numberByte);
        if (x != null) return returnBadMessage();

        Boolean userAuthorization = authorizeUserIdInternal(userId);
        //can't modify your own permission
        if(!userAuthorization) {
            blackListToken();
            String message = "User with id " + userId + " tried to post comment for someone else!";
            dangerousActionWritter(message);
            return returnBadMessage();
        }
        return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
    }

    public byte[] authorizePostComment(Integer userId, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(userId.toString(), route, numberByte);
        if (x != null) return returnBadMessage();

        Boolean userAuthorization = authorizeUserIdInternal(userId);
        //can't modify your own permission
        if(userAuthorization) {
            blackListToken();
            String message = "User with id " + userId + " tried to post comment for someone else!";
            dangerousActionWritter(message);
            return returnBadMessage();
        }
        return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
    }



    private Boolean checkRequestValidity(String userId, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        ProtocolMessages response = checkDigitallySignedMessage(userId, route, numberByte);
        if(response.equals(ProtocolMessages.NOT_OK_FIRST_LEVEL)) {
            writeBadRequestDirectlly(userId, route);
            return false;
        } else if(response.equals(ProtocolMessages.NOT_OK_SECOND_LEVEL)) {
            return false;
        }
        return null;
    }

    public byte[] authorizePermissionModification(Integer userId, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(userId.toString(), route, numberByte);
        if (x != null) {
            return returnBadMessage();
        }

        Boolean userAuthorization = authorizeUserIdInternal(userId);
        //can't modify your own permission
        if(!userAuthorization) {
            blackListToken();
            String message = "User with id " + userId + " tried to change his own permission!";
            dangerousActionWritter(message);
            return returnBadMessage();
        }
        return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
    }

    private byte[] returnBadMessage() throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {
        return MessageHasher.createDigitalSignature(ProtocolMessages.NOT_OK_FIRST_LEVEL.toString(), CertificateAliasResolver.wafAlias);
    }

    public byte[] authorizeCreationUserPermissionsForRoomAndComment(Integer roomId, Integer userId, String route, byte[] roomIdByte,
                                                                     byte[] userIdByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException, NotFoundException {
        Boolean x = checkRequestValidity(roomId.toString(), route, roomIdByte);
        if (x != null) {
            return returnBadMessage();
        }
        Boolean y = checkRequestValidity(userId.toString(), route, userIdByte);
        if (y != null) {
            return returnBadMessage();
        }

        UserRoomPermissionEntity userRoomPermissionEntity = userRoomPermissionService.getPermissionForRoomAndUser(userId, roomId);
        if(userRoomPermissionEntity.getCanCreate()) {
            return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
        } else {
            blackListToken();
            return returnBadMessage();
        }

    }

    private static UserModel extractCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel userDetails = (UserModel) authentication.getPrincipal();
        return userDetails;
    }

    public boolean checkAccountModification(Integer id) {
        UserModel k = extractCredentials();

        if(k.getId() != id) {
            return true;
        }
        blackListToken();
        return false;
    }

    public byte[] authorizeDeleteUserPermissionsForRoomAndComment(Integer roomId, Integer userId,
                                                                  Integer commentId, String route, byte[] roomIdByte,
                                                                   byte[] userIdByte) throws NotFoundException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(roomId.toString(), route, roomIdByte);
        if (x != null) {
            writeBadRequestDirectlly("Bad delete permissions request for room " + roomId, route);
            return returnBadMessage();
        }

        Boolean y = checkRequestValidity(userId.toString(), route, userIdByte);
        if (y != null) {
            writeBadRequestDirectlly("Bad delete permissions request for user " + userId, route);
            return returnBadMessage();
        }

        CommentModel m = commentService.findCommentById(commentId);

        UserRoomPermissionEntity userRoomPermissionEntity = userRoomPermissionService.getPermissionForRoomAndUser(userId, roomId);
        if(userRoomPermissionEntity.getCanDelete() && m.getUserId()==userId) {
            return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
        } else {
            blackListToken();
            writeBadRequestDirectlly("No permissions to delete comment, user "
                    + userId + " and comment " + commentId, route);
            return returnBadMessage();
        }
    }

    public byte[] authorizeUpdateUserPermissionsForRoomAndComment(Integer roomId, Integer userId, Integer commentId, String route, byte[] roomIdByte,
                                                                   byte[] userIdByte) throws NotFoundException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(roomId.toString(), route, roomIdByte);
        if (x != null) return returnBadMessage();

        Boolean y = checkRequestValidity(userId.toString(), route, userIdByte);
        if (y != null) return returnBadMessage();

        CommentModel m = commentService.findCommentById(commentId);
        UserRoomPermissionEntity userRoomPermissionEntity = userRoomPermissionService.getPermissionForRoomAndUser(userId, roomId);
        if(userRoomPermissionEntity.getCanUpdate() && m.getUserId()==userId) {
            return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
        } else {
            blackListToken();
            return returnBadMessage();
        }
    }

    private void writeBadRequestDirectlly(String badRequest, String route) {
        LogModel logModel = new LogModel();
        logModel.setStatus(Status.CHANGED);
        logModel.setInfo("Message for route " + route + " with id " + badRequest + " changed between admin and waf!");
        logService.insertNewLog(logModel.getInfo(), logModel.getStatus());
    }

    public ProtocolMessages checkDigitallySignedMessage(String messageStr, String route, byte[] message) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {
        if(!Validator.checkMessageValidity(messageStr, message, accessControllerCertificate)) {
            return ProtocolMessages.NOT_OK_FIRST_LEVEL;
        } else {
            byte[] mssg = createDigitalSignature(messageStr, CertificateAliasResolver.wafAlias);

            byte[] response = siemService.checkDigitallySignedMessage(messageStr, mssg, route, Status.CORRECT);

            if(Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, siemCertificate)) {
                return ProtocolMessages.OK;
            } else {
                return ProtocolMessages.NOT_OK_SECOND_LEVEL;
            }
        }
    }

    public boolean checkIfCommentIsForbidden(CommentModel commentModel) {
        if(commentModel.getForbidden()) {
            siemService.logAction("Tried to work with forbidden comment! User " + commentModel.getWriter().getUsername(), Status.DANGER);
            blackListToken();
            return true;
        }
        return false;
    }

    private Boolean authorizeUserIdInternal(Integer userId) {
        if(AuthorizeRequests.checkIdValidity(userId)) {
            //System.out.println("false");
            return false;
        }
        //System.out.println("true");
        return true;
    }

    public Boolean checkMySQLInjection(String request) {

        for (String keyword : SQLProblemKeywords.returnSQLKeywords()) {
            if (request.toUpperCase().contains(keyword)) {
                logService.insertNewLog("SQL Injection potential try for " + request, Status.DANGER);
                blackListToken();
                return true;
            }
        }

        return false;
    }

    public Boolean checkIfUserIsAdmin(Integer id) throws NotFoundException {
        UserModel user = userService.findByUserId(id);
        if(user.getRole().toString().equals(Role.ROLE_ADMIN.toString())) {
            logService.insertNewLog("Tried to change admin permissions for admin " + id, Status.DANGER);
            blackListToken();
            return true;
        }
        return false;
    }

    public Boolean checkXSSInjection(String request) {

        for (String keyword : XSSProblemKeywords.returnXSSPatterns()) {
            if (request.toLowerCase().contains(keyword)) {
                logService.insertNewLog("XSS potential try for " + request, Status.DANGER);
                blackListToken();
                return true;
            }
        }

        return false;
    }


    public void handleBadLogin(String username) {
        logService.insertNewLog("Bad login tried for username " + username, Status.DANGER);
    }

    public void handleBadCode(Code code) {
        logService.insertNewLog("Bad code entrance tried for user id" + code.getUserId(), Status.DANGER);
    }

    public void handleGoodCode(Code code) {
        logService.insertNewLog("Good code entrance for user id" + code.getUserId(), Status.CORRECT);
    }

    public void handleGoodLogin(String username) {
        logService.insertNewLog("Correct login credentials for user with username " + username, Status.CORRECT);
    }

    public void logoutUser() {
        this.tokenBlackListService.addToBlacklist(token);
    }
}
