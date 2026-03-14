package ru.mikhailblokhin.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mikhailblokhin.dtos.UserRequestDto;
import ru.mikhailblokhin.dtos.UserResponseDto;
import ru.mikhailblokhin.entities.User;
import ru.mikhailblokhin.mappers.UserMapper;
import ru.mikhailblokhin.repositories.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @AfterEach
    void tearDown() {
        userService = null;
    }

    @Test
    void create_ok() {
        UserRequestDto dto = new UserRequestDto(
                "Name",
                "Email",
                21
        );
        User createdUser = UserMapper.toEntity(dto);
        createdUser.setId(1L);
        createdUser.setCreatedAt(LocalDateTime.now());
        Mockito.when(userRepository.readLast()).thenReturn(createdUser);
        userService.create(dto);
        User userBeingCreated = UserMapper.toEntity(dto);
        Mockito.verify(userRepository).create(userBeingCreated);
    }

    @Test
    void read_ok() {
        User user = new User(
                1L,
                "Name",
                "Email",
                21,
                LocalDateTime.now()
        );
        Mockito.when(userRepository.read(1)).thenReturn(user);
        UserResponseDto expectedUserDto = UserMapper.toDto(user);
        UserResponseDto actualUserDto = userService.read(1);
        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void update_ok() {
        UserRequestDto dto = new UserRequestDto(
                1L,
                "Name",
                "Email",
                21
        );
        User user = UserMapper.toEntity(dto);
        user.setCreatedAt(LocalDateTime.now());
        Mockito.when(userRepository.read(dto.getId())).thenReturn(user);
        userService.update(dto);
        user.setCreatedAt(null);
        Mockito.verify(userRepository).update(user);
    }

    @Test
    void update_userIsNull_throwsRuntimeException() {
        UserRequestDto dto = new UserRequestDto(
                1L,
                "Name",
                "Email",
                21
        );
        Mockito.when(userRepository.read(dto.getId())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> userService.update(dto));
    }

    @Test
    void delete_ok() {
        UserRequestDto dto = new UserRequestDto(
                1L,
                "Name",
                "Email",
                21
        );
        User user = UserMapper.toEntity(dto);
        user.setCreatedAt(LocalDateTime.now());
        Mockito.when(userRepository.read(dto.getId())).thenReturn(user);
        userService.delete(dto.getId());
        Mockito.verify(userRepository).delete(dto.getId());
    }

    @Test
    void delete_userIsNull_throwsRuntimeException() {
        Mockito.when(userRepository.read(1)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> userService.delete(1));
    }

    @Test
    void readAll_ok() {
        User user = new User(
                1L,
                "Name",
                "Email",
                21,
                LocalDateTime.now()
        );
        Mockito.when(userRepository.readAll()).thenReturn(List.of(user));
        UserResponseDto userDto = UserMapper.toDto(user);
        List<UserResponseDto> expectedUserDtoList = List.of(userDto);
        List<UserResponseDto> actualUserDtoList = userService.readAll();
        assertEquals(expectedUserDtoList, actualUserDtoList);
    }

    @Test
    void exit_ok() {
        userService.exit();
        Mockito.verify(userRepository).exit();
    }
}