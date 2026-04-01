package ru.mikhailblokhin.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mikhailblokhin.userservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
