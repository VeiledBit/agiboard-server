package com.agiboard.service;

import com.agiboard.entity.BoardColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ColumnServiceInterface {
    BoardColumn findById(UUID id);
    List<BoardColumn> findAllByBoardIdOrderByOrder(UUID id);
    void save(BoardColumn column);
    void saveAll(ArrayList<BoardColumn> boardColumns);
    void delete(BoardColumn column);
}
