package com.task.ClearSolutions.controllers;

import com.task.ClearSolutions.exception.InvalidUserException;
import com.task.ClearSolutions.model.User;
import com.task.ClearSolutions.service.UserService;
import com.task.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/test/api/v1")
public class Controller {

    @Autowired
    UserService userService;


    @GetMapping("/users")
    public ResponseEntity<Object> getAllUserDetails(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to) {

        return ResponseHandler.responseBuilder(HttpStatus.OK,
                userService.getUsers(from, to), "users");
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) throws InvalidUserException {
        User created = userService.createNewUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + created.getId())
                .buildAndExpand(user.getId()).toUri();
        return ResponseHandler.responseBuilder(HttpStatus.CREATED, ResponseEntity.created(location).build(), "user");
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<Object> updateAllUserFields(@PathVariable String userId,
                                                      @Valid @RequestBody User user) {
        User updated = userService.updateFullUser(userId, user);
        return ResponseHandler.responseBuilder(HttpStatus.OK, updated, "user");
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<Object> updateSomeUserFields(@PathVariable String userId,
                                                       @RequestBody User user) {
        User updated = userService.updatePartOfUser(userId, user);
        return ResponseHandler.responseBuilder(HttpStatus.OK, updated, "user");
    }

    @GetMapping("/users/{usersId}")
    public ResponseEntity<Object> getUserDetails(@PathVariable String usersId) {
        User result = userService.getUserById(usersId);
        if (result != null) {
            return ResponseHandler.responseBuilder(HttpStatus.OK, result, "user");
        }
        else return ResponseHandler.responseBuilder(HttpStatus.NOT_FOUND, null, "user");
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        userService.deleteUserById(userId);
        return ResponseHandler.responseBuilder(HttpStatus.OK, "deleted", "user");
    }
}
