package de.fhkiel.seg.auction;

/**
 * The interface Auction listener. Used to inform all listeners on changes in the auction.
 */
public interface AuctionListener {
  /**
   * Send a message with changes in the auction.
   *
   * @param message the message
   */
  void auctionInfo(String message);
}
