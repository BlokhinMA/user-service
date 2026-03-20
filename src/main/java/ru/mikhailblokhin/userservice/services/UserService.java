package ru.mikhailblokhin.userservice.services;

import ru.mikhailblokhin.userservice.dtos.UserRequestDto;
import ru.mikhailblokhin.userservice.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto create(UserRequestDto dto);
    UserResponseDto read(Long id);
    UserResponseDto update(UserRequestDto dto);
    UserResponseDto delete(Long id);
    List<UserResponseDto> readAll();
}
