package onimen.anni.hmage;

import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import onimen.anni.hmage.command.PrefCommand;
import onimen.anni.hmage.gui.AttackKeyListener;
import onimen.anni.hmage.gui.GuiItemArmorDurability;
import onimen.anni.hmage.gui.GuiItemCPS;
import onimen.anni.hmage.gui.GuiItemPotionEffects;
import onimen.anni.hmage.gui.GuiItemRemainingArrows;
import onimen.anni.hmage.gui.GuiSettings;
import onimen.anni.hmage.gui.IGuiItem;
import onimen.anni.hmage.gui.RightClickItemListener;

@Mod(modid = HMage.MODID, name = HMage.NAME, version = HMage.VERSION)
public class HMage {
	public static final String MODID = "hmage_anni";
	public static final String NAME = "HMage";
	public static final String VERSION = "1.0.0";
	public static Logger logger;

	public static Path config;
	public static HMage INSTANCE;

	public KeyBinding openSettingsKey = new KeyBinding("open.setting", Keyboard.KEY_P, "key.categories.misc");

	public ArrayList<IGuiItem> itemList = new ArrayList<>();

	public HMage() {
		INSTANCE = this;
		MinecraftForge.EVENT_BUS.register(this);

		//Register HUD Items
		this.itemList.add(new GuiItemRemainingArrows());
		this.itemList.add(new GuiItemPotionEffects());
		this.itemList.add(new GuiItemArmorDurability());
		this.itemList.add(new GuiItemCPS());
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

		if (type == ElementType.TEXT) {

			if (mc.currentScreen == null) {

				for (int i = 0; i < this.itemList.size(); ++i) {

					if (!this.itemList.get(i).isEnabled())
						continue;

					((IGuiItem) this.itemList.get(i)).drawItem(mc);
				}

			}

		} else if (type == ElementType.POTION_ICONS) {
			if (event.isCancelable() && Preferences.potionEffectsHUD)
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

		for (int i = 0; i < this.itemList.size(); i++) {

			IGuiItem item = this.itemList.get(i);

			if (item instanceof AttackKeyListener) {

				((AttackKeyListener) item).onAttackKeyClick();

			}
		}

	}

	@SubscribeEvent
	public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {

		if (!Preferences.enabled)
			return;

		for (int i = 0; i < this.itemList.size(); i++) {

			IGuiItem item = this.itemList.get(i);

			if (item instanceof RightClickItemListener) {

				((RightClickItemListener) item).onRightClickItem(event);

			}
		}

	}

	//	@SubscribeEvent
	//	public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
	//		event.setCanceled(true);
	//
	//		Minecraft mc = Minecraft.getMinecraft();
	//
	//		HMageRenderPlayer myRenderer = new HMageRenderPlayer(mc.getRenderManager());
	//		myRenderer.doRender(
	//				(AbstractClientPlayer) event.getEntityPlayer(),
	//				event.getX(),
	//				event.getY(),
	//				event.getZ(), event.getEntityPlayer().getPitchYaw().y, event.getPartialRenderTick());
	//	}

}
