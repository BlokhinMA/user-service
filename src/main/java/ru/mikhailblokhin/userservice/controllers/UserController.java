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

    private static final String CREATE = "create";
    private static final String READ = "read";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String READ_ALL = "read_all";
    
    private final UserService service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto) {
        UserResponseDto userResponseDto = service.create(dto);

        userResponseDto.add(linkTo(methodOn(UserController.class).create(dto)).withSelfRel());
        userResponseDto.add(linkTo(methodOn(UserController.class).read(dto.getId())).withRel(READ));
        userResponseDto.add(linkTo(methodOn(UserController.class).update(dto)).withRel(UPDATE));
        userResponseDto.add(linkTo(methodOn(UserController.class).delete(dto.getId())).withRel(DELETE));
        userResponseDto.add(linkTo(methodOn(UserController.class).readAll()).withRel(READ_ALL));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> read(@PathVariable("id") Long id) {
        UserResponseDto userResponseDto = service.read(id);

        userResponseDto.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel(CREATE));
        userResponseDto.add(linkTo(methodOn(UserController.class).read(id)).withSelfRel());
        userResponseDto.add(linkTo(methodOn(UserController.class).update(new UserRequestDto())).withRel(UPDATE));
        userResponseDto.add(linkTo(methodOn(UserController.class).delete(id)).withRel(DELETE));
        userResponseDto.add(linkTo(methodOn(UserController.class).readAll()).withRel(READ_ALL));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> update(@RequestBody UserRequestDto dto) {
        UserResponseDto userResponseDto = service.update(dto);

        userResponseDto.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel(CREATE));
        userResponseDto.add(linkTo(methodOn(UserController.class).read(dto.getId())).withRel(READ));
        userResponseDto.add(linkTo(methodOn(UserController.class).update(dto)).withSelfRel());
        userResponseDto.add(linkTo(methodOn(UserController.class).delete(dto.getId())).withRel(DELETE));
        userResponseDto.add(linkTo(methodOn(UserController.class).readAll()).withRel(READ_ALL));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable("id") Long id) {
        UserResponseDto userResponseDto = service.delete(id);

        userResponseDto.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel(CREATE));
        userResponseDto.add(linkTo(methodOn(UserController.class).readAll()).withRel(READ_ALL));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<UserResponseDto>> readAll() {
        List<UserResponseDto> dtos = service.readAll();

        for (UserResponseDto dto : dtos) {
            dto.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel(CREATE));
            dto.add(linkTo(methodOn(UserController.class).read(dto.getId())).withRel(READ));
            dto.add(linkTo(methodOn(UserController.class).update(new UserRequestDto())).withRel(UPDATE));
            dto.add(linkTo(methodOn(UserController.class).delete(dto.getId())).withRel(DELETE));
            dto.add(linkTo(methodOn(UserController.class).readAll()).withSelfRel());
        }

        CollectionModel<UserResponseDto> collectionModel = CollectionModel.of(dtos);
        collectionModel.add(linkTo(methodOn(UserController.class).create(new UserRequestDto())).withRel(CREATE));
        collectionModel.add(linkTo(methodOn(UserController.class).readAll()).withSelfRel());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }
}
