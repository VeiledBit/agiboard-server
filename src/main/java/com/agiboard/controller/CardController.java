package com.agiboard.controller;

import com.agiboard.dto.CardDTO;
import com.agiboard.entity.Board;
import com.agiboard.entity.BoardColumn;
import com.agiboard.entity.Card;
import com.agiboard.service.BoardServiceInterface;
import com.agiboard.service.CardServiceInterface;
import com.agiboard.service.ColumnServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "${CORS}", maxAge = 3600)
@RequestMapping(value = "/api/cards")
public class CardController {

    final BoardServiceInterface boardServiceInterface;
    final ColumnServiceInterface columnServiceInterface;
    final CardServiceInterface cardServiceInterface;
    final ColumnController columnController;

    @Autowired
    public CardController(BoardServiceInterface boardServiceInterface,
                          ColumnServiceInterface columnServiceInterface,
                          CardServiceInterface cardServiceInterface, ColumnController columnController) {
        this.boardServiceInterface = boardServiceInterface;
        this.columnServiceInterface = columnServiceInterface;
        this.cardServiceInterface = cardServiceInterface;
        this.columnController = columnController;
    }

    @CrossOrigin
    @PostMapping(value = "/saveCard", consumes = "application/json")
    public ResponseEntity<ArrayList<Object>> saveCard(@RequestBody CardDTO payload) {
        String boardId = payload.getBoardId();
        String columnId = payload.getColumnId();
        String cardName = payload.getCardName();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));
        BoardColumn column = columnServiceInterface.findById(UUID.fromString(columnId));

        Card card = new Card();
        card.setName(cardName.trim());
        card.setBoardColumn(column);

        cardServiceInterface.save(card);
        return ResponseEntity.status(201).body(columnController.getAllColumnsByBoardId(board.getId()).getBody());
    }

    @CrossOrigin
    @PutMapping(value = "/updateCard", consumes = "application/json")
    public ResponseEntity<ArrayList<Object>> updateCard(@RequestBody CardDTO payload) {
        String boardId = payload.getBoardId();
        String cardId = payload.getCardId();
        String cardName = payload.getCardName();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));
        Card card = cardServiceInterface.findById(UUID.fromString(cardId));

        card.setName(cardName.trim());
        cardServiceInterface.save(card);
        return ResponseEntity.status(200).body(columnController.getAllColumnsByBoardId(board.getId()).getBody());
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteCard", consumes = "application/json")
    public ResponseEntity<ArrayList<Object>> deleteCard(@RequestBody CardDTO payload) {
        String boardId = payload.getBoardId();
        String cardId = payload.getCardId();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));
        Card card = cardServiceInterface.findById(UUID.fromString(cardId));

        cardServiceInterface.delete(card);
        return ResponseEntity.status(200).body(columnController.getAllColumnsByBoardId(board.getId()).getBody());
    }
}
