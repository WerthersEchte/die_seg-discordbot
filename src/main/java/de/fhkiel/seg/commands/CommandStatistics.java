package de.fhkiel.seg.commands;

import static de.fhkiel.seg.Configuration.cmdStatistics;
import static de.fhkiel.seg.Configuration.infoChannelName;

import de.fhkiel.seg.auction.Trader;
import de.fhkiel.seg.auction.TraderRegister;
import de.fhkiel.seg.bot.ControlFacade;
import discord4j.common.util.Snowflake;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Command to display statistics of traders and auctions. Usable by {@link UserType#ADMIN}
 */
public class CommandStatistics implements Command {

  private ControlFacade control;

  @Override
  public void inject(ControlFacade control) {
    this.control = control;
  }

  @Override
  public String commandTag() {
    return cmdStatistics();
  }

  @Override
  public String usage() {
    return "``" + commandTag() + "``\n"
        + "Needed Role: ``" + intendedUser() + "``\n"
        + "Shows detailed statistics from trades and traders";
  }

  @Override
  public UserType intendedUser() {
    return UserType.ADMIN;
  }

  @Override
  public String getAllowedChannel() {
    return infoChannelName();
  }

  @Override
  public void process(String message, Snowflake sender) {
    StringBuilder statisticText = new StringBuilder();
    statisticText.append("Statistics:\n");
    Map<String, Integer> soldLetters = new HashMap<>();
    TraderRegister.getInstance().getMerchants()
        .forEach(merchant -> merchant.getSuccessfulBids()
            .forEach(successfulBid -> soldLetters.put(successfulBid.letter,
                soldLetters.getOrDefault(successfulBid.letter, 0) + 1)));
    statisticText.append("Number of sold Letters: ")
        .append(soldLetters.values().stream().mapToInt(Integer::intValue).sum()).append(" ");

    final StringJoiner lettersJoiner = new StringJoiner(", ", "[", "]\n");
    soldLetters.keySet().stream().sorted()
        .forEach(s -> lettersJoiner.add(s + ":" + soldLetters.get(s)));
    statisticText.append(lettersJoiner.toString());

    statisticText.append("Number of starting points: ").append(
        TraderRegister.getInstance().getMerchants().stream().map(Trader::getPoints)
            .mapToInt(Integer::intValue).sum()).append("\n");
    statisticText.append("Number of used points: ").append(
        TraderRegister.getInstance().getMerchants().stream().flatMap(
            merchant -> merchant.getSuccessfulBids().stream()
                .map(successfulBid -> successfulBid.cost)).mapToInt(Integer::intValue).sum())
        .append("\n");
    statisticText.append("\n\nTraders:\n");

    for (Trader trader : TraderRegister.getInstance().getMerchants()) {
      statisticText.append("<@").append(trader.getDiscordId().asString()).append("> ");
      statisticText.append(
          trader.getSuccessfulBids().stream().mapToInt(successfulBid -> successfulBid.cost)
              .sum()).append("/").append(trader.getPoints()).append(" ");

      final StringJoiner bidJoiner = new StringJoiner(", ", "[", "]\n");
      trader.getSuccessfulBids()
          .forEach(
              successfulBid -> bidJoiner.add(successfulBid.letter + "->" + successfulBid.cost));
      statisticText.append(bidJoiner.toString());
    }

    control.sendInfoMessage(statisticText.toString());
  }
}
