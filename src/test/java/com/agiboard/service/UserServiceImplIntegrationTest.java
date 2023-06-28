package com.agiboard.service;

import com.agiboard.entity.User;
import com.agiboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplIntegrationTest {

    private UserServiceInterface userServiceInterface;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userServiceInterface = new UserServiceInterfaceImpl(userRepository);
    }

    @Test
    public void whenSavingValidUser_thenSaveUser() {
        User user = new User("Name", "username", "password");
        userServiceInterface.save(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void whenFindingByValidUsername_thenReturnUser() {
        User user = new User("Name", "username", "password");
        when(userServiceInterface.findOne(user.getUsername())).thenReturn(user);
    }

    @Test
    public void whenDeletingByValidUsername_thenDeleteUser() {
        User user = new User("Name", "username", "password");

        userServiceInterface.delete(user);
        verify(userRepository, times(1)).delete(user);
    }
}
