package onimen.anni.hmage.event;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GetLocationCapeEvent extends Event {

  private AbstractClientPlayer player;
  private ResourceLocation capeLocation;

  public GetLocationCapeEvent(AbstractClientPlayer player) {
    this.player = player;
    this.capeLocation = null;
  }

  public AbstractClientPlayer getPlayer() {
    return this.player;
  }

  public ResourceLocation getCapeLocation() {
    return this.capeLocation;
  }

  public void setCapeLocation(ResourceLocation capeLocation) {
    this.capeLocation = capeLocation;
  }

}
