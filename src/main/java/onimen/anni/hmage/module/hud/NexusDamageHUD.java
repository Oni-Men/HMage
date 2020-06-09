package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.module.hud.layout.Layout;
import onimen.anni.hmage.util.ShotbowUtils;

public class NexusDamageHUD extends AbstractHUD {

  private String text;
  private int width, height;

  public NexusDamageHUD(FontRenderer fontRenderer) {
    text = "Nexus 0";
    width = fontRenderer.getStringWidth(text);
    height = fontRenderer.FONT_HEIGHT;
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

    String nexusDamageText = "Nexus 39";

    if (!layoutMode) {

      if (!ShotbowUtils.isShotbow(mc))
        return;

      if (HMage.anniObserverMap.getAnniObserver() == null)
        return;

      nexusDamageText = "Nexus " + HMage.anniObserverMap.getAnniObserver().getGameInfo().getNexusAttackCount();
    }

    if (!this.text.contentEquals(nexusDamageText)) {
      this.text = nexusDamageText;
      this.width = mc.fontRenderer.getStringWidth(text);
    }

    ScaledResolution sr = new ScaledResolution(mc);

    int x = getComputedX(sr);
    int y = getComputedY(sr);

    int offset = getWidth() - mc.fontRenderer.getStringWidth(text);

    this.drawRect(x, y - 1, getWidth() + 4, getHeight() + 2);
    mc.fontRenderer.drawString(text, x + 2 + offset, y, 0xffffff);
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
  public String getName() {
    return "NexusDamageHUD";
  }

  @Override
  public String getDescription() {
    return "ネクサスを削った回数を表示します";
  }

}
