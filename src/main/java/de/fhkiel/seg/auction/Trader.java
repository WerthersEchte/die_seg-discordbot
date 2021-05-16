package de.fhkiel.seg.auction;

import static de.fhkiel.seg.Configuration.startingPoints;

import discord4j.common.util.Snowflake;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Trader class representing a trader using this application to bid for letters.
 */
public class Trader {

  private final Snowflake discordId;
  private final int startingPoints;
  /**
   * List of successful bids of this trader.
   */
  List<SuccessfullBid> successfulBids = new ArrayList<>();

  /**
   * Instantiates a new Trader.
   *
   * @param discordId the discord id
   */
  public Trader(Snowflake discordId) {
    this.discordId = discordId;
    startingPoints = startingPoints();
  }

  /**
   * Gets discord id.
   *
   * @return the discord id
   */
  public Snowflake getDiscordId() {
    return discordId;
  }

  /**
   * Gets the starting points.
   *
   * @return the points
   */
  public int getPoints() {
    return startingPoints;
  }

  /**
   * Adds a successful bid.
   *
   * @param id the id
   * @param letter the letter
   * @param cost the cost
   */
  public void addSuccessfullBid(int id, String letter, int cost) {
    successfulBids.add(new SuccessfullBid(id, letter, cost));
  }

  /**
   * Gets the successful bids as list.
   *
   * @return the successful bids
   */
  public List<SuccessfullBid> getSuccessfulBids() {
    return successfulBids;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Trader trader = (Trader) o;
    return Objects.equals(discordId, trader.discordId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(discordId);
  }

  /**
   * successful bid storage class.
   */
  public class SuccessfullBid {

    /**
     * The Id of the auction.
     */
    public final int id;
    /**
     * The Letter auctioned.
     */
    public final String letter;
    /**
     * The Cost of the auctioned letter.
     */
    public final int cost;

    /**
     * Instantiates a new successful bid.
     *
     * @param id the Id of the auction
     * @param letter the letter auctioned
     * @param cost the cost of the auctioned letter
     */
    public SuccessfullBid(int id, String letter, int cost) {
      this.id = id;
      this.letter = letter;
      this.cost = cost;
    }
  }
}
