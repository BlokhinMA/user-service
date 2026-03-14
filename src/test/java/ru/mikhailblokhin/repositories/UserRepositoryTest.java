package ru.mikhailblokhin.repositories;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import ru.mikhailblokhin.entities.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserRepositoryTest {

    @Container
    private static final PostgreSQLContainer container = new PostgreSQLContainer("postgres")
            .withDatabaseName("users_info")
            .withUsername("postgres")
            .withPassword("1234");

    private static SessionFactory sessionFactory;

    private static UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        String jdbcUrl = container.getJdbcUrl();
        String username = container.getUsername();
        String password = container.getPassword();

        sessionFactory = new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", jdbcUrl)
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.connection.password", password)
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.current_session_context_class", "thread")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        userRepository = new UserRepositoryImpl(sessionFactory);
    }

    @AfterAll
    static void afterAll() {
        userRepository.exit();
        assertTrue(sessionFactory.isClosed());
    }

    @Test
    void create_ok() {
        User user = new User(
                "Name",
                "Email",
                21
        );
        userRepository.create(user);
        User createdUser = userRepository.readLast();
        user.setId(createdUser.getId());
        user.setCreatedAt(createdUser.getCreatedAt());
        assertEquals(user, createdUser);
    }

    @Test
    void delete_ok() {
        User user = new User(
                "Name",
                "Email",
                21
        );
        userRepository.create(user);
        User createdUser = userRepository.readLast();
        long id = createdUser.getId();
        userRepository.delete(id);
        User deletedUser = userRepository.read(id);
        assertNull(deletedUser);
    }

    @Test
    void update_ok() {
        User user = new User(
                "Name",
                "Email",
                21
        );
        userRepository.create(user);
        User createdUser = userRepository.readLast();
        long id = createdUser.getId();
        LocalDateTime createdAt = createdUser.getCreatedAt();
        User updatingUser = new User(
                id,
                "Name1",
                "Email1",
                22
        );
        userRepository.update(updatingUser);
        User updatedUser = userRepository.read(id);
        updatingUser.setCreatedAt(createdAt);
        assertEquals(updatingUser, updatedUser);
    }
}