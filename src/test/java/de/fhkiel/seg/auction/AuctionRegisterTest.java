package de.fhkiel.seg.auction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import discord4j.common.util.Snowflake;
import javax.swing.Action;
import org.junit.jupiter.api.Test;

class AuctionRegisterTest {

  @Test
  void getNextAuctionId() {
    assertThat(AuctionRegister.getNextAuctionId()).isNotEqualTo(AuctionRegister.getNextAuctionId());
  }

  @Test
  void findAuctionById() {
    final int id = 34;
    Auction auctionMock = mock(Auction.class);
    when(auctionMock.getId()).thenReturn(id);

    AuctionRegister.getInstance().addAuction(auctionMock);

    assertThat(AuctionRegister.getInstance().findAuctionById(id+1)).isEmpty();
    assertThat(AuctionRegister.getInstance().findAuctionById(id)).contains(auctionMock);
  }
}