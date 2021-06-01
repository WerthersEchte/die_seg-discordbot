package de.fhkiel.seg.commands;

import static de.fhkiel.seg.Configuration.cmdClear;
import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.bot.ControlFacade;
import discord4j.common.util.Snowflake;

/**
 * Command to clear all used channel. Usable by {@link UserType#ADMIN}
 */
public class CommandClear implements Command {

  private ControlFacade control;

  @Override
  public void inject(ControlFacade control) {
    this.control = control;
  }

  @Override
  public String commandTag() {
    return cmdClear();
  }

  @Override
  public String usage() {
    return "``" + commandTag() + "``\n"
        + "Needed Role: ``" + intendedUser() + "``\n"
        + "Clears all messages except last from channels used by this bot";
  }

  @Override
  public UserType intendedUser() {
    return UserType.ADMIN;
  }

  @Override
  public void process(String message, Snowflake sender) {
    try {
      logger().info("Clearing all channels");
      control.getAllChannels().forEach(
          snowflake -> control.getClient().getChannelById(snowflake).subscribe(
              channel ->
                  channel.getRestChannel().getData().subscribe(channelData -> {
                        if (!channelData.lastMessageId().isAbsent()) {
                          channelData.lastMessageId().get()
                              .ifPresent(s -> channel.getRestChannel().bulkDelete(
                                  channel.getRestChannel()
                                      .getMessagesBefore(
                                          Snowflake.of(s))
                                      .map(messageData -> Snowflake.of(messageData.id()))
                              ).subscribe(snowflake1 -> {},
                                  throwable -> logger().error("Error deleting messages.", throwable)));
                        }
                      },
                      throwable -> logger().error("Could not get channel data!", throwable)
                  ),
              throwable -> logger().error("Could not get channel!", throwable)
          )
      );
      logger().info("Cleared all channels");
    } catch (Exception exception) {
      logger().error("Could not clear channels!", exception);
    }
  }
}
