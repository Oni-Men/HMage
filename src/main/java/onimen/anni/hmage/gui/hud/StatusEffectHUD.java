package onimen.anni.hmage.gui.hud;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.util.PositionHelper;
import onimen.anni.hmage.util.PositionHelper.Position;

public class StatusEffectHUD extends Gui implements InterfaceHUD {

	private static final int SPACE = 1;

	@Override
	public String getPrefKey() {
		return "statusEffectHUD";
	}

	@Override
	public boolean isEnabled() {
		return Preferences.statusEffectOption.isEnabled();
	}

	public int calculateWidth(FontRenderer fontRenderer, List<PotionEffect> potionEffectList, boolean isHorizontal) {
		int width = 0;

		for (PotionEffect potionEffect : potionEffectList) {
			int durationStringWidth = fontRenderer.getStringWidth(Potion.getPotionDurationString(potionEffect, 1F));
			if (isHorizontal) {
				width += 20 + durationStringWidth + SPACE;
			} else {
				width = Math.max(width, 20 + durationStringWidth);
			}
		}

		return width;
	}

	public int calculateHeight(List<PotionEffect> potionEffectList, boolean isHorizontal) {
		if (isHorizontal) {
			return 20;
		}
		return potionEffectList.stream()
				.filter(p -> p.getPotion().hasStatusIcon() && p.doesShowParticles())
				.collect(Collectors.toList())
				.size() * (20 + SPACE);
	}

	@Override
	public void drawItem(Minecraft mc) {

		FontRenderer fontRenderer = mc.fontRenderer;
		ScaledResolution sr = new ScaledResolution(mc);
		EntityPlayerSP player = mc.player;

		List<PotionEffect> potionEffects = Ordering.natural().reverse().sortedCopy(player.getActivePotionEffects());

		if (potionEffects.isEmpty())
			return;

		Position position = new PositionHelper.Position(Preferences.statusEffectOption.getPosition());

		int x = Preferences.statusEffectOption.getTranslateX();
		int y = Preferences.statusEffectOption.getTranslateY();

		if (position.right) {
			x += sr.getScaledWidth() - calculateWidth(fontRenderer, potionEffects, position.isHorizontal()) - SPACE;
		} else {
			x += SPACE;
		}

		if (position.bottom) {
			y += sr.getScaledHeight() - calculateHeight(potionEffects, position.isHorizontal()) - SPACE;
		} else {
			y += SPACE;
		}

		for (PotionEffect potionEffect : potionEffects) {

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
				this.drawString(fontRenderer, text, x - SPACE, y + 10 - fontRenderer.FONT_HEIGHT / 2, 0xffffff);
			} else {
				this.drawTexturedModalRect(x, y, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
				this.drawString(fontRenderer, text, x + 20, y + 10 - fontRenderer.FONT_HEIGHT / 2, 0xffffff);
			}

			if (position.isHorizontal()) {
				x += 20 + textWidth + SPACE;
			} else {
				y += 20 + SPACE;
			}
		}

	}

}
