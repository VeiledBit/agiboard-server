package com.agiboard.service;

import com.agiboard.entity.Board;
import com.agiboard.entity.BoardColumn;
import com.agiboard.entity.Card;
import com.agiboard.entity.User;
import com.agiboard.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CardServiceImplIntegrationTest {

    private CardServiceInterface cardServiceInterface;

    @Mock
    private CardRepository cardRepository;

    @BeforeEach
    public void setUp() {
        cardServiceInterface = new CardServiceInterfaceImpl(cardRepository);
    }

    @Test
    public void whenSavingValidCard_thenSaveCard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        Card card = new Card("card", column);

        cardServiceInterface.save(card);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    public void whenFindingByValidId_thenReturnCard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        Card card = new Card("card", column);

        when(cardServiceInterface.findById(card.getId())).thenReturn(card);
    }

    @Test
    public void whenFindingAllByValidColumnId_thenReturnCard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        Card card = new Card("card", column);
        Card card2 = new Card("card 2", column);

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);

        when(cardServiceInterface.findByBoardColumn_Id(column.getId())).thenReturn(cards);
    }

    @Test
    public void whenDeletingByValidCard_thenDeleteCard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        Card card = new Card("card", column);

        cardServiceInterface.delete(card);
        verify(cardRepository, times(1)).delete(card);
    }
}
