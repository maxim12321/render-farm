package com.mkohan.render;

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
        api.getAllTasks().forEach(System.out::println);
    }

    private static void task(RenderFarmApi api, Scanner scanner) {
        System.out.print("Task ID: ");
        final long id = scanner.nextLong();
        scanner.nextLine();
        api.getTaskById(id).ifPresent(System.out::println);
    }

    private static void submit(RenderFarmApi api) {
        if (api.submitTask()) {
            System.out.println("New task submitted");
        }
    }
}
