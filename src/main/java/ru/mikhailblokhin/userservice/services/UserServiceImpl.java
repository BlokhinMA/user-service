package ru.mikhailblokhin.userservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mikhailblokhin.userservice.dtos.UserRequestDto;
import ru.mikhailblokhin.userservice.dtos.UserResponseDto;
import ru.mikhailblokhin.userservice.entities.OperationType;
import ru.mikhailblokhin.userservice.entities.User;
import ru.mikhailblokhin.userservice.entities.UserChangeInfo;
import ru.mikhailblokhin.userservice.exceptions.NoContentException;
import ru.mikhailblokhin.userservice.mappers.UserMapper;
import ru.mikhailblokhin.userservice.repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserChangeInfoKafkaProducer userChangeInfoKafkaProducer;

    public UserServiceImpl(UserRepository userRepository, UserChangeInfoKafkaProducer userChangeInfoKafkaProducer) {
        this.userRepository = userRepository;
        this.userChangeInfoKafkaProducer = userChangeInfoKafkaProducer;
    }

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserResponseDto create(UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);
        User createdUser = userRepository.save(user);
        UserResponseDto responseDto = UserMapper.toDto(createdUser);
        UserChangeInfo userChangeInfo = new UserChangeInfo(
                OperationType.CREATION,
                responseDto.getEmail()
        );
        log.info("Пользователь {} создан", responseDto);
        userChangeInfoKafkaProducer.produce(userChangeInfo);
        return responseDto;
    }

    @Override
    public UserResponseDto read(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователя с id " + id
                + " не существует"));
        UserResponseDto dto = UserMapper.toDto(user);
        System.out.println(dto);
        log.info("Была получена информация о пользователе {}", dto);
        return dto;
    }

    @Override
    public UserResponseDto update(UserRequestDto dto) {
        UserResponseDto oldDto = read(dto.getId());
        User user = UserMapper.toEntity(dto);
        user.setCreatedAt(oldDto.getCreatedAt());
        User updatedUser = userRepository.save(user);
        UserResponseDto newDto = UserMapper.toDto(updatedUser);
        log.info("Пользователь {} был обновлен на пользователя {}", oldDto, newDto);
        return newDto;
    }

    @Override
    public UserResponseDto delete(Long id) {
        UserResponseDto dto = read(id);
        userRepository.deleteById(id);
        UserChangeInfo userChangeInfo = new UserChangeInfo(
                OperationType.DELETION,
                dto.getEmail()
        );
        log.info("Пользователь {} был удален", dto);
        userChangeInfoKafkaProducer.produce(userChangeInfo);
        return dto;
    }

    @Override
    public List<UserResponseDto> readAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NoContentException("Пользователи еще не были добавлены");
        }
        List<UserResponseDto> dtos = UserMapper.toDtoList(users);
        log.info("Была получена информация о пользователях {}", dtos);
        return dtos;
    }
}
