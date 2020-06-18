package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.module.hud.layout.Layout;
import onimen.anni.hmage.observer.ClassType;

public class AcroJumpHUD extends LabelHUD {

  private long allowFlightChanged = 0;
  private boolean prevAllowFlying = false;

  public AcroJumpHUD() {
    this.paddingX = 2;
    this.paddingY = 1;
    this.text = "Canno Jump now...";
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
  public Layout getDefaultLayout() {
    return Layout.getLayout().top().centerx();
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {

    if (!layoutMode) {
      if (!canBehaivor() || !this.isAcrobat()) { return; }
    }

    text = layoutMode ? "Jump Ready" : getCooldownText();

    super.drawItem(mc, layoutMode);
  }

  @SubscribeEvent
  public void onPlayerTick(PlayerTickEvent event) {

    boolean allowFlying = event.player.capabilities.allowFlying;

    if (prevAllowFlying && !allowFlying) {
      allowFlightChanged = System.currentTimeMillis();
    }
    text = getCooldownText();
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
