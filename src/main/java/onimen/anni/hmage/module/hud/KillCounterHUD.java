package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.module.hud.layout.Layout;
import onimen.anni.hmage.util.ShotbowUtils;

public class KillCounterHUD extends LabelHUD {

  public KillCounterHUD() {
    this.paddingX = 2;
    this.paddingY = 1;
  }

  @Override
  public String getId() {
    return "hmage.module.hud.kill-counter";
  }

  @Override
  public Layout getDefaultLayout() {
    return Layout.getLayout().top().left();
  }

  @Override
  public int getDefaultX() {
    return 45;
  }

  @Override
  public int getDefaultY() {
    return 16;
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {

    if (!layoutMode) {
      if (!ShotbowUtils.isShotbow(mc))
        return;

      if (HMage.anniObserverMap.getAnniObserver() == null)
        return;

      text = "Kills " + HMage.anniObserverMap.getAnniObserver().getGameInfo().getKillCount();
    } else {
      text = "Kills 79";
    }

    super.drawItem(mc, layoutMode);

  }

}
