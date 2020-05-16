package onimen.anni.hmage.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import onimen.anni.hmage.gui.PlayerTickListener;

public class AcroJumpHUD extends Gui implements InterfaceHUD, PlayerTickListener {

  private long allowFlightChanged = 0;
  private String cooldownText = "Cannot Jump now...";
  private boolean isAcrobat = false;

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

    if (!isAcrobat || isFlyableVampire(mc.player)) {
      return;
    }

    ScaledResolution sr = new ScaledResolution(mc);

    String str = cooldownText;
    int width = sr.getScaledWidth();
    int height = sr.getScaledHeight();
    int strWidth = mc.fontRenderer.getStringWidth(str);

    mc.fontRenderer.drawStringWithShadow(str, (width - strWidth) / 2, height - 69, 0xffffff);
  }

  @Override
  public void onPlayerTick(EntityPlayer player) {
    if (!player.capabilities.allowFlying && !isFlyableVampire(player)) {
      allowFlightChanged = System.currentTimeMillis();
    }
    cooldownText = getCooldownText();
  }

  private boolean isFlyableVampire(EntityPlayer player) {
    if (player == null)
      return false;

    PotionEffect potionEffect = player.getActivePotionEffect(Potion.getPotionById(11));

    if (potionEffect == null)
      return false;

    if (!potionEffect.getIsPotionDurationMax())
      return false;

    return true;
  }

  private String getCooldownText() {
    float elapsedSec = (float) (System.currentTimeMillis() - allowFlightChanged) / 1000f;
    if (elapsedSec < 10) {
      return String.format("Jump %.2fsec", 10 - elapsedSec);
    }
    return "Jump Ready";
  }
}
