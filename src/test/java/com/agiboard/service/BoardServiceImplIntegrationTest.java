package com.agiboard.service;

import com.agiboard.entity.Board;
import com.agiboard.entity.User;
import com.agiboard.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class BoardServiceImplIntegrationTest {

    private BoardServiceInterface boardServiceInterface;

    @Mock
    private BoardRepository boardRepository;

    @BeforeEach
    public void setUp() {
        boardServiceInterface = new BoardServiceInterfaceImpl(boardRepository);
    }

    @Test
    public void whenSavingValidBoard_thenSaveBoard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);

        boardServiceInterface.save(board);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    public void whenFindingByValidId_thenReturnBoard() {
        User user = new User("Name", "username", "password");
        Board board = new Board("board", user);

        when(boardServiceInterface.findById(board.getId())).thenReturn(board);
    }
}
