package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.util.ShotbowUtils;

public class KillCounterHUD extends AbstractHUD {

  @Override
  public String getName() {
    return "KillCountHUD";
  }

  @Override
  public int getDefaultPosition() {
    return 0;
  }

  @Override
  public String getDescription() {
    return "キル数を表示";
  }

  @Override
  public void drawItem(Minecraft mc) {
    if (!ShotbowUtils.isShotbow(mc))
      return;

    if (HMage.getAnniObserver() == null)
      return;

    int killCount = HMage.getAnniObserver().getKillCount();

    mc.fontRenderer.drawString("Kills " + killCount, 150, 20, 0xffffff);

  }


}
