package com.task.ClearSolutions.service;

import com.task.ClearSolutions.model.User;
import java.time.LocalDate;
import java.util.List;

public interface UserService {

    List<User> getUsers(LocalDate from, LocalDate to);

    User createNewUser(User user);

    User updateFullUser(String id, User user);

    User updatePartOfUser(String id, User user);

    String deleteUserById(String userId);

    User getUserById(String userId);

}
