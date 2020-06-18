package onimen.anni.hmage.module;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiLockIconButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OldGUI extends AbstractModule {

  @Override
  public String getId() {
    return "hmage.module.old-gui";
  }

  @SubscribeEvent
  public void onGuiInit(InitGuiEvent event) {
    boolean canBehaivor = canBehaivor();

    GuiScreen gui = event.getGui();

    if (gui instanceof GuiOptions) {
      event.setButtonList(removeDifficultyLock(event.getButtonList()));
    }

    if (gui instanceof GuiContainer) {
      List<Slot> inventorySlots = ((GuiContainer) gui).inventorySlots.inventorySlots;

      if (gui instanceof GuiInventory) {

        for (int i = 0; i < inventorySlots.size(); i++) {
          Slot slot = inventorySlots.get(i);

          if (slot instanceof SlotCrafting) {

            slot.xPos = canBehaivor ? 144 : 154;
            slot.yPos = canBehaivor ? 36 : 28;

            for (int m = 0; m < 2; ++m) {
              for (int n = 0; n < 2; ++n) {
                Slot craft = inventorySlots.get(i + n + m * 2 + 1);
                craft.xPos = (canBehaivor ? 88 : 98) + m * 18;
                craft.yPos = (canBehaivor ? 26 : 18) + n * 18;
              }
            }
            break;
          }
        }
      }

      if (gui instanceof GuiBrewingStand) {
        inventorySlots.get(0).yPos = canBehaivor ? 46 : 51;
        inventorySlots.get(1).yPos = canBehaivor ? 53 : 58;
        inventorySlots.get(2).yPos = canBehaivor ? 46 : 51;
      }

      if (gui instanceof GuiEnchantment) {
        inventorySlots.get(0).xPos = canBehaivor ? 25 : 15;
        inventorySlots.get(1).xPos = canBehaivor ? -25 : 35;
      }

    }

  }

  private List<GuiButton> removeDifficultyLock(List<GuiButton> buttonList) {
    GuiButton current = null, previous = null;
    for (Iterator<GuiButton> i = buttonList.iterator(); i.hasNext();) {
      previous = current;
      current = i.next();
      if (current instanceof GuiLockIconButton) {
        i.remove();
        if (previous != null) {
          previous.setWidth(150);
        }
      }
    }
    return buttonList;
  }
}
