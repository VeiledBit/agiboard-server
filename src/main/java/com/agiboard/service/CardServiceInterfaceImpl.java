package com.agiboard.service;

import com.agiboard.entity.Card;
import com.agiboard.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CardServiceInterfaceImpl implements CardServiceInterface {

    final CardRepository cardRepository;

    @Autowired
    public CardServiceInterfaceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card findById(UUID id) {
        return cardRepository.findById(id);
    }

    @Override
    public List<Card> findByBoardColumn_Id(UUID id) {
        return cardRepository.findByBoardColumn_Id(id);
    }

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }
}
