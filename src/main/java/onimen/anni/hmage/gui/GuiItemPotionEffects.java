package onimen.anni.hmage.gui;

import java.util.Collection;

import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import onimen.anni.hmage.Preferences;

public class GuiItemPotionEffects extends Gui implements IGuiItem {

	@Override
	public boolean isEnabled() {
		return Preferences.potionEffectsHUD;
	}

	@Override
	public void drawItem(Minecraft mc) {

		ScaledResolution sr = new ScaledResolution(mc);
		EntityPlayerSP player = mc.player;
		Collection<PotionEffect> potionEffects = player.getActivePotionEffects();

		if (potionEffects.isEmpty())
			return;

		int index = 0;

		for (PotionEffect potionEffect : Ordering.natural().reverse().sortedCopy(potionEffects)) {

			Potion potion = potionEffect.getPotion();

			if (!potion.hasStatusIcon() || !potionEffect.doesShowParticles())
				continue;

			int x = sr.getScaledWidth() - 20;
			int y = index * 20 + 2;

			int iconIndex = potion.getStatusIconIndex();

			float alpha = 1F;

			mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.color(1F, 1F, 1F, alpha);
			this.drawTexturedModalRect(x, y, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);

			String durationString = Potion.getPotionDurationString(potionEffect, 1.0F);

			this.drawString(mc.fontRenderer, durationString,
					x - mc.fontRenderer.getStringWidth(durationString) - 1, y + 9 - mc.fontRenderer.FONT_HEIGHT / 2,
					0xffffff);

			index++;
		}

	}

}
