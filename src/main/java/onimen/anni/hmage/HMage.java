package onimen.anni.hmage;

import java.io.File;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
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
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.cape.GlobalPlayerUseCapeManager;
import onimen.anni.hmage.cape.SPPlayerUseCape;
import onimen.anni.hmage.command.DebugCommand;
import onimen.anni.hmage.command.NameCommand;
import onimen.anni.hmage.command.TestAnniCommand;
import onimen.anni.hmage.event.GetLocationCapeEvent;
import onimen.anni.hmage.event.PlayParticleEvent;
import onimen.anni.hmage.gui.GuiAnniServers;
import onimen.anni.hmage.gui.GuiSettings;
import onimen.anni.hmage.module.ModuleManager;
import onimen.anni.hmage.module.hud.AcroJumpHUD;
import onimen.anni.hmage.module.hud.ArmorDurabilityHUD;
import onimen.anni.hmage.module.hud.ArrowCounterHUD;
import onimen.anni.hmage.module.hud.CpsCounterHUD;
import onimen.anni.hmage.module.hud.FpsHUD;
import onimen.anni.hmage.module.hud.InterfaceHUD;
import onimen.anni.hmage.module.hud.KillCounterHUD;
import onimen.anni.hmage.module.hud.NexusDamageHUD;
import onimen.anni.hmage.module.hud.PingHUD;
import onimen.anni.hmage.module.hud.StatusEffectHUD;
import onimen.anni.hmage.module.normal.BodyArrowRemover;
import onimen.anni.hmage.module.normal.CustomFont;
import onimen.anni.hmage.module.normal.CustomGuiBackground;
import onimen.anni.hmage.module.normal.FixedFOV;
import onimen.anni.hmage.module.normal.OldGUI;
import onimen.anni.hmage.module.normal.RecipeBookRemover;
import onimen.anni.hmage.module.normal.SSClipboard;
import onimen.anni.hmage.observer.AnniChatReciveExecutor;
import onimen.anni.hmage.observer.AnniObserver;
import onimen.anni.hmage.observer.AnniObserverMap;
import onimen.anni.hmage.observer.data.AnniPlayerData;
import onimen.anni.hmage.observer.data.GameInfo;
import onimen.anni.hmage.observer.killeffect.AnniKillEffectManager;
import onimen.anni.hmage.transformer.HurtingArmorInjector;
import onimen.anni.hmage.util.GuiScreenUtils;
import onimen.anni.hmage.util.HMageLogger;
import onimen.anni.hmage.util.ShotbowUtils;
import onimen.anni.hmage.util.font.FontGenerateData;
import onimen.anni.hmage.util.font.FontGenerateWorker;
import onimen.anni.hmage.util.scheduler.SyncScheduledTaskQueue;
import onimen.anni.hmage.util.scheduler.SyncTaskQueue;

@Mod(modid = HMage.MODID, name = HMage.NAME, version = HMage.VERSION)
public class HMage {
  public static final String MODID = "hmage";
  public static final String NAME = "HMage";
  public static final String VERSION = "1.2.4";
  public static HMageLogger logger;

  public static final long startMilliTime = System.currentTimeMillis();

  public static HMage INSTANCE;
  private Minecraft mc;

  public static AnniObserverMap anniObserverMap;
  public static File modConfigurationDirectory;


  public HMage() {
    INSTANCE = this;
  }

  @SideOnly(Side.CLIENT)
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    logger = new HMageLogger(event.getModLog());
    Preferences.load(event);
    modConfigurationDirectory = event.getModConfigurationDirectory();
    anniObserverMap = AnniObserverMap.getInstance();

    //明るさの設定を変更
    Options options = GameSettings.Options.GAMMA;
    options.setValueMax(15);
  }

  @SideOnly(Side.CLIENT)
  @EventHandler
  public void init(FMLInitializationEvent event) {

    this.mc = Minecraft.getMinecraft();

    //Register Modules
    ModuleManager.registerModule(new RecipeBookRemover());
    ModuleManager.registerModule(new FixedFOV());
    ModuleManager.registerModule(new CustomGuiBackground());
    ModuleManager.registerModule(new OldGUI());
    ModuleManager.registerModule(new SSClipboard());
    ModuleManager.registerModule(new CustomFont());
    ModuleManager.registerModule(new BodyArrowRemover());

    //HUD
    ModuleManager.registerModule(new ArrowCounterHUD());
    ModuleManager.registerModule(new StatusEffectHUD());
    ModuleManager.registerModule(new ArmorDurabilityHUD());
    ModuleManager.registerModule(new CpsCounterHUD());
    ModuleManager.registerModule(new AcroJumpHUD());
    ModuleManager.registerModule(new KillCounterHUD());
    ModuleManager.registerModule(new NexusDamageHUD());
    ModuleManager.registerModule(new FpsHUD());
    ModuleManager.registerModule(new PingHUD());

    //モジュールの設定をファイルから読み込む
    ModuleManager.loadPreferenceAll();

    ClientCommandHandler.instance.registerCommand(new DebugCommand());
    ClientCommandHandler.instance.registerCommand(new NameCommand());
    ClientCommandHandler.instance.registerCommand(new TestAnniCommand());

    ClientRegistry.registerKeyBinding(Preferences.openSettingsKey);
    ClientRegistry.registerKeyBinding(Preferences.showAnniRankingTab);

    RenderManager renderManager = this.mc.getRenderManager();
    HurtingArmorInjector.replaceSkinMap(renderManager);
    //Capeをロード
    GlobalPlayerUseCapeManager.loadCape();

    //Anni用スレッド開始
    AnniChatReciveExecutor.startThread();

    HMageDiscordHandler.INSTANCE.updatePresenceWithNormal();
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onClientTick(ClientTickEvent event) {
    if (anniObserverMap.getAnniObserver() != null) {
      anniObserverMap.getAnniObserver().onClientTick(event);
    } else {
      HMageDiscordHandler.INSTANCE.updatePresenceWithNormal();
    }
    Runnable nextTask;
    while ((nextTask = SyncTaskQueue.getNextTask()) != null) {
      nextTask.run();
    }
    FontGenerateData result;
    while ((result = FontGenerateWorker.getNextResult()) != null) {
      try {
        int glTextureId = GlStateManager.generateTexture();
        TextureUtil.uploadTextureImage(glTextureId, result.image);
        result.data.setGlTextureId(glTextureId);
        result.data.complete();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    SyncScheduledTaskQueue.tick();
  }

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {

    if (Preferences.openSettingsKey.isPressed()) {
      if (mc.currentScreen == null) {
        //mc.displayGuiScreen(new CapeSetting());
        mc.displayGuiScreen(new GuiSettings(ModuleManager.getModuleMap()));
      }
    }
  }

  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {

    if (!Preferences.enabled)
      return;

    if (mc.gameSettings.showDebugInfo)
      return;

    if (mc.currentScreen == null && event.getType() == ElementType.TEXT) {
      if (Preferences.showAnniRankingTab.isKeyDown()) {
        renderAnniRanking(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
      } else if (event.getType() == ElementType.TEXT) {
        for (InterfaceHUD item : ModuleManager.getHUDMap().values()) {
          if (item.isEnable())
            item.drawItem(mc);
        }
      }
    }
  }

  @SubscribeEvent
  public void onDrawScreen(DrawScreenEvent.Post event) {
    if (!Preferences.showGameStatsInInventory) { return; }
    if (!(event.getGui() instanceof GuiInventory)) { return; }

    renderAnniRanking(event.getGui().width, event.getGui().height);
  }

  private void renderAnniRanking(int width, int height) {
    AnniObserver anniObserver = HMage.anniObserverMap.getAnniObserver();

    if (anniObserver == null)
      return;

    GameInfo gameInfo = anniObserver.getGameInfo();

    List<AnniPlayerData> killRanking = gameInfo.getTotalKillRanking(10);
    List<AnniPlayerData> nexusRanking = gameInfo.getNexusRanking(10);

    GlStateManager.disableLighting();

    GuiScreenUtils.drawRankingLeft("Kills in this Game", killRanking, mc.fontRenderer, 4, 4,
        d -> String.format("%dK", d.getTotalKillCount()));

    GuiScreenUtils.drawRankingRight("Nexus damage in this Game", nexusRanking, mc.fontRenderer, 4, width - 4,
        d -> String.format("%dD", d.getNexusDamageCount()));

    mc.fontRenderer.drawStringWithShadow("You can disable this ranking in Match history", 4,
        height - 10,
        0xdddddd);
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
    //デバック情報
    TestAnniCommand.debugPrint(event);

    if (anniObserverMap.getAnniObserver() != null) {
      anniObserverMap.getAnniObserver().onRecieveChat(event);
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPlayParticleEvent(PlayParticleEvent event) {
    if (ShotbowUtils.isShotbow(Minecraft.getMinecraft())) {
      AnniKillEffectManager.getInstance().executeKillEffect(event.getParticle());
    }
  }

  @SubscribeEvent
  public void onGetLocationCape(GetLocationCapeEvent event) {

    //capeのリソースを取得
    UUID uniqueID = event.getPlayer().getUniqueID();
    boolean isMe = uniqueID.equals(Minecraft.getMinecraft().player.getUniqueID());
    ResourceLocation locationCape = isMe ? SPPlayerUseCape.getResourceLocation()
        : GlobalPlayerUseCapeManager.getCapeResource(uniqueID);

    event.setCapeLocation(locationCape);
  }

}
