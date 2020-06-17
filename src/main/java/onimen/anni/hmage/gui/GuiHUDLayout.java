package onimen.anni.hmage.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.module.hud.InterfaceHUD;
import onimen.anni.hmage.module.hud.layout.Layout;

public class GuiHUDLayout extends GuiScreen {

  private ScaledResolution sr;

  private Map<String, InterfaceHUD> hudMap;
  private InterfaceHUD selectedHUD;

  private Set<Integer> xAxisGuides, yAxisGuides;
  private Map<Integer, Consumer<Integer>> xSnappingMap, ySnappingMap;

  private boolean isDragging;

  private int prevMouseX, prevMouseY;

  public GuiHUDLayout(Map<String, InterfaceHUD> hudList) {
    this.hudMap = hudList;
    this.xAxisGuides = new HashSet<Integer>();
    this.yAxisGuides = new HashSet<Integer>();
    xSnappingMap = new HashMap<>();
    ySnappingMap = new HashMap<>();
  }

  @Override
  public void initGui() {
    super.initGui();
    sr = new ScaledResolution(mc);

    initGuides();

    addButton(new GuiButton(0, this.width / 2 - 75, this.height - 62, 70, 20, I18n.format("hmage.reset")));
    addButton(new GuiButton(1, this.width / 2 + 5, this.height - 62, 70, 20, I18n.format("hmage.reset-all")));
    addButton(new GuiButton(2, this.width / 2 - 75, this.height - 38, 150, 20, I18n.format("gui.done")));
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    switch (button.id) {
    case 0:
      resetPosition(selectedHUD);
      break;
    case 1:
      resetAllPosition();
      break;
    case 2:
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

    GlStateManager.glLineWidth(1F);
    GlStateManager.disableTexture2D();

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();

    if (this.selectedHUD != null) {
      buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

      float x = this.selectedHUD.getComputedX(sr);
      float y = this.selectedHUD.getComputedY(sr);
      float w = this.selectedHUD.getWidth();
      float h = this.selectedHUD.getHeight();

      buffer.pos(x + w, y, 0).color(1f, 1f, 1f, 1f).endVertex();
      buffer.pos(x, y, 0).color(1f, 1f, 1f, 1f).endVertex();
      buffer.pos(x, y + h, 0).color(1f, 1f, 1f, 1f).endVertex();
      buffer.pos(x + w, y + h, 0).color(1f, 1f, 1f, 1f).endVertex();

      tessellator.draw();
    }

    buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

    Integer xguide = this.xAxisGuides.stream()
        .sorted((a, b) -> Math.abs(a - mouseX) - Math.abs(b - mouseX))
        .findFirst().orElse(null);

    if (xguide != null) {
      buffer.pos(xguide, 0, 0).color(1F, 1F, 1F, 1F).endVertex();
      buffer.pos(xguide, this.height, 0).color(1F, 1F, 1F, 1F).endVertex();
    }

    Integer yguide = this.yAxisGuides.stream()
        .sorted((a, b) -> Math.abs(a - mouseY) - Math.abs(b - mouseY))
        .findFirst().orElse(null);

    if (yguide != null) {

      buffer.pos(0, yguide, 0).color(1F, 1F, 1F, 1F).endVertex();
      buffer.pos(this.width, yguide, 0).color(1F, 1F, 1F, 1F).endVertex();
    }

    tessellator.draw();

    GlStateManager.enableTexture2D();

    if (!this.isDragging) {
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
        this.isDragging = true;

        if (mouseButton == 0) {
          this.xSnappingMap.clear();
          this.ySnappingMap.clear();

          int snapX = hud.getComputedX(sr);
          int snapY = hud.getComputedX(sr);
          int width = hud.getWidth();
          int height = hud.getHeight();

          Layout layout = hud.getLayout();
          int w = this.width;
          int h = this.height;
          this.xSnappingMap.put(snapX, i -> {
            hud.setX(i - (layout.isCenterX() ? w / 2 : layout.isRight() ? w : 0));
          });
          this.xSnappingMap.put(snapX + width / 2, i -> {
            hud.setX(i - width / 2);
          });
          this.xSnappingMap.put(snapX + width, i -> {
            hud.setX(i - width);
          });
          this.ySnappingMap.put(snapY, i -> {
            hud.setY(i);
          });
          this.ySnappingMap.put(snapY + height / 2, i -> {
            hud.setY(i - height / 2);
          });
          this.ySnappingMap.put(snapY + height, i -> {
            hud.setY(i - height);
          });
        }

        if (mouseButton == 2) {
          this.selectedHUD.setLayout(this.selectedHUD.getLayout().toggleDirection());
          return;
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

        int freeDragX = mouseX - prevMouseX;
        int freeDragY = mouseY - prevMouseY;

        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
          int hudX = selectedHUD.getComputedX(sr);
          int hudY = selectedHUD.getComputedY(sr);
          int hudWidth = selectedHUD.getWidth();
          int hudHeight = selectedHUD.getHeight();

          final Layout layout = selectedHUD.getLayout();

          Integer nearestGuideX = this.xSnappingMap.keySet().stream()
              .map(x -> { //それぞれのX座標から一番近いガイドの座標
                return this.xAxisGuides.stream()
                    .sorted((a, b) -> Math.abs(a - x) - Math.abs(b - x))
                    .findFirst()
                    .orElse(null);
              })
              .filter(i -> i != null)
              .sorted((a, b) -> Math.abs(a - mouseX) - Math.abs(b - mouseX))
              .findFirst().orElse(null);

          Consumer<Integer> onSnapped = xSnappingMap.get(nearestGuideX);

          if (onSnapped != null) {
            onSnapped.accept(nearestGuideX);
          }

          Integer snapY = this.yAxisGuides.stream()
              .filter(y -> Math.abs(y - mouseY) < 8)
              .sorted((a, b) -> Math.abs(a - hudY) - Math.abs(b - hudY))
              .findFirst().orElse(null);
          if (snapY != null) {
            this.selectedHUD.setY(snapY - (selectedHUD.getLayout().isCenterY() ? this.height / 2 : 0));
          }

        } else {
          //free drag
          this.selectedHUD.setX(this.selectedHUD.getX() + freeDragX);
          this.selectedHUD.setY(this.selectedHUD.getY() + freeDragY);
        }

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
    this.isDragging = false;
    if (this.selectedHUD != null) {
      initGuides();
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();

    if (selectedHUD != null) {
      float scale = selectedHUD.getScale();
      int delta = Mouse.getDWheel();
      if (delta != 0) {
        if (delta > 1) {
          scale *= 1.1;
        } else if (delta < -1) {
          scale *= 0.9;
        }
        selectedHUD.setScale(scale);
      }
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
    xAxisGuides.add(4);
    xAxisGuides.add(this.width - 4);
    xAxisGuides.add(this.width / 2);

    yAxisGuides.add(4);
    yAxisGuides.add(this.height - 4);
    yAxisGuides.add(this.height / 2);
  }

  private void resetPosition(InterfaceHUD hud) {
    if (hud != null) {
      hud.setLayout(hud.getDefaultLayout());
      hud.setX(hud.getDefaultX());
      hud.setY(hud.getDefaultY());
      hud.setScale(hud.getDefaultScale());
    }
  }

  private void resetAllPosition() {
    for (InterfaceHUD hud : hudMap.values()) {
      resetPosition(hud);
    }
  }
}
