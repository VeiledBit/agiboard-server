package com.agiboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "boards")
public class Board implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "board_id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<BoardColumn> columns = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_boards",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> boardMembers;

    public Board() {
    }

    public Board(String name, User user) {
        this.name = name;
        this.user = user;
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
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public List<BoardColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<BoardColumn> boardColumns) {
        this.columns = boardColumns;
    }

    @JsonIgnore
    public Set<User> getBoardMembers() {
        return boardMembers;
    }

    public void setBoardMembers(Set<User> boardMembers) {
        this.boardMembers = boardMembers;
    }
}
