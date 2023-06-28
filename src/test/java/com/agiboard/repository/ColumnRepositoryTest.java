package com.agiboard.repository;

import com.agiboard.entity.Board;
import com.agiboard.entity.BoardColumn;
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
public class ColumnRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ColumnRepository columnRepository;

    @Test
    public void whenSavingValidColumn_thenSaveColumn() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("Name", 1, board);

        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(board);
        columnRepository.saveAndFlush(column);
    }

    @Test
    @org.springframework.transaction.annotation.Transactional(propagation =
            Propagation.NOT_SUPPORTED)
    public void whenSavingInvalidColumn_thenThrowException() {
        BoardColumn column = new BoardColumn("Name", 1, null);
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> columnRepository.saveAndFlush(column));

        String errorMessage = "not-null property references a null or transient value";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(errorMessage));
    }

    @Test
    public void whenFindingByValidId_thenReturnColumn() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);

        entityManager.persist(user);
        entityManager.persist(board);
        UUID columnId = (UUID) entityManager.persistAndGetId(column);

        BoardColumn foundColumn = columnRepository.findById(columnId);
        assertThat(foundColumn.getId()).isEqualTo(columnId);
    }

    @Test
    public void whenFindingByInvalidId_thenReturnNull() {
        BoardColumn foundColumn = columnRepository.findById(UUID.randomUUID());

        assertThat(foundColumn).isNull();
    }

    @Test
    public void whenFindingAllByValidBoardId_thenReturnColumns() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        BoardColumn column2 = new BoardColumn("column2", 2, board);

        entityManager.persist(user);
        UUID boardId = (UUID) entityManager.persistAndGetId(board);
        entityManager.persist(column);
        entityManager.persist(column2);

        List<BoardColumn> foundColumns = columnRepository.findAllByBoardIdOrderByOrder(boardId);
        assertThat(column).isIn(foundColumns);
    }

    @Test
    public void whenFindingAllByInvalidBoardId_thenReturnEmpty() {
        List<BoardColumn> foundColumns = columnRepository.findAllByBoardIdOrderByOrder(UUID.randomUUID());

        assertThat(foundColumns).isEmpty();
    }

    @Test
    public void whenDeletingByValidColumn_thenDeleteColumn() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);

        entityManager.persist(user);
        entityManager.persist(board);
        UUID uuid = (UUID) entityManager.persistAndGetId(column);

        columnRepository.delete(column);

        BoardColumn foundColumn = columnRepository.findById(uuid);
        assertThat(foundColumn).isNull();
    }
}
