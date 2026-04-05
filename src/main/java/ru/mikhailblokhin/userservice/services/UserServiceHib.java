package ru.mikhailblokhin.userservice.services;

import ru.mikhailblokhin.userservice.dtos.UserRequestDto;
import ru.mikhailblokhin.userservice.dtos.UserResponseDto;

import java.util.List;

public interface UserServiceHib {
    void create(UserRequestDto dto);
    UserResponseDto read(long id);
    void update(UserRequestDto dto);
    void delete(long id);
    List<UserResponseDto> readAll();
    void exit();
}
