package com.agiboard.repository;

import com.agiboard.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ColumnRepository extends JpaRepository<BoardColumn, Integer> {
    BoardColumn findById(UUID id);
    List<BoardColumn> findAllByBoardIdOrderByOrder(UUID id);
}
