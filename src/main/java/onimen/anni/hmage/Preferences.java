package onimen.anni.hmage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Preferences {

	public static Properties cfg;
	public static final String GENERAL = "General";

	public static boolean enabled = true;

	public static HUDOptionData arrowCounterOption;
	public static HUDOptionData statusEffectOption;
	public static HUDOptionData statusArmorOption;
	public static HUDOptionData cpsCounterOption;
	static {
		arrowCounterOption = new HUDOptionData("arrowCounterHUD", true, 6, -30, 0);
		statusEffectOption = new HUDOptionData("statusEffectHUD", true, 2, 0, 0);
		statusArmorOption = new HUDOptionData("statusArmorHUD", true, 0, 0, 0);
		cpsCounterOption = new HUDOptionData("cpsCounterHUD", true, 0, 45, 5);
	}

	public static boolean toggleSneak = true;
	public static int toggleSneakThreshold = 300;

	public static boolean toggleSprint = true;
	public static int toggleSprintThreshold = 300;

	//TODO
	public static boolean redTintArmor = false;

	public static boolean checkIsConfigExisting() {
		if (Files.notExists(HMage.config)) {
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
		enabled = getBoolean("enabled", true);

		statusArmorOption.read();
		statusEffectOption.read();
		arrowCounterOption.read();
		cpsCounterOption.read();

		toggleSneak = getBoolean("toggleSneak.enabled", true);
		toggleSneakThreshold = getInt("toggleSneak.threshold", 300);

		toggleSprint = getBoolean("toggleSprint.enabled", true);
		toggleSprintThreshold = getInt("toggleSprint.threshold", 300);

		redTintArmor = getBoolean("redTintArmor.enabled", true);
	}

	public static void save() {
		if (HMage.config == null)
			return;

		setBoolean("enabled", enabled);

		statusArmorOption.save();
		statusEffectOption.save();
		arrowCounterOption.save();
		cpsCounterOption.save();

		setBoolean("toggleSneak", toggleSneak);
		setInt("toggleSneak.threshold", toggleSneakThreshold);

		setBoolean("toggleSprint.enabled", toggleSprint);
		setInt("toggleSprint.threshold", toggleSprintThreshold);

		setBoolean("redTintArmor.enabled", redTintArmor);

		try {
			cfg.store(Files.newBufferedWriter(HMage.config, StandardCharsets.UTF_8), "Created by HMage");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		return Integer.valueOf(cfg.getProperty(key, String.valueOf(defaultValue)));
	}

	public static void setInt(String key, int value) {
		if (cfg == null)
			return;
		cfg.setProperty(key, String.valueOf(value));
	}

	public static class HUDOptionData {

		private final String rootKey;

		private boolean enabled;
		private int position;
		private int translateX;
		private int translateY;

		public HUDOptionData(String rootKey) {
			this(rootKey, true, 0, 0, 0);
		}

		public HUDOptionData(String rootKey, boolean isEnable, int position, int translateX, int translateY) {
			this.rootKey = rootKey;
			this.enabled = isEnable;
			this.position = position;
			this.translateX = translateX;
			this.translateY = translateY;
		}

		public void read() {
			this.enabled = Preferences.getBoolean(rootKey + ".enabled", enabled);
			this.position = Preferences.getInt(rootKey + ".position", position);
			this.translateX = Preferences.getInt(rootKey + ".x", translateX);
			this.translateY = Preferences.getInt(rootKey + ".y", translateY);
		}

		public void save() {
			Preferences.setBoolean(rootKey + ".enabled", enabled);
			Preferences.setInt(rootKey + ".position", position);
			Preferences.setInt(rootKey + ".x", translateX);
			Preferences.setInt(rootKey + ".y", translateY);
		}

		public List<String> getTabCompletionsList() {
			return Arrays.asList("enabled", "position", "x", "y")
					.stream()
					.map(e -> rootKey + "." + e)
					.collect(Collectors.toList());
		}

		public boolean isEnabled() {
			return this.enabled;
		}

		public void setEnabled(boolean value) {
			this.enabled = value;
		}

		public void toggleEnabled() {
			this.enabled = !this.enabled;
		}

		public int getPosition() {
			return this.position;
		}

		public void setPosition(int value) {
			this.position = value;
		}

		public int getTranslateX() {
			return this.translateX;
		}

		public void setTranslateX(int value) {
			this.translateX = value;
		}

		public int getTranslateY() {
			return this.translateY;
		}

		public void setTranslateY(int value) {
			this.translateY = value;
		}
	}

}
