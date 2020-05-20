package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.util.ShotbowUtils;

public class KillCounterHUD implements InterfaceHUD {

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getPrefKey() {
    return "killCounterHUD";
  }

  @Override
  public void drawItem(Minecraft mc) {
    if (!ShotbowUtils.isShotbow(mc))
      return;

    //    if (HMage.anniObserver == null)
    //      return;
    //
    //    int killCount = HMage.anniObserver.getKillCount();

    //mc.fontRenderer.drawString("Kills " + killCount, 150, 20, 0xffffff);

  }

}
