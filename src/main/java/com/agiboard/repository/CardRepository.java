package com.agiboard.repository;

import com.agiboard.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    Card findById(UUID id);
    List<Card> findByBoardColumn_Id(UUID id);
}
