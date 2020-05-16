/*
 * Referenced https://github.com/GitFyu/KeystrokesBase
 */

package onimen.anni.hmage.gui.hud;

import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.gui.AttackKeyListener;
import onimen.anni.hmage.util.PositionHelper;
import onimen.anni.hmage.util.PositionHelper.Position;

public class CPSCounterHUD extends Gui implements InterfaceHUD, AttackKeyListener {

  private final static int SPACE = 1;

  private Queue<Long> clicks = new LinkedList<Long>();

  public CPSCounterHUD() {
    this.clicks = new LinkedList<>();
  }

  private int calculateCPS() {

    long now = System.currentTimeMillis();

    while (!clicks.isEmpty() && clicks.peek() < now) {
      clicks.remove();
    }

    return clicks.size();

  }

  @Override
  public String getPrefKey() {
    return "cpsCounterHUD";
  }

  @Override
  public boolean isEnabled() {
    return Preferences.cpsCounterOption.isEnabled();
  }

  @Override
  public void drawItem(Minecraft mc) {

    String text = String.format("%d CPS", this.calculateCPS());

    ScaledResolution sr = new ScaledResolution(mc);

    int width = mc.fontRenderer.getStringWidth("00 CPS") + 4;
    int height = mc.fontRenderer.FONT_HEIGHT;

    Position position = new PositionHelper.Position(Preferences.cpsCounterOption.getPosition());

    int x = Preferences.cpsCounterOption.getTranslateX();
    int y = Preferences.cpsCounterOption.getTranslateY();

    if (position.centerx) {
      x += sr.getScaledWidth() / 2 - width / 2;
    } else if (position.right) {
      x += sr.getScaledWidth() - width - SPACE;
    } else {
      x += SPACE;
    }

    if (position.centery) {
      y += sr.getScaledHeight() / 2 - height / 2;
    }
    if (position.bottom) {
      y += sr.getScaledHeight() - height - SPACE;
    } else {
      y += SPACE;
    }

    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();

    GlStateManager.pushMatrix();
    GlStateManager.color(0, 0, 0, 0.3f);
    GlStateManager.translate(x, y - 1, 0);
    GlStateManager.glBegin(GL11.GL_QUADS);
    GlStateManager.glVertex3f(0, height + 2, 0);
    GlStateManager.glVertex3f(width, height + 2, 0);
    GlStateManager.glVertex3f(width, 0, 0);
    GlStateManager.glVertex3f(0, 0, 0);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();

    GlStateManager.disableBlend();
    GlStateManager.enableTexture2D();

    this.drawCenteredString(mc.fontRenderer, text, x + width / 2, y + 1, 0xffffff);

  }

  @Override
  public void onAttackKeyClick() {
    this.clicks.add(System.currentTimeMillis() + 1000L);
  }

}
