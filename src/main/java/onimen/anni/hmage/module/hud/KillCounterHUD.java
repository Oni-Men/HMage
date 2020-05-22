package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class KillCounterHUD extends AbstractHUD {

  private String text = "";

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
    return Minecraft.getMinecraft().fontRenderer.getStringWidth("Kills 000");
  }

  @Override
  public int getHeight() {
    return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
  }

  @Override
  public void drawItem(Minecraft mc) {

    //    if (!ShotbowUtils.isShotbow(mc))
    //      return;
    //
    //    if (HMage.getAnniObserver() == null)
    //      return;

    text = "19 Kills";//HMage.getAnniObserver().getKillCount();
    ScaledResolution sr = new ScaledResolution(mc);

    int x = getComputedX(sr);
    int y = getComputedY(sr);

    int offset = getWidth() - mc.fontRenderer.getStringWidth(text);

    this.drawRect(x, y - 1, getWidth() + 4, getHeight() + 2);
    mc.fontRenderer.drawString(text, x + 2 + offset, y, 0xffffff);

  }


}
