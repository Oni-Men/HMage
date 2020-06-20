package onimen.anni.hmage.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class HMageLogger implements Logger {

  private Logger logger;

  public volatile static boolean printOnDisplay;

  public HMageLogger(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void catching(Level level, Throwable t) {
    logger.catching(level, t);
  }

  @Override
  public void catching(Throwable t) {
    logger.catching(t);
  }

  @Override
  public void debug(Marker marker, Message msg) {
    logger.debug(marker, msg);
  }

  @Override
  public void debug(Marker marker, Message msg, Throwable t) {
    logger.debug(marker, msg, t);
  }

  @Override
  public void debug(Marker marker, MessageSupplier msgSupplier) {
    logger.debug(marker, msgSupplier);
  }

  @Override
  public void debug(Marker marker, MessageSupplier msgSupplier, Throwable t) {
    logger.debug(marker, msgSupplier, t);
  }

  @Override
  public void debug(Marker marker, CharSequence message) {
    logger.debug(marker, message);
  }

  @Override
  public void debug(Marker marker, CharSequence message, Throwable t) {
    logger.debug(marker, message, t);
  }

  @Override
  public void debug(Marker marker, Object message) {
    logger.debug(marker, message);
  }

  @Override
  public void debug(Marker marker, Object message, Throwable t) {
    logger.debug(marker, message, t);
  }

  @Override
  public void debug(Marker marker, String message) {
    logger.debug(marker, message);
  }

  @Override
  public void debug(Marker marker, String message, Object... params) {
    logger.debug(marker, message, params);
  }

  @Override
  public void debug(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logger.debug(marker, message, paramSuppliers);
  }

  @Override
  public void debug(Marker marker, String message, Throwable t) {
    logger.debug(marker, message, t);
  }

  @Override
  public void debug(Marker marker, Supplier<?> msgSupplier) {
    logger.debug(marker, msgSupplier);
  }

  @Override
  public void debug(Marker marker, Supplier<?> msgSupplier, Throwable t) {
    logger.debug(marker, msgSupplier, t);
  }

  @Override
  public void debug(Message msg) {
    logger.debug(msg);
  }

  @Override
  public void debug(Message msg, Throwable t) {
    logger.debug(msg, t);
  }

  @Override
  public void debug(MessageSupplier msgSupplier) {
    logger.debug(msgSupplier);
  }

  @Override
  public void debug(MessageSupplier msgSupplier, Throwable t) {
    logger.debug(msgSupplier, t);
  }

  @Override
  public void debug(CharSequence message) {
    logger.debug(message);
  }

  @Override
  public void debug(CharSequence message, Throwable t) {
    logger.debug(message, t);
  }

  @Override
  public void debug(Object message) {
    logger.debug(message);
  }

  @Override
  public void debug(Object message, Throwable t) {
    logger.debug(message, t);
  }

  @Override
  public void debug(String message) {
    logger.debug(message);
  }

  @Override
  public void debug(String message, Object... params) {
    logger.debug(message, params);
  }

  @Override
  public void debug(String message, Supplier<?>... paramSuppliers) {
    logger.debug(message, paramSuppliers);
  }

  @Override
  public void debug(String message, Throwable t) {
    logger.debug(message, t);
  }

  @Override
  public void debug(Supplier<?> msgSupplier) {
    logger.debug(msgSupplier);
  }

  @Override
  public void debug(Supplier<?> msgSupplier, Throwable t) {
    logger.debug(msgSupplier, t);
  }

  @Override
  public void debug(Marker marker, String message, Object p0) {
    logger.debug(marker, message, p0);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1) {
    logger.debug(marker, message, p0, p1);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2) {
    logger.debug(marker, message, p0, p1, p2);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logger.debug(marker, message, p0, p1, p2, p3);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.debug(marker, message, p0, p1, p2, p3, p4);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.debug(marker, message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6) {
    logger.debug(marker, message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7) {
    logger.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8) {
    logger.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8, Object p9) {
    logger.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public void debug(String message, Object p0) {
    logger.debug(message, p0);
  }

  @Override
  public void debug(String message, Object p0, Object p1) {
    logger.debug(message, p0, p1);
  }

  @Override
  public void debug(String message, Object p0, Object p1, Object p2) {
    logger.debug(message, p0, p1, p2);
  }

  @Override
  public void debug(String message, Object p0, Object p1, Object p2, Object p3) {
    logger.debug(message, p0, p1, p2, p3);
  }

  @Override
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.debug(message, p0, p1, p2, p3, p4);
  }

  @Override
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.debug(message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logger.debug(message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7) {
    logger.debug(message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8) {
    logger.debug(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8, Object p9) {
    logger.debug(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Deprecated
  @Override
  public void entry() {
    logger.entry();
  }

  @Override
  public void entry(Object... params) {
    logger.entry(params);
  }

  @Override
  public void error(Marker marker, Message msg) {
    logger.error(marker, msg);
  }

  @Override
  public void error(Marker marker, Message msg, Throwable t) {
    logger.error(marker, msg, t);
  }

  @Override
  public void error(Marker marker, MessageSupplier msgSupplier) {
    logger.error(marker, msgSupplier);
  }

  @Override
  public void error(Marker marker, MessageSupplier msgSupplier, Throwable t) {
    logger.error(marker, msgSupplier, t);
  }

  @Override
  public void error(Marker marker, CharSequence message) {
    logger.error(marker, message);
  }

  @Override
  public void error(Marker marker, CharSequence message, Throwable t) {
    logger.error(marker, message, t);
  }

  @Override
  public void error(Marker marker, Object message) {
    logger.error(marker, message);
  }

  @Override
  public void error(Marker marker, Object message, Throwable t) {
    logger.error(marker, message, t);
  }

  @Override
  public void error(Marker marker, String message) {
    logger.error(marker, message);
  }

  @Override
  public void error(Marker marker, String message, Object... params) {
    logger.error(marker, message, params);
  }

  @Override
  public void error(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logger.error(marker, message, paramSuppliers);
  }

  @Override
  public void error(Marker marker, String message, Throwable t) {
    logger.error(marker, message, t);
  }

  @Override
  public void error(Marker marker, Supplier<?> msgSupplier) {
    logger.error(marker, msgSupplier);
  }

  @Override
  public void error(Marker marker, Supplier<?> msgSupplier, Throwable t) {
    logger.error(marker, msgSupplier, t);
  }

  @Override
  public void error(Message msg) {
    logger.error(msg);
  }

  @Override
  public void error(Message msg, Throwable t) {
    logger.error(msg, t);
  }

  @Override
  public void error(MessageSupplier msgSupplier) {
    logger.error(msgSupplier);
  }

  @Override
  public void error(MessageSupplier msgSupplier, Throwable t) {
    logger.error(msgSupplier, t);
  }

  @Override
  public void error(CharSequence message) {
    logger.error(message);
  }

  @Override
  public void error(CharSequence message, Throwable t) {
    logger.error(message, t);
  }

  @Override
  public void error(Object message) {
    logger.error(message);
  }

  @Override
  public void error(Object message, Throwable t) {
    logger.error(message, t);
  }

  @Override
  public void error(String message) {
    logger.error(message);
  }

  @Override
  public void error(String message, Object... params) {
    logger.error(message, params);
  }

  @Override
  public void error(String message, Supplier<?>... paramSuppliers) {
    logger.error(message, paramSuppliers);
  }

  @Override
  public void error(String message, Throwable t) {
    logger.error(message, t);
  }

  @Override
  public void error(Supplier<?> msgSupplier) {
    logger.error(msgSupplier);
  }

  @Override
  public void error(Supplier<?> msgSupplier, Throwable t) {
    logger.error(msgSupplier, t);
  }

  @Override
  public void error(Marker marker, String message, Object p0) {
    logger.error(marker, message, p0);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1) {
    logger.error(marker, message, p0, p1);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1, Object p2) {
    logger.error(marker, message, p0, p1, p2);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logger.error(marker, message, p0, p1, p2, p3);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.error(marker, message, p0, p1, p2, p3, p4);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.error(marker, message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6) {
    logger.error(marker, message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7) {
    logger.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8) {
    logger.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8, Object p9) {
    logger.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public void error(String message, Object p0) {
    logger.error(message, p0);
  }

  @Override
  public void error(String message, Object p0, Object p1) {
    logger.error(message, p0, p1);
  }

  @Override
  public void error(String message, Object p0, Object p1, Object p2) {
    logger.error(message, p0, p1, p2);
  }

  @Override
  public void error(String message, Object p0, Object p1, Object p2, Object p3) {
    logger.error(message, p0, p1, p2, p3);
  }

  @Override
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.error(message, p0, p1, p2, p3, p4);
  }

  @Override
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.error(message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logger.error(message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7) {
    logger.error(message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8) {
    logger.error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8, Object p9) {
    logger.error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Deprecated
  @Override
  public void exit() {
    logger.exit();
  }

  @Deprecated
  @Override
  public <R> R exit(R result) {
    return logger.exit(result);
  }

  @Override
  public void fatal(Marker marker, Message msg) {
    logger.fatal(marker, msg);
  }

  @Override
  public void fatal(Marker marker, Message msg, Throwable t) {
    logger.fatal(marker, msg, t);
  }

  @Override
  public void fatal(Marker marker, MessageSupplier msgSupplier) {
    logger.fatal(marker, msgSupplier);
  }

  @Override
  public void fatal(Marker marker, MessageSupplier msgSupplier, Throwable t) {
    logger.fatal(marker, msgSupplier, t);
  }

  @Override
  public void fatal(Marker marker, CharSequence message) {
    logger.fatal(marker, message);
  }

  @Override
  public void fatal(Marker marker, CharSequence message, Throwable t) {
    logger.fatal(marker, message, t);
  }

  @Override
  public void fatal(Marker marker, Object message) {
    logger.fatal(marker, message);
  }

  @Override
  public void fatal(Marker marker, Object message, Throwable t) {
    logger.fatal(marker, message, t);
  }

  @Override
  public void fatal(Marker marker, String message) {
    logger.fatal(marker, message);
  }

  @Override
  public void fatal(Marker marker, String message, Object... params) {
    logger.fatal(marker, message, params);
  }

  @Override
  public void fatal(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logger.fatal(marker, message, paramSuppliers);
  }

  @Override
  public void fatal(Marker marker, String message, Throwable t) {
    logger.fatal(marker, message, t);
  }

  @Override
  public void fatal(Marker marker, Supplier<?> msgSupplier) {
    logger.fatal(marker, msgSupplier);
  }

  @Override
  public void fatal(Marker marker, Supplier<?> msgSupplier, Throwable t) {
    logger.fatal(marker, msgSupplier, t);
  }

  @Override
  public void fatal(Message msg) {
    logger.fatal(msg);
  }

  @Override
  public void fatal(Message msg, Throwable t) {
    logger.fatal(msg, t);
  }

  @Override
  public void fatal(MessageSupplier msgSupplier) {
    logger.fatal(msgSupplier);
  }

  @Override
  public void fatal(MessageSupplier msgSupplier, Throwable t) {
    logger.fatal(msgSupplier, t);
  }

  @Override
  public void fatal(CharSequence message) {
    logger.fatal(message);
  }

  @Override
  public void fatal(CharSequence message, Throwable t) {
    logger.fatal(message, t);
  }

  @Override
  public void fatal(Object message) {
    logger.fatal(message);
  }

  @Override
  public void fatal(Object message, Throwable t) {
    logger.fatal(message, t);
  }

  @Override
  public void fatal(String message) {
    logger.fatal(message);
  }

  @Override
  public void fatal(String message, Object... params) {
    logger.fatal(message, params);
  }

  @Override
  public void fatal(String message, Supplier<?>... paramSuppliers) {
    logger.fatal(message, paramSuppliers);
  }

  @Override
  public void fatal(String message, Throwable t) {
    logger.fatal(message, t);
  }

  @Override
  public void fatal(Supplier<?> msgSupplier) {
    logger.fatal(msgSupplier);
  }

  @Override
  public void fatal(Supplier<?> msgSupplier, Throwable t) {
    logger.fatal(msgSupplier, t);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0) {
    logger.fatal(marker, message, p0);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1) {
    logger.fatal(marker, message, p0, p1);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2) {
    logger.fatal(marker, message, p0, p1, p2);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logger.fatal(marker, message, p0, p1, p2, p3);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.fatal(marker, message, p0, p1, p2, p3, p4);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.fatal(marker, message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6) {
    logger.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7) {
    logger.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8) {
    logger.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8, Object p9) {
    logger.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public void fatal(String message, Object p0) {
    logger.fatal(message, p0);
  }

  @Override
  public void fatal(String message, Object p0, Object p1) {
    logger.fatal(message, p0, p1);
  }

  @Override
  public void fatal(String message, Object p0, Object p1, Object p2) {
    logger.fatal(message, p0, p1, p2);
  }

  @Override
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3) {
    logger.fatal(message, p0, p1, p2, p3);
  }

  @Override
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.fatal(message, p0, p1, p2, p3, p4);
  }

  @Override
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.fatal(message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logger.fatal(message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7) {
    logger.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8) {
    logger.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8, Object p9) {
    logger.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public Level getLevel() {
    return logger.getLevel();
  }

  @Override
  public <MF extends MessageFactory> MF getMessageFactory() {
    return logger.getMessageFactory();
  }

  @Override
  public String getName() {
    return logger.getName();
  }

  @Override
  public void info(Marker marker, Message msg) {
    logger.info(marker, msg);
  }

  @Override
  public void info(Marker marker, Message msg, Throwable t) {
    logger.info(marker, msg, t);
  }

  @Override
  public void info(Marker marker, MessageSupplier msgSupplier) {
    logger.info(marker, msgSupplier);
  }

  @Override
  public void info(Marker marker, MessageSupplier msgSupplier, Throwable t) {
    logger.info(marker, msgSupplier, t);
  }

  @Override
  public void info(Marker marker, CharSequence message) {
    logger.info(marker, message);
  }

  @Override
  public void info(Marker marker, CharSequence message, Throwable t) {
    logger.info(marker, message, t);
  }

  @Override
  public void info(Marker marker, Object message) {
    logger.info(marker, message);
  }

  @Override
  public void info(Marker marker, Object message, Throwable t) {
    logger.info(marker, message, t);
  }

  @Override
  public void info(Marker marker, String message) {
    logger.info(marker, message);
  }

  @Override
  public void info(Marker marker, String message, Object... params) {
    logger.info(marker, message, params);
  }

  @Override
  public void info(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logger.info(marker, message, paramSuppliers);
  }

  @Override
  public void info(Marker marker, String message, Throwable t) {
    logger.info(marker, message, t);
  }

  @Override
  public void info(Marker marker, Supplier<?> msgSupplier) {
    logger.info(marker, msgSupplier);
  }

  @Override
  public void info(Marker marker, Supplier<?> msgSupplier, Throwable t) {
    logger.info(marker, msgSupplier, t);
  }

  @Override
  public void info(Message msg) {
    logger.info(msg);
  }

  @Override
  public void info(Message msg, Throwable t) {
    logger.info(msg, t);
  }

  @Override
  public void info(MessageSupplier msgSupplier) {
    logger.info(msgSupplier);
  }

  @Override
  public void info(MessageSupplier msgSupplier, Throwable t) {
    logger.info(msgSupplier, t);
  }

  @Override
  public void info(CharSequence message) {
    printOnDisplay(message);
    logger.info(message);
  }

  @Override
  public void info(CharSequence message, Throwable t) {
    printOnDisplay(message);
    logger.info(message, t);
  }

  @Override
  public void info(Object message) {
    printOnDisplay(message);
    logger.info(message);
  }

  @Override
  public void info(Object message, Throwable t) {
    printOnDisplay(message);
    logger.info(message, t);
  }

  @Override
  public void info(String message) {
    printOnDisplay(message);
    logger.info(message);
  }

  @Override
  public void info(String message, Object... params) {
    printOnDisplay(message);
    logger.info(message, params);
  }

  @Override
  public void info(String message, Supplier<?>... paramSuppliers) {
    logger.info(message, paramSuppliers);
  }

  @Override
  public void info(String message, Throwable t) {
    logger.info(message, t);
  }

  @Override
  public void info(Supplier<?> msgSupplier) {
    logger.info(msgSupplier);
  }

  @Override
  public void info(Supplier<?> msgSupplier, Throwable t) {
    logger.info(msgSupplier, t);
  }

  @Override
  public void info(Marker marker, String message, Object p0) {
    logger.info(marker, message, p0);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1) {
    logger.info(marker, message, p0, p1);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1, Object p2) {
    logger.info(marker, message, p0, p1, p2);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logger.info(marker, message, p0, p1, p2, p3);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.info(marker, message, p0, p1, p2, p3, p4);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.info(marker, message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6) {
    logger.info(marker, message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7) {
    logger.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8) {
    logger.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8, Object p9) {
    logger.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public void info(String message, Object p0) {
    logger.info(message, p0);
  }

  @Override
  public void info(String message, Object p0, Object p1) {
    logger.info(message, p0, p1);
  }

  @Override
  public void info(String message, Object p0, Object p1, Object p2) {
    logger.info(message, p0, p1, p2);
  }

  @Override
  public void info(String message, Object p0, Object p1, Object p2, Object p3) {
    logger.info(message, p0, p1, p2, p3);
  }

  @Override
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.info(message, p0, p1, p2, p3, p4);
  }

  @Override
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.info(message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logger.info(message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7) {
    logger.info(message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8) {
    logger.info(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8, Object p9) {
    logger.info(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  @Override
  public boolean isDebugEnabled(Marker marker) {
    return logger.isDebugEnabled(marker);
  }

  @Override
  public boolean isEnabled(Level level) {
    return logger.isEnabled(level);
  }

  @Override
  public boolean isEnabled(Level level, Marker marker) {
    return logger.isEnabled(level, marker);
  }

  @Override
  public boolean isErrorEnabled() {
    return logger.isErrorEnabled();
  }

  @Override
  public boolean isErrorEnabled(Marker marker) {
    return logger.isErrorEnabled(marker);
  }

  @Override
  public boolean isFatalEnabled() {
    return logger.isFatalEnabled();
  }

  @Override
  public boolean isFatalEnabled(Marker marker) {
    return logger.isFatalEnabled(marker);
  }

  @Override
  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  @Override
  public boolean isInfoEnabled(Marker marker) {
    return logger.isInfoEnabled(marker);
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  @Override
  public boolean isTraceEnabled(Marker marker) {
    return logger.isTraceEnabled(marker);
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.isWarnEnabled();
  }

  @Override
  public boolean isWarnEnabled(Marker marker) {
    return logger.isWarnEnabled(marker);
  }

  @Override
  public void log(Level level, Marker marker, Message msg) {
    logger.log(level, marker, msg);
  }

  @Override
  public void log(Level level, Marker marker, Message msg, Throwable t) {
    logger.log(level, marker, msg, t);
  }

  @Override
  public void log(Level level, Marker marker, MessageSupplier msgSupplier) {
    logger.log(level, marker, msgSupplier);
  }

  @Override
  public void log(Level level, Marker marker, MessageSupplier msgSupplier, Throwable t) {
    logger.log(level, marker, msgSupplier, t);
  }

  @Override
  public void log(Level level, Marker marker, CharSequence message) {
    logger.log(level, marker, message);
  }

  @Override
  public void log(Level level, Marker marker, CharSequence message, Throwable t) {
    logger.log(level, marker, message, t);
  }

  @Override
  public void log(Level level, Marker marker, Object message) {
    logger.log(level, marker, message);
  }

  @Override
  public void log(Level level, Marker marker, Object message, Throwable t) {
    logger.log(level, marker, message, t);
  }

  @Override
  public void log(Level level, Marker marker, String message) {
    logger.log(level, marker, message);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object... params) {
    logger.log(level, marker, message, params);
  }

  @Override
  public void log(Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
    logger.log(level, marker, message, paramSuppliers);
  }

  @Override
  public void log(Level level, Marker marker, String message, Throwable t) {
    logger.log(level, marker, message, t);
  }

  @Override
  public void log(Level level, Marker marker, Supplier<?> msgSupplier) {
    logger.log(level, marker, msgSupplier);
  }

  @Override
  public void log(Level level, Marker marker, Supplier<?> msgSupplier, Throwable t) {
    logger.log(level, marker, msgSupplier, t);
  }

  @Override
  public void log(Level level, Message msg) {
    logger.log(level, msg);
  }

  @Override
  public void log(Level level, Message msg, Throwable t) {
    logger.log(level, msg, t);
  }

  @Override
  public void log(Level level, MessageSupplier msgSupplier) {
    logger.log(level, msgSupplier);
  }

  @Override
  public void log(Level level, MessageSupplier msgSupplier, Throwable t) {
    logger.log(level, msgSupplier, t);
  }

  @Override
  public void log(Level level, CharSequence message) {
    logger.log(level, message);
  }

  @Override
  public void log(Level level, CharSequence message, Throwable t) {
    logger.log(level, message, t);
  }

  @Override
  public void log(Level level, Object message) {
    logger.log(level, message);
  }

  @Override
  public void log(Level level, Object message, Throwable t) {
    logger.log(level, message, t);
  }

  @Override
  public void log(Level level, String message) {
    logger.log(level, message);
  }

  @Override
  public void log(Level level, String message, Object... params) {
    logger.log(level, message, params);
  }

  @Override
  public void log(Level level, String message, Supplier<?>... paramSuppliers) {
    logger.log(level, message, paramSuppliers);
  }

  @Override
  public void log(Level level, String message, Throwable t) {
    logger.log(level, message, t);
  }

  @Override
  public void log(Level level, Supplier<?> msgSupplier) {
    logger.log(level, msgSupplier);
  }

  @Override
  public void log(Level level, Supplier<?> msgSupplier, Throwable t) {
    logger.log(level, msgSupplier, t);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0) {
    logger.log(level, marker, message, p0);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1) {
    logger.log(level, marker, message, p0, p1);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
    logger.log(level, marker, message, p0, p1, p2);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logger.log(level, marker, message, p0, p1, p2, p3);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.log(level, marker, message, p0, p1, p2, p3, p4);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
      Object p5) {
    logger.log(level, marker, message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
      Object p5, Object p6) {
    logger.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
      Object p5, Object p6, Object p7) {
    logger.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
      Object p5, Object p6, Object p7, Object p8) {
    logger.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
      Object p5, Object p6, Object p7, Object p8, Object p9) {
    logger.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public void log(Level level, String message, Object p0) {
    logger.log(level, message, p0);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1) {
    logger.log(level, message, p0, p1);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1, Object p2) {
    logger.log(level, message, p0, p1, p2);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3) {
    logger.log(level, message, p0, p1, p2, p3);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.log(level, message, p0, p1, p2, p3, p4);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.log(level, message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6) {
    logger.log(level, message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7) {
    logger.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8) {
    logger.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8, Object p9) {
    logger.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public void printf(Level level, Marker marker, String format, Object... params) {
    logger.printf(level, marker, format, params);
  }

  @Override
  public void printf(Level level, String format, Object... params) {
    logger.printf(level, format, params);
  }

  @Override
  public <T extends Throwable> T throwing(Level level, T t) {
    return logger.throwing(level, t);
  }

  @Override
  public <T extends Throwable> T throwing(T t) {
    return logger.throwing(t);
  }

  @Override
  public void trace(Marker marker, Message msg) {
    logger.trace(marker, msg);
  }

  @Override
  public void trace(Marker marker, Message msg, Throwable t) {
    logger.trace(marker, msg, t);
  }

  @Override
  public void trace(Marker marker, MessageSupplier msgSupplier) {
    logger.trace(marker, msgSupplier);
  }

  @Override
  public void trace(Marker marker, MessageSupplier msgSupplier, Throwable t) {
    logger.trace(marker, msgSupplier, t);
  }

  @Override
  public void trace(Marker marker, CharSequence message) {
    logger.trace(marker, message);
  }

  @Override
  public void trace(Marker marker, CharSequence message, Throwable t) {
    logger.trace(marker, message, t);
  }

  @Override
  public void trace(Marker marker, Object message) {
    logger.trace(marker, message);
  }

  @Override
  public void trace(Marker marker, Object message, Throwable t) {
    logger.trace(marker, message, t);
  }

  @Override
  public void trace(Marker marker, String message) {
    logger.trace(marker, message);
  }

  @Override
  public void trace(Marker marker, String message, Object... params) {
    logger.trace(marker, message, params);
  }

  @Override
  public void trace(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logger.trace(marker, message, paramSuppliers);
  }

  @Override
  public void trace(Marker marker, String message, Throwable t) {
    logger.trace(marker, message, t);
  }

  @Override
  public void trace(Marker marker, Supplier<?> msgSupplier) {
    logger.trace(marker, msgSupplier);
  }

  @Override
  public void trace(Marker marker, Supplier<?> msgSupplier, Throwable t) {
    logger.trace(marker, msgSupplier, t);
  }

  @Override
  public void trace(Message msg) {
    logger.trace(msg);
  }

  @Override
  public void trace(Message msg, Throwable t) {
    logger.trace(msg, t);
  }

  @Override
  public void trace(MessageSupplier msgSupplier) {
    logger.trace(msgSupplier);
  }

  @Override
  public void trace(MessageSupplier msgSupplier, Throwable t) {
    logger.trace(msgSupplier, t);
  }

  @Override
  public void trace(CharSequence message) {
    logger.trace(message);
  }

  @Override
  public void trace(CharSequence message, Throwable t) {
    logger.trace(message, t);
  }

  @Override
  public void trace(Object message) {
    logger.trace(message);
  }

  @Override
  public void trace(Object message, Throwable t) {
    logger.trace(message, t);
  }

  @Override
  public void trace(String message) {
    logger.trace(message);
  }

  @Override
  public void trace(String message, Object... params) {
    logger.trace(message, params);
  }

  @Override
  public void trace(String message, Supplier<?>... paramSuppliers) {
    logger.trace(message, paramSuppliers);
  }

  @Override
  public void trace(String message, Throwable t) {
    logger.trace(message, t);
  }

  @Override
  public void trace(Supplier<?> msgSupplier) {
    logger.trace(msgSupplier);
  }

  @Override
  public void trace(Supplier<?> msgSupplier, Throwable t) {
    logger.trace(msgSupplier, t);
  }

  @Override
  public void trace(Marker marker, String message, Object p0) {
    logger.trace(marker, message, p0);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1) {
    logger.trace(marker, message, p0, p1);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2) {
    logger.trace(marker, message, p0, p1, p2);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logger.trace(marker, message, p0, p1, p2, p3);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.trace(marker, message, p0, p1, p2, p3, p4);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.trace(marker, message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6) {
    logger.trace(marker, message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7) {
    logger.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8) {
    logger.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8, Object p9) {
    logger.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public void trace(String message, Object p0) {
    logger.trace(message, p0);
  }

  @Override
  public void trace(String message, Object p0, Object p1) {
    logger.trace(message, p0, p1);
  }

  @Override
  public void trace(String message, Object p0, Object p1, Object p2) {
    logger.trace(message, p0, p1, p2);
  }

  @Override
  public void trace(String message, Object p0, Object p1, Object p2, Object p3) {
    logger.trace(message, p0, p1, p2, p3);
  }

  @Override
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.trace(message, p0, p1, p2, p3, p4);
  }

  @Override
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.trace(message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logger.trace(message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7) {
    logger.trace(message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8) {
    logger.trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8, Object p9) {
    logger.trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public EntryMessage traceEntry() {
    return logger.traceEntry();
  }

  @Override
  public EntryMessage traceEntry(String format, Object... params) {
    return logger.traceEntry(format, params);
  }

  @Override
  public EntryMessage traceEntry(Supplier<?>... paramSuppliers) {
    return logger.traceEntry(paramSuppliers);
  }

  @Override
  public EntryMessage traceEntry(String format, Supplier<?>... paramSuppliers) {
    return logger.traceEntry(format, paramSuppliers);
  }

  @Override
  public EntryMessage traceEntry(Message message) {
    return logger.traceEntry(message);
  }

  @Override
  public void traceExit() {
    logger.traceExit();
  }

  @Override
  public <R> R traceExit(R result) {
    return logger.traceExit(result);
  }

  @Override
  public <R> R traceExit(String format, R result) {
    return logger.traceExit(format, result);
  }

  @Override
  public void traceExit(EntryMessage message) {
    logger.traceExit(message);
  }

  @Override
  public <R> R traceExit(EntryMessage message, R result) {
    return logger.traceExit(message, result);
  }

  @Override
  public <R> R traceExit(Message message, R result) {
    return logger.traceExit(message, result);
  }

  @Override
  public void warn(Marker marker, Message msg) {
    logger.warn(marker, msg);
  }

  @Override
  public void warn(Marker marker, Message msg, Throwable t) {
    logger.warn(marker, msg, t);
  }

  @Override
  public void warn(Marker marker, MessageSupplier msgSupplier) {
    logger.warn(marker, msgSupplier);
  }

  @Override
  public void warn(Marker marker, MessageSupplier msgSupplier, Throwable t) {
    logger.warn(marker, msgSupplier, t);
  }

  @Override
  public void warn(Marker marker, CharSequence message) {
    logger.warn(marker, message);
  }

  @Override
  public void warn(Marker marker, CharSequence message, Throwable t) {
    logger.warn(marker, message, t);
  }

  @Override
  public void warn(Marker marker, Object message) {
    logger.warn(marker, message);
  }

  @Override
  public void warn(Marker marker, Object message, Throwable t) {
    logger.warn(marker, message, t);
  }

  @Override
  public void warn(Marker marker, String message) {
    logger.warn(marker, message);
  }

  @Override
  public void warn(Marker marker, String message, Object... params) {
    logger.warn(marker, message, params);
  }

  @Override
  public void warn(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logger.warn(marker, message, paramSuppliers);
  }

  @Override
  public void warn(Marker marker, String message, Throwable t) {
    logger.warn(marker, message, t);
  }

  @Override
  public void warn(Marker marker, Supplier<?> msgSupplier) {
    logger.warn(marker, msgSupplier);
  }

  @Override
  public void warn(Marker marker, Supplier<?> msgSupplier, Throwable t) {
    logger.warn(marker, msgSupplier, t);
  }

  @Override
  public void warn(Message msg) {
    logger.warn(msg);
  }

  @Override
  public void warn(Message msg, Throwable t) {
    logger.warn(msg, t);
  }

  @Override
  public void warn(MessageSupplier msgSupplier) {
    logger.warn(msgSupplier);
  }

  @Override
  public void warn(MessageSupplier msgSupplier, Throwable t) {
    logger.warn(msgSupplier, t);
  }

  @Override
  public void warn(CharSequence message) {
    logger.warn(message);
  }

  @Override
  public void warn(CharSequence message, Throwable t) {
    logger.warn(message, t);
  }

  @Override
  public void warn(Object message) {
    logger.warn(message);
  }

  @Override
  public void warn(Object message, Throwable t) {
    logger.warn(message, t);
  }

  @Override
  public void warn(String message) {
    logger.warn(message);
  }

  @Override
  public void warn(String message, Object... params) {
    logger.warn(message, params);
  }

  @Override
  public void warn(String message, Supplier<?>... paramSuppliers) {
    logger.warn(message, paramSuppliers);
  }

  @Override
  public void warn(String message, Throwable t) {
    logger.warn(message, t);
  }

  @Override
  public void warn(Supplier<?> msgSupplier) {
    logger.warn(msgSupplier);
  }

  @Override
  public void warn(Supplier<?> msgSupplier, Throwable t) {
    logger.warn(msgSupplier, t);
  }

  @Override
  public void warn(Marker marker, String message, Object p0) {
    logger.warn(marker, message, p0);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1) {
    logger.warn(marker, message, p0, p1);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2) {
    logger.warn(marker, message, p0, p1, p2);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logger.warn(marker, message, p0, p1, p2, p3);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.warn(marker, message, p0, p1, p2, p3, p4);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.warn(marker, message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6) {
    logger.warn(marker, message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7) {
    logger.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8) {
    logger.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
      Object p6, Object p7, Object p8, Object p9) {
    logger.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  @Override
  public void warn(String message, Object p0) {
    logger.warn(message, p0);
  }

  @Override
  public void warn(String message, Object p0, Object p1) {
    logger.warn(message, p0, p1);
  }

  @Override
  public void warn(String message, Object p0, Object p1, Object p2) {
    logger.warn(message, p0, p1, p2);
  }

  @Override
  public void warn(String message, Object p0, Object p1, Object p2, Object p3) {
    logger.warn(message, p0, p1, p2, p3);
  }

  @Override
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logger.warn(message, p0, p1, p2, p3, p4);
  }

  @Override
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logger.warn(message, p0, p1, p2, p3, p4, p5);
  }

  @Override
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logger.warn(message, p0, p1, p2, p3, p4, p5, p6);
  }

  @Override
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7) {
    logger.warn(message, p0, p1, p2, p3, p4, p5, p6, p7);
  }

  @Override
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8) {
    logger.warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }

  @Override
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
      Object p7, Object p8, Object p9) {
    logger.warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }

  private static void printOnDisplay(Object msg) {
    if (!printOnDisplay) { return; }
    try {
      Minecraft.getMinecraft().player.sendMessage(new TextComponentString(msg.toString()));
    } catch (Exception e) {
      //何もしない
      e.printStackTrace();
    }
  }
}
