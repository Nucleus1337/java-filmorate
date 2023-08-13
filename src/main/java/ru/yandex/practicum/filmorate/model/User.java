package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.util.Sequence;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    @NonNull
    private Long id;

    @NotBlank(message = "email не может быть пустым")
    @Email(message = "Неверный формат для email")
    private String email;

    @NotBlank(message = "login не может быть пустым")
    private String login;

    private String name;

    @PastOrPresent(message = "birthday не может быть в будущем")
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.id = Sequence.getNextId();
        this.email = email;
        this.login = login;
        this.name = name.isBlank() ? login : name;
        this.birthday = birthday;
    }
}
