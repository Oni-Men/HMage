/*
 * Referenced https://github.com/GitFyu/KeystrokesBase
 */

package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import onimen.anni.hmage.module.CpsCounter;

public class CpsCounterHUD extends AbstractHUD {

  private final CpsCounter counter;

  public CpsCounterHUD(CpsCounter counter) {
    this.counter = counter;
  }

  @Override
  public String getName() {
    return "CPSCountHUD";
  }

  @Override
  public String getDescription() {
    return "計測したCPSを表示";
  }

  @Override
  public int getDefaultPosition() {
    return 0;
  }

  @Override
  public int getDefaultX() {
    return 45;
  }

  @Override
  public int getDefaultY() {
    return 5;
  }

  @Override
  public int getWidth() {
    return Minecraft.getMinecraft().fontRenderer.getStringWidth("00 CPS");
  }

  @Override
  public int getHeight() {
    return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
  }

  @Override
  public void drawItem(Minecraft mc) {
    int currentCPS = this.counter.getCurrentCPS();

    String text = String.format("%2d CPS", currentCPS);

    ScaledResolution sr = new ScaledResolution(mc);

    int offset = getWidth() - mc.fontRenderer.getStringWidth(text);

    int width = getWidth() + 4;
    int height = getHeight();

    int x = getComputedX(sr);
    int y = getComputedY(sr);

    drawRect(x, y - 1, width, height + 2);

    mc.fontRenderer.drawString(text, x + 2 + offset, y + 1, 0xffffff);

  }

}
