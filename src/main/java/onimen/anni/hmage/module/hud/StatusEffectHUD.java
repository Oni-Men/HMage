package onimen.anni.hmage.module.hud;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import onimen.anni.hmage.util.PositionHelper;
import onimen.anni.hmage.util.PositionHelper.Position;

public class StatusEffectHUD extends AbstractHUD {

  private List<PotionEffect> potionEffectList = Lists.newArrayList();

  @Override
  public String getName() {
    return "StatusEffectHUD";
  }

  @Override
  public String getDescription() {
    return "ステータス効果と残り時間を表示";
  }

  @Override
  public int getDefaultPosition() {
    return 2;
  }

  @Override
  public int getDefaultX() {
    return 0;
  }

  @Override
  public int getDefaultY() {
    return 0;
  }

  @Override
  public int getWidth() {
    int width = 0;


    for (PotionEffect potionEffect : potionEffectList) {
      int durationStringWidth = Minecraft.getMinecraft().fontRenderer
          .getStringWidth(Potion.getPotionDurationString(potionEffect, 1F));
      if (isHorizontal()) {
        width += 20 + durationStringWidth;
      } else {
        width = Math.max(width, 20 + durationStringWidth);
      }
    }

    return width;
  }

  @Override
  public int getHeight() {
    if (isHorizontal()) { return 20; }
    return potionEffectList.stream()
        .filter(p -> p.getPotion().hasStatusIcon() && p.doesShowParticles())
        .collect(Collectors.toList())
        .size() * (20);
  }

  @Override
  public void drawItem(Minecraft mc) {

    FontRenderer fontRenderer = mc.fontRenderer;
    ScaledResolution sr = new ScaledResolution(mc);
    EntityPlayerSP player = mc.player;

    potionEffectList.clear();
    potionEffectList.addAll(Ordering.natural().reverse().sortedCopy(player.getActivePotionEffects()));

    if (potionEffectList.isEmpty())
      return;

    Position position = new PositionHelper.Position(getPositionFlag());

    int x = getComputedX(sr);
    int y = getComputedY(sr);

    for (PotionEffect potionEffect : potionEffectList) {

      Potion potion = potionEffect.getPotion();

      if (!potion.hasStatusIcon() || !potionEffect.doesShowParticles())
        continue;

      String text = Potion.getPotionDurationString(potionEffect, 1.0F);
      int textWidth = fontRenderer.getStringWidth(text);
      int iconIndex = potion.getStatusIconIndex();

      mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
      GlStateManager.enableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.color(1F, 1F, 1F, 1F);

      if (position.right && !position.isHorizontal()) {
        this.drawTexturedModalRect(x + textWidth, y, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
        fontRenderer.drawString(text, x, y + 10 - fontRenderer.FONT_HEIGHT / 2, 0xffffff);
      } else {
        this.drawTexturedModalRect(x, y, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
        fontRenderer.drawString(text, x + 20, y + 10 - fontRenderer.FONT_HEIGHT / 2, 0xffffff);
      }

      if (position.isHorizontal()) {
        x += 20 + textWidth;
      } else {
        y += 20;
      }
    }

  }

}
