import java.util.ArrayList;
import java.util.Scanner;

public class ToDoListApp {
    private static ArrayList<String> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            showMenu();
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1 -> addTask();
                case 2 -> displayTasks();
                case 3 -> removeTask();
                case 4 -> System.out.println("Exiting the application. Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        } while (choice != 4);
    }

    private static void showMenu() {
        System.out.println("===== TO-DO LIST MENU =====");
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Remove Task");
        System.out.println("4. Exit");
    }

    private static void addTask() {
        System.out.print("Enter the task description: ");
        String task = scanner.nextLine();
        if (!task.isBlank()) {
            tasks.add(task.trim());
            System.out.println("Task added.");
        } else {
            System.out.println("Task cannot be empty.");
        }
    }

    private static void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in the list.");
        } else {
            System.out.println("Your Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    private static void removeTask() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to remove.");
            return;
        }

        System.out.print("Remove by (1) Index or (2) Name? Enter choice: ");
        int method = getIntInput();

        switch (method) {
            case 1 -> {
                displayTasks();
                System.out.print("Enter the task number to remove: ");
                int index = getIntInput() - 1;
                if (index >= 0 && index < tasks.size()) {
                    String removed = tasks.remove(index);
                    System.out.println("Removed task: " + removed);
                } else {
                    System.out.println("Invalid index.");
                }
            }
            case 2 -> {
                System.out.print("Enter the task name to remove: ");
                String name = scanner.nextLine().trim();
                if (tasks.remove(name)) {
                    System.out.println("Removed task: " + name);
                } else {
                    System.out.println("Task not found.");
                }
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.nextLine();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // clear newline
        return input;
    }
}