package onimen.anni.hmage.event;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LoadFontTextureEvent extends Event {

  private int page;

  private ResourceLocation resourceLocation;

  public LoadFontTextureEvent(int page, ResourceLocation resourceLocation) {
    this.page = page;
    this.resourceLocation = resourceLocation;
  }

  public int getPage() {
    return this.page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public ResourceLocation getResourceLocation() {
    return this.resourceLocation;
  }

  public void setResourceLocation(ResourceLocation resourceLocation) {
    this.resourceLocation = resourceLocation;
  }

  @Override
  public boolean isCancelable() {
    return false;
  }

}
