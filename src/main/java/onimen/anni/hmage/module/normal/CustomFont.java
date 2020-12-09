package onimen.anni.hmage.module.normal;

import java.awt.Font;

import org.lwjgl.util.vector.Vector2f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import onimen.anni.hmage.event.GetCharWidthEvent;
import onimen.anni.hmage.event.LoadFontTextureEvent;
import onimen.anni.hmage.event.RenderFontEvent;
import onimen.anni.hmage.util.font.FontTextureData;

public class CustomFont extends AbstractModule {

  private static FontTextureData[] fontDatas = new FontTextureData[256];
  public static String fontName = "System";//"sushiki Regular";//"源真ゴシック Light";
  private int prevScaleFactor = -1;

  @Override
  public String getId() {
    return "hmage.module.custom-font";
  }

  @SubscribeEvent
  public void onRenderChar(RenderFontEvent event) {
    if (!this.canBehave()) {
      resetFontTexture();
      return;
    }

    int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    if (prevScaleFactor != scaleFactor) {
      prevScaleFactor = scaleFactor;
      resetFontTexture();
    }

    event.setCanceled(true);

    char ch = event.getChar();
    boolean italic = event.isItalic();
    float posX = event.getPosX();
    float posY = event.getPosY();

    int page = ch / 256;
    FontTextureData data = this.getFontTextureData(page, scaleFactor);

    if (!data.isInitialized()) {
      event.setWidth(4F);
      return;
    }

    float k = italic ? 1.0F : 0.0F;
    float w = data.getTextureWidth();
    float h = data.getTextureHeight();
    //float tx = (ch % 16) * 16F * data.getScale();
    //float ty = (ch % 256 / 16) * data.getCharHeight() * data.getScale();
    float charWidth = data.getCharWidth(ch);
    float texCharWidth = charWidth * data.getScale() / w;
    float texCharHeight = data.getCharHeight() * data.getScale() / h;
    Vector2f uv = data.getUVCoord(ch);

    GlStateManager.bindTexture(data.getGlTextureId());
    GlStateManager.enableAlpha();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.glBegin(5);
    //LEFT TOP
    GlStateManager.glTexCoord2f(uv.x, uv.y);
    GlStateManager.glVertex3f(posX + k, posY, 0.0F);
    //LEFT BOT
    GlStateManager.glTexCoord2f(uv.x, uv.y + texCharHeight);
    GlStateManager.glVertex3f(posX - k, posY + 8F, 0.0F);
    //RIGHT TOP
    GlStateManager.glTexCoord2f(uv.x + texCharWidth, uv.y);
    GlStateManager.glVertex3f(posX + charWidth / 2F + k, posY, 0.0F);
    //RIGHT BOT
    GlStateManager.glTexCoord2f(uv.x + texCharWidth, uv.y + texCharHeight);
    GlStateManager.glVertex3f(posX + charWidth / 2F - k, posY + 8F, 0.0F);
    GlStateManager.glEnd();
    event.setWidth((float) charWidth / 2F);
  }

  @SubscribeEvent
  public void onLoadFontTexture(LoadFontTextureEvent event) {
    if (canBehave()) {
      event.setResourceLocation(null);
    }
  }

  @SubscribeEvent
  public void onGetCharWidthEvent(GetCharWidthEvent event) {
    int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    if (canBehave()) {
      event.setCanceled(true);
      int page = event.getChar() / 256;
      FontTextureData data = this.getFontTextureData(page, scaleFactor);
      if (!data.isInitialized()) {
        event.setWidth(4);
      }
      event.setWidth((int) data.getCharWidth(event.getChar()) / 2);
    }
  }

  private FontTextureData getFontTextureData(int page, int scaleFactor) {
    FontTextureData data = fontDatas[page];
    if (data == null) {
      data = new FontTextureData(new Font(fontName, Font.PLAIN, 12 * scaleFactor), page, scaleFactor);
      fontDatas[page] = data;
    }
    return data;
  }

  public static void setFont(Font font) {
    fontName = font.getFamily();
  }

  public static void resetFontTexture() {
    if (fontDatas != null) {
      for (FontTextureData data : fontDatas) {
        if (data != null)
          data.destroy();
      }
      fontDatas = new FontTextureData[256];
    }
  }
}
