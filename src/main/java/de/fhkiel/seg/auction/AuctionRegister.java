package de.fhkiel.seg.auction;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Auction register class representing a fleeting database. Holds all auctions. Singleton.
 */
public class AuctionRegister {
  private static int nextAuctionId = 1;
  private static AuctionRegister auctionRegister = null;
  /**
   * The Auction register set.
   */
  Set<Auction> auctionRegisterSet = new HashSet<>();

  private AuctionRegister() {
  }

  /**
   * Gets the instance.
   *
   * @return the instance
   */
  public static AuctionRegister getInstance() {
    if (auctionRegister == null) {
      auctionRegister = new AuctionRegister();
    }
    return auctionRegister;
  }

  /**
   * Gets the next auction id.
   *
   * @return the next auction id
   */
  public static synchronized int getNextAuctionId() {
    return nextAuctionId++;
  }

  /**
   * Adds an auction.
   *
   * @param auction the auction
   */
  public void addAuction(Auction auction) {
    auctionRegisterSet.add(auction);
  }

  /**
   * Find the auction with ther id. Returns an optional.
   *
   * @param id the id of the auction
   * @return the optional containing the found auction. if not found contains null.
   */
  public Optional<Auction> findAuctionById(int id) {
    return auctionRegisterSet.stream().filter(auction -> id == auction.getId()).findFirst();
  }
}