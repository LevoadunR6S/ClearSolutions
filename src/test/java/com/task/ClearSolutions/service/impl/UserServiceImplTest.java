package com.task.ClearSolutions.service.impl;

import com.task.ClearSolutions.exception.InvalidUserException;
import com.task.ClearSolutions.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceImplTest {

    @Mock
    private UserServiceImpl userService;
    private User correctOne;
    private User correctTwo;
    private User incorrectUser;
    private User userWithMissingFields;
    private Map<String, User> userMap;

    private AutoCloseable autoCloseable;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl();
        final int minAge = 18;
        correctOne = new User("VLegunLviv", "xxxxxx@gmail.com",
                "Volodymyr", "Legun", LocalDate.now().minusYears(minAge),
                "Lviv", "xxxxxxxx");
        correctTwo = new User("xx", "xxxxxx@gmail.com",
                "Vladislav", "Legun", LocalDate.now().minusYears(50),
                "Lviv", "XXXXXXX");
        incorrectUser = new User("XXXXX", "xxxxxxxxxx",
                "XXXXX", "XXXX", LocalDate.now().plusYears(minAge + 1),
                null, "xxxxxxxx");
        userWithMissingFields = new User("VLegunLviv", null,
                "Volodymyr", "Legun", LocalDate.now().minusYears(40),
                "Lviv", "xxxxxxxx");
        userMap = userService.userMap;
        userMap.put(correctOne.getId(), correctOne);
        userMap.put(correctTwo.getId(),correctTwo);
        userMap.put(incorrectUser.getId(),incorrectUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getUsers_WithArguments() {
        LocalDate from = LocalDate.now().minusYears(30);
        LocalDate to = LocalDate.now().minusYears(10);
        assertThat(userService.getUsers(from, to)).containsOnly(correctOne);
    }

    @Test
    void getUsers_WithNullArguments() {
        assertThat(userService.getUsers(null, null)).containsOnly(correctOne,correctTwo,incorrectUser);
    }

    @Test
    void getUsers_WithSecondArgument() {
        LocalDate to = LocalDate.now().minusYears(30);
        assertThat(userService.getUsers(null, to)).containsOnly(correctTwo);
    }

    @Test
    void getUsers_WithFirstArgument() {
        LocalDate from = LocalDate.now().minusYears(30);
        assertThat(userService.getUsers(from, null)).containsOnly(correctOne,incorrectUser);
    }

    @Test
    void getUsers_WithIllegalArguments() {
        LocalDate from = LocalDate.now().minusYears(5);
        LocalDate to = LocalDate.now().minusYears(10);
        assertThrows(IllegalArgumentException.class,
                ()->userService.getUsers(from,to));
    }


    @Test
    void createNewUser_CorrectInput() {
        assertThat(userService.createNewUser(correctOne)).isEqualTo(correctOne);
    }

    @Test
    void createNewUser_IncorrectInput() {
        assertThrows(InvalidUserException.class, () -> {
            userService.createNewUser(incorrectUser);
        });
    }

    @Test
    void createNewUser_MissingRequiredFields() {
        assertThrows(NullPointerException.class,
                () -> userService.createNewUser(userWithMissingFields));
    }


    @Test
    void updateFullUser() {
        assertThat(userService.updateFullUser(correctOne.getId(), correctTwo))
                .isEqualTo(correctOne);
        assertThat(correctTwo.getId()).isEqualTo(userService.userMap.get("VLegunLviv").getId());
    }

    @Test
    void updatePartOfUser() {
        assertThat(userService.updatePartOfUser(correctOne.getId(), correctTwo))
                .isEqualTo(correctOne);
        assertThat(correctOne.getFirstName())
                .isEqualTo(correctTwo.getFirstName());
    }

    @Test
    void getUserById() {
        assertThat(userService.getUserById("VLegunLviv")).isEqualTo(correctOne);
    }


    @Test
    void deleteUserById_InMap() {
        assertThat(userService.deleteUserById("VLegunLviv")).isEqualTo("Success");
    }

    @Test
    void deleteUserById_NotInMap() {
        assertThat(userService.deleteUserById("Doesn't exist")).isEqualTo("Success");
    }
}