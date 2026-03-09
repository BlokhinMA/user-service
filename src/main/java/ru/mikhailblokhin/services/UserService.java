package ru.mikhailblokhin.services;

import ru.mikhailblokhin.dtos.UserRequestDto;
import ru.mikhailblokhin.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    void create(UserRequestDto dto);
    UserResponseDto read(long id);
    void update(UserRequestDto dto);
    void delete(long id);
    List<UserResponseDto> readAll();
    void exit();
}
