package com.agiboard.controller;

import com.agiboard.dto.ColumnDTO;
import com.agiboard.entity.Board;
import com.agiboard.entity.BoardColumn;
import com.agiboard.entity.Card;
import com.agiboard.service.BoardServiceInterface;
import com.agiboard.service.CardServiceInterface;
import com.agiboard.service.ColumnServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "${CORS}", maxAge = 3600)
@RequestMapping(value = "/api/columns")
public class ColumnController {

    final BoardServiceInterface boardServiceInterface;
    final ColumnServiceInterface columnServiceInterface;
    final CardServiceInterface cardServiceInterface;

    @Autowired
    public ColumnController(BoardServiceInterface boardServiceInterface,
                            ColumnServiceInterface columnServiceInterface, CardServiceInterface cardServiceInterface) {
        this.boardServiceInterface = boardServiceInterface;
        this.columnServiceInterface = columnServiceInterface;
        this.cardServiceInterface = cardServiceInterface;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ArrayList<Object>> getAllColumnsByBoardId(@PathVariable("id") UUID boardId) {
        Map<UUID, BoardColumn> columnMap = new HashMap<>();
        Map<UUID, Card> cardMap = new HashMap<>();
        Map<String, List<UUID>> columnOrderMap = new HashMap<>();
        List<UUID> columnOrderArray = new ArrayList<>();

        List<BoardColumn> columns = columnServiceInterface.findAllByBoardIdOrderByOrder(boardId);

        for (BoardColumn column : columns) {
            columnMap.put(column.getId(), column);
            columnOrderArray.add(column.getId());

            List<Card> cards = cardServiceInterface.findByBoardColumn_Id(column.getId());
            for (Card card : cards) {
                cardMap.put(card.getId(), card);
            }
        }

        Map<String, Map<UUID, BoardColumn>> returnColumnMap = new HashMap<>();
        returnColumnMap.put("columns", columnMap);

        Map<String, Map<UUID, Card>> returnCardMap = new HashMap<>();
        returnCardMap.put("cards", cardMap);

        columnOrderMap.put("columnOrder", columnOrderArray);

        String boardName = boardServiceInterface.findById(boardId).getName();

        ArrayList<Object> returnValues = new ArrayList<>();
        returnValues.add(returnColumnMap);
        returnValues.add(returnCardMap);
        returnValues.add(columnOrderMap);
        returnValues.add(boardName);

        return ResponseEntity.status(200).body(returnValues);
    }

    @CrossOrigin
    @PutMapping(value = "/saveOrder", consumes = "application/json")
    public ResponseEntity<Void> saveOrder(@RequestBody Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        ArrayList<String> orderNew = (ArrayList<String>) payload.get("columnOrder");
        String boardId = (String) payload.get("boardId");

        ArrayList<BoardColumn> columnsByOrderOld =
                (ArrayList<BoardColumn>) columnServiceInterface.findAllByBoardIdOrderByOrder(UUID.fromString(boardId));

        ArrayList<BoardColumn> columnsByOrderNew = new ArrayList<>();
        for (String id : orderNew) {
            columnsByOrderNew.add(columnServiceInterface.findById(UUID.fromString(id)));
        }

        for (BoardColumn columnToUpdate : columnsByOrderOld) {
            columnToUpdate.setOrder(columnsByOrderNew.indexOf(columnToUpdate));
        }

        columnServiceInterface.saveAll(columnsByOrderOld);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping(value = "/saveCardToColumn", consumes = "application/json")
    public ResponseEntity<Void> saveCardToColumn(@RequestBody Map<String, Object> payload) {
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            @SuppressWarnings("unchecked")
            LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) entry.getValue();

            @SuppressWarnings("unchecked")
            ArrayList<Object> cards = (ArrayList<Object>) data.get("cards");

            if (cards.isEmpty())
                continue;
            for (Object idAsObject : cards) {
                Card card = cardServiceInterface.findById(UUID.fromString(idAsObject.toString()));
                card.setBoardColumn(columnServiceInterface.findById(UUID.fromString(entry.getKey())));
                cardServiceInterface.save(card);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/saveColumn", consumes = "application/json")
    public ResponseEntity<ArrayList<Object>> saveColumn(@RequestBody ColumnDTO payload) {
        String boardId = payload.getBoardId();
        String columnName = payload.getColumnName();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));

        ArrayList<BoardColumn> columns = (ArrayList<BoardColumn>) columnServiceInterface.findAllByBoardIdOrderByOrder(board.getId());

        BoardColumn column = new BoardColumn();
        column.setName(columnName.trim());
        column.setBoard(board);
        column.setCards(new ArrayList<>());
        column.setOrder(columns.size());

        columnServiceInterface.save(column);
        return ResponseEntity.status(201).body(getAllColumnsByBoardId(board.getId()).getBody());
    }

    @CrossOrigin
    @PutMapping(value = "/updateColumn", consumes = "application/json")
    public ResponseEntity<ArrayList<Object>> updateColumn(@RequestBody ColumnDTO payload) {
        String boardId = payload.getBoardId();
        String columnId = payload.getColumnId();
        String columnName = payload.getColumnName();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));
        BoardColumn column = columnServiceInterface.findById(UUID.fromString(columnId));

        column.setName(columnName.trim());
        columnServiceInterface.save(column);
        return ResponseEntity.status(200).body(getAllColumnsByBoardId(board.getId()).getBody());
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteColumn", consumes = "application/json")
    public ResponseEntity<ArrayList<Object>> deleteColumn(@RequestBody ColumnDTO payload) {
        String boardId = payload.getBoardId();
        String columnId = payload.getColumnId();

        Board board = boardServiceInterface.findById(UUID.fromString(boardId));
        BoardColumn column = columnServiceInterface.findById(UUID.fromString(columnId));

        columnServiceInterface.delete(column);
        return ResponseEntity.status(200).body(getAllColumnsByBoardId(board.getId()).getBody());
    }
}
