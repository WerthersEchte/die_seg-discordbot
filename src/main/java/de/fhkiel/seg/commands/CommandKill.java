package de.fhkiel.seg.commands;

import static de.fhkiel.seg.Configuration.cmdKill;
import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.bot.ControlFacade;
import discord4j.common.util.Snowflake;

/**
 * Command to kill the bot gracefully. Usable by {@link UserType#ADMIN}
 */
public class CommandKill implements Command {

  private ControlFacade control;

  @Override
  public void inject(ControlFacade control) {
    this.control = control;
  }

  @Override
  public String commandTag() {
    return cmdKill();
  }

  @Override
  public String usage() {
    return "``" + commandTag() + "``\n"
        + "Needed Role: ``" + intendedUser() + "``\n"
        + "Gracefully closes the bot";
  }

  @Override
  public UserType intendedUser() {
    return UserType.ADMIN;
  }

  @Override
  public void process(String message, Snowflake sender) {
    logger().info("Shutdown Bot");
    control.stop();
    System.exit(0);
  }
}
