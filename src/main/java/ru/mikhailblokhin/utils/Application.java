package ru.mikhailblokhin.utils;

import ru.mikhailblokhin.dtos.UserRequestDto;
import ru.mikhailblokhin.services.UserService;

import java.util.Scanner;

import static ru.mikhailblokhin.utils.ConsoleUtils.*;

public class Application {

    private static final UserService userService = new UserService();

    public static void run() {
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            showMainMenu();
            String value = scanner.next();
            switch (value) {
                case "1":
                    System.out.println("ВСЕ ПОЛЬЗОВАТЕЛИ");
                    System.out.println(userService.readAll());
                    break;
                case "2":
                    System.out.println("СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ");
                    System.out.print("Введите имя (для выхода введите \"exit\"): ");
                    String nameForCreatingUser = scanner.next();
                    if (nameForCreatingUser.equals("exit")) {
                        break;
                    }

                    System.out.print("Введите эл. почту (для выхода введите \"exit\"): ");
                    String emailForCreatingUser = scanner.next();
                    if (emailForCreatingUser.equals("exit")) {
                        break;
                    }

                    System.out.print("Введите возраст: ");
                    int ageForCreatingUser = getAgeValueFromConsole(scanner);

                    UserRequestDto creatingUser = new UserRequestDto(
                            nameForCreatingUser,
                            emailForCreatingUser,
                            ageForCreatingUser
                    );
                    userService.create(creatingUser);
                    break;
                case "3":
                    System.out.println("ПРОСМОТР ПОЛЬЗОВАТЕЛЯ");
                    System.out.print("Введите id пользователя: ");
                    long idForShowing = getLongValueFromConsole(scanner);
                    System.out.println(userService.read(idForShowing));
                    break;
                case "4":
                    System.out.println("ОБНОВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ");
                    System.out.print("Введите id пользователя: ");
                    long idForUpdatingUser = getLongValueFromConsole(scanner);

                    System.out.print("Введите имя (для выхода введите \"exit\"): ");
                    String nameForUpdatingUser = scanner.next();
                    if (nameForUpdatingUser.equals("exit")) {
                        break;
                    }

                    System.out.print("Введите эл. почту (для выхода введите \"exit\"): ");
                    String emailForUpdatingUser = scanner.next();
                    if (emailForUpdatingUser.equals("exit")) {
                        break;
                    }

                    System.out.print("Введите возраст: ");
                    int ageForUpdatingUser = getAgeValueFromConsole(scanner);

                    UserRequestDto updatingUser = new UserRequestDto(
                            idForUpdatingUser,
                            nameForUpdatingUser,
                            emailForUpdatingUser,
                            ageForUpdatingUser
                    );

                    try {
                        userService.update(updatingUser);
                    } catch (NullPointerException e) {
                        System.out.println("Пользователя с таким id не существует");
                    }
                    break;
                case "5":
                    System.out.println("УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ");
                    System.out.print("Введите id пользователя: ");
                    long idForDeleting = getLongValueFromConsole(scanner);
                    try {
                        userService.delete(idForDeleting);
                    } catch (NullPointerException e) {
                        System.out.println("Пользователя с таким id не существует");
                    }
                    break;
                case "6":
                    userService.exit();
                    scanner.close();
                    isRunning = false;
                    break;
                default:
                    System.out.println("Неверное значение. Повторите попытку");
                    break;
            }
        }
    }
}
