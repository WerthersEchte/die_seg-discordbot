package de.fhkiel.seg.bot;

import static de.fhkiel.seg.Configuration.admins;
import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.auction.TraderRegister;
import de.fhkiel.seg.commands.Command;
import de.fhkiel.seg.commands.Factory;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import java.util.Arrays;
import java.util.Optional;

/**
 * Message handler class faciliating the sending of messages to channels.
 */
public class MessageHandler {

  private final Bot bot;
  private final ChannelHandler channelHandler;

  private final Factory commandFactory;

  /**
   * Instantiates a new Message handler.
   *
   * @param bot the bot
   * @param channelHandler the channel handler
   * @param commandFactory the used command factory
   */
  public MessageHandler(Bot bot, ChannelHandler channelHandler, Factory commandFactory) {
    this.bot = bot;
    this.channelHandler = channelHandler;

    this.commandFactory = commandFactory;
  }

  /**
   * Instantiates a new Message handler with an default {@link Factory}.
   *
   * @param bot the bot
   * @param channelHandler the channel handler
   */
  public MessageHandler(Bot bot, ChannelHandler channelHandler) {
    this.bot = bot;
    this.channelHandler = channelHandler;

    this.commandFactory = new Factory(new ControlFacade(bot, this, channelHandler));
  }

  /**
   * Handles an incomming message.
   *
   * @param message the incomming message
   */
  public void handleMessage(Message message) {
    if (messageAuthorEqualsSelf(message)) {
      return;
    }
    Command command = commandFactory.findCommand(message.getContent());
    if (command != null
        && (command.getAllowedChannel().equals(Command.ALL_CHANNEL)
        && channelHandler.getAllChannels().contains(message.getChannelId())
        || channelHandler.getChannels().get(command.getAllowedChannel())
            .contains(message.getChannelId()))) {
      logger().debug("Message of type {} Message: {}", command, message);

      checkAllowance(message, command);
    }
  }

  private void checkAllowance(Message message, Command command) {
    Optional<User> author = message.getAuthor();
    boolean canExecuteCmd = false;
    if (author.isPresent()) {
      switch (command.intendedUser()) {
        case ADMIN:
          if (Arrays.stream(admins())
              .anyMatch(admin -> admin.equals(author.get().getTag()))) {
            canExecuteCmd = true;
          }
          break;
        case TRADER:
          if (TraderRegister.getInstance().getMerchant(author.get().getId()).isPresent()) {
            canExecuteCmd = true;
          }
          break;
        case NONE:
          break;
        case ALL:
        default:
          canExecuteCmd = true;
      }
      if (canExecuteCmd) {
        command.process(message.getContent(),
            author.map(User::getId).orElseGet(() -> Snowflake.of(0)));
      }
    }
  }

  private boolean messageAuthorEqualsSelf(Message message) {
    return message
        .getAuthor()
        .map(user -> user.getId().equals(bot.getClient().getSelfId()))
        .orElse(false);
  }

  /**
   * Send a message to a channel.
   *
   * @param channelId the channel id
   * @param message the message
   */
  public void sendMessage(Snowflake channelId, String message) {
    channelHandler.getChannelBySnowflake(channelId)
        .subscribe(value -> value.getRestChannel().createMessage(message).subscribe());
  }

  /**
   * Gets used command factory.
   *
   * @return the command factory
   */
  public Factory getCommandFactory() {
    return commandFactory;
  }
}
