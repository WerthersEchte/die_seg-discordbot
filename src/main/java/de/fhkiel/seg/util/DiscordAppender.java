package de.fhkiel.seg.util;

import de.fhkiel.seg.bot.ControlFacade;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 * A logging appender for log4j printing logs to discord.
 */
@Plugin(name = "DiscordAppender", category = "Core", elementType = "appender", printObject = true)
public class DiscordAppender extends AbstractAppender {

  private static ControlFacade control;

  protected DiscordAppender(String name, Filter filter) {
    super(name, filter, null, true, null);
  }

  /**
   * Inject an interface to the bot for communicating with discord.
   *
   * @param control the control interface
   */
  public static void inject(ControlFacade control) {
    DiscordAppender.control = control;
  }

  @PluginFactory
  public static DiscordAppender createAppender(
      @PluginAttribute("name") String name,
      @PluginElement("Filter") Filter filter) {
    return new DiscordAppender(name, filter);
  }

  @Override
  public void append(LogEvent event) {
    if (control != null && control.isStarted()) {
      control.sendLogMessage("[" + event.getLevel() + "] " + event.getLoggerName() + ": "
          + event.getMessage().getFormattedMessage());
    }
  }
}
