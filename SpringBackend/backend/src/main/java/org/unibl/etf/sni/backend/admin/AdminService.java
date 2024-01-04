package org.unibl.etf.sni.backend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.comment.CommentRepository;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.mail.MailService;
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

    public UserModel configureUser(UserModel user) {
        return userRepository.save(user);
    }

    public CommentModel configureComment(CommentModel comment) {
        return commentRepository.save(comment);
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
        return users.stream().filter((x) -> x.getId()!=adminId && !x.getActive() && !x.getIsTerminated()).toList();
    }

    public UserModel configureUserEnabled(UserModel usr) throws NotFoundException  {
        UserModel user = userRepository.findById(usr.getId()).orElseThrow(NotFoundException::new);

        user.setActive(true);
        user.setIsTerminated(false);
        user.setRole(usr.getRole());

        mailService.sendEmail(user.getEmail(), "Account activation notification",
                    "Your account with username " + user.getUsername() + " activated!");
        return userRepository.save(user);
    }


    public UserModel configureUserDisabled(Integer userId) throws NotFoundException  {
        UserModel user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        user.setActive(false);
        user.setIsTerminated(true);
        mailService.sendEmail(user.getEmail(), "Account activation notification",
                    "Your account with username " + user.getUsername() + " activated!");
        return userRepository.save(user);
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
