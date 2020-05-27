package onimen.anni.hmage;

import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
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
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.cape.GlobalPlayerUseCapeManager;
import onimen.anni.hmage.command.DebugCommand;
import onimen.anni.hmage.command.NameCommand;
import onimen.anni.hmage.command.PrefCommand;
import onimen.anni.hmage.gui.GuiAnniServers;
import onimen.anni.hmage.gui.GuiSettings;
import onimen.anni.hmage.module.CpsCounter;
import onimen.anni.hmage.module.InterfaceModule;
import onimen.anni.hmage.module.RecipeBookRemover;
import onimen.anni.hmage.module.SpeedFovDisabler;
import onimen.anni.hmage.module.hud.AcroJumpHUD;
import onimen.anni.hmage.module.hud.ArmorDurabilityHUD;
import onimen.anni.hmage.module.hud.ArrowCounterHUD;
import onimen.anni.hmage.module.hud.CpsCounterHUD;
import onimen.anni.hmage.module.hud.InterfaceHUD;
import onimen.anni.hmage.module.hud.KillCounterHUD;
import onimen.anni.hmage.module.hud.NexusDamageHUD;
import onimen.anni.hmage.module.hud.StatusEffectHUD;
import onimen.anni.hmage.observer.AnniObserverMap;
import onimen.anni.hmage.transformer.HurtingArmorInjector;
import onimen.anni.hmage.util.CustomMovementInput;
import onimen.anni.hmage.util.GuiScreenUtils;
import onimen.anni.hmage.util.scheduler.SyncTaskQueue;

@Mod(modid = HMage.MODID, name = HMage.NAME, version = HMage.VERSION)
public class HMage {
  public static final String MODID = "hmage";
  public static final String NAME = "HMage";
  public static final String VERSION = "1.0.1";
  public static Logger logger;

  public static HMage INSTANCE;
  private Minecraft mc;

  private CustomMovementInput customMovementInput = new CustomMovementInput(Minecraft.getMinecraft().gameSettings);

  private static Map<String, InterfaceModule> moduleMap = Maps.newLinkedHashMap();
  private static Map<String, InterfaceHUD> hudMap = Maps.newLinkedHashMap();

  public static AnniObserverMap anniObserverMap;

  public void registerModule(InterfaceModule module) {
    if (module == null) { return; }

    MinecraftForge.EVENT_BUS.register(module);

    moduleMap.put(module.getName(), module);
    if (module instanceof InterfaceHUD) {
      hudMap.put(module.getName(), (InterfaceHUD) module);
    }
  }

  public static Map<String, InterfaceModule> getModuleMap() {
    return moduleMap;
  }

  public static Map<String, InterfaceHUD> getHUDMap() {
    return hudMap;
  }

  public HMage() {
    INSTANCE = this;
  }

  @SideOnly(Side.CLIENT)
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    logger = event.getModLog();
    Preferences.load(event);
  }

  @SideOnly(Side.CLIENT)
  @EventHandler
  public void init(FMLInitializationEvent event) {

    this.mc = Minecraft.getMinecraft();
    anniObserverMap = AnniObserverMap.getInstance();

    CpsCounter cpsCounter = new CpsCounter();
    //Register Modules
    this.registerModule(cpsCounter);
    this.registerModule(new RecipeBookRemover());
    this.registerModule(new SpeedFovDisabler());

    //HUD
    this.registerModule(new ArrowCounterHUD());
    this.registerModule(new StatusEffectHUD());
    this.registerModule(new ArmorDurabilityHUD());
    this.registerModule(new CpsCounterHUD(cpsCounter));
    this.registerModule(new AcroJumpHUD());
    this.registerModule(new KillCounterHUD(this.mc.fontRenderer));
    this.registerModule(new NexusDamageHUD(this.mc.fontRenderer));

    ClientCommandHandler.instance.registerCommand(new PrefCommand());
    ClientCommandHandler.instance.registerCommand(new DebugCommand());
    ClientCommandHandler.instance.registerCommand(new NameCommand());
    ClientRegistry.registerKeyBinding(Preferences.openSettingsKey);

    RenderManager renderManager = this.mc.getRenderManager();
    HurtingArmorInjector.replaceSkinMap(renderManager);
    //Capeをロード
    GlobalPlayerUseCapeManager.loadCape();
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

    if (anniObserverMap.getAnniObserver() != null) {
      anniObserverMap.getAnniObserver().onClientTick(event);
    }
  }

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {

    if (Preferences.openSettingsKey.isPressed()) {
      if (mc.currentScreen == null) {
        //mc.displayGuiScreen(new CapeSetting());
        mc.displayGuiScreen(new GuiSettings(getModuleMap()));
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

        for (InterfaceHUD item : hudMap.values()) {
          if (item.isEnable())
            item.drawItem(mc);
        }

      }

    } else if (type == ElementType.POTION_ICONS) {
      if (event.isCancelable() && Preferences.getBoolean("StatusEffectHUD.enabled", true))
        event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onInitGuiEvent(InitGuiEvent event) {
    if (!(event.getGui() instanceof GuiChest)) { return; }
    GuiChest gui = (GuiChest) event.getGui();

    if (gui instanceof GuiAnniServers) { return; }

    IInventory chestInventory = GuiScreenUtils.getChestInventory(gui);

    if (chestInventory == null) { return; }

    ITextComponent chestDisplayName = chestInventory.getDisplayName();
    if (chestDisplayName.getFormattedText().startsWith(GuiScreenUtils.SELEC_SERVER)) {
      mc.displayGuiScreen(new GuiAnniServers(mc.player.inventory, chestInventory));
    }
  }

  @SubscribeEvent
  public void onRecieveChat(ClientChatReceivedEvent event) {
    if (anniObserverMap.getAnniObserver() != null) {
      anniObserverMap.getAnniObserver().onRecieveChat(event);
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onClientTickEvent(TickEvent.ClientTickEvent event) {
    Runnable nextTask;
    while ((nextTask = SyncTaskQueue.getNextTask()) != null) {
      nextTask.run();
    }
  }
}
