package org.unibl.etf.sni.backend.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.sni.backend.comment.CommentModel;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/comments/{roomId}")
    public ResponseEntity<List<CommentModel>> commentsForRoom(@PathVariable("roomId") Integer roomId) {
        return new ResponseEntity<>(roomService.getCommentsForRoom(roomId), HttpStatus.OK);
    }

}
