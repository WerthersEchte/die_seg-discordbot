package de.fhkiel.seg.commands;

import static de.fhkiel.seg.Configuration.cmdHelp;
import static de.fhkiel.seg.Configuration.prefix;

import de.fhkiel.seg.bot.ControlFacade;
import discord4j.common.util.Snowflake;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Command help to show help for users. Usable by {@link UserType#ALL}
 */
public class CommandHelp implements Command {

  private ControlFacade control;

  @Override
  public void inject(ControlFacade control) {
    this.control = control;
  }

  @Override
  public String commandTag() {
    return cmdHelp();
  }

  @Override
  public String usage() {
    StringBuilder helpText = new StringBuilder();
    helpText.append("Help:\n");
    helpText.append("Prefix before every command is ``").append(prefix()).append("``\n");
    helpText.append("Possible commands are: ");
    control.getCommandFactory().getCommandStrings().stream()
        .map(s -> control.getCommandFactory().findCommand(s)).filter(
        Objects::nonNull)
        .forEach(command -> helpText.append(" ``").append(command.commandTag()).append("`` "));
    helpText.append("\nFor more help use ``").append(Command.commandBuilder(commandTag()))
        .append(" <Command>``");

    return helpText.toString();
  }

  @Override
  public UserType intendedUser() {
    return UserType.ALL;
  }

  @Override
  public void process(String message, Snowflake sender) {
    Optional<String> helpForCommand = control.getCommandFactory().getCommandStrings().stream()
        .filter(s -> s.toLowerCase(Locale.ROOT)
            .equals(Command.commandBuilder(
                message
                    .toLowerCase()
                    .replace(Command
                        .commandBuilder(commandTag())
                        .toLowerCase(Locale.ROOT), "")
                    .trim()
                ).toLowerCase(Locale.ROOT)
            )
        ).findFirst();
    if (helpForCommand.isPresent()) {
      control
          .sendInfoMessage(control
              .getCommandFactory()
              .findCommand(helpForCommand
                  .get())
              .usage());
    } else {
      control.sendInfoMessage(usage());
    }
  }
}
