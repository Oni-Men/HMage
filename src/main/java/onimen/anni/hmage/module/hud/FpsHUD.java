package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.module.hud.layout.Layout;

public class FpsHUD extends LabelHUD {

  public FpsHUD() {
    this.paddingX = 2;
    this.paddingY = 1;
  }

  @Override
  public Layout getDefaultLayout() {
    return Layout.getLayout().centerx().top();
  }

  @Override
  public int getDefaultX() {
    return 0;
  }

  @Override
  public int getDefaultY() {
    return 16;
  }

  @Override
  public String getId() {
    return "hmage.module.hud.fps";
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {
    text = String.format("%d FPS", Minecraft.getDebugFPS());
    super.drawItem(mc, layoutMode);
  }
}
