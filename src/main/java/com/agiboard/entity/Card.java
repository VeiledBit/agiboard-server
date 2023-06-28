package com.agiboard.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue
    @Column(name = "card_id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "column_id", referencedColumnName = "column_id", nullable = false)
    private BoardColumn boardColumn;

    public Card() {
    }

    public Card(String name, BoardColumn boardColumn) {
        this.name = name;
        this.boardColumn = boardColumn;
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

    public void setBoardColumn(BoardColumn column) {
        this.boardColumn = column;
    }
}
