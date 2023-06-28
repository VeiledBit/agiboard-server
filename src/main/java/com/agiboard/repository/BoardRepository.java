package com.agiboard.repository;

import com.agiboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findById(UUID id);
    void deleteById(UUID id);
}
