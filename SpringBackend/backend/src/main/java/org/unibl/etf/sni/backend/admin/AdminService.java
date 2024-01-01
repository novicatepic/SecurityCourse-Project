package org.unibl.etf.sni.backend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.comment.CommentRepository;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.mail.MailService;
import org.unibl.etf.sni.backend.user.UserModel;
import org.unibl.etf.sni.backend.user.UserRepository;

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

    public UserModel configureUserEnabled(Integer userId) throws NotFoundException  {
        UserModel user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        user.setActive(true);
        user.setIsTerminated(false);
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

}
