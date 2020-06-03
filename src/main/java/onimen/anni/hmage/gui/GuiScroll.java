package onimen.anni.hmage.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiScroll extends GuiScreen {

  public int amountScroll;
  public int mouseX, mouseY, prevMouseY;

  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.mouseX = mouseX;
    this.mouseY = mouseY;
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    this.scroll(prevMouseY - mouseY);
    prevMouseY = mouseY;

  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    prevMouseY = mouseY;
    if (mouseButton == 0) {
      for (int i = 0; i < this.buttonList.size(); ++i) {
        GuiButton guibutton = this.buttonList.get(i);

        if (guibutton.mousePressed(this.mc, mouseX, mouseY + amountScroll + 24)) {
          net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(
              this, guibutton, this.buttonList);
          if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
            break;
          guibutton = event.getButton();
          this.selectedButton = guibutton;
          guibutton.playPressSound(this.mc.getSoundHandler());
          this.actionPerformed(guibutton);
          if (this.equals(this.mc.currentScreen))
            net.minecraftforge.common.MinecraftForge.EVENT_BUS
                .post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this,
                    event.getButton(), this.buttonList));
        }
      }
    }
  }

  /**
   * Called when a mouse button is released.
   */
  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    if (this.selectedButton != null && state == 0) {
      this.selectedButton.mouseReleased(mouseX, mouseY + amountScroll);
      this.selectedButton = null;
    }
  }

  public int getMaxScroll() {
    return 0;
  }

  public void scroll(int delta) {
    amountScroll += delta;

    if (amountScroll < 0) {
      amountScroll = 0;
    }

    if (amountScroll > getMaxScroll()) {
      amountScroll = getMaxScroll();
    }
  }
}
