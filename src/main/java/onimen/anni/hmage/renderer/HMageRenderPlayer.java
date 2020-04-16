package onimen.anni.hmage.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class HMageRenderPlayer extends RenderPlayer {

	public HMageRenderPlayer(RenderManager renderManager) {
		super(renderManager);
	}

	public HMageRenderPlayer(RenderManager renderManager, boolean useSmallArms) {
		super(renderManager, useSmallArms);
	}

	@Override
	public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.enableColorMaterial();
		float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
		float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
		float netHeadYaw = f1 - f;

		if (entity.isRiding() && entity.getRidingEntity() instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
			f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset,
					partialTicks);
			netHeadYaw = f1 - f;
			float f3 = MathHelper.wrapDegrees(netHeadYaw);

			if (f3 < -85.0F) {
				f3 = -85.0F;
			}

			if (f3 >= 85.0F) {
				f3 = 85.0F;
			}

			f = f1 - f3;

			if (f3 * f3 > 2500.0F) {
				f += f3 * 0.2F;
			}

			netHeadYaw = f1 - f;
		}

		float headPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
		this.renderLivingAt(entity, x, y, z);
		float ageInTicks = this.handleRotationFloat(entity, partialTicks);
		this.rotateCorpse(entity, ageInTicks, f, partialTicks);
		float scaleFactor = this.prepareScale(entity, partialTicks);
		float limbSwingAmount = 0.0F;
		float limbSwing = 0.0F;

		if (!entity.isRiding()) {
			limbSwingAmount = entity.prevLimbSwingAmount
					+ (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
			limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

			if (entity.isChild()) {
				limbSwing *= 3.0F;
			}

			if (limbSwingAmount > 1.0F) {
				limbSwingAmount = 1.0F;
			}
		}

		this.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

		GlStateManager.enableColorMaterial();
	}

	protected void rotateCorpse(AbstractClientPlayer entityLiving, float p_77043_2_, float p_77043_3_,
			float partialTicks) {
		GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

		if (entityLiving.deathTime > 0) {
			float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);

			if (f > 1.0F) {
				f = 1.0F;
			}

			GlStateManager.rotate(f * this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
		} else {
			String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());

			if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer)
					|| ((EntityPlayer) entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
				GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}

}
