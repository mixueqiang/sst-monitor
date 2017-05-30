package cc.walkup.task.utils;

import java.util.Date;

import org.apache.commons.logging.Log;

/**
 * @author mixueqiang
 * @since May 5, 2017
 *
 */
public final class LogUtils {
  public static final boolean enableConsole = true;
  public static final boolean enableDebug = true;
  public static final boolean enableInfo = true;
  public static final boolean enableWarn = true;
  public static final boolean enableError = true;
  public static final boolean bufferedOutput = false;

  @SuppressWarnings("deprecation")
  public static void console(String message, Object... params) {
    if (!enableConsole)
      return;

    String msg = StringUtils.format(message, params);
    System.out.println(new Date().toLocaleString() + " - " + msg);
  }

  public static void debug(Log log, long startTime, long threshold, String message, Object... params) {
    if (!enableDebug)
      return;

    long costtime = isOnThreshold(startTime, threshold, message);
    if (costtime != -1) {
      message = formatCosttime(message, costtime);
      debug(log, message, params);
    }
  }

  public static void debug(Log log, String message, Object... params) {
    if (!enableDebug)
      return;

    log.debug(StringUtils.format(message, params));
  }

  public static void error(Log log, String message, Object... params) {
    if (!enableError)
      return;

    log.error(StringUtils.format(message, params));
  }

  public static void error(Log log, Throwable t, String message, Object... params) {
    if (!enableError)
      return;

    String info = StringUtils.format(message, params);
    if (t == null)
      log.error(info);
    else
      log.error(info, t);
  }

  /**
   * "cost time {t}" will be replaced to "cost time 100"
   */
  public static String formatCosttime(String message, long costtime) {
    return message.replaceAll("\\{t(ime)?\\}", String.valueOf(costtime));
  }

  public static void info(Log log, long startTime, long threshold, String message, Object... params) {
    if (!enableInfo)
      return;

    long costtime = isOnThreshold(startTime, threshold, message);
    if (costtime != -1) {
      message = formatCosttime(message, costtime);
      info(log, message, params);
    }
  }

  public static void info(Log log, String message, Object... params) {
    if (!enableInfo)
      return;

    log.info(StringUtils.format(message, params));
  }

  public static void warn(Log log, long startTime, long threshold, String message, Object... params) {
    if (!enableWarn)
      return;

    long costtime = isOnThreshold(startTime, threshold, message);
    if (costtime != -1) {
      message = formatCosttime(message, costtime);
      warn(log, message, params);
    }
  }

  public static void warn(Log log, String message, Object... params) {
    if (!enableWarn)
      return;

    log.warn(StringUtils.format(message, params));
  }

  private static long isOnThreshold(long startTime, long threshold, String message) {
    long costtime = System.currentTimeMillis() - startTime;
    return costtime >= threshold ? costtime : -1;
  }

}
