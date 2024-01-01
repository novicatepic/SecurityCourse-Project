package org.unibl.etf.sni.backend.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.comment.CommentRepository;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CommentRepository commentRepository;

    public List<CommentModel> getCommentsForRoom(Integer roomId) {
        return commentRepository.findByRoomId(roomId);
    }

}
