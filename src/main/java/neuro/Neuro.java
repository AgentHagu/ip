package neuro;

import java.io.FileNotFoundException;

import neuro.command.Command;
import neuro.task.TaskList;

/**
 * The {@code Neuro} class is the main class for the Neuro Chatbot application.
 * It handles the initialization of necessary components, such as storage, task list and UI.
 */
public class Neuro {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a new Neuro object that initializes the user interface and storage.
     *
     * @param filePath The path to the save-file for Neuro
     */
    public Neuro(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = storage.load();
        } catch (FileNotFoundException e) {
            ui.showError("Save file missing!");
        }
    }

    /**
     * Runs the main loop of the Neuro application, processing user commands until an exit command is given.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (IllegalArgumentException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    public String getResponse(String input) {
        return "Neuro heard: " + input;
    }


    public static void main(String[] args) {
        new Neuro("data/Neuro.txt").run();
    }
}
