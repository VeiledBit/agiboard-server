package com.agiboard.repository;

import com.agiboard.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenSavingValidUser_thenSaveUser() {
        User user = new User("Name", "username", "password");

        userRepository.saveAndFlush(user);
    }

    @Test
    @org.springframework.transaction.annotation.Transactional(propagation =
            Propagation.NOT_SUPPORTED)
    public void whenSavingInValidUser_thenThrowException() {
        User user = new User("Name", "username", null);
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(user));

        String errorMessage = "not-null property references a null or transient value";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(errorMessage));
    }

    @Test
    public void whenFindingByValidUsername_thenReturnUser() {
        User user = new User("Name", "username", "password");
        String username = user.getUsername();

        entityManager.persistAndFlush(user);

        User foundUser = userRepository.findByUsername(username);
        assertThat(foundUser.getUsername()).isEqualTo(username);
    }

    @Test
    public void whenFindingByInValidUsername_thenReturnNull() {
        User foundUser = userRepository.findByUsername(RandomStringUtils.randomAlphabetic(10));
        assertThat(foundUser).isNull();
    }

    @Test
    public void whenDeletingByValidUsername_thenDeleteUser() {
        User user = new User("Name", "username", "password");
        String username = user.getUsername();

        entityManager.persistAndFlush(user);
        userRepository.delete(user);

        User foundUser = userRepository.findByUsername(username);
        assertThat(foundUser).isNull();
    }
}
