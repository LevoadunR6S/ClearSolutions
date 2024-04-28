package com.task.ClearSolutions.service.impl;

import com.task.ClearSolutions.exception.InvalidUserException;
import com.task.ClearSolutions.model.User;
import com.task.ClearSolutions.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserServiceImpl implements UserService {

    @Value("${user.minAge}")
    private int minAge;

    Map<String, User> userMap = User.getUsersMap();

    @Override
    public List<User> getUsers(LocalDate from, LocalDate to) {
        if (from == null && to == null) {
            return userMap.values().stream().toList();
        } else {
            LocalDate fromDate = Optional.ofNullable(from).orElse(LocalDate.MIN);
            LocalDate toDate = Optional.ofNullable(to).orElse(LocalDate.MAX);
            return getUsersByBirthDateRange(fromDate, toDate);
        }
    }

    @Override
    public User createNewUser(User user) {
        int age = (int) ChronoUnit.YEARS.between(user.getBirthDate(), LocalDate.now());
        if (age >= minAge) {
            userMap.put(createId(user), user);
            return userMap.get(user.getId());
        } else {
            throw new InvalidUserException("Invalid user");
        }
    }

    @Override
    public User updateFullUser(String userId, User user) {
        user.setId(userId);
        User updated = userMap.put(userId, user);
        return updated;
    }

    @Override
    public User updatePartOfUser(String userId, User updatedUser) {
        User oldUser = userMap.get(userId);

        if (updatedUser.getEmail() != null) {
            oldUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getFirstName() != null) {
            oldUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getSecondName() != null) {
            oldUser.setSecondName(updatedUser.getSecondName());
        }
        if (updatedUser.getBirthDate() != null) {
            oldUser.setBirthDate(updatedUser.getBirthDate());
        }
        if (updatedUser.getAddress() != null) {
            oldUser.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getPhoneNumber() != null) {
            oldUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        return oldUser;
    }


    @Override
    public User getUserById(String userId) {
        return userMap.get(userId);
    }

    @Override
    public String deleteUserById(String userId) {
        userMap.remove(userId);
        return "Success";
    }


    private String createId(User user) {
        //виглядатиме приблизно так: VLegunvowa. (Звичайно, що про безпеку тут мова і не йде)
        String id = user.getFirstName().charAt(0) +
                user.getSecondName() + user.getEmail().substring(0, 4);
        user.setId(id);
        return id;
    }

    private List<User> getUsersByBirthDateRange(LocalDate from, LocalDate to) {
        //Перевірка на те, що from настане перед to
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' must be before 'to'");
        }

        //фільтруємо юзерів по даті народження
        return userMap.values().stream()
                .filter(user -> user.getBirthDate().isAfter(from))
                .filter(user -> user.getBirthDate().isBefore(to))
                .toList();
    }
}
