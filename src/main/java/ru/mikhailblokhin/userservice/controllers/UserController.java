package ru.mikhailblokhin.userservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mikhailblokhin.userservice.dtos.UserRequestDto;
import ru.mikhailblokhin.userservice.dtos.UserResponseDto;
import ru.mikhailblokhin.userservice.services.UserService;
import ru.mikhailblokhin.userservice.services.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> read(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.read(id));
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> update(@RequestBody UserRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.delete(id));

    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> readAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.readAll());
    }
}
