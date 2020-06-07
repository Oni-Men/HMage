package onimen.anni.hmage.observer.particle;

import java.util.Arrays;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class AnniParticleData {

  private EnumParticleTypes particleType;
  private float xOffset;
  private float yOffset;
  private float zOffset;
  private float particleSpeed;
  private int particleCount;
  private boolean longDistance;
  private int[] particleArguments;

  public AnniParticleData(EnumParticleTypes particleType, float xOffset, float yOffset, float zOffset,
      float particleSpeed, int particleCount, boolean longDistance, int[] particleArguments) {
    this.particleType = particleType;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.zOffset = zOffset;
    this.particleSpeed = particleSpeed;
    this.particleCount = particleCount;
    this.longDistance = longDistance;
    this.particleArguments = particleArguments;
  }

  public AnniParticleData(EnumParticleTypes particleType, double xOffset, double yOffset, double zOffset,
      double particleSpeed, int particleCount, boolean longDistance, int[] particleArguments) {
    this.particleType = particleType;
    this.xOffset = (float) xOffset;
    this.yOffset = (float) yOffset;
    this.zOffset = (float) zOffset;
    this.particleSpeed = (float) particleSpeed;
    this.particleCount = particleCount;
    this.longDistance = longDistance;
    this.particleArguments = particleArguments;
  }

  /**
   * 指定したParticleと同じパーティクルかどうかを調べる。
   *
   * @param particle パーティクル
   * @return 同じ場合はtrue
   */
  public boolean sameParticle(SPacketParticles particle) {
    if (particle.getParticleType() != particleType) { return false; }
    if (particle.getXOffset() != xOffset) { return false; }
    if (particle.getYOffset() != yOffset) { return false; }
    if (particle.getZOffset() != zOffset) { return false; }
    if (particle.getParticleSpeed() != particleSpeed) { return false; }
    if (particle.getParticleCount() != particleCount) { return false; }
    if (particle.isLongDistance() != longDistance) { return false; }

    if (particle.getParticleArgs() == null || particle.getParticleArgs().length == 0) {
      if (particleArguments != null && particleArguments.length != 0) { return false; }
    } else {
      if (particleArguments == null || particleArguments.length == 0) { return false; }
      if (!Arrays.equals(particle.getParticleArgs(), particleArguments)) { return false; }
    }
    return true;
  }

  /**
   * 指定した場所でParticleを実行する。
   *
   * @param w ワールド
   * @param x x座標
   * @param y y座標
   * @param z z座標
   */
  public void playParticle(World w, float x, float y, float z) {

    NetHandlerPlayClient clientPlayHandler = (NetHandlerPlayClient) FMLClientHandler.instance()
        .getClientPlayHandler();
    if (clientPlayHandler != null) {
      clientPlayHandler
          .handleParticles(new SPacketParticles(particleType, longDistance, x, y, z, xOffset, yOffset, zOffset,
              particleSpeed, particleCount, particleArguments));
    }
  }
}
