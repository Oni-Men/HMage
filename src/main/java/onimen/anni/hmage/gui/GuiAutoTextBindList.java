package onimen.anni.hmage.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.module.normal.AutoText;
import onimen.anni.hmage.module.normal.AutoText.TextBinding;

public class GuiAutoTextBindList extends GuiListExtended {
  private final GuiAutoTextSetting parent;
  private final Minecraft mc;
  private final List<GuiListExtended.IGuiListEntry> listEntries;
  private final AutoText autoText;

  public GuiAutoTextBindList(GuiAutoTextSetting parent, Minecraft mcIn, AutoText autoText) {
    super(mcIn, parent.width + 45, parent.height, 32, parent.height - 32, 20);
    this.parent = parent;
    this.mc = mcIn;
    this.autoText = autoText;
    HashMap<UUID, TextBinding> bindMap = autoText.getBindMap();

    this.listEntries = new ArrayList<GuiListExtended.IGuiListEntry>();

    for (TextBinding b : bindMap.values()) {
      this.listEntries.add(new GuiAutoTextBindList.BindingEntry(b));
    }

    this.listEntries.add(new AddBindingEntry());
  }

  protected int getSize() {
    return this.listEntries.size();
  }

  /**
   * Gets the IGuiListEntry object for the given index
   */
  public GuiListExtended.IGuiListEntry getListEntry(int index) {
    return this.listEntries.get(index);
  }

  protected int getScrollBarX() {
    return super.getScrollBarX() + 35;
  }

  /**
   * Gets the width of the list
   */
  public int getListWidth() {
    return super.getListWidth() + 32;
  }

  @SideOnly(Side.CLIENT)
  public class BindingEntry implements GuiListExtended.IGuiListEntry {
    private final TextBinding binding;
    private final GuiButton btnChangeKeyBinding;
    private final GuiButton btnRemove;
    private final GuiTextField messageField;

    private BindingEntry(TextBinding binding) {
      this.binding = binding;
      this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 95, 20, String.format("%d", binding.keyCode));
      this.btnRemove = new GuiButton(0, 0, 0, 50, 20, "Remove");
      this.messageField = new GuiTextField(1, GuiAutoTextBindList.this.mc.fontRenderer, 0, 0, 150, 20);

      this.messageField.setEnableBackgroundDrawing(true);
      this.messageField.setMaxStringLength(64);
      this.messageField.setText(binding.text);
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
        boolean isSelected, float partialTicks) {

      this.btnRemove.x = x + 210;
      this.btnRemove.y = y;
      this.btnRemove.drawButton(GuiAutoTextBindList.this.mc, mouseX, mouseY, partialTicks);
      this.btnChangeKeyBinding.x = x + 105;
      this.btnChangeKeyBinding.y = y;
      this.btnChangeKeyBinding.displayString = String.valueOf(binding.keyCode);//this.keybinding.getDisplayName();

      this.messageField.x = x - 50;
      this.messageField.y = y;

      messageField.drawTextBox();
      this.btnChangeKeyBinding.drawButton(GuiAutoTextBindList.this.mc, mouseX, mouseY, partialTicks);
    }

    /**
     * Called when the mouse is clicked within this entry. Returning true means that something within this entry was clicked and the list should not be dragged.
     */
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
      if (this.btnChangeKeyBinding.mousePressed(GuiAutoTextBindList.this.mc, mouseX, mouseY)) {
        GuiAutoTextBindList.this.parent.handlingBindId = this.binding.id;
        return true;
      } else if (this.btnRemove.mousePressed(GuiAutoTextBindList.this.mc, mouseX, mouseY)) {
        GuiAutoTextBindList.this.autoText.removeTextBinding(this.binding.id);
        GuiAutoTextBindList.this.listEntries.remove(this);
        return true;
      } else if (this.messageField.mouseClicked(mouseX, mouseY, mouseEvent)) {
        GuiAutoTextBindList.this.parent.handlingFieldId = this.binding.id;
        GuiAutoTextBindList.this.parent.handlingTextField = this.messageField;
        return true;
      } else {
        return false;
      }
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
      this.btnChangeKeyBinding.mouseReleased(x, y);
      this.btnRemove.mouseReleased(x, y);
    }

    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
    }
  }

  @SideOnly(Side.CLIENT)
  public class AddBindingEntry implements GuiListExtended.IGuiListEntry {
    private final String labelText;
    private final int labelWidth;

    public AddBindingEntry() {
      this.labelText = ChatFormatting.UNDERLINE + I18n.format("Add binding");
      this.labelWidth = GuiAutoTextBindList.this.mc.fontRenderer.getStringWidth(this.labelText);
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
        boolean isSelected, float partialTicks) {
      GuiAutoTextBindList.this.mc.fontRenderer.drawString(this.labelText,
          GuiAutoTextBindList.this.mc.currentScreen.width / 2 - this.labelWidth / 2,
          y + slotHeight - GuiAutoTextBindList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
    }

    /**
     * Called when the mouse is clicked within this entry. Returning true means that something within this entry was clicked and the list should not be dragged.
     */
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
      int index = Math.max(0, GuiAutoTextBindList.this.getSize() - 1);
      TextBinding added = GuiAutoTextBindList.this.autoText.addTextBinding(Keyboard.KEY_NONE, "No Message");
      GuiAutoTextBindList.this.listEntries.add(index, new GuiAutoTextBindList.BindingEntry(added));

      return false;
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }

    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
    }
  }

}
