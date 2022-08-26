package com.mkohan.render;

import com.mkohan.render.dtos.TaskDto;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class RenderFarmClientApplication {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final RenderFarmApi api = new RenderFarmApi();

        help();

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            switch (command) {
                case "login": {
                    login(api, scanner);
                    break;
                }
                case "logout": {
                    logout(api);
                    break;
                }
                case "sign up": {
                    signUp(api, scanner);
                    break;
                }
                case "tasks": {
                    tasks(api);
                    break;
                }
                case "task": {
                    task(api, scanner);
                    break;
                }
                case "submit": {
                    submit(api);
                    break;
                }
                case "exit": {
                    return;
                }
                default: {
                    help();
                }
            }
            System.out.println();
        }
    }

    private static void help() {
        System.out.println("Enter command: [login, logout, sign up, tasks, task, submit, exit]\n");
    }

    private static void login(RenderFarmApi api, Scanner scanner) {
        System.out.print("Username: ");
        final String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        final String password = scanner.nextLine().trim();

        if (api.login(username, password)) {
            System.out.println("Logged in successfully");
        }
    }

    private static void logout(RenderFarmApi api) {
        api.logout();
        System.out.println("Logged out successfully");
    }

    private static void signUp(RenderFarmApi api, Scanner scanner) {
        System.out.print("Username: ");
        final String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        final String password = scanner.nextLine().trim();

        if (api.signUp(username, password)) {
            System.out.println("Signed up successfully, now you can log in");
        }
    }

    private static void tasks(RenderFarmApi api) {
        final String separator = "-----------------------------------";
        final List<TaskDto> tasks = api.getAllTasks();

        if (!tasks.isEmpty()) {
            System.out.println(separator);
            System.out.println("|    ID    |        STATUS        |");
            System.out.println(separator);

            tasks.forEach(task -> System.out.printf("|   %-7s|       %-15s|\n", task.getId(), task.getStatus()));

            System.out.println(separator);
        }
    }

    private static void task(RenderFarmApi api, Scanner scanner) {
        final SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

        final String separator = "-------------------------------------------------";
        final String row = "| %s at %s |       %s      |\n";

        System.out.print("Task ID: ");
        final long id = scanner.nextLong();
        scanner.nextLine();

        api.getTaskById(id).ifPresent(task -> {
            System.out.println(separator);
            System.out.println("|          DATE          |        STATUS        |");
            System.out.println(separator);

            System.out.printf(row,
                    date.format(task.getSubmittedAt()),
                    time.format(task.getSubmittedAt()),
                    "SUBMITTED"
            );

            if (task.getCompletedAt() != null) {
                System.out.printf(row,
                        date.format(task.getCompletedAt()),
                        time.format(task.getCompletedAt()),
                        "COMPLETED"
                );
            }

            System.out.println(separator);
        });
    }

    private static void submit(RenderFarmApi api) {
        if (api.submitTask()) {
            System.out.println("New task submitted");
        }
    }
}
