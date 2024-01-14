package org.unibl.etf.sni.backend.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.exception.NotFoundException;

@Service
public class CommentService {

    @Autowired
    private CommentRepository repository;


    public CommentModel findCommentById(Integer id) throws NotFoundException {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public CommentModel createComment(CommentModel commentModel) {
        return repository.save(commentModel);
    }

    public CommentModel updateComment(CommentModel commentModel) {
        commentModel.setEnabled(false);
        return repository.save(commentModel);
    }

    public SuccessOperation deleteComment(Integer id) {
        repository.deleteById(id);
        return new SuccessOperation(true, "Successfully deleted comment with id " + id);
    }

}
