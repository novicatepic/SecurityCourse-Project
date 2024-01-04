package org.unibl.etf.sni.backend.waf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.authorization.AuthorizeRequests;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.comment.CommentService;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.LogService;
import org.unibl.etf.sni.backend.log.Status;
import org.unibl.etf.sni.backend.permission.UserRoomPermissionEntity;
import org.unibl.etf.sni.backend.permission.UserRoomPermissionService;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.siem.SIEMService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.sql.Date;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

@Service
public class WAFService {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 10_000_000;

    public static Certificate accessControllerCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.acAlias);
    public static Certificate wafCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.wafAlias);
    public static Certificate siemCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.siemAlias);

    @Autowired
    private SIEMService siemService;

    @Autowired
    private UserRoomPermissionService userRoomPermissionService;

    @Autowired
    private CommentService commentService;

    //Direct writting if message is bad
    @Autowired
    private LogService logService;

    public Boolean checkNumberLength(Integer number, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {
        Boolean x = checkRequestValidity(number.toString(), route, numberByte);
        if (x != null) return x;

        if(number < MIN_LENGTH || number > MAX_LENGTH) {
            String message = "Integer parameter for route " + route + " too long or too short!";
            dangerousActionWritter(message);
            return false;
        }

        System.out.println("Fine");

        return true;
    }

    private void dangerousActionWritter(String message) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {
        byte[] mssg = createDigitalSignature(message, CertificateAliasResolver.wafAlias);
        siemService.writeDangerousAction(message, mssg);
    }



    public byte[] authorizeCommentsEnablingDisabling(Integer userId, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(userId.toString(), route, numberByte);
        if (x != null) return returnBadMessage();

        Boolean userAuthorization = authorizeUserIdInternal(userId);
        //can't modify your own permission
        if(userAuthorization) {
            String message = "User with id " + userId + " tried to enable his own comment!";
            dangerousActionWritter(message);
            return returnBadMessage();
        }
        return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
    }

    public byte[] authorizeUserId(Integer userId, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(userId.toString(), route, numberByte);
        if (x != null) return returnBadMessage();

        Boolean userAuthorization = authorizeUserIdInternal(userId);
        //can't modify your own permission
        if(!userAuthorization) {
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



    public byte[] checkObjectValidity(String object, String route, byte[] byteObject) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(object, route, byteObject);
        if (x != null) return returnBadMessage();

        if(checkMySQLInjection(object)) {
            returnBadMessage();
        }

        if(checkXSSInjection(object)) {
            return returnBadMessage();
        }

        return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
    }

    public byte[] authorizePermissionModification(Integer userId, String route, byte[] numberByte) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, InvalidKeyException {
        Boolean x = checkRequestValidity(userId.toString(), route, numberByte);
        if (x != null) {
            System.out.println("a");
            return returnBadMessage();
        }

        Boolean userAuthorization = authorizeUserIdInternal(userId);
        //can't modify your own permission
        if(!userAuthorization) {
            System.out.println("b");
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
        //System.out.println("a");
        Boolean x = checkRequestValidity(roomId.toString(), route, roomIdByte);
        if (x != null) {
            return returnBadMessage();
        }
        //System.out.println("b");
        Boolean y = checkRequestValidity(userId.toString(), route, userIdByte);
        if (y != null) {
            return returnBadMessage();
        }
        //System.out.println("c");

        UserRoomPermissionEntity userRoomPermissionEntity = userRoomPermissionService.getPermissionForRoomAndUser(userId, roomId);
        if(userRoomPermissionEntity.getCanCreate()) {
            System.out.println("Can create");
            return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.wafAlias);
        } else {
            return returnBadMessage();
        }

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
        System.out.println("message " + messageStr);
        if(!Validator.checkMessageValidity(messageStr, message, accessControllerCertificate)) {
            //byte[] mssg = createDigitalSignature(messageStr, CertificateAliasResolver.wafAlias);
            //siemService.checkDigitallySignedMessage(messageStr, mssg, route, Status.CHANGED);

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


    private Boolean authorizeUserIdInternal(Integer userId) {
        if(AuthorizeRequests.checkIdValidity(userId)) {
            return false;
        }
        return true;
    }

    /*public Boolean authorizeUserProfileRequests() {
        if(!AuthorizeRequests.checkRoleValidity("ROLE_ADMIN")) {
            return false;
        }
        return true;
    }*/



    /*public Boolean authorizePermissionRequests() {
        return authorizeUserProfileRequests();
    }*/


    /*public Boolean authorizeCommentCUD() {
        if(!AuthorizeRequests.checkRoleValidity("ROLE_ADMIN") &&
                !AuthorizeRequests.checkRoleValidity("ROLE_MODERATOR") &&
                !AuthorizeRequests.checkRoleValidity("ROLE_FORUM")) {
            return false;
        }
        return true;
    }*/



    public Boolean checkMySQLInjection(String request) {

        for (String keyword : SQLProblemKeywords.returnSQLKeywords()) {
            if (request.toUpperCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }



    public Boolean checkXSSInjection(String request) {

        for (String keyword : XSSProblemKeywords.returnXSSPatterns()) {
            if (request.toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }


}
