package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AcroJumpHUD extends Gui implements InterfaceHUD {

  private long allowFlightChanged = 0;
  private String cooldownText = "Cannot Jump now...";
  private boolean prevAllowFlying = false;

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getPrefKey() {
    return "acrojumpHUD";
  }

  @Override
  public void drawItem(Minecraft mc) {

    if (!this.isAcrobat()) { return; }

    ScaledResolution sr = new ScaledResolution(mc);

    String str = cooldownText;
    int width = sr.getScaledWidth();
    int height = sr.getScaledHeight();
    int strWidth = mc.fontRenderer.getStringWidth(str);

    mc.fontRenderer.drawStringWithShadow(str, (width - strWidth) / 2, height - 69, 0xffffff);
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
    //if (HMage.anniObserver != null) { return HMage.anniObserver.getUsingClassType() == ClassType.ACROBAT; }
    return false;
  }

  private String getCooldownText() {
    int elapsedSec = (int) ((System.currentTimeMillis() - allowFlightChanged) / 1000f);
    if (elapsedSec != 0) { return String.format("Jump %dsec", 10 - elapsedSec); }
    return "Jump Ready";
  }
}
