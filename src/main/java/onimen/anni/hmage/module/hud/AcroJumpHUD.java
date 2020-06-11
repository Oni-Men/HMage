package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.module.hud.layout.Layout;
import onimen.anni.hmage.observer.ClassType;

public class AcroJumpHUD extends AbstractHUD {

  private long allowFlightChanged = 0;
  private String cooldownText = "Cannot Jump now...";
  private boolean prevAllowFlying = false;


  @Override
  public boolean isEnable() {
    return true;
  }

  @Override
  public String getId() {
    return "hmage.module.hud.acrojump-ct";
  }

  @Override
  public int getDefaultX() {
    return 0;
  }

  @Override
  public int getDefaultY() {
    return 30;
  }

  @Override
  public int getWidth() {
    return Minecraft.getMinecraft().fontRenderer.getStringWidth(cooldownText);
  }

  @Override
  public int getHeight() {
    return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
  }

  @Override
  public Layout getDefaultLayout() {
    return Layout.getLayout().top().centerx();
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {

    if (!layoutMode) {
      if (!this.isAcrobat()) { return; }
    }

    ScaledResolution sr = new ScaledResolution(mc);
    mc.fontRenderer.drawStringWithShadow(layoutMode ? "Jump Ready!" : cooldownText, getComputedX(sr), getComputedY(sr),
        0xffffff);
  }

  @SubscribeEvent
  public void onPlayerTick(PlayerTickEvent event) {

    boolean allowFlying = event.player.capabilities.allowFlying;

    if (prevAllowFlying && !allowFlying) {
      allowFlightChanged = System.currentTimeMillis();
    }
    cooldownText = getCooldownText();
    prevAllowFlying = allowFlying;
  }

  private boolean isAcrobat() {
    if (HMage.anniObserverMap.getAnniObserver() != null) {
      return HMage.anniObserverMap.getAnniObserver().getGameInfo().getClassType() == ClassType.ACROBAT;
    }
    return false;
  }

  private String getCooldownText() {
    int cooltime = 10 - (int) ((System.currentTimeMillis() - allowFlightChanged) / 1000f);
    if (cooltime > 0) {
      return String.format("Jump %dsec", cooltime);
    }
    return "Jump Ready!";
  }

}
