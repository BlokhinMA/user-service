package ru.mikhailblokhin.userservice.repositories;

import ru.mikhailblokhin.userservice.entities.User;

import java.util.List;

public interface UserRepositoryHib {
    void create(User user);
    User read(long id);
    void update(User user);
    void delete(long id);
    User readLast();
    List<User> readAll();
    void exit();
}
