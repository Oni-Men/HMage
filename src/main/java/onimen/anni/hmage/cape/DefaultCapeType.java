package onimen.anni.hmage.cape;

import net.minecraft.util.ResourceLocation;

public enum DefaultCapeType {
  OLD_DESIGN("Old Mojang Cape", "mojang_cape2.png"),
  UPDATED_DESIGN("New Mojang Cape", "mojang_cape.png"),
  MINECON_2011("Minecon 2011", "minecon_2011.png"),
  MINECON_2012("Minecon 2012", "minecon_2012.png"),
  MINECON_2013("Minecon 2013", "minecon_2013.png"),
  MINECON_2014("Minecon 2014", "minecon_2014.png"),
  MINECON_2015("Minecon 2015", "minecon_2015.png"),
  MINECON_2016("Minecon 2016", "minecon_2016.png"),
  MINECON_2019("Minecon 2019", "minecon_2019.png"),
  ;

  private String fileName;

  private String title;

  private DefaultCapeType(String title, String fileName) {
    this.title = title;
    this.fileName = fileName;
  }

  public String getTitle() {
    return title;
  }

  public ResourceLocation getResourceLocation() {
    return new ResourceLocation("hmage", "cape/" + fileName);
  }
}
