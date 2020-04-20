package onimen.anni.hmage;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import onimen.anni.hmage.command.PrefCommand;
import onimen.anni.hmage.gui.AttackKeyListener;
import onimen.anni.hmage.gui.GuiSettings;
import onimen.anni.hmage.gui.hud.ArrowCounterHUD;
import onimen.anni.hmage.gui.hud.CPSCounterHUD;
import onimen.anni.hmage.gui.hud.InterfaceHUD;
import onimen.anni.hmage.gui.hud.StatusArmorHUD;
import onimen.anni.hmage.gui.hud.StatusEffectHUD;
import onimen.anni.hmage.util.CustomMovementInput;

@Mod(modid = HMage.MODID, name = HMage.NAME, version = HMage.VERSION)
public class HMage {
	public static final String MODID = "hmage";
	public static final String NAME = "HMage";
	public static final String VERSION = "1.0.1";
	public static Logger logger;

	public static Path config;
	public static HMage INSTANCE;

	public KeyBinding openSettingsKey = new KeyBinding("hmage.key.settings", Keyboard.KEY_P, "key.categories.misc");

	private Map<String, InterfaceHUD> hudMap = new HashMap<String, InterfaceHUD>();
	private CustomMovementInput customMovementInput = new CustomMovementInput(Minecraft.getMinecraft().gameSettings);

	public void registerItem(InterfaceHUD item) {
		hudMap.put(item.getPrefKey(), item);
	}

	public Map<String, InterfaceHUD> getHUDMap() {
		return this.hudMap;
	}

	public HMage() {
		INSTANCE = this;
		MinecraftForge.EVENT_BUS.register(this);

		//Register HUD Items
		this.registerItem(new ArrowCounterHUD());
		this.registerItem(new StatusEffectHUD());
		this.registerItem(new StatusArmorHUD());
		this.registerItem(new CPSCounterHUD());
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		Preferences.load(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ClientCommandHandler.instance.registerCommand(new PrefCommand());
		ClientRegistry.registerKeyBinding(openSettingsKey);
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.player == null)
			return;

		//If player uses vanilla MovementInput. use togglesneak input
		if (mc.player.movementInput instanceof MovementInputFromOptions)
			mc.player.movementInput = customMovementInput;
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		Minecraft mc = Minecraft.getMinecraft();
		if (openSettingsKey.isPressed()) {
			if (mc.currentScreen == null) {
				mc.displayGuiScreen(new GuiSettings());
			} else if (mc.currentScreen instanceof GuiSettings) {
				mc.displayGuiScreen((GuiScreen) null);
			}
		}

	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {

		if (!Preferences.enabled)
			return;

		Minecraft mc = Minecraft.getMinecraft();
		ElementType type = event.getType();

		if (mc.gameSettings.showDebugInfo)
			return;

		if (type == ElementType.TEXT) {

			if (mc.currentScreen == null) {

				for (InterfaceHUD item : this.hudMap.values()) {
					if (item.isEnabled())
						item.drawItem(mc);
				}

			}

		} else if (type == ElementType.POTION_ICONS) {
			if (event.isCancelable() && Preferences.statusEffectOption.isEnabled())
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onLeftClick(MouseInputEvent event) {

		if (!Preferences.enabled)
			return;

		if (!Mouse.getEventButtonState())
			return;

		if (Mouse.getEventButton() != 0)
			return;

		for (InterfaceHUD item : this.hudMap.values()) {
			if (item.isEnabled() && item instanceof AttackKeyListener)
				((AttackKeyListener) item).onAttackKeyClick();
		}

	}

	//	@SubscribeEvent
	//	public void onRenderPlayer(EntityConstructing event) {
	//		if (!(event.getEntity() instanceof EntityPlayer)) {
	//			return;
	//		}
	//
	//		Minecraft mc = Minecraft.getMinecraft();
	//		Collection<RenderPlayer> rendererValues = mc.getRenderManager().getSkinMap().values();
	//
	//		for (RenderPlayer renderer : rendererValues) {
	//			renderer.addLayer(new RedTintArmor(renderer));
	//		}
	//	}

}
