package ru.mikhailblokhin.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUtils {

    public static void showMainMenu() {
        System.out.print("""
                МЕНЮ
                1. ПРОСМОТРЕТЬ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ
                2. СОЗДАТЬ ПОЛЬЗОВАТЕЛЯ
                3. ПРОСМОТРЕТЬ ПОЛЬЗОВАТЕЛЯ
                4. ОБНОВИТЬ ПОЛЬЗОВАТЕЛЯ
                5. УДАЛИТЬ ПОЛЬЗОВАТЕЛЯ
                6. ВЫХОД
                ВЫБЕРИТЕ ДЕЙСТВИЕ:\s""");
    }

    public static long getLongValueFromConsole(Scanner scanner) {
        Long value = null;
        while (value == null) {
            try {
                value = scanner.nextLong();
            } catch (InputMismatchException e) {
                System.out.print("Неверное значение. Повторите попытку: ");
                scanner.next();
            }
        }
        return value;
    }

    public static int getAgeValueFromConsole(Scanner scanner) {
        Integer value = null;
        while (value == null) {
            try {
                value = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Неверное значение. Повторите попытку: ");
                scanner.next();
                continue;
            }
            if (value < 0) {
                System.out.print("Значение должно быть неотрицательным. Повторите попытку: ");
                value = null;
            }
        }
        return value;
    }
}
