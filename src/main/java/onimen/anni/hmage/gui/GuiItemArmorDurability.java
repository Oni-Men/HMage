package onimen.anni.hmage.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import onimen.anni.hmage.Preferences;

public class GuiItemArmorDurability extends Gui implements IGuiItem {

	@Override
	public boolean isEnabled() {
		return Preferences.armorDurabilityHUD;
	}

	@Override
	public void drawItem(Minecraft mc) {

		EntityPlayerSP player = mc.player;

		int x = 1;
		int y = 2;

		List<ItemStack> armorList = new ArrayList<>();

		Iterator<ItemStack> iterator = player.getArmorInventoryList().iterator();
		while (iterator.hasNext()) {
			armorList.add(iterator.next());
		}

		Collections.reverse(armorList);

		for (ItemStack stack : armorList) {

			if (Item.getIdFromItem(stack.getItem()) == 0)
				continue;

			int durability = (int) (255 - 255 * ((float) stack.getItemDamage() / (float) stack.getMaxDamage()));
			/*
			 * Durability
			 * 	100%: 0xffffff
			 * 	  0%: 0xff0000
			 */
			int red = 0xff;
			int green = durability;
			int blue = (int) (durability * ((float) durability / 256));

			int color = (red << 16) | (green << 8) | blue;

			String text = String.valueOf(stack.getMaxDamage() - stack.getItemDamage());

			mc.getRenderItem().renderItemIntoGUI(stack, x, y + 2);
			this.drawString(mc.fontRenderer, text, x + 20, y + 10 - mc.fontRenderer.FONT_HEIGHT / 2, color);

			y += 20;
		}

		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();

	}
}
