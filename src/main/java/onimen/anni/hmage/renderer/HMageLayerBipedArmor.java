package onimen.anni.hmage.renderer;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class HMageLayerBipedArmor extends LayerArmorBase<ModelBiped> {

  private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);

  private final RenderLivingBase<?> renderer;
  private float colorR = 1F;
  private float colorG = 1F;
  private float colorB = 1F;
  private float alpha = 1F;
  private boolean skipRenderGlint;

  private FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);

  public HMageLayerBipedArmor(RenderLivingBase<?> rendererIn) {
    super(rendererIn);

    this.renderer = rendererIn;
  }

  @Override
  protected void initArmor() {
    this.modelLeggings = new ModelBiped(0.5F);
    this.modelArmor = new ModelBiped(1.0F);
  }

  @Override
  public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
    this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
    this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
    this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
  }

  private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn) {
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

        boolean isHurt = entityLivingBaseIn.hurtTime > 0 || entityLivingBaseIn.deathTime > 0;

        {
          if (itemarmor.hasOverlay(itemstack)) // Allow this for anything, not only cloth
          {
            int i = itemarmor.getColor(itemstack);
            float f = (float) (i >> 16 & 255) / 255.0F;
            float f1 = (float) (i >> 8 & 255) / 255.0F;
            float f2 = (float) (i & 255) / 255.0F;
            if (isHurt) {
              setGLParamForHurt();
            }
            GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
            t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            unsetGLParam();
            this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
          }
          { // Non-colored
            if (isHurt) {
              setGLParamForHurt();
            }
            GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
            t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            unsetGLParam();
          } // Default
          if (!this.skipRenderGlint && itemstack.hasEffect()) {
            renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
          }
        }
      }
    }
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

  @SuppressWarnings("incomplete-switch")
  @Override
  protected void setModelSlotVisible(ModelBiped modelBiped, EntityEquipmentSlot slotIn) {
    this.setModelVisible(modelBiped);

    switch (slotIn) {
    case HEAD:
      modelBiped.bipedHead.showModel = true;
      modelBiped.bipedHeadwear.showModel = true;
      break;
    case CHEST:
      modelBiped.bipedBody.showModel = true;
      modelBiped.bipedRightArm.showModel = true;
      modelBiped.bipedLeftArm.showModel = true;
      break;
    case LEGS:
      modelBiped.bipedBody.showModel = true;
      modelBiped.bipedRightLeg.showModel = true;
      modelBiped.bipedLeftLeg.showModel = true;
      break;
    case FEET:
      modelBiped.bipedRightLeg.showModel = true;
      modelBiped.bipedLeftLeg.showModel = true;
    }
  }

  protected void setModelVisible(ModelBiped model) {
    model.setVisible(false);
  }

  @Override
  protected ModelBiped getArmorModelHook(net.minecraft.entity.EntityLivingBase entity, net.minecraft.item.ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model) {
    return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
  }

}
