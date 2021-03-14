package de.fhkiel.seg.bot;

import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.Configuration;
import de.fhkiel.seg.auction.AuctionListener;
import de.fhkiel.seg.auction.Auctioneer;
import de.fhkiel.seg.auction.LetterGenerator;
import de.fhkiel.seg.commands.Command;
import de.fhkiel.seg.commands.CommandHelp;
import de.fhkiel.seg.util.DiscordAppender;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;

/**
 * The base class of this application.
 * Contains and maintains the connection to discord.
 */
public class Bot implements AuctionListener {

  private GatewayDiscordClient client;
  private boolean isStarted = false;

  private Auctioneer auctioneer;
  private ControlFacade control;

  /**
   * Start the bot. Connects to discord and when successfull, start all the other services.
   */
  public void start() {
    logger().debug("Connecting to server");
    client = DiscordClientBuilder.create(Configuration.token())
        .build()
        .login()
        .block();

    if (client != null) {
      ChannelHandler channelHandler = new ChannelHandler(this);
      MessageHandler messageHandler = new MessageHandler(this, channelHandler);
      control = new ControlFacade(this, messageHandler, channelHandler);

      DiscordAppender.inject(control);

      logger().debug("Setting messagehandler");
      setMessageHandler(messageHandler);

      logger().debug("Bot fully started");
      isStarted = true;

      control.sendInfoMessage("Bot started\n"
          + "For help use command " + Command.commandBuilder(new CommandHelp().commandTag()));

      auctioneer = new Auctioneer(new LetterGenerator());
      auctioneer.register(this);
      auctioneer.startAuctions();

      client.onDisconnect().block();
    }
  }

  /**
   * Sets a message handler to handle incomming messages.
   *
   * @param messageHandler the message handler to handle messages
   */
  public void setMessageHandler(MessageHandler messageHandler) {
    client.getEventDispatcher().on(MessageCreateEvent.class)
        .subscribe(message -> messageHandler.handleMessage(message.getMessage()));
  }

  /**
   * Returns True when the server has been started.
   *
   * @return true when started
   */
  public boolean isStarted() {
    return isStarted;
  }

  /**
   * Gets client interface from dicord4j to discord.
   *
   * @return the client
   */
  public GatewayDiscordClient getClient() {
    return client;
  }

  /**
   * Stops the bot and gracefully disconnects and closes all services.
   */
  public void stop() {
    if (isStarted()) {
      auctioneer.stopAuctions();
      client.logout().block();
    }
  }

  @Override
  public void auctionInfo(String message) {
    control.sendAuctionMessage(message);
  }
}
