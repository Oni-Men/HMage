package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.module.hud.layout.Layout;
import onimen.anni.hmage.util.ShotbowUtils;

public class NexusDamageHUD extends LabelHUD {

  public NexusDamageHUD() {
    text = "Nexus 0";
    paddingX = 2;
    paddingY = 1;
  }

  @Override
  public String getId() {
    return "module.hud.nexus-damage-counter";
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
    return 27;
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {

    if (!layoutMode) {

      if (!ShotbowUtils.isShotbow(mc))
        return;

      if (HMage.anniObserverMap.getAnniObserver() == null)
        return;

      text = "Nexus " + HMage.anniObserverMap.getAnniObserver().getGameInfo().getNexusAttackCount();
    } else {
      text = "Nexus 99";
    }

    super.drawItem(mc, layoutMode);

  }

}
