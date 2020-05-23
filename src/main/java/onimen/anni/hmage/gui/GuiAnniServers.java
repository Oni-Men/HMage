package onimen.anni.hmage.gui;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import onimen.anni.hmage.HMage;

public class GuiAnniServers extends GuiChest {

  public GuiAnniServers(IInventory upperInv, IInventory lowerInv) {
    super(upperInv, lowerInv);
  }

  @Override
  protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {

    if (slotIn == null) {
      super.handleMouseClick(slotIn, slotId, mouseButton, type);
      return;
    }

    ItemStack stack = slotIn.getStack();
    boolean isServerSelectAction = type == ClickType.PICKUP &&
        slotIn != null &&
        stack != null &&
        Item.getIdFromItem(stack.getItem()) != 0;

    if (isServerSelectAction) {
      HMage.setAnniObserver(stack.getDisplayName());
    }
    super.handleMouseClick(slotIn, slotId, mouseButton, type);
  }

}
