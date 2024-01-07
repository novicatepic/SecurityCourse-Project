package org.unibl.etf.sni.backend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.comment.CommentRepository;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.mail.MailService;
import org.unibl.etf.sni.backend.permission.UserRoomPermissionEntity;
import org.unibl.etf.sni.backend.permission.UserRoomPermissionRepository;
import org.unibl.etf.sni.backend.role.Role;
import org.unibl.etf.sni.backend.room.RoomModel;
import org.unibl.etf.sni.backend.room.RoomRepository;
import org.unibl.etf.sni.backend.user.UserModel;
import org.unibl.etf.sni.backend.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRoomPermissionRepository userRoomPermissionRepository;

    @Autowired
    private RoomRepository roomRepository;

    /*public UserModel configureUser(UserModel user) {
        return userRepository.save(user);
    }

    public CommentModel configureComment(CommentModel comment) {
        return commentRepository.save(comment);
    }*/

    public List<UserModel> getUsersToModifyPermissions() {
        return userRepository.findAll().stream().filter(
                (x) -> !x.getRole().toString().equals(Role.ROLE_ADMIN.toString())
         && x.getActive()).toList();
    }

    public CommentModel disableComment(CommentModel comment) {
        comment.setForbidden(true);
        comment.setEnabled(false);
        return commentRepository.save(comment);
    }

    public CommentModel enableComment(CommentModel comment) {
        comment.setForbidden(false);
        comment.setEnabled(true);
        return commentRepository.save(comment);
    }

    public List<CommentModel> unprocessedComments(Integer userId) {
        return commentRepository.findAll().stream().filter((x) -> x.getUserId()!=userId && !x.getForbidden() && !x.getEnabled()).toList();
    }

    public List<UserModel> getWaitingUsers(Integer adminId) {
        List<UserModel> users = userRepository.findAll();
        return users.stream().filter((x) -> x.getId()!=adminId && !x.getActive() /*&& !x.getIsTerminated()*/).toList();
    }

    public UserModel configureUserEnabled(UserModel usr) throws NotFoundException  {
        UserModel user = userRepository.findById(usr.getId()).orElseThrow(NotFoundException::new);

        user.setActive(true);
        user.setRole(usr.getRole());

        List<RoomModel> rooms = roomRepository.findAll();


        for(RoomModel room : rooms) {
            UserRoomPermissionEntity userRoomPermissionEntity = new UserRoomPermissionEntity();
            userRoomPermissionEntity.setUserId(usr.getId());
            userRoomPermissionEntity.setRoomId(room.getId());
            if(usr.getRole().toString().equals(Role.ROLE_ADMIN.toString())) {
                //admin has all roles set to true, can't be modified later
                userRoomPermissionEntity.setCanCreate(true);
                userRoomPermissionEntity.setCanDelete(true);
                userRoomPermissionEntity.setCanUpdate(true);
                //System.out.println("done");
            } else {
                //other users everything set to false, admin can correct it later on
                userRoomPermissionEntity.setCanCreate(false);
                userRoomPermissionEntity.setCanDelete(false);
                userRoomPermissionEntity.setCanUpdate(false);
            }

            userRoomPermissionRepository.save(userRoomPermissionEntity);
        }

        mailService.sendEmail(user.getEmail(), "Account activation notification - SNI Project",
                    "Your account with username " + user.getUsername() + " activated!");
        return userRepository.save(user);
    }


    public UserModel configureUserDisabled(Integer userId) throws NotFoundException  {
        UserModel user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        user.setActive(false);
        //user.setIsTerminated(true);
        mailService.sendEmail(user.getEmail(), "Account activation notification - SNI Project",
                    "Your account with username " + user.getUsername() + " not activated, deleted request!");
        //return userRepository.save(user);
        userRepository.delete(user);
        return user;
    }

    public UserModel getUserById(Integer userId) throws NotFoundException{
        return userRepository.findById(userId).orElseThrow(NotFoundException::new);
    }

    public UserModel update(UserModel user) throws NotFoundException {
        UserModel u =  this.userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        //System.out.println("Found");
        u.setRole(user.getRole());
        return userRepository.save(u);
    }
}
