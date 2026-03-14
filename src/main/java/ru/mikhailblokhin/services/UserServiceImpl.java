package ru.mikhailblokhin.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikhailblokhin.dtos.UserRequestDto;
import ru.mikhailblokhin.dtos.UserResponseDto;
import ru.mikhailblokhin.entities.User;
import ru.mikhailblokhin.mappers.UserMapper;
import ru.mikhailblokhin.repositories.UserRepository;
import ru.mikhailblokhin.repositories.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
        repository = new UserRepositoryImpl();
    }

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public void create(UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);
        repository.create(user);
        LOGGER.info("Пользователь {} создан", UserMapper.toDto(repository.readLast()));
    }

    public UserResponseDto read(long id) {
        User user = repository.read(id);
        if (user == null)
            return null;
        UserResponseDto dto = UserMapper.toDto(user);
        LOGGER.info("Была получена информация о пользователе {}", dto);
        return dto;
    }

    public void update(UserRequestDto dto) throws NullPointerException {
        User user = repository.read(dto.getId());
        if (user == null) {
            throw new NullPointerException();
        }
        UserResponseDto oldDto = UserMapper.toDto(user);
        user = UserMapper.toEntity(dto);
        repository.update(user);
        LOGGER.info("Пользователь {} был обновлен на пользователя {}", oldDto, dto);
    }

    public void delete(long id) throws NullPointerException {
        User user = repository.read(id);
        if (user == null) {
            throw new NullPointerException();
        }
        UserResponseDto dto = UserMapper.toDto(user);
        repository.delete(id);
        LOGGER.info("Пользователь {} был удален", dto);
    }

    public List<UserResponseDto> readAll() {
        List<User> users = repository.readAll();
        List<UserResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(UserMapper.toDto(user));
        }
        LOGGER.info("Была получена информация о пользователях {}", dtos);
        return dtos;
    }

    public void exit() {
        repository.exit();
        LOGGER.info("Совершен выход из системы");
    }
}
