package de.fhkiel.seg.commands;

import static de.fhkiel.seg.Configuration.cmdRegister;
import static de.fhkiel.seg.Configuration.infoChannelName;
import static de.fhkiel.seg.Configuration.prefix;
import static de.fhkiel.seg.Configuration.startingPoints;

import de.fhkiel.seg.auction.Trader;
import de.fhkiel.seg.auction.TraderRegister;
import de.fhkiel.seg.bot.ControlFacade;
import discord4j.common.util.Snowflake;

/**
 * Command to register as a trader. Usable by {@link UserType#ALL}
 */
public class CommandRegister implements Command {

  private ControlFacade control;

  @Override
  public void inject(ControlFacade control) {
    this.control = control;
  }

  @Override
  public String getAllowedChannel() {
    return infoChannelName();
  }

  @Override
  public String commandTag() {
    return cmdRegister();
  }

  @Override
  public String usage() {
    return "``" + commandTag() + "`\n"
        + "Needed Role: ``" + intendedUser() + "``\n"
        + "Registers User as a ``trader``\n"
        + "Returns with '" + prefix() + " <id> <starting points>'";
  }

  @Override
  public UserType intendedUser() {
    return UserType.ALL;
  }

  @Override
  public void process(String message, Snowflake sender) {
    TraderRegister.getInstance().addMerchant(new Trader(sender));
    control.sendInfoMessage(
        String.format("%s <@%s> %d", prefix(), sender.asString(), startingPoints()));
  }
}
