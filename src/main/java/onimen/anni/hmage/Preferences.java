package onimen.anni.hmage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Preferences {

  public static Path configPath;
  public static Properties cfg;

  public static boolean enabled = true;
  public static boolean toggleSneak = true;
  public static int toggleSneakThreshold = 300;
  public static boolean hurtingArmor = true;

  public static KeyBinding openSettingsKey = new KeyBinding("hmage.key.settings", Keyboard.KEY_P,
      "key.categories.misc");

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
    toggleSneak = getBoolean("toggleSneak.enabled", true);
    toggleSneakThreshold = getInt("toggleSneak.threshold", 300);
    hurtingArmor = getBoolean("hurtingArmor.enabled", true);
  }

  public static void save() {
    if (configPath == null)
      return;

    setBoolean("enabled", enabled);

    setBoolean("toggleSneak.enabled", toggleSneak);
    setInt("toggleSneak.threshold", toggleSneakThreshold);
    setBoolean("hurtingArmor.enabled", hurtingArmor);

    try {
      cfg.store(Files.newBufferedWriter(configPath, StandardCharsets.UTF_8), "Created by HMage");
    } catch (IOException e) {
      e.printStackTrace();
    }
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
    return Boolean.valueOf(cfg.getProperty(key, String.valueOf(defaultValue)));
  }

  public static void setBoolean(String key, boolean value) {
    if (cfg == null)
      return;
    cfg.setProperty(key, String.valueOf(value));
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

}
