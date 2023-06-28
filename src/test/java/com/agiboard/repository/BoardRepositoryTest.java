package com.agiboard.repository;

import com.agiboard.entity.Board;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void whenSavingValidBoard_thenSaveBoard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);

        entityManager.persistAndFlush(user);
        boardRepository.saveAndFlush(board);
    }

    @Test
    @org.springframework.transaction.annotation.Transactional(propagation =
            Propagation.NOT_SUPPORTED)
    public void whenSavingInvalidBoard_thenThrowException() {
        Board board = new Board("Name", null);
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> boardRepository.saveAndFlush(board));

        String errorMessage = "not-null property references a null or transient value";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(errorMessage));
    }

    @Test
    public void whenFindingByValidId_thenReturnBoard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);

        entityManager.persistAndFlush(user);
        UUID uuid = (UUID) entityManager.persistAndGetId(board);

        Board foundBoard = boardRepository.findById(uuid);
        assertThat(foundBoard).isEqualTo(board);
    }

    @Test
    public void whenFindingByInvalidId_ThenReturnNull() {
        Board foundBoard = boardRepository.findById(UUID.randomUUID());
        assertThat(foundBoard).isNull();
    }

    @Test
    public void whenDeletingByValidId_ThenDeleteBoard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);

        entityManager.persistAndFlush(user);
        UUID uuid = (UUID) entityManager.persistAndGetId(board);

        boardRepository.deleteById(uuid);

        Board foundBoard = boardRepository.findById(uuid);
        assertThat(foundBoard).isNull();
    }
}
