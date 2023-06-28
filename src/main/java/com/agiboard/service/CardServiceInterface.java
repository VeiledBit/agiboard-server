package com.agiboard.service;

import com.agiboard.entity.Card;

import java.util.List;
import java.util.UUID;

public interface CardServiceInterface {
    Card findById(UUID id);
    List<Card> findByBoardColumn_Id(UUID id);
    void save(Card card);
    void delete(Card card);
}
