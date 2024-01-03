package org.unibl.etf.sni.backend.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.waf.WAFService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private WAFService wafService;

    @GetMapping("/comments/{roomId}")
    public ResponseEntity<List<CommentModel>> commentsForRoom(@PathVariable("roomId") Integer roomId) {

        //hashing problems
        /*if(!wafService.checkNumberLength(roomId)) {
            return BadEntity.returnBadRequst();
        }*/

        return new ResponseEntity<>(roomService.getCommentsForRoom(roomId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RoomModel>> getRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomModel> getRoomById(@PathVariable("roomId") Integer roomId) throws NotFoundException {

        //hashing problems
        /*if(!wafService.checkNumberLength(roomId)) {
            return BadEntity.returnBadRequst();
        }*/

        return new ResponseEntity<>(roomService.getRoomById(roomId), HttpStatus.OK);
    }

}
