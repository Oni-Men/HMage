package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.util.ShotbowUtils;

public class KillCounterHUD extends AbstractHUD {

  private String text = "";
  private int width, height;

  public KillCounterHUD(FontRenderer fontRenderer) {
    width = fontRenderer.getStringWidth("Kills 000");
    height = fontRenderer.FONT_HEIGHT;
  }

  @Override
  public String getName() {
    return "KillCountHUD";
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
    return 16;
  }

  @Override
  public String getDescription() {
    return "キル数を表示";
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void drawItem(Minecraft mc) {

    if (!ShotbowUtils.isShotbow(mc))
      return;

    if (HMage.anniObserverMap.getAnniObserver() == null)
      return;

    String killCountText = "Kills " + HMage.anniObserverMap.getAnniObserver().getGameInfo().getKillCount();

    if (!this.text.contentEquals(killCountText)) {
      this.text = killCountText;
      this.width = mc.fontRenderer.getStringWidth(killCountText);
    }
    ScaledResolution sr = new ScaledResolution(mc);

    int x = getComputedX(sr);
    int y = getComputedY(sr);

    int offset = getWidth() - mc.fontRenderer.getStringWidth(text);

    this.drawRect(x, y - 1, getWidth() + 4, getHeight() + 2);
    mc.fontRenderer.drawString(text, x + 2 + offset, y, 0xffffff);

  }


}
