package org.unibl.etf.sni.backend.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.comment.CommentModel;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/comments/{roomId}")
    public ResponseEntity<List<CommentModel>> commentsForRoom(@PathVariable("roomId") Integer roomId) {
        return new ResponseEntity<>(roomService.getCommentsForRoom(roomId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RoomModel>> getRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

}
