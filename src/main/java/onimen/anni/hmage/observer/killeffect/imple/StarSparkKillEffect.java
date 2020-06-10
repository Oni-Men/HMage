package onimen.anni.hmage.observer.killeffect.imple;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import onimen.anni.hmage.observer.killeffect.AnniKillEffect;
import onimen.anni.hmage.observer.particle.AnniParticleData;

public class StarSparkKillEffect implements AnniKillEffect {

  private static AnniParticleData particleData = new AnniParticleData(EnumParticleTypes.FIREWORKS_SPARK, 1, 2, 1, 0,
      250, true, new int[] {});

  @Override
  public String getId() {
    return "starSpark";
  }

  @Override
  public String getName() {
    return "Star Spark";
  }

  @Override
  public void execute(World w, float x, float y, float z) {
    particleData.playParticle(w, x, y, z);
  }

}
