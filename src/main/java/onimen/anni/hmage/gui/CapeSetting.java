package onimen.anni.hmage.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import onimen.anni.hmage.cape.DefaultCapeType;
import onimen.anni.hmage.cape.SPPlayerUseCape;

@SideOnly(Side.CLIENT)
public class CapeSetting extends GuiScreen {
  /** The List GuiSlot object reference. */
  private CapeSetting.List list;

  public CapeSetting() {
  }

  /**
   * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the window resizes, the buttonList is cleared beforehand.
   */
  @Override
  public void initGui() {
    this.list = new CapeSetting.List(this.mc);
    this.list.registerScrollButtons(7, 8);
  }

  /**
   * Handles mouse input.
   */
  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.list.handleMouseInput();
  }

  /**
   * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
   */
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.enabled) {
      switch (button.id) {
      case 5:
        break;
      case 6:
        break;
      default:
        this.list.actionPerformed(button);
      }
    }
  }

  /**
   * Draws the screen and all the components in it.
   */
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.list.drawScreen(mouseX, mouseY, partialTicks);
    this.drawCenteredString(this.fontRenderer, "Select Cape", this.width / 2, 16, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @SideOnly(Side.CLIENT)
  class List extends GuiSlot {
    private final DefaultCapeType[] capeList = DefaultCapeType.values();

    public List(Minecraft mcIn) {
      super(mcIn, CapeSetting.this.width, CapeSetting.this.height, 32, CapeSetting.this.height - 65 + 4, 18);
    }

    @Override
    protected int getSize() {
      return this.capeList.length;
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
      SPPlayerUseCape.setResourceLocation(capeList[slotIndex]);
      this.mc.displayGuiScreen((GuiScreen) null);
      this.mc.setIngameFocus();
    }

    @Override
    protected boolean isSelected(int slotIndex) {
      return capeList[slotIndex] == SPPlayerUseCape.getCapeType();
    }

    @Override
    protected int getContentHeight() {
      return this.getSize() * 18;
    }

    @Override
    protected void drawBackground() {
      CapeSetting.this.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn,
        float partialTicks) {
      CapeSetting.this.fontRenderer.setBidiFlag(true);
      CapeSetting.this.drawCenteredString(CapeSetting.this.fontRenderer,
          capeList[slotIndex].getTitle(), this.width / 2, yPos + 1,
          16777215);
    }
  }
}