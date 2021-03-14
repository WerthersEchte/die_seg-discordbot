package de.fhkiel.seg.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.helpers.Util;


/**
 * A log4j logger store holding all loggers used in the application.
 * Makes ist so the need for extra static memebers holding logger redundant.
 */
public class LoggerStore {

  private static final Map<Class<?>, Logger> LOGGERS = new HashMap<>();

  private LoggerStore() {
  }

  /**
   * Get a logger tailored for the calling class.
   * If call for the first time, will create a logger for the calling class.
   *
   * @return the logger used by the calling class
   */
  public static Logger logger() {
    if (!LOGGERS.containsKey(Util.getCallingClass())) {
      LOGGERS.put(Util.getCallingClass(), LogManager.getLogger(Util.getCallingClass()));
    }
    return LOGGERS.get(Util.getCallingClass());
  }
}
