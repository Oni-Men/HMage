package onimen.anni.hmage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Preferences {

	public static Properties cfg;
	public static final String GENERAL = "General";

	public static boolean enabled = true;

	public static boolean arrowCounter = true;

	public static boolean potionEffectsHUD = true;

	public static boolean armorDurabilityHUD = true;

	public static boolean cpsHUD = true;

	public static boolean checkIsConfigExisting() {
		if (!Files.notExists(HMage.config)) {
			try {
				Files.createFile(HMage.config);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	public static void load(FMLPreInitializationEvent event) {

		cfg = new Properties();

		HMage.config = Paths.get(event.getModConfigurationDirectory() + "/" + HMage.MODID + ".properties");

		try {
			cfg.load(Files.newBufferedReader(HMage.config, StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}

		read();
	}

	public static void read() {
		enabled = getBoolean("enabled", "true");
		potionEffectsHUD = getBoolean("potionEffectsHUD.enabled", "true");
		armorDurabilityHUD = getBoolean("armorDurabilityHUD.enabled", "true");
		arrowCounter = getBoolean("arrowCounter.enabled", "true");
		cpsHUD = getBoolean("cpsHUD.enabled", "true");
	}

	public static void save() {
		if (HMage.config == null)
			return;

		setBoolean("enabled", enabled);
		setBoolean("potionEffectsHUD.enabled", potionEffectsHUD);
		setBoolean("armorDurabilityHUD.enabled", armorDurabilityHUD);
		setBoolean("arrowCounter.enabled", arrowCounter);
		setBoolean("cpsHUD.enabled", cpsHUD);

		try {
			cfg.store(Files.newBufferedWriter(HMage.config, StandardCharsets.UTF_8), "Created by HMage");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean getBoolean(String key, String defaultValue) {
		if (cfg == null)
			return false;
		return Boolean.valueOf(cfg.getProperty(key, defaultValue));
	}

	public static void setBoolean(String key, boolean value) {
		if (cfg == null)
			return;
		cfg.setProperty(key, String.valueOf(value));
	}

}
