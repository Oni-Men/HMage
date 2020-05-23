package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.observer.AnniObserver.ClassType;

public class AcroJumpHUD extends AbstractHUD {

  private long allowFlightChanged = 0;
  private String cooldownText = "Cannot Jump now...";
  private boolean prevAllowFlying = false;


  @Override
  public boolean isEnable() {
    return true;
  }

  @Override
  public String getDescription() {
    return "アクロジャンプのクールタイムを表示";
  }

  @Override
  public String getName() {
    return "AcroJumpHUD";
  }

  @Override
  public int getDefaultX() {
    return 0;
  }

  @Override
  public int getDefaultY() {
    return 0;
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
  public void drawItem(Minecraft mc) {

    if (!this.isAcrobat()) { return; }

    ScaledResolution sr = new ScaledResolution(mc);
    mc.fontRenderer.drawStringWithShadow(cooldownText, getComputedX(sr), getComputedY(sr), 0xffffff);
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
      return HMage.anniObserverMap.getAnniObserver().getUsingClassType() == ClassType.ACROBAT;
    }
    return false;
  }

  private String getCooldownText() {
    int elapsedSec = (int) ((System.currentTimeMillis() - allowFlightChanged) / 1000f);
    if (elapsedSec != 0) { return String.format("Jump %dsec", 10 - elapsedSec); }
    return "Jump Ready";
  }

  @Override
  public int getDefaultPosition() {
    return 0;
  }

}
