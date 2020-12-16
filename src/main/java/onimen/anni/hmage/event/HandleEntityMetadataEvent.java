package onimen.anni.hmage.event;

import javax.annotation.Nonnull;

import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraftforge.fml.common.eventhandler.Event;

public class HandleEntityMetadataEvent extends Event {

  private final SPacketEntityMetadata metadata;

  public HandleEntityMetadataEvent(SPacketEntityMetadata packetEntityMetadata) {
    if (packetEntityMetadata == null)
      throw new NullPointerException();
    this.metadata = packetEntityMetadata;
  }

  @Override
  public boolean isCancelable() {
    return false;
  }

  @Nonnull
  public SPacketEntityMetadata getMetadata() {
    return this.metadata;
  }
}
