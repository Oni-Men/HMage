package onimen.anni.hmage.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class DrawWorldBackgroundEvent extends Event {

  public DrawWorldBackgroundEvent() {
  }

  @Override
  public boolean isCancelable() {
    return true;
  }
}
