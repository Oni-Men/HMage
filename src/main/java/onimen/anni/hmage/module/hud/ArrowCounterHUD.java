package onimen.anni.hmage.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ArrowCounterHUD extends AbstractHUD {

  @Override
  public String getName() {
    return "ArrowCountHUD";
  }

  @Override
  public String getDescription() {
    return "インベントリ内の残りの矢を表示";
  }

  @Override
  public int getDefaultPosition() {
    return 11;
  }

  @Override
  public int getDefaultX() {
    return 110;
  }

  @Override
  public int getDefaultY() {
    return -1;
  }

  @Override
  public int getWidth() {
    return 18;
  }

  @Override
  public int getHeight() {
    return 18;
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

    int x = getComputedX(sr);
    int y = getComputedY(sr);

    mc.getRenderItem().renderItemIntoGUI(stack, x, y);
    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, String.valueOf(arrowCount));

    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableRescaleNormal();
    GlStateManager.disableBlend();

  }
}
