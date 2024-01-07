package org.unibl.etf.sni.backend.room;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.waf.WAFService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

//@CrossOrigin("*")
@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private WAFService wafService;

    @GetMapping("/comments/{roomId}")
    public ResponseEntity<List<CommentModel>> commentsForRoom(@PathVariable("roomId") Integer roomId
            , HttpServletRequest request)  {

        if(!wafService.checkNumberLength(roomId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(roomService.getCommentsForRoom(roomId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RoomModel>> getRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomModel> getRoomById(@PathVariable("roomId") Integer roomId, HttpServletRequest request) throws NotFoundException {

        if(!wafService.checkNumberLength(roomId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(roomService.getRoomById(roomId), HttpStatus.OK);
    }

}
