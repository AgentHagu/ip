package neuro.command;

import neuro.Parser;
import neuro.Ui;
import neuro.Storage;

import neuro.task.*;

import java.util.Arrays;

public class AddCommand extends Command {
    private final TaskType taskType;
    private final String[] commandComponents;

    public AddCommand(String[] commandComponents) {
        this.taskType = TaskType.stringToEnum(commandComponents[0]);
        this.commandComponents = commandComponents;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task task = null;

        switch (taskType) {
            case TODO:
                String todoDescription = String.join(" ",
                        Arrays.copyOfRange(commandComponents, 1, commandComponents.length));

                if (todoDescription.isEmpty()) {
                    throw new IllegalArgumentException("The description of a 'todo' cannot be empty! Try adding" +
                            " a description like 'todo read book'.");
                }

                task = new Todo(todoDescription);
                break;

            case DEADLINE:
                int byIndex = Parser.getDeadlineByIndex(commandComponents);

                String deadlineDescription = String.join(" ",
                        Arrays.copyOfRange(commandComponents, 1, byIndex));
                String deadline = String.join(" ",
                        Arrays.copyOfRange(commandComponents, byIndex + 1, commandComponents.length));

                if (deadlineDescription.isEmpty()) {
                    throw new IllegalArgumentException("UH OH! The description of a 'deadline' cannot be empty! Try adding" +
                            " a description like 'deadline finish homework /by Mon 2359'.");
                } else if (deadline.isEmpty()) {
                    throw new IllegalArgumentException("UH OH! The deadline time/date of a 'deadline' cannot be empty! Try adding" +
                            " a valid deadline like 'deadline finish homework /by Mon 2359'.");
                }

                task = new Deadline(deadlineDescription, Parser.parseDateTime(deadline));
                break;

            case EVENT:
                int fromIndex = -1;
                int toIndex = -1;

                for (int i = 1; i < commandComponents.length; i++) {
                    if (commandComponents[i].equals("/from")) {
                        fromIndex = i;
                    } else if (commandComponents[i].equals("/to")) {
                        toIndex = i;
                        break;
                    }
                }

                // Missing /from
                if (fromIndex < 0) {
                    throw new IllegalArgumentException("UH OH! The command given is missing the '/from' input for event." +
                            "Try updating the command like 'event project meeting /from Mon 2pm /to 5pm'.");
                }

                // Missing /to
                if (toIndex < 0) {
                    throw new IllegalArgumentException("UH OH! The command given is missing the '/to' input for event." +
                            "Try updating the command like 'event project meeting /from Mon 2pm /to 5pm'.");
                }

                String eventDescription = String.join(" ",
                        Arrays.copyOfRange(commandComponents, 1, fromIndex));
                String from = String.join(" ",
                        Arrays.copyOfRange(commandComponents, fromIndex + 1, toIndex));
                String to = String.join(" ",
                        Arrays.copyOfRange(commandComponents, toIndex + 1, commandComponents.length));

                if (eventDescription.isEmpty()) {
                    throw new IllegalArgumentException("UH OH! The description of an 'event' cannot be empty! Try adding" +
                            " a description like 'event project meeting /from Mon 2pm /to 5pm'.");
                } else if (from.isEmpty()) {
                    throw new IllegalArgumentException("UH OH! The from time/date of an 'event' cannot be empty! Try adding" +
                            " a valid from time/date like 'event project meeting /from Mon 2pm /to 5pm'.");
                } else if (to.isEmpty()) {
                    throw new IllegalArgumentException("UH OH! The to time/date of an 'event' cannot be empty! Try adding" +
                            " a valid to time/date like 'event project meeting /from Mon 2pm /to 5pm'.");
                }

                task = new Event(eventDescription, from, to);
                break;

            default:
                throw new IllegalArgumentException("UH OH! I'm sorry but I don't recognise that command. Try the following:" +
                        " 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete', 'bye'.");
        }

        tasks.addTask(task);
        ui.showMessage("Ok, I've added this task:");
        ui.showMessage("    " + task);
        ui.showMessage("You now have " + tasks.getSize() + " tasks in the list.");
    }


    @Override
    public boolean isExit() {
        return false;
    }
}
