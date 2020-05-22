package onimen.anni.hmage.module;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpeedFovDisabler extends AbstractModule {

  @Override
  public String getName() {
    return "DisableSpeedFOV";
  }

  @Override
  public String getDescription() {
    return "スピード効果時の視野を通常時に戻す";
  }

  @SubscribeEvent
  public void onFOVUpdate(FOVUpdateEvent event) {
    if (isEnable()) {
      event.setNewfov(getFovModifier(event.getEntity()));
    }
  }

  /**
   * ステータス効果に影響されないように変更を加えた{@link AbstractClientPlayer#getFovModifier()}のコピー。
   *
   * @param player
   * @return
   */
  public float getFovModifier(EntityPlayer player) {
    float f = 1.0F;

    if (player.capabilities.isFlying) {
      f *= 1.1F;
    }

    IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
    double baseSpeed = iattributeinstance.getAttributeValue();

    PotionEffect activepotioneffect = player.getActivePotionEffect(Potion.getPotionFromResourceLocation("speed"));

    if (activepotioneffect != null) {
      baseSpeed = iattributeinstance.getAttributeValue() * (5.0D / (6.0D + activepotioneffect.getAmplifier()));
    }

    f = (float) ((double) f * ((baseSpeed / (double) player.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

    if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
      f = 1.0F;
    }

    if (player.isHandActive() && player.getActiveItemStack().getItem() == Items.BOW) {
      int i = player.getItemInUseMaxCount();
      float f1 = (float) i / 20.0F;

      if (f1 > 1.0F) {
        f1 = 1.0F;
      } else {
        f1 = f1 * f1;
      }

      f *= 1.0F - f1 * 0.15F;
    }

    return f;
  }
}
