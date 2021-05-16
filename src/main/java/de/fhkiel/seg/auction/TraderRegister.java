package de.fhkiel.seg.auction;

import discord4j.common.util.Snowflake;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Trader register  class representing a fleeting database. Holds all known traders. Singleton.
 */
public class TraderRegister {

  private static TraderRegister traderRegister = null;
  private Set<Trader> traderRegisterSet = new CopyOnWriteArraySet<>();

  private TraderRegister() {
  }

  /**
   * Gets the instance.
   *
   * @return the instance
   */
  public static synchronized TraderRegister getInstance() {
    if (traderRegister == null) {
      traderRegister = new TraderRegister();
    }
    return traderRegister;
  }

  /**
   * Reset the database.
   */
  static void reset() {
    traderRegister = null;
  }

  /**
   * Adds merchant to the database.
   *
   * @param trader the trader
   */
  public void addMerchant(Trader trader) {
    traderRegisterSet.add(trader);
  }

  /**
   * Gets all known merchants as a set.
   *
   * @return the merchants
   */
  public Set<Trader> getMerchants() {
    return traderRegisterSet;
  }

  /**
   * Gets a merchant with an id.
   *
   * @param id the id
   * @return the merchant enclosed in an optional. if not found the optional contains null.
   */
  public Optional<Trader> getMerchant(Snowflake id) {
    return traderRegisterSet.stream().filter(merchant -> merchant.getDiscordId().equals(id))
        .findFirst();
  }
}
