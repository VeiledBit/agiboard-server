package com.agiboard.service;

import com.agiboard.entity.Board;
import com.agiboard.entity.BoardColumn;
import com.agiboard.entity.User;
import com.agiboard.repository.ColumnRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ColumnServiceImplIntegrationTest {

    private ColumnServiceInterface columnServiceInterface;

    @Mock
    private ColumnRepository columnRepository;

    @BeforeEach
    public void setUp() {
        columnServiceInterface = new ColumnServiceInterfaceImpl(columnRepository);
    }

    @Test
    public void whenSavingValidColumn_thenSaveColumn() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);

        columnServiceInterface.save(column);
        verify(columnRepository, times(1)).save(column);
    }

    @Test
    public void whenSavingAllValidColumns_thenSaveColumns() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        BoardColumn column2 = new BoardColumn("column 2", 1, board);

        ArrayList<BoardColumn> columns = new ArrayList<>();
        columns.add(column);
        columns.add(column2);

        columnServiceInterface.saveAll(columns);
        verify(columnRepository, times(1)).saveAll(columns);
    }

    @Test
    public void whenFindingByValidId_thenReturnColumn() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);

        when(columnServiceInterface.findById(column.getId())).thenReturn(column);
    }

    @Test
    public void whenFindingAllByValidId_thenReturnColumns() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);
        BoardColumn column2 = new BoardColumn("column 2", 1, board);

        ArrayList<BoardColumn> columns = new ArrayList<>();
        columns.add(column);
        columns.add(column2);

        when(columnServiceInterface.findAllByBoardIdOrderByOrder(board.getId())).thenReturn(columns);
    }

    @Test
    public void whenDeletingByValidColumn_thenDeleteColumn() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);
        BoardColumn column = new BoardColumn("column", 1, board);

        columnServiceInterface.delete(column);
        verify(columnRepository, times(1)).delete(column);
    }
}
