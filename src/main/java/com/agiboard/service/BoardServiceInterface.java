package com.agiboard.service;

import com.agiboard.entity.Board;

import java.util.UUID;

public interface BoardServiceInterface {
    Board findById(UUID id);
    void save(Board board);
    void deleteById(UUID id);
}
