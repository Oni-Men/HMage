package onimen.anni.hmage.observer.config;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class AnniConfiguration extends Thread {

  private static final String CONFIG_URL = "https://raw.githubusercontent.com/HMage123456/hmgemod/master/config/anni.cfg";

  private static Properties config;

  public static String getConfig(AnniConfigKey key) {
    return config.getProperty(key.getKey());
  }

  public static void load() {
    new AnniConfiguration().run();
  }

  @Override
  public void run() {
    try {
      config = new Properties();
      URLConnection connection = new URL(CONFIG_URL).openConnection();
      connection.connect();
      config.load(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
