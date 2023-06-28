package com.agiboard.service;

import com.agiboard.entity.Board;
import com.agiboard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BoardServiceInterfaceImpl implements BoardServiceInterface {

    final BoardRepository boardRepository;

    @Autowired
    public BoardServiceInterfaceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board findById(UUID id) {
        return boardRepository.findById(id);
    }

    @Override
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        boardRepository.deleteById(id);
    }
}
