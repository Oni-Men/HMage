package onimen.anni.hmage.cape;

import net.minecraft.util.ResourceLocation;

public class SPPlayerUseCape {

  private static DefaultCapeType capeType = DefaultCapeType.DEFAULT_CAPE;

  public static void setResourceLocation(DefaultCapeType capeType) {
    SPPlayerUseCape.capeType = capeType;
  }

  public static ResourceLocation getResourceLocation() {
    return capeType.getResourceLocation();
  }

  public static DefaultCapeType getCapeType() {
    return capeType;
  }
}
