package com.task.ClearSolutions.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task.ClearSolutions.exception.InvalidUserException;
import com.task.ClearSolutions.model.User;
import com.task.ClearSolutions.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(Controller.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    private User one;
    private User two;
    private User incorrectUser;


    @BeforeEach
    void setUp() {
        one = new User("VLegunLviv", "xxxxxx@gmail.com",
                "Volodymyr", "Bidyuk", LocalDate.now().minusYears(30),
                "Lviv", "xxxxxxxx");
        two = new User("VLegunLviv", "xxxxxx@gmail.com",
                "Liubomir", "Bezrukyi", LocalDate.now().minusYears(19),
                "Lviv", "XXXXXXX");
        incorrectUser = new User("XXXXX", "xxxxxxxxxx",
                "Volodymyr", "", LocalDate.now().minusYears(5),
                null, "xxxxxxxx");

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getAllUserDetails() throws Exception {
        when(userService.getUsers(LocalDate.now().minusYears(40),
                LocalDate.now().minusYears(20)))
                .thenReturn(Collections.singletonList(one));
        mockMvc.perform(get("/test/api/v1/users"))
                .andExpect(status().isOk());
    }


    @Test
    void createUser_CorrectInput() throws Exception {
        String requestJson = toJson(one);

        when(userService.createNewUser(one)).thenReturn(one);
        mockMvc.perform(post("/test/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(201));
    }

    @Test
    public void createUser_InvalidUser() throws Exception {
        String requestJson = toJson(incorrectUser);
        when(userService.createNewUser(incorrectUser))
                .thenThrow(new InvalidUserException("Invalid user"));

        mockMvc.perform(post("/test/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateAllUserFields_ValidUser() throws Exception {
        String requestJson = toJson(two);
        when(userService.updateFullUser(one.getId(), two)).thenReturn(one);

        mockMvc.perform(put("/test/api/v1/users/VLegunLviv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void updateAllUserFields_InvalidUser() throws Exception {
        String requestJson = toJson(two);
        when(userService.updateFullUser(one.getId(), two)).thenReturn(one);

        mockMvc.perform(put("/test/api/v1/users/VLegunLviv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void updateSomeUserFields() throws Exception {
        String requestJson = toJson(two);
        when(userService.updatePartOfUser(one.getId(), two)).thenReturn(one);

        mockMvc.perform(patch("/test/api/v1/users/VLegunLviv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void getUserDetails() throws Exception {
        when(userService.getUserById("VLegunLviv")).thenReturn(one);

        mockMvc.perform(get("/test/api/v1/users/VLegunLviv"))
                .andExpect(status().isOk());

    }

    @Test
    void deleteUser() throws Exception {
        when(userService.deleteUserById("VLegunLviv")).thenReturn("Success");

        mockMvc.perform(delete("/test/api/v1/users/VLegunLviv"))
                .andExpect(status().isOk());
    }


    private String toJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            mapper.registerModule(new JavaTimeModule());
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}