package com.agiboard.repository;

import com.agiboard.entity.Board;
import com.agiboard.entity.BoardColumn;
import com.agiboard.entity.Card;
import com.agiboard.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void whenSavingValidCard_thenSaveCard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        Card card = new Card("card", column);

        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(board);
        entityManager.persistAndFlush(column);
        cardRepository.saveAndFlush(card);
    }

    @Test
    @org.springframework.transaction.annotation.Transactional(propagation =
            Propagation.NOT_SUPPORTED)
    public void whenSavingInvalidColumn_thenThrowException() {
        Card card = new Card("card", null);
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> cardRepository.saveAndFlush(card));

        String errorMessage = "not-null property references a null or transient value";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(errorMessage));
    }

    @Test
    public void whenFindingByValidId_thenReturnCard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        Card card = new Card("card", column);

        entityManager.persist(user);
        entityManager.persist(board);
        entityManager.persist(column);
        UUID cardId = (UUID) entityManager.persistAndGetId(card);

        Card foundCard = cardRepository.findById(cardId);
        assertThat(foundCard.getId()).isEqualTo(cardId);
    }

    @Test
    public void whenFindingByInvalidId_thenReturnNull() {
        Card foundCard = cardRepository.findById(UUID.randomUUID());

        assertThat(foundCard).isNull();
    }

    @Test
    public void whenFindingAllByValidColumnId_thenReturnCards() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        Card card = new Card("card", column);

        entityManager.persist(user);
        entityManager.persist(board);
        UUID columnId = (UUID) entityManager.persistAndGetId(column);
        entityManager.persist(card);

        List<Card> foundCards = cardRepository.findByBoardColumn_Id(columnId);
        assertThat(card).isIn(foundCards);
    }

    @Test
    public void whenFindingAllByInvalidColumnId_thenReturnEmpty() {
        List<Card> foundCards = cardRepository.findByBoardColumn_Id(UUID.randomUUID());

        assertThat(foundCards).isEmpty();
    }

    @Test
    public void whenDeletingByValidCard_thenDeleteCard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        Card card = new Card("card", column);

        entityManager.persist(user);
        entityManager.persist(board);
        entityManager.persist(column);
        UUID uuid = (UUID) entityManager.persistAndGetId(card);

        cardRepository.delete(card);

        Card foundCard = cardRepository.findById(uuid);
        assertThat(foundCard).isNull();
    }
}
