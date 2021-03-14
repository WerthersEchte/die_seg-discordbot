package de.fhkiel.seg.auction;



import static org.assertj.core.api.Assertions.assertThat;


import discord4j.common.util.Snowflake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TraderRegisterTest {

  @BeforeEach
  void setUp(){
    TraderRegister.reset();
  }

  @Test
  void addMerchant() {
    final Snowflake firstSnowflake = Snowflake.of("0123");
    final Trader firstTrader = new Trader(firstSnowflake);

    assertThat(TraderRegister.getInstance().getMerchants()).hasSize(0);

    TraderRegister.getInstance().addMerchant(firstTrader);
    assertThat(TraderRegister.getInstance().getMerchants())
        .hasSize(1)
        .contains(firstTrader);


    final Snowflake secondSnowflake = Snowflake.of("01234");
    final Trader secondTrader = new Trader(secondSnowflake);

    TraderRegister.getInstance().addMerchant(secondTrader);
    assertThat(TraderRegister.getInstance().getMerchants())
        .hasSize(2)
        .contains(firstTrader, secondTrader);
  }

  @Test
  void addMerchantOnlyOnce() {
    final Snowflake snowflake = Snowflake.of("0123");
    final Trader trader = new Trader(snowflake);

    assertThat(TraderRegister.getInstance().getMerchants()).hasSize(0);

    TraderRegister.getInstance().addMerchant(trader);
    assertThat(TraderRegister.getInstance().getMerchants()).hasSize(1);

    TraderRegister.getInstance().addMerchant(trader);
    assertThat(TraderRegister.getInstance().getMerchants()).hasSize(1);

    final Trader clonedTrader = new Trader(snowflake);

    TraderRegister.getInstance().addMerchant(clonedTrader);
    assertThat(TraderRegister.getInstance().getMerchants()).hasSize(1);
  }

  @Test
  void getMerchant() {
    final Snowflake firstSnowflake = Snowflake.of("0123");
    final Trader firstTrader = new Trader(firstSnowflake);

    final Snowflake secondSnowflake = Snowflake.of("01234");
    final Trader secondTrader = new Trader(secondSnowflake);

    assertThat(TraderRegister.getInstance().getMerchants()).hasSize(0);

    TraderRegister.getInstance().addMerchant(firstTrader);
    TraderRegister.getInstance().addMerchant(secondTrader);

    assertThat(TraderRegister.getInstance().getMerchants()).hasSize(2);

    assertThat(TraderRegister.getInstance().getMerchant(firstSnowflake)).containsSame(firstTrader);
    assertThat(TraderRegister.getInstance().getMerchant(secondSnowflake)).containsSame(
        secondTrader);


  }
}