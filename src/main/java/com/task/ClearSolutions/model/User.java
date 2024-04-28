package com.task.ClearSolutions.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
public class User {

    @Getter
    static private Map<String, User> usersMap = new HashMap<>();

    @JsonIgnoreProperties
    private String id;

    @NotBlank
    @Email(message = "invalid email")
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String secondName;

    @NotNull
    @Past(message = "invalid date")
    private LocalDate birthDate;

    private String address;
    private String phoneNumber;

}
