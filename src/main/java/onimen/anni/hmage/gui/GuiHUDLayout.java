package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.module.hud.InterfaceHUD;
import onimen.anni.hmage.module.hud.layout.Layout;

public class GuiHUDLayout extends GuiScreen {

  private ScaledResolution sr;

  private Map<String, InterfaceHUD> hudMap;
  private InterfaceHUD selectedHUD;

  private Set<Integer> xAxisGuides, yAxisGuides;
  private Layout snapBase;

  private int prevMouseX, prevMouseY;

  public GuiHUDLayout(Map<String, InterfaceHUD> hudList) {
    this.hudMap = hudList;
    this.xAxisGuides = new HashSet<Integer>();
    this.yAxisGuides = new HashSet<Integer>();

    this.snapBase = Layout.getLayout(0);
  }

  @Override
  public void initGui() {
    super.initGui();
    sr = new ScaledResolution(mc);

    initGuides();

    addButton(new GuiButton(0, this.width / 2 + 10, this.height - 38, 100, 20, I18n.format("hmage.reset")));
    addButton(new GuiButton(1, this.width / 2 - 110, this.height - 38, 100, 20, I18n.format("gui.done")));
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    switch (button.id) {
    case 0:
      resetAllPosition();
      break;
    case 1:
      Preferences.save();
      mc.displayGuiScreen((GuiScreen) null);
      mc.setIngameFocus();
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    for (InterfaceHUD hud : hudMap.values()) {
      hud.drawItem(mc, true);
    }
    if (this.selectedHUD == null) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      drawCenteredString(fontRenderer,
          I18n.format("hmage.gui.guihudlayout.desc1"), this.width / 2, this.height / 2 - 10, 0xFFFFFF);
      drawCenteredString(fontRenderer,
          I18n.format("hmage.gui.guihudlayout.desc2"), this.width / 2, this.height / 2, 0xFFFFFF);
      drawCenteredString(fontRenderer,
          I18n.format("hmage.gui.guihudlayout.desc3"), this.width / 2, this.height / 2 + 10, 0xFFFFFF);
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);

    prevMouseX = mouseX;
    prevMouseY = mouseY;

    if (sr == null)
      return;

    for (InterfaceHUD hud : hudMap.values()) {
      if (hud.isInside(sr, mouseX, mouseY)) {
        this.selectedHUD = hud;

        if (mouseButton == 2) {
          this.selectedHUD.setLayout(this.selectedHUD.getLayout().toggleDirection());
          return;
        }

        float x = (float) (mouseX - hud.getComputedX(sr)) / (float) hud.getWidth();
        float y = (float) (mouseY - hud.getComputedY(sr)) / (float) hud.getHeight();

        snapBase = Layout.getLayout(0);

        if (x > 0.5) {
          snapBase.right();
        }

        if (y > 0.5) {
          snapBase.bottom();
        }

        return;
      }
    }
    this.selectedHUD = null;
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    if (this.selectedHUD != null) {
      if (clickedMouseButton == 0) {

        int freeDragX = (mouseX - prevMouseX) * (selectedHUD.getLayout().isRight() ? -1 : 1);
        int freeDragY = (mouseY - prevMouseY) * (selectedHUD.getLayout().isBottom() ? -1 : 1);

        //free drag
        this.selectedHUD.setX(this.selectedHUD.getX() + freeDragX);
        this.selectedHUD.setY(this.selectedHUD.getY() + freeDragY);

      } else if (clickedMouseButton == 1) {
        //change base position
        double px = (double) mouseX / sr.getScaledWidth_double();
        double py = (double) mouseY / sr.getScaledHeight_double();

        Layout layout = this.selectedHUD.getLayout();

        if (px < 0.4)
          layout.left();
        else if (px > 0.6)
          layout.right();
        else
          layout.centerx();

        if (py < 0.4)
          layout.top();
        else if (py > 0.6)
          layout.bottom();
        else
          layout.centery();

        this.selectedHUD.setLayout(layout);
      }
    }

    prevMouseX = mouseX;
    prevMouseY = mouseY;
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
    if (this.selectedHUD != null) {
      initGuides();
      this.selectedHUD = null;
    }
  }

  private void initGuides() {
    xAxisGuides.clear();
    yAxisGuides.clear();
    for (InterfaceHUD hud : hudMap.values()) {
      xAxisGuides.add(hud.getComputedX(sr));
      xAxisGuides.add(hud.getComputedX(sr) + hud.getWidth() * (hud.getLayout().isRight() ? -1 : 1));
      yAxisGuides.add(hud.getComputedY(sr));
      yAxisGuides.add(hud.getComputedY(sr) + hud.getHeight() * (hud.getLayout().isBottom() ? -1 : 1));
    }
  }

  private void resetAllPosition() {
    for (InterfaceHUD hud : hudMap.values()) {
      hud.setLayout(hud.getDefaultLayout());
      hud.setX(hud.getDefaultX());
      hud.setY(hud.getDefaultY());
    }
  }
}
