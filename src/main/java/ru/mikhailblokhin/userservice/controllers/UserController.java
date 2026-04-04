package ru.mikhailblokhin.userservice.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mikhailblokhin.userservice.dtos.UserRequestDto;
import ru.mikhailblokhin.userservice.dtos.UserResponseDto;
import ru.mikhailblokhin.userservice.services.UserService;
import ru.mikhailblokhin.userservice.services.UserServiceImpl;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto) {
        UserResponseDto userResponseDto = service.create(dto);

        userResponseDto.add(linkTo(methodOn(UserController.class).create(dto)).withSelfRel());
        userResponseDto.add(linkTo(methodOn(UserController.class).read(dto.getId())).withRel("read"));
        userResponseDto.add(linkTo(methodOn(UserController.class).update(dto)).withRel("update"));
        userResponseDto.add(linkTo(methodOn(UserController.class).delete(dto.getId())).withRel("delete"));
        userResponseDto.add(linkTo(methodOn(UserController.class).readAll()).withRel("read-all"));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> read(@PathVariable("id") Long id) {
        UserResponseDto userResponseDto = service.read(id);

        userResponseDto.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel("create"));
        userResponseDto.add(linkTo(methodOn(UserController.class).read(id)).withSelfRel());
        userResponseDto.add(linkTo(methodOn(UserController.class).update(new UserRequestDto())).withRel("update"));
        userResponseDto.add(linkTo(methodOn(UserController.class).delete(id)).withRel("delete"));
        userResponseDto.add(linkTo(methodOn(UserController.class).readAll()).withRel("read-all"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> update(@RequestBody UserRequestDto dto) {
        UserResponseDto userResponseDto = service.update(dto);

        userResponseDto.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel("create"));
        userResponseDto.add(linkTo(methodOn(UserController.class).read(dto.getId())).withRel("read"));
        userResponseDto.add(linkTo(methodOn(UserController.class).update(dto)).withSelfRel());
        userResponseDto.add(linkTo(methodOn(UserController.class).delete(dto.getId())).withRel("delete"));
        userResponseDto.add(linkTo(methodOn(UserController.class).readAll()).withRel("read-all"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable("id") Long id) {
        UserResponseDto userResponseDto = service.delete(id);

        userResponseDto.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel("create"));
        userResponseDto.add(linkTo(methodOn(UserController.class).readAll()).withRel("read-all"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<UserResponseDto>> readAll() {
        List<UserResponseDto> dtos = service.readAll();

        for (UserResponseDto dto : dtos) {
            dto.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel("create"));
            dto.add(linkTo(methodOn(UserController.class).read(dto.getId())).withRel("read"));
            dto.add(linkTo(methodOn(UserController.class).update(new UserRequestDto())).withRel("update"));
            dto.add(linkTo(methodOn(UserController.class).delete(dto.getId())).withRel("delete"));
            dto.add(linkTo(methodOn(UserController.class).readAll()).withSelfRel());
        }

        CollectionModel<UserResponseDto> collectionModel = CollectionModel.of(dtos);
        collectionModel.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel("create"));
        collectionModel.add(linkTo(methodOn(UserController.class).readAll()).withSelfRel());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }
}
