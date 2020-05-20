package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import onimen.anni.hmage.Preferences;

public class ArrowCounterHUD extends Gui implements InterfaceHUD {

  @Override
  public String getPrefKey() {
    return "arrowCounterHUD";
  }

  @Override
  public boolean isEnabled() {
    return Preferences.arrowCounterOption.isEnabled();
  }

  private int countArrows(InventoryPlayer inventory) {
    int arrows = 0;

    for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
      ItemStack stack = inventory.getStackInSlot(slot);

      if (stack == null)
        continue;

      if (Item.getIdFromItem(stack.getItem()) == 262) {
        arrows += stack.getCount();
      }
    }

    return arrows;
  }

  @Override
  public void drawItem(Minecraft mc) {

    ItemStack stackInHand = mc.player.getHeldItemMainhand();
    int itemId = Item.getIdFromItem(stackInHand.getItem());
    //If item that's in player's main hand wasn't arrow or bow.
    if (itemId != 262 && itemId != 261)
      return;

    InventoryPlayer inventory = mc.player.inventory;
    int arrowCount = this.countArrows(inventory);

    if (arrowCount == 0)
      return;

    ScaledResolution sr = new ScaledResolution(mc);
    ItemStack stack = new ItemStack(Item.getItemById(262), 1);

    EnumHandSide enumhandside = Minecraft.getMinecraft().player.getPrimaryHand().opposite();
    int inverter = (enumhandside == EnumHandSide.LEFT ? 1 : -1);

    int x = sr.getScaledWidth() / 2 + inverter * (91 + 20) + 2;
    int y = sr.getScaledHeight() - 16 - 3;

    mc.getRenderItem().renderItemIntoGUI(stack, x, y);
    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, String.valueOf(arrowCount));

    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableRescaleNormal();
    GlStateManager.disableBlend();

  }
}
