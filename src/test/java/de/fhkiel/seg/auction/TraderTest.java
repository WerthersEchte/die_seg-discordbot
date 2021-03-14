package de.fhkiel.seg.auction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;


import de.fhkiel.seg.Configuration;
import discord4j.common.util.Snowflake;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class TraderTest {

  static final int STARTING_POINTS = 777;

  static MockedStatic<Configuration> mockedConfiguration;

  @BeforeAll
  static void setUp(){
    mockedConfiguration = mockStatic(Configuration.class);
    mockedConfiguration.when(Configuration::startingPoints).thenReturn(STARTING_POINTS);
  }

  @AfterAll
  static void tearDown(){
    mockedConfiguration.close();
  }

  @Test
  void createMerchant() {
    final Snowflake snowflake = Snowflake.of("0123");
    Trader traderUnderTest = new Trader(snowflake);

    assertThat(traderUnderTest.getDiscordId()).isEqualTo(snowflake);
    assertThat(traderUnderTest.getPoints()).isEqualTo(STARTING_POINTS);

    assertThat(traderUnderTest.getSuccessfullBids()).isEmpty();
  }

  @Test
  void addSuccessFullBid() {
    final Snowflake snowflake = Snowflake.of("0123");
    Trader traderUnderTest = new Trader(snowflake);

    final int firstId = 5;
    final String firstLetter = "A";
    final int firstCost = 10;

    traderUnderTest.addSuccessfullBid(firstId, firstLetter, firstCost);
    assertThat(traderUnderTest.getSuccessfullBids()).hasSize(1);
    assertThat(traderUnderTest
        .getSuccessfullBids().stream().filter(successfullBid -> successfullBid.id == firstId && successfullBid.letter == firstLetter && successfullBid.cost == firstCost).count())
        .isEqualTo(1);

    final int secondId = 7;
    final String secondLetter = "X";
    final int secondCost = 7;

    traderUnderTest.addSuccessfullBid(secondId, secondLetter, secondCost);
    assertThat(traderUnderTest.getSuccessfullBids()).hasSize(2);
    assertThat(traderUnderTest
        .getSuccessfullBids().stream().filter(successfullBid -> successfullBid.id == firstId && successfullBid.letter == firstLetter && successfullBid.cost == firstCost).count())
        .isEqualTo(1);
    assertThat(traderUnderTest
        .getSuccessfullBids().stream().filter(successfullBid -> successfullBid.id == secondId && successfullBid.letter == secondLetter && successfullBid.cost == secondCost).count())
        .isEqualTo(1);
  }
}