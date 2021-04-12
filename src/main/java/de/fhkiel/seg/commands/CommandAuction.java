package de.fhkiel.seg.commands;

import static de.fhkiel.seg.Configuration.*;
import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.auction.Auction;
import de.fhkiel.seg.auction.AuctionRegister;
import de.fhkiel.seg.bot.ControlFacade;
import discord4j.common.util.Snowflake;
import java.util.Locale;
import java.util.Optional;

/**
 * Command to interact with an auction. Usable by {@link UserType#TRADER}
 */
public class CommandAuction implements Command {

  private ControlFacade control;

  @Override
  public void inject(ControlFacade control) {
    this.control = control;
  }

  @Override
  public String commandTag() {
    return cmdAuction();
  }

  @Override
  public String usage() {
    return "``" + commandTag()  + " bid <id> <points> ``\n"
        + "Needed Role: ``" + intendedUser() + "``\n"
        + "Places a bid with value ``<points>`` on the auction with ``<ID>``";
  }

  @Override
  public UserType intendedUser() {
    return UserType.TRADER;
  }

  @Override
  public String getAllowedChannel() {
    return actionChannelName();
  }

  @Override
  public void process(String message, Snowflake sender) {
    String[] subCommand = message.toLowerCase(Locale.ROOT)
        .replace(Command.commandBuilder(commandTag()).toLowerCase(Locale.ROOT), "").trim()
        .split(" ");

    try {
      switch (subCommand[0]) {
        case "bid":
          int auctionId = Integer.parseInt(subCommand[1]);
          int bid = Integer.parseInt(subCommand[2]);
          Optional<Auction> auction = AuctionRegister.getInstance().findAuctionById(auctionId);

          logger().info("Bid from @{} - Auction[{}] bid[{}]", sender, auctionId, bid);
          if (auction.isPresent() && auction.get().newBid(bid, sender)) {
            logger().info("@{} is new highest bidder with {}", sender, bid);
            control.sendAuctionMessage(
                String.format("%s %s %d <@%s> %d", prefix(), resHigherBid(), auctionId, sender.asString(), bid));
          } else {
            logger().info("Auction does not exist");
          }
          return;
        default:
          control.sendInfoMessage(usage());
      }
    } catch (Exception e) {
      logger().error("Something in Auction went wrong", e);
    }
  }
}
