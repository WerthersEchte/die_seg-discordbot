package de.fhkiel.seg.auction;

import static de.fhkiel.seg.Configuration.auctionTime;
import static de.fhkiel.seg.Configuration.resEndAuction;
import static de.fhkiel.seg.Configuration.resStartAuction;
import static de.fhkiel.seg.Configuration.resWonAuction;
import static de.fhkiel.seg.util.LoggerStore.logger;

import discord4j.common.util.Snowflake;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Auction class to coordinate an auction.
 */
public class Auction {
  private final List<AuctionListener> listeners;
  private int id;
  private int bid;
  private String letter;
  private Snowflake bidder;
  private Timer timer;

  /**
   * Instantiates a new Auction.
   *
   * @param startingBid the starting bid
   * @param letter the letter to auction
   * @param listeners the current listeners
   */
  public Auction(int startingBid, String letter, List<AuctionListener> listeners) {
    this.listeners = listeners;

    id = AuctionRegister.getNextAuctionId();
    bid = startingBid;
    this.letter = letter;
    bidder = null;
  }

  /**
   * Entering a new bid. Returns true if successfull.
   *
   * @param newBid the new bid
   * @param bidder the bidder
   * @return the boolean representing the acceptance of the bid
   */
  public boolean newBid(int newBid, Snowflake bidder) {
    if (running() && this.bid < newBid) {
      this.bid = newBid;
      this.bidder = bidder;
      increaseTimer();
      return true;
    } else {
      logger().info("Bid {} from @{} is not accepted", newBid, bidder);
      return false;
    }
  }

  private void endAuction() {
    if (bidder != null) {
      listeners.forEach(auctionListener -> auctionListener.auctionInfo(
          String.format("%s %d <@%s> %d", resWonAuction(), id, bidder.asString(), bid)));
      TraderRegister.getInstance().getMerchant(bidder).ifPresent(value ->
          value.addSuccessfullBid(id, letter, bid));
    }
    listeners.forEach(auctionListener -> auctionListener
        .auctionInfo(String.format("%s %d", resEndAuction(), id)));
  }

  private void startTimer() {
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        endAuction();
        running = false;
      }
    }, auctionTime());
  }

  private void increaseTimer() {
    timer.cancel();
    timer.purge();
    startTimer();
  }

  /**
   * Cancel the auction.
   */
  public void cancelAuction() {
    timer.cancel();
    timer.purge();

    running = false;
  }

  private volatile boolean running = false;
  private volatile boolean started = false;

  /**
   * Is the auction running.
   *
   * @return the boolean
   */
  public boolean running() {
    return running;
  }

  /**
   * Has the auction been started.
   *
   * @return the boolean
   */
  public boolean started() {
    return started;
  }

  /**
   * Has the auction been finished.
   *
   * @return the boolean
   */
  public boolean finished() {
    return !running && started;
  }

  /**
   * Start the auction.
   */
  public void start() {
    listeners.forEach(auctionListener -> auctionListener
        .auctionInfo(String.format(resStartAuction(), this.id, letter)));
    logger().info("Starting Auction [{}] - {} [{}]", this.id, letter, this.bid);

    started = true;
    running = true;

    startTimer();
  }

  /**
   * Gets id of the auction.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets current highest bid.
   *
   * @return the bid
   */
  public int getBid() {
    return bid;
  }

  /**
   * Gets the auctioned letter.
   *
   * @return the letter
   */
  public String getLetter() {
    return letter;
  }

  /**
   * Gets the current highest bidder.
   *
   * @return the bidder
   */
  public Snowflake getBidder() {
    return bidder;
  }
}
