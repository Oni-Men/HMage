/*
 * Referenced https://github.com/GitFyu/KeystrokesBase
 */

package onimen.anni.hmage.module.hud;

import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.module.hud.layout.Layout;

public class CpsCounterHUD extends AbstractHUD {
  private final CPSCounter counter;

  public CpsCounterHUD() {
    this.counter = new CPSCounter();
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
  public Layout getDefaultLayout() {
    return Layout.getLayout().top().left();
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
  public void drawItem(Minecraft mc, boolean layoutMode) {
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

  @SubscribeEvent
  public void onMouseInputEvent(MouseInputEvent event) {
    if (!Preferences.enabled)
      return;

    if (!Mouse.getEventButtonState())
      return;

    if (Mouse.getEventButton() != 0)
      return;

    this.counter.clicked();
  }

  static class CPSCounter {
    private final Queue<Long> clicks = new LinkedList<>();

    public int getCurrentCPS() {
      long now = System.currentTimeMillis();
      while (!clicks.isEmpty() && clicks.peek() < now) {
        clicks.remove();
      }
      return clicks.size();
    }

    public void clicked() {
      this.clicks.add(System.currentTimeMillis() + 1000L);
    }
  }

}
