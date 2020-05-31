package onimen.anni.hmage;

import net.minecraftforge.common.MinecraftForge;
import onimen.anni.hmage.event.DrawWorldBackgroundEvent;

public class HMageHooks {

  public static boolean onDrawWorldBackground() {
    return MinecraftForge.EVENT_BUS.post(new DrawWorldBackgroundEvent());
  }

}
