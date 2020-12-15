package onimen.anni.hmage;

import java.awt.Font;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import onimen.anni.hmage.module.normal.CustomFont;

public class Preferences {

  public static Path configPath;
  public static Properties cfg;

  public static boolean enabled = true;
  public static boolean hurtingArmor = true;
  public static int hurtingArmorColor = 0xFFFF0000;

  public static boolean showGameStatsInInventory = true;

  public static boolean enableDiscordRPC = true;
  public static boolean showServerIPonDiscord = true;
  public static boolean showUserNameOnDiscord = true;
  public static boolean showAnniGameInfoOnDiscord = true;

  public static KeyBinding openSettingsKey = new KeyBinding("hmage.key.settings", Keyboard.KEY_P,
      "key.categories.hmage");

  public static KeyBinding showAnniRankingTab = new KeyBinding("hmage.key.anni-ranking", Keyboard.KEY_H,
      "key.categories.hmage");

  public static void load(FMLPreInitializationEvent event) {
    cfg = new Properties();

    configPath = Paths.get(event.getModConfigurationDirectory() + "/" + HMage.MODID + ".properties");
    createFileIfNotExist(configPath);
    try {
      cfg.load(Files.newBufferedReader(configPath, StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }

    enabled = getBoolean("enabled", true);

    hurtingArmor = getBoolean("hurtingArmor.enabled", true);
    hurtingArmorColor = getInt("hurtingArmor.color", 0xFFFF0000);

    showGameStatsInInventory = getBoolean("hmage.game-stats-on-inv", false);

    enableDiscordRPC = getBoolean("hmage.discord.enabled", true);
    showUserNameOnDiscord = getBoolean("hmage.discord.username", true);
    showServerIPonDiscord = getBoolean("hmage.discord.server-address", true);
    showAnniGameInfoOnDiscord = getBoolean("hmage.discord.game-info", true);

    String[] fontNames = getString("hmage.module.custom-font.font-names", "").split(",");
    List<Font> fonts = Stream.of(fontNames).map(n -> new Font(n, Font.PLAIN, 12)).collect(Collectors.toList());
    CustomFont.setFontList(fonts);
  }

  public static void save() {
    if (configPath == null)
      return;

    setBoolean("enabled", enabled);

    setBoolean("hurtingArmor.enabled", hurtingArmor);
    setInt("hurtingArmor.color", hurtingArmorColor);

    setBoolean("hmage.game-stats-on-inv", showGameStatsInInventory);

    setBoolean("hmage.discord.enabled", enableDiscordRPC);
    setBoolean("hmage.discord.username", showUserNameOnDiscord);
    setBoolean("hmage.discord.server-address", showServerIPonDiscord);
    setBoolean("hmage.discord.game-info", showAnniGameInfoOnDiscord);

    setString("hmage.module.custom-font.font-names", String.join(",", CustomFont.getFontNameList()));
    new Thread(() ->{
      try {
        cfg.store(Files.newBufferedWriter(configPath, StandardCharsets.UTF_8), "Created by HMage");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();

  }

  /**
   * targetが存在する場合true、そうでない場合falseを返します。またディレクトリが存在しない場合、作成します。
   *
   * @param target
   * @return
   */
  public static boolean createFileIfNotExist(Path target) {
    if (Files.notExists(target)) {
      try {
        Files.createFile(target);
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
      return true;
    }
    return false;
  }

  public static void enable() {
    enabled = true;
    setBoolean("enabled", enabled);
  }

  public static void disable() {
    enabled = false;
    setBoolean("enabled", enabled);
  }

  public static void setProperty(String key, String value) {
    if (cfg == null)
      return;
    cfg.setProperty(key, value);
  }

  public static boolean getBoolean(String key, boolean defaultValue) {
    if (cfg == null)
      return defaultValue;
    return Boolean.parseBoolean(cfg.getProperty(key, String.valueOf(defaultValue)));
  }

  public static void setBoolean(String key, boolean value) {
    if (cfg == null)
      return;
    cfg.setProperty(key, String.valueOf(value));
  }

  public static String getString(String key, String defaultValue) {
    if (cfg == null)
      return defaultValue;
    return cfg.getProperty(key, String.valueOf(defaultValue));
  }

  public static void setString(String key, String value) {
    if (cfg == null)
      return;
    cfg.setProperty(key, value);
  }

  public static int getInt(String key, int defaultValue) {
    if (cfg == null)
      return defaultValue;
    try {
      return Integer.valueOf(cfg.getProperty(key, String.valueOf(defaultValue)));
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public static void setInt(String key, int value) {
    if (cfg == null)
      return;
    cfg.setProperty(key, String.valueOf(value));
  }

  public static float getFloat(String key, float defaultValue) {
    if (cfg == null)
      return defaultValue;
    try {
      return Float.parseFloat(cfg.getProperty(key, String.valueOf(defaultValue)));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public static void setFloat(String key, float value) {
    if (cfg == null)
      return;
    cfg.setProperty(key, String.valueOf(value));
  }

}
