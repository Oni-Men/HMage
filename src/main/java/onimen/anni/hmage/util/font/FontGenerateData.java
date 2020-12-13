package onimen.anni.hmage.util.font;

import java.awt.image.BufferedImage;

public class FontGenerateData {
  public FontTextureData data;
  public BufferedImage image;

  public FontGenerateData(FontTextureData data, BufferedImage image) {
    this.data = data;
    this.image = image;
  }
}