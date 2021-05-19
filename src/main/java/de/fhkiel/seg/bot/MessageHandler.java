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
 * Message handler class facilitating the sending of messages to channels.
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
   * Handles an incoming message.
   *
   * @param message the incoming message
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

      if(AuthorAllowedToUseCommand(message, command)){
        Optional<User> author = message.getAuthor();
        command.process(message.getContent(),
                author.map(User::getId).orElseGet(() -> Snowflake.of(0)));
      };
    }
  }

  private boolean AuthorAllowedToUseCommand(Message message, Command command) {
    Optional<User> author = message.getAuthor();
    boolean isAllowed = false;
    if (author.isPresent()) {
      switch (command.intendedUser()) {
        case ADMIN:
          if (Arrays.stream(admins())
              .anyMatch(admin -> admin.equals(author.get().getTag()))) {
            isAllowed = true;
          }
          break;
        case TRADER:
          if (TraderRegister.getInstance().getMerchant(author.get().getId()).isPresent()) {
            isAllowed = true;
          }
          break;
        case NONE:
          break;
        case ALL:
        default:
          isAllowed = true;
      }
    }
    return isAllowed;
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
        .subscribe(value -> value.getRestChannel().createMessage(message)
            .subscribe(null, throwable -> logger().error("Error handling message!", throwable)));
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
