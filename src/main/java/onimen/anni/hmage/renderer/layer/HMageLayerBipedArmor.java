package onimen.anni.hmage.renderer.layer;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import onimen.anni.hmage.Preferences;

public class HMageLayerBipedArmor extends LayerBipedArmor {

  private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);

  private final RenderLivingBase<?> renderer;
  private float alpha = 1.0F;
  private float colorR = 1.0F;
  private float colorG = 1.0F;
  private float colorB = 1.0F;
  private boolean skipRenderGlint;

  private FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);

  public HMageLayerBipedArmor(RenderLivingBase<?> rendererIn) {
    super(rendererIn);

    this.renderer = rendererIn;
  }

  @Override
  public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    if (!Preferences.hurtingArmor || !Preferences.enabled) {
      super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw,
          headPitch, scale);
      return;
    }
    this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw,
        headPitch, scale, EntityEquipmentSlot.CHEST);
    this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw,
        headPitch, scale, EntityEquipmentSlot.LEGS);
    this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw,
        headPitch, scale, EntityEquipmentSlot.FEET);
    this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw,
        headPitch, scale, EntityEquipmentSlot.HEAD);
  }

  private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount,
      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale,
      EntityEquipmentSlot slotIn) {
    ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);

    if (itemstack.getItem() instanceof ItemArmor) {
      ItemArmor itemarmor = (ItemArmor) itemstack.getItem();

      if (itemarmor.getEquipmentSlot() == slotIn) {
        ModelBiped t = this.getModelFromSlot(slotIn);
        t = getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t);
        t.setModelAttributes(this.renderer.getMainModel());
        t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
        this.setModelSlotVisible(t, slotIn);
        this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));

        boolean shouldBeHurt = shouldBeHurting(entityLivingBaseIn);

        {
          if (itemarmor.hasOverlay(itemstack)) // Allow this for anything, not only cloth
          {
            int i = itemarmor.getColor(itemstack);
            float f = (float) (i >> 16 & 255) / 255.0F;
            float f1 = (float) (i >> 8 & 255) / 255.0F;
            float f2 = (float) (i & 255) / 255.0F;
            if (shouldBeHurt) {
              setGLParamForHurt();
            }
            GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
            t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (shouldBeHurt) {
              unsetGLParam();
            }
            this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
          }
          { // Non-colored
            if (shouldBeHurt) {
              setGLParamForHurt();
            }
            GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
            t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (shouldBeHurt) {
              unsetGLParam();
            }
          } // Default
          if (!this.skipRenderGlint && itemstack.hasEffect()) {
            renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks,
                ageInTicks, netHeadYaw, headPitch, scale);
          }
        }
      }
    }
  }

  private boolean shouldBeHurting(EntityLivingBase entityLivingBaseIn) {
    Minecraft mc = Minecraft.getMinecraft();

    if (mc == null)
      return false;

    EntityPlayerSP player = mc.player;

    if (player == null)
      return false;


    EntityLivingBase attackingEntity = player.getLastAttackedEntity();

    if (!entityLivingBaseIn.isEntityEqual(attackingEntity))
      return false;

    int maxHurtTime = entityLivingBaseIn.maxHurtTime;

    if (maxHurtTime < player.ticksExisted - player.getLastAttackedEntityTime())
      return false;

    return (entityLivingBaseIn.hurtTime > 0 || entityLivingBaseIn.deathTime > 0);
  }

  private void setGLParamForHurt() {
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.enableTexture2D();
    GlStateManager.glTexEnvi(8960, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.enableTexture2D();
    GlStateManager.glTexEnvi(8960, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, GL11.GL_SRC_ALPHA);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);

    this.brightnessBuffer.position(0);
    this.brightnessBuffer.put(255F / 255F);
    this.brightnessBuffer.put(51F / 255F);
    this.brightnessBuffer.put(51F / 255F);
    this.brightnessBuffer.put(1F);
    this.brightnessBuffer.flip();

    GlStateManager.glTexEnv(8960, GL11.GL_TEXTURE_ENV_COLOR, this.brightnessBuffer);
    GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
    GlStateManager.enableTexture2D();
    GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
    GlStateManager.glTexEnvi(8960, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }

  private void unsetGLParam() {
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.enableTexture2D();
    GlStateManager.glTexEnvi(8960, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, GL11.GL_SRC_ALPHA);
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.glTexEnvi(8960, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
    GlStateManager.disableTexture2D();
    GlStateManager.bindTexture(0);
    GlStateManager.glTexEnvi(8960, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }

  static {

    int[] aint = TEXTURE_BRIGHTNESS.getTextureData();

    for (int i = 0; i < 256; ++i) {
      aint[i] = -1;
    }

    TEXTURE_BRIGHTNESS.updateDynamicTexture();
  }

}
