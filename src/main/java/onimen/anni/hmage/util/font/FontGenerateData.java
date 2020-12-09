package onimen.anni.hmage.util.font;

import java.awt.image.BufferedImage;

public class FontGenerateData {
  public FontTextureData data;
  public BufferedImage image;
  public int glTextureId;

  public FontGenerateData(FontTextureData data, BufferedImage image, int glTextureId) {
    this.data = data;
    this.image = image;
    this.glTextureId = glTextureId;
  }
}