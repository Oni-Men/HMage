package onimen.anni.hmage.observer.killeffect.imple;

import net.minecraft.world.World;
import onimen.anni.hmage.observer.killeffect.AnniKillEffect;

public class DefaultKillEffect implements AnniKillEffect {

  public final static DefaultKillEffect INSTANCE = new DefaultKillEffect();

  private DefaultKillEffect() {
  }

  @Override
  public String getId() {
    return "Deafult";
  }

  @Override
  public String getName() {
    return "Deafult";
  }

  @Override
  public void execute(World w, float x, float y, float z) {
  }

}
