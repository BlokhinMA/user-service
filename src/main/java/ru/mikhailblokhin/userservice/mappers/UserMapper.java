package ru.mikhailblokhin.userservice.mappers;

import ru.mikhailblokhin.userservice.dtos.UserRequestDto;
import ru.mikhailblokhin.userservice.dtos.UserResponseDto;
import ru.mikhailblokhin.userservice.entities.User;

import java.util.List;

public class UserMapper {

    public static User toEntity(UserRequestDto dto) {
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getAge()
        );
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public static List<UserResponseDto> toDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }
}
