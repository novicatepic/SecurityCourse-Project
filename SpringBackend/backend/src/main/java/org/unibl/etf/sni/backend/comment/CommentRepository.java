package org.unibl.etf.sni.backend.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Integer> {

    List<CommentModel> findByRoomId(Integer roomId);

}
