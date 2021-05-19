package de.fhkiel.seg.commands;

import static de.fhkiel.seg.commands.Command.commandBuilder;
import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.bot.ControlFacade;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.reflections.Reflections;

/**
 * Factory class to store all possible commands and resolve incoming request by finding
 * and instantiation of the fitting command.
 */
public class Factory {

  private final ControlFacade control;
  private Map<String, Class<? extends Command>> commands = new HashMap<>();

  /**
   * Instantiates a new Factory.
   * Automatically finds all Subtypes of the {@link Command}-interface
   * and registers them as usable commands.
   *
   * @param control the control interface to the bot using this factory.
   */
  public Factory(ControlFacade control) {
    this.control = control;

    Reflections reflections = new Reflections("de.fhkiel.seg.commands");
    Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
    for (Class<? extends Command> clazz : classes) {
      try {
        logger().trace("Found Command {}", clazz.getName());
        addCommand(clazz.getConstructor().newInstance().commandTag(), clazz);
      } catch (java.lang.Exception exception) {
        exception.printStackTrace();
      }
    }
  }

  private void addCommand(String command, Class<? extends Command> clazz) {
    if (!command.isEmpty()) {
      String fullCommand = commandBuilder(command);
      logger().debug("Registered Command {}", fullCommand);
      commands.put(fullCommand, clazz);
    }
  }

  /**
   * Find the command fitting to the message.
   *
   * @param message the incoming message with an command
   * @return the command or null
   */
  public Command findCommand(String message) {
    Optional<String> commandString = commands.keySet().stream()
        .filter(s -> message.toLowerCase(Locale.ROOT).startsWith(s.toLowerCase(Locale.ROOT)))
        .findFirst();
    if (commandString.isPresent()) {
      Class<? extends Command> command = commands.get(commandString.get());
      try {
        Command newCommand = command.getConstructor().newInstance();
        newCommand.inject(control);
        return newCommand;
      } catch (java.lang.Exception exception) {
        logger().error("Could not build command.", exception);
      }
    }
    return null;
  }

  /**
   * Gets all known commands as strings.
   *
   * @return the command strings in a {@link Set}
   */
  public Set<String> getCommandStrings() {
    return commands.keySet();
  }

}
