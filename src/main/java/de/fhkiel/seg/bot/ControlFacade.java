package de.fhkiel.seg.bot;

import de.fhkiel.seg.commands.Factory;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import java.util.List;

/**
 * A facade to facilitate the communication with the bot and its handlers.
 */
public class ControlFacade {

  private final Bot bot;
  private final MessageHandler messageHandler;
  private final ChannelHandler channelHandler;

  /**
   * Instantiates a new Control facade.
   *
   * @param bot the bot
   * @param messageHandler the message handler
   * @param channelHandler the channel handler
   */
  public ControlFacade(Bot bot, MessageHandler messageHandler, ChannelHandler channelHandler) {
    this.bot = bot;
    this.messageHandler = messageHandler;
    this.channelHandler = channelHandler;
  }

  // Message

  /**
   * Send an auction message.
   *
   * @param message the message
   */
  public void sendAuctionMessage(String message) {
    channelHandler.getAuctionChannel()
        .forEach(channelSnowflake -> messageHandler.sendMessage(channelSnowflake, message));
  }

  /**
   * Send an info message.
   *
   * @param message the message
   */
  public void sendInfoMessage(String message) {
    channelHandler.getInfoChannel()
        .forEach(channelSnowflake -> messageHandler.sendMessage(channelSnowflake, message));
  }

  /**
   * Send a log message.
   *
   * @param message the message
   */
  public void sendLogMessage(String message) {
    channelHandler.getLogChannel()
        .forEach(channelSnowflake -> messageHandler.sendMessage(channelSnowflake, message));
  }

  /**
   * Gets the used command factory.
   *
   * @return the command factory
   */
  public Factory getCommandFactory() {
    return messageHandler.getCommandFactory();
  }

  // Channel

  /**
   * Gets all channel-{@link Snowflake}s.
   *
   * @return list of {@link Snowflake}s
   */
  public List<Snowflake> getAllChannels() {
    return channelHandler.getAllChannels();
  }

  /**
   * Gets the client interface of discord4j to discord.
   *
   * @return the client
   */
  public GatewayDiscordClient getClient() {
    return bot.getClient();
  }

  // Bot

  /**
   * Stop the bot.
   */
  public void stop() {
    bot.stop();
  }

  /**
   * Is the bot started.
   *
   * @return the boolean
   */
  public boolean isStarted() {
    return bot.isStarted();
  }

}
