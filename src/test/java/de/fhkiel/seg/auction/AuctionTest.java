package de.fhkiel.seg.auction;


import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import de.fhkiel.seg.Configuration;
import discord4j.common.util.Snowflake;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class AuctionTest {

  private static final long TIMEOFAUCTION = 100L;
  private static final int ID = 22;

  static MockedStatic<Configuration> mockedConfiguration;

  @BeforeAll
  static void setUp(){
    mockedConfiguration = mockStatic(Configuration.class);
    mockedConfiguration.when(Configuration::auctionTime).thenReturn(TIMEOFAUCTION);

    MockedStatic<AuctionRegister> mockedAuctionRegister = mockStatic(AuctionRegister.class);
    mockedAuctionRegister.when(AuctionRegister::getNextAuctionId).thenReturn(ID);
  }

  @AfterAll
  static void tearDown(){
    mockedConfiguration.close();
  }

  @Test
  void newAuction() {
    final int startingBid = 11;
    final String letter = "P";
    Auction auctionUnderTest = new Auction(startingBid, letter, new ArrayList<>());

    assertThat(auctionUnderTest.getId()).isEqualTo(ID);
    assertThat(auctionUnderTest.getLetter()).isEqualTo(letter);
    assertThat(auctionUnderTest.getBid()).isEqualTo(startingBid);
    assertThat(auctionUnderTest.getBidder()).isNull();

    assertThat(auctionUnderTest.running()).isFalse();
    assertThat(auctionUnderTest.started()).isFalse();
    assertThat(auctionUnderTest.finished()).isFalse();

  }

  @Test
  void startedAuctionEnds() {
    final int startingBid = 11;
    final String letter = "P";
    Auction auctionUnderTest = new Auction(startingBid, letter, new ArrayList<>());

    auctionUnderTest.start();

    assertThat(auctionUnderTest.running()).isTrue();
    assertThat(auctionUnderTest.started()).isTrue();
    assertThat(auctionUnderTest.finished()).isFalse();

    await().atMost(TIMEOFAUCTION*5, TimeUnit.MILLISECONDS).until(auctionUnderTest::finished);

    assertThat(auctionUnderTest.running()).isFalse();
    assertThat(auctionUnderTest.started()).isTrue();
  }

  @Test
  void bidGetsAccepted() {
    final Snowflake bidder = Snowflake.of("123");
    final int bid = 10;

    final int startingBid = 1;
    final String letter = "P";
    Auction auctionUnderTest = new Auction(startingBid, letter, new ArrayList<>());

    auctionUnderTest.start();
    assertThat(auctionUnderTest.newBid(bid, bidder)).isTrue();

    assertThat(auctionUnderTest.getBidder()).isEqualTo(bidder);
    assertThat(auctionUnderTest.getBid()).isEqualTo(bid);

    auctionUnderTest.cancelAuction();
  }

  @Test
  void bidGetsRejected() {
    final Snowflake bidderOne = Snowflake.of("123");
    final int bidOne = 10;
    final Snowflake bidderTwo = Snowflake.of("1234");
    final int bidTwo = 0;

    final int startingBid = 1;
    final String letter = "P";
    Auction auctionUnderTest = new Auction(startingBid, letter, new ArrayList<>());

    assertThat(auctionUnderTest.newBid(bidOne, bidderOne)).isFalse();

    assertThat(auctionUnderTest.getBidder()).isNull();
    assertThat(auctionUnderTest.getBid()).isEqualTo(startingBid);

    auctionUnderTest.start();
    assertThat(auctionUnderTest.newBid(bidTwo, bidderOne)).isFalse();

    assertThat(auctionUnderTest.getBidder()).isNull();
    assertThat(auctionUnderTest.getBid()).isEqualTo(startingBid);

    auctionUnderTest.newBid(bidOne, bidderOne);
    assertThat(auctionUnderTest.newBid(bidTwo, bidderTwo)).isFalse();

    assertThat(auctionUnderTest.getBidder()).isNotEqualTo(bidderTwo);
    assertThat(auctionUnderTest.getBid()).isEqualTo(bidOne);

    auctionUnderTest.cancelAuction();
    assertThat(auctionUnderTest.newBid(bidOne + 1, bidderTwo)).isFalse();

    assertThat(auctionUnderTest.getBidder()).isNotEqualTo(bidderTwo);
    assertThat(auctionUnderTest.getBid()).isEqualTo(bidOne);
  }

  @Test
  void bidOverOther() {
    final Snowflake bidderOne = Snowflake.of("123");
    final int bidOne = 10;
    final Snowflake bidderTwo = Snowflake.of("1234");
    final int bidTwo = 12;

    final int startingBid = 1;
    final String letter = "P";
    Auction auctionUnderTest = new Auction(startingBid, letter, new ArrayList<>());

    auctionUnderTest.start();
    assertThat(auctionUnderTest.newBid(bidOne, bidderOne)).isTrue();

    assertThat(auctionUnderTest.getBidder()).isEqualTo(bidderOne);
    assertThat(auctionUnderTest.getBid()).isEqualTo(bidOne);

    assertThat(auctionUnderTest.newBid(bidTwo, bidderTwo)).isTrue();

    assertThat(auctionUnderTest.getBidder()).isEqualTo(bidderTwo);
    assertThat(auctionUnderTest.getBid()).isEqualTo(bidTwo);

    auctionUnderTest.cancelAuction();
  }

  @Test
  void auctionSellsToHighestBidder() {
    final Snowflake bidder = Snowflake.of("123");
    final int bid = 10;

    Trader trader = mock(Trader.class);
    when(trader.getDiscordId()).thenReturn(bidder);

    TraderRegister.reset();
    TraderRegister.getInstance().addMerchant(trader);

    final int startingBid = 0;
    final String letter = "P";
    Auction auctionUnderTest = new Auction(startingBid, letter, new ArrayList<>());

    auctionUnderTest.start();
    assertThat(auctionUnderTest.newBid(bid, bidder)).isTrue();

    await().atMost(TIMEOFAUCTION*5, TimeUnit.MILLISECONDS).until(auctionUnderTest::finished);

    verify(trader, times(1)).addSuccessfullBid(ID, letter, bid);
  }

  @Test
  void auctionCancels() {
    final Snowflake bidder = Snowflake.of("123");
    final int bid = 10;

    Trader trader = mock(Trader.class);
    when(trader.getDiscordId()).thenReturn(bidder);

    TraderRegister.reset();
    TraderRegister.getInstance().addMerchant(trader);

    final int startingBid = 0;
    final String letter = "P";
    Auction auctionUnderTest = new Auction(startingBid, letter, new ArrayList<>());

    auctionUnderTest.start();
    assertThat(auctionUnderTest.newBid(bid, bidder)).isTrue();

    auctionUnderTest.cancelAuction();

    assertThat(auctionUnderTest.getBidder()).isEqualTo(bidder);
    assertThat(auctionUnderTest.getBid()).isEqualTo(bid);

    assertThat(auctionUnderTest.running()).isFalse();
    assertThat(auctionUnderTest.started()).isTrue();
    assertThat(auctionUnderTest.finished()).isTrue();

    verify(trader, times(0)).addSuccessfullBid(ID, letter, bid);
  }

}