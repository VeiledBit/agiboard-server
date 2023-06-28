package com.agiboard.controller;

import com.agiboard.dto.BoardDTO;
import com.agiboard.entity.Board;
import com.agiboard.entity.User;
import com.agiboard.service.BoardServiceInterface;
import com.agiboard.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "${CORS}", maxAge = 3600)
@RequestMapping(value = "/api/boards")
public class BoardController {

    final UserServiceInterface userServiceInterface;
    final BoardServiceInterface boardServiceInterface;
    private final EntityManager entityManager;

    @Autowired
    public BoardController(UserServiceInterface userServiceInterface, BoardServiceInterface boardServiceInterface, EntityManager entityManager) {
        this.userServiceInterface = userServiceInterface;
        this.boardServiceInterface = boardServiceInterface;
        this.entityManager = entityManager;
    }

    @GetMapping
    public ResponseEntity<ArrayList<Board>> getAllBoardsForUser(@RequestParam("username") String username) {
        entityManager.clear();
        User user = userServiceInterface.findOne(username);
        Set<Board> boards = user.getBoardsMember();

        ArrayList<Board> sortedBoards = new ArrayList<>(boards);
        sortedBoards.sort((board1, board2) -> board1.getName().compareToIgnoreCase(board2.getName()));
        return ResponseEntity.status(200).body(sortedBoards);
    }

    @PostMapping(value = "/saveBoard", consumes = "application/json")
    public ResponseEntity<ArrayList<Board>> saveBoard(@RequestBody BoardDTO payload) {
        String boardName = payload.getBoardName();
        String username = payload.getUsername();

        User user = userServiceInterface.findOne(username);

        Board board = new Board();
        board.setName(boardName.trim());
        board.setColumns(new ArrayList<>());
        board.setUser(user);
        board.setBoardMembers(new HashSet<>());
        board.getBoardMembers().add(user);
        user.getBoardsMember().add(board);

        boardServiceInterface.save(board);
        userServiceInterface.save(user);
        return ResponseEntity.status(201).body(getAllBoardsForUser(username).getBody());
    }

    @PutMapping(value = "/updateBoard", consumes = "application/json")
    public ResponseEntity<ArrayList<Board>> updateBoard(@RequestBody BoardDTO payload) {
        String boardId = payload.getBoardId();
        String boardName = payload.getBoardName();
        String username = payload.getUsername();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));

        board.setName(boardName.trim());
        boardServiceInterface.save(board);
        return ResponseEntity.status(200).body(getAllBoardsForUser(username).getBody());
    }

    @PutMapping(value = "/addUserToBoard", consumes = "application/json")
    public ResponseEntity<Void> addUserToBoard(@RequestBody BoardDTO payload) {
        String boardId = payload.getBoardId();
        String username = payload.getUsername();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));
        User user = userServiceInterface.findOne(username);

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        user.getBoardsMember().add(board);
        board.getBoardMembers().add(user);

        userServiceInterface.save(user);
        boardServiceInterface.save(board);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/removeUserFromBoard", consumes = "application/json")
    public ResponseEntity<Void> removeUserFromBoard(@RequestBody BoardDTO payload) {
        String boardId = payload.getBoardId();
        String username = payload.getUsername();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));
        User user = userServiceInterface.findOne(username);

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        user.getBoardsMember().remove(board);
        board.getBoardMembers().remove(user);

        userServiceInterface.save(user);
        boardServiceInterface.save(board);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteBoard", consumes = "application/json")
    public ResponseEntity<ArrayList<Board>> deleteBoard(@RequestBody BoardDTO payload) {
        String boardId = payload.getBoardId();
        String username = payload.getUsername();

        boardServiceInterface.deleteById(UUID.fromString(boardId));
        return ResponseEntity.status(200).body(getAllBoardsForUser(username).getBody());
    }
}
