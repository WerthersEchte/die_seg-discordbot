package de.fhkiel.seg.auction;

import static de.fhkiel.seg.util.LoggerStore.logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Auctioneer class to officiate auctions.
 */
public class Auctioneer implements Runnable {

  private Auction currentAuction;
  private boolean running = true;
  private LetterGenerator generator;
  private List<AuctionListener> listeners = new ArrayList<>();

  /**
   * Instantiates a new Auctioneer.
   *
   * @param generator the letter generator
   */
  public Auctioneer(LetterGenerator generator) {
    this.generator = generator;
  }

  /**
   * Register a listener for auction changes.
   *
   * @param listener the listener
   */
  public void register(AuctionListener listener) {
    listeners.add(listener);
  }

  /**
   * Start the auctions.
   */
  public void startAuctions() {
    this.running = true;
    new Thread(this).start();
  }

  /**
   * Stop the auctions.
   */
  public void stopAuctions() {
    this.running = false;
  }

  @Override
  public void run() {
    listeners.forEach(auctionListener -> auctionListener.auctionInfo("Starting auctions"));
    while (running) {
      try {
        if (currentAuction == null || currentAuction.finished()) {
          currentAuction = new Auction(0, generator.getNextLetter(), listeners);
          AuctionRegister.getInstance().addAuction(currentAuction);
          currentAuction.start();
        }
        Thread.sleep(15);
      } catch (Exception auctionException) {
        logger().error("Something wrong with the auctions", auctionException);
      }
    }
    listeners.forEach(auctionListener -> auctionListener.auctionInfo("Stopping auctions"));
  }
}
