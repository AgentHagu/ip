package neuro;

import neuro.command.Command;
import neuro.task.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Arrays;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Neuro {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    public Neuro(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = storage.load();
        } catch (FileNotFoundException e) {
            ui.showError("Save file missing!");
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            // TODO: Switch to specific or custom exception
            } catch (IllegalArgumentException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }


    public static void main(String[] args) {
        new Neuro("data/Neuro.txt").run();
    }

//    public static void main(String[] args) {
//        ArrayList<Task> taskList;
//        try {
//            taskList = loadOrCreateTaskFile("./data/neuro.Neuro.txt");
//        } catch (FileNotFoundException e) {
//            System.out.println("No save file found");
//            System.out.println("Error encountered: " + e);
//            return;
//        }
//
//        // Scanner creation format inspired by https://www.w3schools.com/java/java_user_input.asp
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("    ___________________________________________________");
//        System.out.println("    Hii, I'm neuro.Neuro, your chatbot assistant!");
//        System.out.println("    How can I help you?");
//        System.out.println("    ___________________________________________________");
//
//        while (true) {
//            String input = scanner.nextLine();
//            if (input.equals("bye")) {
//                try {
//                    updateTaskFile("./data/neuro.Neuro.txt", taskList);
//                } catch (IOException e) {
//                    System.out.println("Error encountered: " + e);
//                }
//                break;
//            } else if (input.equals("list")) {
//                System.out.println("    ___________________________________________________");
//

//
//                System.out.println("    ___________________________________________________");
//            } else if (input.startsWith("mark")) {

//
//            } else if (input.startsWith("unmark")) {
//                // String split inspired by https://www.w3schools.com/java/ref_string_split.asp
//                String[] inputComponents = input.split("[\s]");
//
//                System.out.println("    ___________________________________________________");
//                try {
//                    int taskIndex = Integer.valueOf(inputComponents[1]);
//                    Task task = taskList.get(taskIndex - 1);
//                    task.markUndone();
//
//                    System.out.println("    Ok, I've marked this task as not done:");
//                    System.out.println("    " + task);
//                } catch (ArrayIndexOutOfBoundsException e) {
//                    System.out.println("    UH OH! Missing index for 'unmark' command! Add a valid index for a task" +
//                            " to unmark, like 'unmark 2'.");
//                } catch (IndexOutOfBoundsException e) {
//                    System.out.println("    UH OH! Index out of bounds! Try calling the command 'list' to verify the" +
//                            " index of the desired task.");
//                } catch (NumberFormatException e) {
//                    System.out.println("    UH OH! Invalid index for 'mark' command! Add a valid index for a task" +
//                            " to mark, like 'mark 2'.");
//                }
//                System.out.println("    ___________________________________________________");
//            } else if (input.startsWith("delete")) {
//                // String split inspired by https://www.w3schools.com/java/ref_string_split.asp
//                String[] inputComponents = input.split("[\s]");
//
//                System.out.println("    ___________________________________________________");
//                try {
//                    int taskIndex = Integer.valueOf(inputComponents[1]);
//                    Task task = taskList.remove(taskIndex - 1);
//
//                    System.out.println("    Ok, I've removed this task:");
//                    System.out.println("    " + task);
//                    System.out.println("    Now you have " + taskList.size() + " tasks in the list.");
//                } catch (ArrayIndexOutOfBoundsException e) {
//                    System.out.println("    UH OH! Missing index for 'delete' command! Add a valid index for a task" +
//                            " to delete, like 'delete 2'.");
//                } catch (IndexOutOfBoundsException e) {
//                    System.out.println("    UH OH! Index out of bounds! Try calling the command 'list' to verify the" +
//                            " index of the desired task.");
//                }
//                System.out.println("    ___________________________________________________");
//            } else {
//                try {
//                    Task task = getTask(input);
//                    taskList.add(task);
//
//                    System.out.println("    ___________________________________________________");
//                    System.out.println("    Ok, I've added this task:");
//                    System.out.println("        " + task);
//                    System.out.println("    Now you have " + taskList.size() + " tasks in the list.");
//                    System.out.println("    ___________________________________________________");
//                } catch (IllegalArgumentException e) {
//                    System.out.println("    ___________________________________________________");
//                    System.out.println("    " + e.getMessage());
//                    System.out.println("    ___________________________________________________");
//                }
//            }
//        }
//    }
}
