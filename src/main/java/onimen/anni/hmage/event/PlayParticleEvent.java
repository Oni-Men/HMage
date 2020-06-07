package onimen.anni.hmage.event;

import net.minecraft.network.play.server.SPacketParticles;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayParticleEvent extends Event {

  private SPacketParticles particle;

  public PlayParticleEvent(SPacketParticles particle) {
    this.particle = particle;
  }

  @Override
  public boolean isCancelable() {
    return true;
  }

  public SPacketParticles getParticle() {
    return particle;
  }
}
