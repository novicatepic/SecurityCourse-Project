package org.unibl.etf.sni.backend.waf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.authorization.AuthorizeRequests;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.Status;
import org.unibl.etf.sni.backend.permission.UserRoomPermissionEntity;
import org.unibl.etf.sni.backend.permission.UserRoomPermissionService;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.siem.SIEMService;

import java.security.cert.Certificate;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

@Service
public class WAFService {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 10_000_000;

    Certificate accessControllerCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.acAlias);
    Certificate wafCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.wafAlias);
    Certificate siemCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.siemAlias);

    @Autowired
    private SIEMService siemService;

    @Autowired
    private UserRoomPermissionService userRoomPermissionService;

    public Boolean checkNumberLength(Integer number, String route, byte[] numberByte) throws Exception {

        if(!checkDigitallySignedMessage(number.toString(), route, numberByte).equals(ProtocolMessages.OK)) {
            return false;
        }

        if(number < MIN_LENGTH || number > MAX_LENGTH) {

            return false;
        }

        return true;
    }

    public ProtocolMessages checkDigitallySignedMessage(String messageStr, String route, byte[] message) throws Exception  {
        if(!Validator.checkMessageValidity(messageStr, message, accessControllerCertificate)) {
            byte[] mssg = createDigitalSignature(messageStr, CertificateAliasResolver.wafAlias);
            siemService.checkDigitallySignedMessage(messageStr, mssg, route, Status.CHANGED);
            return ProtocolMessages.NOT_OK;
        } else {
            byte[] mssg = createDigitalSignature(messageStr, CertificateAliasResolver.wafAlias);
            if(!siemService.checkDigitallySignedMessage(messageStr, mssg, route, Status.CORRECT).equals(ProtocolMessages.OK)) {
                return ProtocolMessages.NOT_OK;
            } else {
                return ProtocolMessages.OK;
            }
        }
    }


    public Boolean checkStringValidity(String request) {

        if(!checkMySQLInjection(request)) {
            return false;
        }

        if(!checkXSSInjection(request)) {
            return false;
        }

        return false;
    }

    public Boolean authorizeUserId(Integer userId) {
        if(!AuthorizeRequests.checkIdValidity(userId)) {
            return false;
        }
        return true;
    }

    public Boolean authorizeUserProfileRequests() {
        if(!AuthorizeRequests.checkRoleValidity("ROLE_ADMIN")) {
            return false;
        }
        return true;
    }

    public Boolean authorizeCommentsEnablingDisabling(Integer userId) {
        Boolean userAuthorization = authorizeUserId(userId);
        //can't modify your own permission
        if(userAuthorization) {
            return false;
        }
        return authorizeCommentModification();
    }

    public Boolean authorizePermissionRequests() {
        return authorizeUserProfileRequests();
    }

    public Boolean authorizePermissionModification(Integer userId) {
        Boolean userAuthorization = authorizeUserId(userId);
        //can't modify your own permission
        if(userAuthorization) {
            return false;
        }
        return authorizeUserProfileRequests();
    }

    public Boolean authorizeCommentModification() {
        if(!AuthorizeRequests.checkRoleValidity("ROLE_ADMIN") && !AuthorizeRequests.checkRoleValidity("ROLE_MODERATOR")) {
            return false;
        }
        return true;
    }

    public Boolean authorizeCommentCUD() {
        if(!AuthorizeRequests.checkRoleValidity("ROLE_ADMIN") &&
                !AuthorizeRequests.checkRoleValidity("ROLE_MODERATOR") &&
                !AuthorizeRequests.checkRoleValidity("ROLE_FORUM")) {
            return false;
        }
        return true;
    }

    public Boolean authorizeCreationUserPermissionsForRoomAndComment(Integer roomId, Integer userId) throws NotFoundException {
        UserRoomPermissionEntity userRoomPermissionEntity = userRoomPermissionService.getPermissionForRoomAndUser(userId, roomId);
        return userRoomPermissionEntity.getCanCreate();
    }

    public Boolean authorizeDeleteUserPermissionsForRoomAndComment(Integer roomId, Integer userId) throws NotFoundException {
        UserRoomPermissionEntity userRoomPermissionEntity = userRoomPermissionService.getPermissionForRoomAndUser(userId, roomId);
        return userRoomPermissionEntity.getCanDelete();
    }

    public Boolean authorizeUpdateUserPermissionsForRoomAndComment(Integer roomId, Integer userId) throws NotFoundException {
        UserRoomPermissionEntity userRoomPermissionEntity = userRoomPermissionService.getPermissionForRoomAndUser(userId, roomId);
        return userRoomPermissionEntity.getCanUpdate();
    }

    private Boolean checkMySQLInjection(String request) {

        for (String keyword : SQLProblemKeywords.returnSQLKeywords()) {
            if (request.toUpperCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }



    private Boolean checkXSSInjection(String request) {

        for (String keyword : XSSProblemKeywords.returnXSSPatterns()) {
            if (request.toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    /*public LogModel logAction(String message) {
        return siemService.logAction(message);
    }*/

}
