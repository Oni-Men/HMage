package onimen.anni.hmage.gui;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import onimen.anni.hmage.util.JavaUtil;

public class GuiColorPicker extends GuiScreen {

  private static final Pattern colorCodeMatcher = Pattern.compile("[a-fA-F0-9]+");

  @Nullable
  private GuiScreen parent;

  @Nullable
  private Consumer<Integer> callback;

  /*
   * 0.0D - 1.0D
   */
  private double hue, sat, val, alpha = 1D;

  private double centerX, centerY;
  private double hueCircleRadius = 64;
  private double lineWidth = 16;

  private double boxSize = hueCircleRadius - 2 * lineWidth;

  private FloatBuffer hueBuffer = null;

  private FloatBuffer[] vertexs;
  private FloatBuffer[] colors;

  private GuiTextField colorCodeField;

  public GuiColorPicker(GuiScreen parent, Consumer<Integer> callback) {
    this(parent, callback, 0xFFFF0000);
  }

  public GuiColorPicker(GuiScreen parent, Consumer<Integer> callback, int initialColor) {

    this.setIntColor(initialColor);

    this.parent = parent;
    this.callback = callback;
    vertexs = new FloatBuffer[362];
    colors = new FloatBuffer[362];
    hueBuffer = getColorHue(hue);
    for (int i = 0; i < 362; i++) {
      double rad = Math.toRadians(i);
      vertexs[i] = GLAllocation.createDirectFloatBuffer(2);
      if (i % 2 == 0) {
        vertexs[i].put((float) (hueCircleRadius * Math.cos(rad)));
        vertexs[i].put((float) (hueCircleRadius * Math.sin(rad)));
      } else {
        vertexs[i].put((float) ((hueCircleRadius - lineWidth) * Math.cos(rad)));
        vertexs[i].put((float) ((hueCircleRadius - lineWidth) * Math.sin(rad)));
      }

      colors[i] = getColorHue(rad / 2 / Math.PI);
    }
  }

  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
    this.colorCodeField = new GuiTextField(1, fontRenderer, this.width / 2 - 40, 48, 80,
        this.fontRenderer.FONT_HEIGHT);
    this.colorCodeField.setMaxStringLength(10);
    this.colorCodeField.setEnableBackgroundDrawing(true);
    this.colorCodeField.setTextColor(0xFFFFFF);
    this.colorCodeField.setVisible(true);
    this.colorCodeField.setText("#" + Integer.toHexString(this.getIntColor()));

    this.addButton(new GuiButton(0, this.width / 2 - 100, this.height - 38, I18n.format("gui.done")));
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 0) {
      this.mc.displayGuiScreen(this.parent);
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    GlStateManager.disableLighting();
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);

    int textColor = getIntColor();

    this.drawCenteredString(fontRenderer, "Color Picker", this.width / 2, 24, textColor);
    //    this.drawCenteredString(fontRenderer,
    //        String.format("Hue %.0f, Saturation %.0f%%, Value %.0f%%, Alpha %.0f%%", hue * 360, sat * 100, val * 100,
    //            alpha * 100),
    //        this.width / 2,
    //        48, textColor);

    this.colorCodeField.drawTextBox();

    ScaledResolution sr = new ScaledResolution(mc);

    centerX = sr.getScaledWidth() / 2;
    centerY = sr.getScaledHeight() / 2;
    drawColorPicker();
  }

  private void drawColorPicker() {
    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.shadeModel(7425);

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder builder = tessellator.getBuffer();

    builder.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

    for (int i = 0, len = vertexs.length; i < len; i++) {
      FloatBuffer pos = vertexs[i];
      FloatBuffer color = colors[i];
      pos.flip();
      color.flip();
      builder
          .pos(centerX + pos.get(), centerY + pos.get(), this.zLevel)
          .color(color.get(), color.get(), color.get(), 1F)
          .endVertex();
    }

    tessellator.draw();

    builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

    hueBuffer.flip();
    builder.pos(centerX + boxSize, centerY - boxSize, this.zLevel)
        .color(hueBuffer.get(), hueBuffer.get(), hueBuffer.get(), 1F)
        .endVertex();
    builder.pos(centerX - boxSize, centerY - boxSize, this.zLevel).color(1F, 1F, 1F, 1F).endVertex();
    builder.pos(centerX - boxSize, centerY + boxSize, this.zLevel).color(0F, 0F, 0F, 1F).endVertex();
    builder.pos(centerX + boxSize, centerY + boxSize, this.zLevel).color(0F, 0F, 0F, 1F).endVertex();

    tessellator.draw();

    double alphaSliderLeft = centerX + hueCircleRadius + 10;
    double alphaSliderRight = alphaSliderLeft + lineWidth;
    double alphaSliderTop = centerY - hueCircleRadius;
    double alhpaSliderBottom = centerY + hueCircleRadius;
    builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    builder.pos(alphaSliderRight, alphaSliderTop, this.zLevel).color(0F, 0F, 0F, 1F).endVertex();
    builder.pos(alphaSliderLeft, alphaSliderTop, this.zLevel).color(0F, 0F, 0F, 1F).endVertex();
    builder.pos(alphaSliderLeft, alhpaSliderBottom, this.zLevel).color(1F, 1F, 1F, 1F).endVertex();
    builder.pos(alphaSliderRight, alhpaSliderBottom, this.zLevel).color(1F, 1F, 1F, 1F).endVertex();

    tessellator.draw();

    double px = centerX + 2 * sat * boxSize - boxSize;
    double py = centerY - 2 * val * boxSize + boxSize;
    GlStateManager.glLineWidth(2F);
    builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    builder.pos(px - 2, py, this.zLevel).color(1F, 1F, 1F, 1F).endVertex();
    builder.pos(px + 2, py, this.zLevel).color(1F, 1F, 1F, 1F).endVertex();
    builder.pos(px, py - 2, this.zLevel).color(1F, 1F, 1F, 1F).endVertex();
    builder.pos(px, py + 2, this.zLevel).color(1F, 1F, 1F, 1F).endVertex();
    tessellator.draw();

    GlStateManager.glLineWidth(2F);
    GlStateManager.pushMatrix();
    GlStateManager.translate(centerX, centerY, 0);
    GlStateManager.rotate((float) (hue * 360), 0f, 0f, 1f);
    GlStateManager.translate(hueCircleRadius, 0, 0);

    builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

    builder.pos(lineWidth * 0.25, 0, 0).color(1F, 1F, 1F, 1F).endVertex();
    builder.pos(-lineWidth * 0.75, 0, 0).color(1F, 1F, 1F, 1F).endVertex();

    tessellator.draw();
    GlStateManager.popMatrix();
    GlStateManager.glLineWidth(2F);
    builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

    double alphaY = centerY + (alpha - 0.5) * hueCircleRadius * 2;
    builder.pos(alphaSliderLeft, alphaY, 0).color(1F, 1F, 1F, 1F).endVertex();
    builder.pos(alphaSliderRight, alphaY, 0).color(1F, 1F, 1F, 1F).endVertex();

    tessellator.draw();

    GlStateManager.shadeModel(7424);
    GlStateManager.glLineWidth(1);
    GlStateManager.enableTexture2D();
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.colorCodeField.textboxKeyTyped(typedChar, keyCode)) {
      String text = this.colorCodeField.getText().replace("#", "");

      Matcher matcher = colorCodeMatcher.matcher(text);

      if (matcher.matches()) {
        JavaUtil.tryExecuteConsumer(text, hex -> {
          if (hex.length() > 6) {
            int alpha = 0, rgb = 0;
            alpha = Integer.parseInt(hex.substring(0, 2), 16);
            rgb = Integer.parseInt(hex.substring(2, hex.length()), 16);
            GuiColorPicker.this.setIntColor((alpha << 24) | rgb);
          } else {
            GuiColorPicker.this.setIntColor(Integer.parseInt(hex, 16));
          }
        });
        this.callback();
      }

    } else {
      super.keyTyped(typedChar, keyCode);
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    this.colorCodeField.mouseClicked(mouseX, mouseY, mouseButton);
    calculateHSV(mouseX, mouseY);
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    calculateHSV(mouseX, mouseY);
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
    this.callback();
  }

  private void calculateHSV(int mouseX, int mouseY) {
    double distance = Math.hypot(mouseX - centerX, mouseY - centerY);

    double alphaLeft = centerX + hueCircleRadius + 10;
    double alphaRight = alphaLeft + lineWidth;
    double alphaTop = centerY - hueCircleRadius;
    double alphaBottom = centerY + hueCircleRadius;

    if (distance < hueCircleRadius && distance > hueCircleRadius - lineWidth) {
      double atan2 = Math.atan2(mouseY - centerY, mouseX - centerX) / 2 / Math.PI;
      if (atan2 < 0) {
        hue = 1 + atan2;
      } else {
        hue = atan2;
      }
      this.hueBuffer = getColorHue(hue);
      this.colorCodeField.setText("#" + Integer.toHexString(this.getIntColor()));
    } else if (mouseX >= centerX - boxSize && mouseX <= centerX + boxSize
        && mouseY >= centerY - boxSize && mouseY <= centerY + boxSize) {
      sat = 0.5 + (mouseX - centerX) / boxSize / 2;
      val = 0.5 - (mouseY - centerY) / boxSize / 2;
      this.colorCodeField.setText("#" + Integer.toHexString(this.getIntColor()));
    } else if (mouseX >= alphaLeft && mouseX <= alphaRight &&
        mouseY >= alphaTop && mouseY <= alphaBottom) {
      alpha = (mouseY - alphaTop) / (alphaBottom - alphaTop);
      this.colorCodeField.setText("#" + Integer.toHexString(this.getIntColor()));
    }

  }

  private FloatBuffer getColorHue(double hue) {
    FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(3);
    Color color = new Color();
    color.fromHSB((float) hue, 1F, 1F);

    buffer.put((float) color.getRed() / 255F);
    buffer.put((float) color.getGreen() / 255F);
    buffer.put((float) color.getBlue() / 255F);

    return buffer;
  }

  private int getIntColor() {
    Color color = new Color();
    color.fromHSB((float) hue, (float) sat, (float) val);
    color.setAlpha((int) (255 * alpha));
    return (color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | (color.getBlue());
  }

  private void setIntColor(int color) {
    Color c = new Color(color >> 16, color >> 8, color, color >> 24);
    float[] hsb = c.toHSB(null);

    hue = hsb[0];
    sat = hsb[1];
    val = hsb[2];
    alpha = c.getAlpha() / 255F;
  }

  private void callback() {
    if (this.callback != null) {
      callback.accept(getIntColor());
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }
}
