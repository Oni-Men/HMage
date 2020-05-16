package onimen.anni.hmage.cape;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.UUID;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import onimen.anni.hmage.HMage;

public class CapeResourceLoadTask extends TimerTask {

  private static final String IMAGE_BASE_URL = "https://raw.githubusercontent.com/HMage123456/hmgemod/master/cape/image/";

  private static final String UUIDLIST_URL = "https://raw.githubusercontent.com/HMage123456/hmgemod/master/cape/uuidlist";

  private static final String DEFAULT_CAPE_NAME = "original.png";

  @Override
  public void run() {
    try {
      Map<UUID, ResourceLocation> capeMap = new HashMap<>();

      //cape使用者とcape名を取得
      List<String> allLine = readAllLine(UUIDLIST_URL);
      if (allLine == null) { return; }
      for (String line : allLine) {
        if (line.startsWith("#")) {
          continue;
        }
        if (line.contains(":")) {
          String[] split = line.split(":");
          capeMap.put(UUID.fromString(split[0]), new ResourceLocation("hmage-cape", split[1]));
        } else {
          capeMap.put(UUID.fromString(line), new ResourceLocation("hmage-cape", DEFAULT_CAPE_NAME));
        }
      }

      //cape imageを取得
      Set<ResourceLocation> capeNameSet = new HashSet<>(capeMap.values());
      for (ResourceLocation resourceLocation : capeNameSet) {
        BufferedImage readImage = readImage(IMAGE_BASE_URL + resourceLocation.getResourcePath());
        if (readImage == null) {
          continue;
        }
        TextureManager renderEngine = Minecraft.getMinecraft().getRenderManager().renderEngine;
        renderEngine.loadTexture(resourceLocation, new DynamicTexture(readImage));
      }

      CapeManager.reload(capeMap);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 指定したURL上のテキストファイルを読み込む。
   *
   * @param urlStr URL
   * @return 読み込んだファイル
   */
  protected static List<String> readAllLine(String urlStr) {
    List<String> allLine = new ArrayList<>();
    try {
      URL url = new URL(urlStr);
      try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));) {

        String line;
        while ((line = br.readLine()) != null) {
          allLine.add(line);
        }
      }
    } catch (IOException e) {
      HMage.logger.warn("Failed to read the file on the network.:" + urlStr, e);
      return null;
    }
    return allLine;
  }

  /**
   * 指定したURL上の画像ファイルを読み込む。
   *
   * @param urlStr URL
   * @return 読み込んだファイル
   */
  protected static BufferedImage readImage(String urlStr) {
    try {
      URL url = new URL(urlStr);
      try (InputStream openStream = url.openStream();) {
        return ImageIO.read(openStream);
      }
    } catch (IOException e) {
      HMage.logger.warn("Image loading failed.:" + urlStr, e);
      return null;
    }
  }
}
