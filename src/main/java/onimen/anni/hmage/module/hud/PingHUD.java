package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import onimen.anni.hmage.module.hud.layout.Layout;

public class PingHUD extends LabelHUD {

  public PingHUD() {
    this.paddingX = 2;
    this.paddingY = 1;
  }

  @Override
  public Layout getDefaultLayout() {
    return Layout.getLayout().top().right();
  }

  @Override
  public int getDefaultX() {
    return -100;
  }

  @Override
  public int getDefaultY() {
    return 2;
  }

  @Override
  public String getId() {
    return "hmage.module.hud.ping";
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {

    EntityPlayerSP player = mc.player;

    if (player == null) { return; }

    if (player.connection == null) { return; }

    NetworkPlayerInfo playerInfo = player.connection.getPlayerInfo(player.getUniqueID());

    if (playerInfo == null) { return; }

    int ping = playerInfo.getResponseTime();

    if (ping < 10000) {
      text = String.format("%4d ms", ping);
    } else {
      text = String.format("%.2e ms", (float) ping);
    }
    super.drawItem(mc, layoutMode);
  }
}
