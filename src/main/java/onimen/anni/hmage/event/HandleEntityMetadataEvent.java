package onimen.anni.hmage.event;

import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraftforge.fml.common.eventhandler.Event;

public class HandleEntityMetadataEvent extends Event {

  private final SPacketEntityMetadata metadata;

  public HandleEntityMetadataEvent(SPacketEntityMetadata packetEntityMetadata) {
    this.metadata = packetEntityMetadata;
  }

  @Override
  public boolean isCancelable() {
    return false;
  }

  public SPacketEntityMetadata getMetadata() {
    return this.metadata;
  }
}
