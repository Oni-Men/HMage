package onimen.anni.hmage.util.font;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;

public class FontTextureData {

  private boolean isInitialized = false;
  private int glTextureId = -1;
  private int[] glyphWidth;
  private int width;
  private int height;
  private int scale;
  private Vector2f[] uvCoords;

  public FontTextureData(List<Font> fonts, int page, int scale) {
    this.scale = scale;
    this.glyphWidth = new int[256];
    this.uvCoords = new Vector2f[256];
    this.glTextureId = GlStateManager.generateTexture();
    FontGenerateWorker.addGenerateTask(() -> {
      BufferedImage generated = this.generate(fonts, page, scale);
      return new FontGenerateData(this, generated, glTextureId);
    });
  }

  public void complete() {
    this.isInitialized = true;
  }

  public BufferedImage generate(List<Font> fonts, int page, int scale) {
    width = 256 * scale;
    height = 256 * scale;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) image.getGraphics();

    int offset = page * 256;
    for (int y = 0; y < 16; y++) {
      for (int x = 0; x < 16; x++) {
        char codePoint = (char) (offset + y * 16 + x);
        float u = (float) (x * 16 * scale) / (float) width;
        float v = (float) (y * 16 * scale) / (float) height;

        g.drawImage(generateChar(fonts, codePoint, scale), (int) (u * width), (int) (v * height), null);
        this.uvCoords[codePoint % 256] = new Vector2f(u, v);
      }
    }
    return image;
  }

  public void destroy() {
    TextureUtil.deleteTexture(this.glTextureId);
  }

  public boolean isInitialized() {
    return this.isInitialized;
  }

  public BufferedImage generateChar(List<Font> fonts, char ch, int scale) {
    BufferedImage image = new BufferedImage(16 * scale, 16 * scale, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) image.getGraphics();
    g.setFont(new Font("System", Font.PLAIN, 12 * scale));
    g.setFont(findFontCanDisplayUp(g, fonts, ch));
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    FontMetrics fm = g.getFontMetrics();

    float x = 0F;
    float y = fm.getHeight() - fm.getLeading() - fm.getDescent();

    g.drawString(String.valueOf(ch), x, y);
    g.drawString(String.valueOf(ch), x, y);

    this.glyphWidth[ch % 256] = fm.charWidth(ch) / scale + 1;

    return image;
  }

  public Font findFontCanDisplayUp(Graphics2D g, List<Font> fonts, char ch) {
    Iterator<Font> iterator = fonts.iterator();
    while (iterator.hasNext()) {
      Font next = iterator.next();
      if (next.canDisplay(ch))
        return next;
    }
    return fonts.isEmpty() ? null : fonts.get(0);
  }

  public int getGlTextureId() {
    return this.glTextureId;
  }

  public int getScale() {
    return this.scale;
  }

  public float getCharWidth(char ch) {
    return (float) this.glyphWidth[ch % 256];
  }

  public Vector2f getUVCoord(char ch) {
    return this.uvCoords[ch % 256];
  }

  public float getCharHeight() {
    return (float) this.height / (16 * scale);
  }

  public float getTextureWidth() {
    return (float) this.width;
  }

  public float getTextureHeight() {
    return (float) this.height;
  }
}
