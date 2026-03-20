package ru.mikhailblokhin.userservice.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.mikhailblokhin.userservice.dtos.UserRequestDto;
import ru.mikhailblokhin.userservice.dtos.UserResponseDto;
import ru.mikhailblokhin.userservice.services.UserServiceImpl;
import tools.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String URI_PREFIX = "/api/v1/users";

    @InjectMocks
    private UserController controller;

    @Mock
    private UserServiceImpl service;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private static final long ID = 1;
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final int AGE = 21;

    private static final String USER_WITH_ID_DOESNT_EXIST_ERROR_MSG = "Пользователя с id %s не существует";
    private static final String USERS_WERE_NOT_ADDED_ERROR_MSG = "Пользователи еще не были добавлены";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void create_ok() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(
                NAME,
                EMAIL,
                AGE
        );
        String requestDtoJson = objectMapper.writeValueAsString(requestDto);
        UserResponseDto responseDto = new UserResponseDto(
                ID,
                NAME,
                EMAIL,
                AGE,
                LocalDateTime.now()
        );
        String responseDtoJson = objectMapper.writeValueAsString(responseDto);
        when(service.create(requestDto)).thenReturn(responseDto);
        mockMvc.perform(post(URI_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDtoJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseDtoJson));
    }

    @Test
    void read_ok() throws Exception {
        UserResponseDto dto = new UserResponseDto(
                ID,
                NAME,
                EMAIL,
                AGE,
                LocalDateTime.now()
        );
        String dtoJson = objectMapper.writeValueAsString(dto);
        System.out.println(dtoJson);
        when(service.read(ID)).thenReturn(dto);
        mockMvc.perform(get(URI_PREFIX + "/{ID}", ID))
                .andExpect(status().isOk())
                .andExpect(content().json(dtoJson));
    }

    @Test
    void update_ok() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(
                ID,
                NAME,
                EMAIL,
                AGE
        );
        String requestDtoJson = objectMapper.writeValueAsString(requestDto);
        UserResponseDto responseDto = new UserResponseDto(
                ID,
                NAME,
                EMAIL,
                AGE,
                LocalDateTime.now()
        );
        String responseDtoJson = objectMapper.writeValueAsString(requestDto);
        when(service.update(requestDto)).thenReturn(responseDto);
        mockMvc.perform(patch(URI_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseDtoJson));
    }

    @Test
    void delete_ok() throws Exception {
        UserResponseDto dto = new UserResponseDto(
                ID,
                NAME,
                EMAIL,
                AGE,
                LocalDateTime.now()
        );
        String dtoJson = objectMapper.writeValueAsString(dto);
        when(service.delete(ID)).thenReturn(dto);
        mockMvc.perform(delete(URI_PREFIX + "/{ID}", ID))
                .andExpect(status().isOk())
                .andExpect(content().json(dtoJson));
    }

    @Test
    void readAll_ok() throws Exception {
        List<UserResponseDto> dtos = List.of(new UserResponseDto(
                ID,
                NAME,
                EMAIL,
                AGE,
                LocalDateTime.now())
        );
        String dtoJson = objectMapper.writeValueAsString(dtos);
        when(service.readAll()).thenReturn(dtos);
        mockMvc.perform(get(URI_PREFIX))
                .andExpect(status().isOk())
                .andExpect(content().json(dtoJson));
    }
}
