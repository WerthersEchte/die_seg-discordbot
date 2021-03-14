package de.fhkiel.seg.commands;

import static de.fhkiel.seg.Configuration.prefix;

import de.fhkiel.seg.bot.ControlFacade;
import discord4j.common.util.Snowflake;

/**
 * The interface Command to handle incomming commands.
 */
public interface Command {
  /**
   * The constant ALL_CHANNEL to represent all channels.
   */
  String ALL_CHANNEL = "all";

  /**
   * Command builder to build a command out of the prefix and the tag.
   *
   * @param command the command
   * @return the build string
   */
  static String commandBuilder(String command) {
    return prefix() + " " + command;
  }

  /**
   * Command tag of the command.
   *
   * @return the string
   */
  String commandTag();

  /**
   * Help string containing text on how to use this command.
   *
   * @return the string
   */
  String usage();

  /**
   * Intended user type allowed to use this command.
   *
   * @return the user type
   */
  UserType intendedUser();

  /**
   * Processing the message.
   *
   * @param message the message to process
   * @param sender the sender of the message
   */
  void process(String message, Snowflake sender);

  /**
   * Gets allowed channel from where these messages can be sent.
   *
   * @return the allowed channel
   */
  default String getAllowedChannel() {
    return ALL_CHANNEL;
  }

  /**
   * Inject the interface to the bot.
   *
   * @param control the control
   */
  default void inject(ControlFacade control) {
  }

}
