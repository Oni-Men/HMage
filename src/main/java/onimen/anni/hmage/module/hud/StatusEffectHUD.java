package onimen.anni.hmage.module.hud;

import java.util.ArrayList;
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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import onimen.anni.hmage.module.hud.layout.Layout;

public class StatusEffectHUD extends AbstractHUD {

  private static final List<PotionEffect> effectsForLayoutMode = new ArrayList<>();

  private List<PotionEffect> potionEffectList = Lists.newArrayList();

  public StatusEffectHUD() {
    effectsForLayoutMode.add(new PotionEffect(Potion.getPotionById(1), 1800));
    effectsForLayoutMode.add(new PotionEffect(Potion.getPotionById(2), 3600));
    effectsForLayoutMode.add(new PotionEffect(Potion.getPotionById(3), 7200));
    effectsForLayoutMode.add(new PotionEffect(Potion.getPotionById(4), 14400));
  }

  @Override
  public String getId() {
    return "hmage.module.hud.status-effect";
  }

  @Override
  public Layout getDefaultLayout() {
    return Layout.getLayout().top().right();
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
    if (widthHashCode != potionEffectList.hashCode()) {
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
      cachedWidth = width;
      widthHashCode = potionEffectList.hashCode();
    }

    return this.cachedWidth;
  }

  @Override
  public int getHeight() {
    if (isHorizontal()) { return 20; }
    if (heightHashCode != potionEffectList.hashCode()) {
      cachedHeight = potionEffectList.stream()
          .filter(p -> p.getPotion().hasStatusIcon() && p.doesShowParticles())
          .collect(Collectors.toList())
          .size() * (20);
      heightHashCode = potionEffectList.hashCode();
    }
    return cachedHeight;
  }

  @Override
  public void drawItem(Minecraft mc, boolean layoutMode) {

    FontRenderer fontRenderer = mc.fontRenderer;
    ScaledResolution sr = new ScaledResolution(mc);
    EntityPlayerSP player = mc.player;

    potionEffectList.clear();
    if (layoutMode) {
      potionEffectList.addAll(effectsForLayoutMode);
    } else {
      potionEffectList.addAll(Ordering.natural().reverse().sortedCopy(player.getActivePotionEffects()));
    }

    if (potionEffectList.isEmpty())
      return;

    Layout position = getLayout();

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

      if (position.isRight() && !position.isHorizontal()) {
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

  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
    if (event.getType() == ElementType.POTION_ICONS) {
      if (this.isEnable())
        event.setCanceled(true);
    }
  }
}
