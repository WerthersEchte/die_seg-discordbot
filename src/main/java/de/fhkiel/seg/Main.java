package de.fhkiel.seg;

import static de.fhkiel.seg.Configuration.setToken;
import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.bot.Bot;

/**
 * The starterclass. Only a carrier for the main function.
 */
public class Main {

  /**
   * The entry point of application. Can be started with the TOKEN as a parameter
   *
   * @param args the input arguments. if they are not empty, first argument will be used as TOKEN
   */
  public static void main(String[] args) {
    Bot bot = new Bot();
    try {
      Configuration.loadConfiguration();
      if (args.length > 0 && args[0] != null) {
        setToken(args[0]);
      }
      bot.start();
    } catch (Exception allThatFellThrough) {
      logger().error("Something went wrong!", allThatFellThrough);
      bot.stop();
    }
  }
}
