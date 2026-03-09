package ru.mikhailblokhin.repositories;

import ru.mikhailblokhin.entities.User;

import java.util.List;

public interface UserRepository {
    void create(User user);
    User read(long id);
    void update(User user);
    void delete(long id);
    User readLast();
    List<User> readAll();
    void exit();
}
