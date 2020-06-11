/*
 * Referenced https://github.com/GitFyu/KeystrokesBase
 */

package onimen.anni.hmage.module.hud;

import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.module.hud.layout.Layout;

public class CpsCounterHUD extends LabelHUD {
  private final CPSCounter counter;

  public CpsCounterHUD() {
    this.counter = new CPSCounter();
    this.paddingX = 2;
    this.paddingY = 1;
    this.text = this.getCPSText(0);
  }

  @Override
  public String getId() {
    return "hmage.module.hud.cps-counter";
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

  private String getCPSText(int cps) {
    return String.format("%2d CPS", cps);
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {
    text = this.getCPSText(this.counter.getCurrentCPS());
    super.drawItem(mc, layoutMode);
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
