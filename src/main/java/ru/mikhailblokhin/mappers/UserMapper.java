package ru.mikhailblokhin.mappers;

import ru.mikhailblokhin.dtos.UserRequestDto;
import ru.mikhailblokhin.dtos.UserResponseDto;
import ru.mikhailblokhin.entities.User;

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
}
