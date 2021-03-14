package de.fhkiel.seg;

import static de.fhkiel.seg.util.LoggerStore.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

/**
 * The Configuration class.
 * Stores all changable parameters. Can be set through the config.json file
 */
public class Configuration {

  private static Configuration configuration = new Configuration();
  private String token = "";
  private String[] admins = new String[0];
  private String botName = "DIE SEG";
  private long auctionTime = 5000L;    //1000 = 1s
  private int startingPoints = 1000;
  private String auctionChannelName = "auction";
  private String infoChannelName = "info";
  private String logChannelName = "log";
  private String cmdPrefix = "!SEG";
  private String cmdAuction = "auction";
  private String cmdInfo = "info";
  private String cmdRegister = "register";
  private String cmdKill = "kill";
  private String cmdClear = "clear";
  private String cmdHelp = "help";
  private String cmdStatistics = "stats";
  private String resStartAuction = "auction start %d %s";
  private String resEndAuction = "auction closed";
  private String resWonAuction = "auction won";
  private String resHigherBid = "auction bid %d %d <@%s>";
  private String resRegistered = "registered";
  private String resMerchantInfo = "info traders";

  private Configuration() {
  }

  /**
   * Load configuration from the file secified in the parameter.
   *
   * @param configFile the config file
   */
  public static void loadConfiguration(String configFile) {
    Gson gson = new Gson();
    try (JsonReader reader = new JsonReader(new FileReader(configFile))) {
      configuration = gson.fromJson(reader, Configuration.class);
    } catch (IOException io) {
      logger().error("Could not load Config file!", io);
    }
  }

  /**
   * Load configuration from the file 'config.json'.
   */
  public static void loadConfiguration() {
    loadConfiguration("config.json");
  }

  /**
   * Export configuration to  the file secified in the parameter.
   *
   * @param configFile the config file to write
   */
  public static void exportConfiguration(String configFile) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (FileWriter writer = new FileWriter(configFile)) {
      gson.toJson(configuration, writer);
    } catch (IOException io) {
      logger().error("Could not export Configuration!", io);
    }
  }

  /**
   * Token string. Used to authenticate bot
   *
   * @return the token string
   */
  public static String token() {
    return configuration.token;
  }

  /**
   * Sets the token.
   *
   * @param token the token
   */
  public static void setToken(String token) {
    configuration.token = token;
  }

  /**
   * Prefix string.
   *
   * @return the string
   */
  public static String prefix() {
    return configuration.cmdPrefix;
  }

  /**
   * Bot name.
   *
   * @return the string
   */
  public static String botName() {
    return configuration.botName;
  }

  /**
   * Admins tag in a string array.
   *
   * @return the string array with all admin tags
   */
  public static String[] admins() {
    return configuration.admins;
  }

  /**
   * Action channel name.
   *
   * @return the string
   */
  public static String actionChannelName() {
    return configuration.auctionChannelName;
  }

  /**
   * Info channel name.
   *
   * @return the string
   */
  public static String infoChannelName() {
    return configuration.infoChannelName;
  }

  /**
   * Log channel name.
   *
   * @return the string
   */
  public static String logChannelName() {
    return configuration.logChannelName;
  }

  /**
   * Starting points.
   *
   * @return the int
   */
  public static int startingPoints() {
    return configuration.startingPoints;
  }

  /**
   * The command used to intercat with an auction.
   *
   * @return the string
   */
  public static String cmdAuction() {
    return configuration.cmdAuction;
  }

  /**
   * The command used to show info.
   *
   * @return the string
   */
  public static String cmdInfo() {
    return configuration.cmdInfo;
  }

  /**
   * The command used to register.
   *
   * @return the string
   */
  public static String cmdRegister() {
    return configuration.cmdRegister;
  }

  /**
   * The command used to kill the bot.
   *
   * @return the string
   */
  public static String cmdKill() {
    return configuration.cmdKill;
  }

  /**
   * The command used to clear all bot-channels.
   *
   * @return the string
   */
  public static String cmdClear() {
    return configuration.cmdClear;
  }

  /**
   * The command used to show statistics.
   *
   * @return the string
   */
  public static String cmdStatistics() {
    return configuration.cmdStatistics;
  }

  /**
   * The command used to show help.
   *
   * @return the string
   */
  public static String cmdHelp() {
    return configuration.cmdHelp;
  }

  /**
   * Response when starting an auction.
   *
   * @return the string
   */
  public static String resStartAuction() {
    return configuration.resStartAuction;
  }

  /**
   * Response when ending an auction.
   *
   * @return the string
   */
  public static String resEndAuction() {
    return configuration.resEndAuction;
  }

  /**
   * Response on winning an auction.
   *
   * @return the string
   */
  public static String resWonAuction() {
    return configuration.resWonAuction;
  }

  /**
   * Response when recieving a higher bid.
   *
   * @return the string
   */
  public static String resHigherBid() {
    return configuration.resHigherBid;
  }

  /**
   * Auction time in millseconds.
   *
   * @return the long
   */
  public static long auctionTime() {
    return configuration.auctionTime;
  }

  /**
   * Response on registering.
   *
   * @return the string
   */
  public static String resRegistered() {
    return configuration.resRegistered;
  }

  /**
   * Respose for merchant info.
   *
   * @return the string
   */
  public static String resMerchantInfo() {
    return configuration.resMerchantInfo;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Configuration.class.getSimpleName() + "[", "]")
        .add("token='" + token + "'")
        .add("botName='" + botName + "'")
        .add("auctionChannelName='" + auctionChannelName + "'")
        .add("infoChannelName='" + infoChannelName + "'")
        .add("logChannelName='" + logChannelName + "'")
        .add("startingPoints=" + startingPoints)
        .add("cmdPrefix='" + cmdPrefix + "'")
        .add("cmdAuction='" + cmdAuction + "'")
        .add("cmdInfo='" + cmdInfo + "'")
        .add("cmdRegister='" + cmdRegister + "'")
        .add("cmdKill='" + cmdKill + "'")
        .add("cmdClear='" + cmdClear + "'")
        .add("cmdHelp='" + cmdHelp + "'")
        .add("cmdStatistics='" + cmdStatistics + "'")
        .add("resStartAuction='" + resStartAuction + "'")
        .add("resEndAuction='" + resEndAuction + "'")
        .add("resWonAuction='" + resWonAuction + "'")
        .add("resHigherBid='" + resHigherBid + "'")
        .add("auctionTime=" + auctionTime)
        .add("resRegistered='" + resRegistered + "'")
        .add("resMerchantInfo='" + resMerchantInfo + "'")
        .toString();
  }
}
