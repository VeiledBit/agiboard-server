package com.agiboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "board_columns")
public class BoardColumn {

    @Id
    @GeneratedValue
    @Column(name = "column_id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "column_order", nullable = false)
    private int order;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "boardColumn", orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    public BoardColumn() {
    }

    public BoardColumn(String name, int order, Board board) {
        this.name = name;
        this.order = order;
        this.board = board;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public UUID getBoard() {
        return board.getId();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<UUID> getCards() {
        ArrayList<UUID> cardIds = new ArrayList<>();
        for (Card card : cards) {
            cardIds.add(card.getId());
        }
        return cardIds;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
