package ru.mikhailblokhin.userservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mikhailblokhin.userservice.dtos.UserRequestDto;
import ru.mikhailblokhin.userservice.dtos.UserResponseDto;
import ru.mikhailblokhin.userservice.entities.User;
import ru.mikhailblokhin.userservice.mappers.UserMapper;
import ru.mikhailblokhin.userservice.repositories.UserRepositoryHib;
import ru.mikhailblokhin.userservice.repositories.UserRepositoryHibImpl;

import java.util.List;

@Service
public class UserServiceHibImpl implements UserServiceHib {

    private final UserRepositoryHib repository;

    private static final Logger log = LoggerFactory.getLogger(UserServiceHibImpl.class);

    public UserServiceHibImpl() {
        repository = new UserRepositoryHibImpl();
    }

    public UserServiceHibImpl(UserRepositoryHib repository) {
        this.repository = repository;
    }

    public void create(UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);
        repository.create(user);
        log.info("Пользователь {} создан", UserMapper.toDto(repository.readLast()));
    }

    public UserResponseDto read(long id) {
        User user = repository.read(id);
        if (user == null)
            return null;
        UserResponseDto dto = UserMapper.toDto(user);
        log.info("Была получена информация о пользователе {}", dto);
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
        log.info("Пользователь {} был обновлен на пользователя {}", oldDto, dto);
    }

    public void delete(long id) throws NullPointerException {
        User user = repository.read(id);
        if (user == null) {
            throw new NullPointerException();
        }
        UserResponseDto dto = UserMapper.toDto(user);
        repository.delete(id);
        log.info("Пользователь {} был удален", dto);
    }

    public List<UserResponseDto> readAll() {
        List<User> users = repository.readAll();
        List<UserResponseDto> dtos = UserMapper.toDtoList(users);
        log.info("Была получена информация о пользователях {}", dtos);
        return dtos;
    }

    public void exit() {
        repository.exit();
        log.info("Совершен выход из системы");
    }
}
