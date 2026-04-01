package ru.mikhailblokhin.userservice.entities;

public record UserChangeInfo(
        OperationType operationType,
        String userEmail
) {

}
