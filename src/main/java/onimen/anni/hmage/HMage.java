package onimen.anni.hmage;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.command.PrefCommand;
import onimen.anni.hmage.gui.AttackKeyListener;
import onimen.anni.hmage.gui.GuiSettings;
import onimen.anni.hmage.gui.PlayerTickListener;
import onimen.anni.hmage.gui.hud.ArrowCounterHUD;
import onimen.anni.hmage.gui.hud.CPSCounterHUD;
import onimen.anni.hmage.gui.hud.InterfaceHUD;
import onimen.anni.hmage.gui.hud.StatusArmorHUD;
import onimen.anni.hmage.gui.hud.StatusEffectHUD;
import onimen.anni.hmage.transformer.HurtingArmorInjector;
import onimen.anni.hmage.util.CustomMovementInput;

@Mod(modid = HMage.MODID, name = HMage.NAME, version = HMage.VERSION)
public class HMage {
  public static final String MODID = "hmage";
  public static final String NAME = "HMage";
  public static final String VERSION = "1.0.1";
  public static Logger logger;

  public static Path config;
  public static HMage INSTANCE;

  private Minecraft mc;

  public KeyBinding openSettingsKey = new KeyBinding("hmage.key.settings", Keyboard.KEY_P, "key.categories.misc");

  private Map<String, InterfaceHUD> hudMap = new HashMap<String, InterfaceHUD>();
  private CustomMovementInput customMovementInput = new CustomMovementInput(Minecraft.getMinecraft().gameSettings);

  private boolean prevAllowFlying = false;

  public void registerItem(InterfaceHUD item) {
    hudMap.put(item.getPrefKey(), item);
  }

  public Map<String, InterfaceHUD> getHUDMap() {
    return this.hudMap;
  }

  public HMage() {
    INSTANCE = this;
    MinecraftForge.EVENT_BUS.register(this);

    this.mc = Minecraft.getMinecraft();

    //Register HUD Items
    this.registerItem(new ArrowCounterHUD());
    this.registerItem(new StatusEffectHUD());
    this.registerItem(new StatusArmorHUD());
    this.registerItem(new CPSCounterHUD());
    //this.registerItem(new AcroJumpHUD());
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

    RenderManager renderManager = this.mc.getRenderManager();
    HurtingArmorInjector.replaceSkinMap(renderManager);
  }

  @SubscribeEvent
  public void onClientTick(ClientTickEvent event) {

    if (mc == null)
      return;

    if (mc.player != null) {
      //If player uses vanilla MovementInput. use togglesneak input
      if (mc.player.movementInput instanceof MovementInputFromOptions)
        mc.player.movementInput = customMovementInput;
    }

  }

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {

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

    if (mc != null && mc.player != null) {
      EntityPlayerSP player = mc.player;

      ItemStack heldStack = player.getHeldItemMainhand();

      if (heldStack != null) {
        switch (Item.getIdFromItem(heldStack.getItem())) {
        case 267:
        case 268:
        case 272:
        case 276:
        case 283:
          player.setSneaking(true);
        }

      }

    }

    for (InterfaceHUD item : this.hudMap.values()) {
      if (item.isEnabled() && item instanceof AttackKeyListener)
        ((AttackKeyListener) item).onAttackKeyClick();
    }

  }

  @SubscribeEvent
  public void onEntityViewRender(RenderWorldLastEvent event) {

    Entity entity = mc.getRenderViewEntity();
    String str = entity.getDisplayName().getFormattedText();

    int thirdPersonView = mc.gameSettings.thirdPersonView;

    if (thirdPersonView == 0) { return; }

    boolean isThirdPersonFrontal = thirdPersonView == 2;
    double partialTicks = event.getPartialTicks();
    FontRenderer fontRenderer = mc.fontRenderer;

    //double dx = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
    //double dy = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
    //double dz = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
    float dyaw = (float) (entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks);
    float dpitch = (float) (entity.prevRotationPitch
        + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks);
    float height = entity.height + 0.5F - (entity.isSneaking() ? 0.25F : 0F);

    //EntityRenderer.drawNameplate(fontRenderer, str, 0, height, 0, 0, dyaw, dpitch, isThirdPersonFrontal, entity.isSneaking());
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {

    boolean allowFlying = event.player.capabilities.allowFlying;

    if (allowFlying != prevAllowFlying) {
      for (InterfaceHUD item : this.hudMap.values()) {
        if (item.isEnabled() && item instanceof PlayerTickListener)
          ((PlayerTickListener) item).onPlayerTick(event.player);
      }
    }

    prevAllowFlying = allowFlying;
  }

  @SubscribeEvent
  public void onRenderSpecificHand(RenderSpecificHandEvent event) {
    if (!Preferences.blockingAttack)
      return;

    //    event.setCanceled(true);

  }

}
