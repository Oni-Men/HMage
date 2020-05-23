package onimen.anni.hmage.gui;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.observer.AnniObserver.GamePhase;
import onimen.anni.hmage.util.NBTUtils;

public class GuiAnniServers extends GuiChest {

  public GuiAnniServers(IInventory upperInv, IInventory lowerInv) {
    super(upperInv, lowerInv);
  }

  @Override
  protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {

    ItemStack stack = slotIn.getStack();
    boolean isServerSelectAction = type == ClickType.PICKUP &&
        slotIn != null &&
        stack != null &&
        Item.getIdFromItem(stack.getItem()) != 0;

    if (isServerSelectAction) {
      HMage.anniObserverMap.setAnniObserver(stack.getDisplayName(), getPhaseFromStack(stack));
    }
    super.handleMouseClick(slotIn, slotId, mouseButton, type);
  }

  private GamePhase getPhaseFromStack(ItemStack stack) {
    List<String> lore = NBTUtils.getLore(stack);
    if (lore != null && lore.size() == 3)
      return GamePhase.getGamePhasebyText(lore.get(2));
    return GamePhase.UNKNOWN;
  }

}
