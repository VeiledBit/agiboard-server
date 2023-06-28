package com.agiboard.service;

import com.agiboard.entity.BoardColumn;
import com.agiboard.repository.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ColumnServiceInterfaceImpl implements ColumnServiceInterface {
    final ColumnRepository columnRepository;

    @Autowired
    public ColumnServiceInterfaceImpl(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }

    @Override
    public BoardColumn findById(UUID id) {
        return columnRepository.findById(id);
    }

    @Override
    public List<BoardColumn> findAllByBoardIdOrderByOrder(UUID id) {
        return columnRepository.findAllByBoardIdOrderByOrder(id);
    }

    @Override
    public void save(BoardColumn column) {
        columnRepository.save(column);
    }

    @Override
    public void saveAll(ArrayList<BoardColumn> boardColumns) {
        columnRepository.saveAll(boardColumns);
    }

    @Override
    public void delete(BoardColumn column) {
        columnRepository.delete(column);
    }
}
