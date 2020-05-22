/*
 * Referenced https://github.com/GitFyu/KeystrokesBase
 */

package onimen.anni.hmage.module.hud;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import onimen.anni.hmage.module.CpsCounter;
import onimen.anni.hmage.util.PositionHelper;
import onimen.anni.hmage.util.PositionHelper.Position;

public class CpsCounterHUD extends AbstractHUD {

  private final static int SPACE = 1;
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
  public void drawItem(Minecraft mc) {

    String text = String.format("%2d CPS", this.counter.getCurrentCPS());

    ScaledResolution sr = new ScaledResolution(mc);

    int width = mc.fontRenderer.getStringWidth("00 CPS") + 4;
    int offset = text.length() == 4 ? mc.fontRenderer.getStringWidth("0") : 0;
    int height = mc.fontRenderer.FONT_HEIGHT;

    Position position = new PositionHelper.Position(getPosition());

    int x = getX();
    int y = getY();

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

    mc.fontRenderer.drawString(text, x + 2 + offset, y + 1, 0xffffff);

  }


}
