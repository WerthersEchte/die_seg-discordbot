package de.fhkiel.seg.commands;

import static de.fhkiel.seg.Configuration.cmdInfo;
import static de.fhkiel.seg.Configuration.infoChannelName;
import static de.fhkiel.seg.Configuration.prefix;
import static de.fhkiel.seg.Configuration.resMerchantInfo;
import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.auction.Trader;
import de.fhkiel.seg.auction.TraderRegister;
import de.fhkiel.seg.bot.ControlFacade;
import discord4j.common.util.Snowflake;
import java.util.Locale;

/**
 * Command to get info over traders and auctions. Usable by {@link UserType#TRADER}
 */
public class CommandInfo implements Command {

  private ControlFacade control;

  @Override
  public void inject(ControlFacade control) {
    this.control = control;
  }

  @Override
  public String commandTag() {
    return cmdInfo();
  }

  @Override
  public String usage() {
    return "``" + commandTag() + "``\n"
        + "Needed Role: ``" + intendedUser() + "``\n"
        + "Gives a space-separated List of all registered Traders";
  }

  @Override
  public UserType intendedUser() {
    return UserType.TRADER;
  }

  @Override
  public String getAllowedChannel() {
    return infoChannelName();
  }

  @Override
  public void process(String message, Snowflake sender) {
    String[] subCommand = message.toLowerCase(Locale.ROOT)
        .replace(Command.commandBuilder(commandTag()).toLowerCase(Locale.ROOT), "").trim()
        .split(" ");

    try {
      switch (subCommand[0]) {
        case "traders":
          StringBuilder merchants = new StringBuilder();
          for (Trader trader : TraderRegister.getInstance().getMerchants()) {
            merchants.append("<@").append(trader.getDiscordId().asString()).append("> ");
          }

          control.sendInfoMessage(
              String.format("%s %s %s", prefix(), resMerchantInfo(), merchants.toString()));
          return;
        default:
          control.sendInfoMessage(usage());
      }
    } catch (Exception e) {
      logger().error("Something in Info went wrong", e);
    }


  }
}
