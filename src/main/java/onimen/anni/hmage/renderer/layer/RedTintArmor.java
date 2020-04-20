package onimen.anni.hmage.renderer.layer;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import onimen.anni.hmage.Preferences;

public class RedTintArmor implements LayerRenderer<EntityLivingBase> {

	protected ModelBiped modelLeggings;
	protected ModelBiped modelArmor;
	private final RenderLivingBase<?> renderer;

	private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps
			.<String, ResourceLocation> newHashMap();

	public RedTintArmor(RenderLivingBase<?> rendererIn) {
		this.renderer = rendererIn;
		this.initArmor();
		System.out.println("red tint armor");
	}

	protected void initArmor() {
		this.modelLeggings = new ModelBiped(0.5F);
		this.modelArmor = new ModelBiped(1.0F);
	}

	protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn) {
		model.setVisible(false);
		switch (slotIn) {
		case HEAD:
			model.bipedHead.showModel = true;
			model.bipedHeadwear.showModel = true;
			break;
		case CHEST:
			model.bipedBody.showModel = true;
			model.bipedRightArm.showModel = true;
			model.bipedLeftArm.showModel = true;
			break;
		case LEGS:
			model.bipedBody.showModel = true;
			model.bipedRightLeg.showModel = true;
			model.bipedLeftLeg.showModel = true;
			break;
		case FEET:
			model.bipedRightLeg.showModel = true;
			model.bipedLeftLeg.showModel = true;
		default:
		}
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		if (Preferences.enabled && Preferences.redTintArmor && entitylivingbaseIn.hurtTime <= 0)
			return;

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

		ItemStack stack = entityLivingBaseIn.getItemStackFromSlot(slotIn);

		if (stack.getItem() instanceof ItemArmor) {

			ItemArmor itemArmor = (ItemArmor) stack.getItem();

			if (itemArmor.getEquipmentSlot() == slotIn) {

				ModelBiped model = this.getModelFromSlot(slotIn);
				model.setModelAttributes(this.renderer.getMainModel());
				model.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
				this.setModelSlotVisible(model, slotIn);

				boolean isLegSlot = this.isLegSlot(slotIn);

				this.renderer.bindTexture(this.getArmorResource(itemArmor, isLegSlot));

				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.depthFunc(514);
				GlStateManager.depthMask(false);
				GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_COLOR, DestFactor.ZERO,
						SourceFactor.ZERO, DestFactor.ONE);
				GlStateManager.color(1F, 0.4F, 0.4F, 1F);
				model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

				//restore
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.enableLighting();
				GlStateManager.depthMask(true);
				GlStateManager.depthFunc(515);
			}
		}
	}

	public ModelBiped getModelFromSlot(EntityEquipmentSlot slotIn) {
		return this.isLegSlot(slotIn) ? this.modelLeggings : this.modelArmor;
	}

	public boolean isLegSlot(EntityEquipmentSlot slotIn) {
		return slotIn == EntityEquipmentSlot.LEGS;
	}

	private ResourceLocation getArmorResource(ItemArmor armor, boolean isLegSlot) {
		return this.getArmorResource(armor, isLegSlot, null);
	}

	private ResourceLocation getArmorResource(ItemArmor armor, boolean isLegSlot, String suffix) {
		String s = String.format("textures/models/armor/%s_layer_%d%s.png",
				armor.getArmorMaterial().getName(), isLegSlot ? 2 : 1,
				suffix == null ? "" : String.format("_%s", suffix));

		ResourceLocation resourceLocation = ARMOR_TEXTURE_RES_MAP.get(s);

		if (resourceLocation == null) {
			resourceLocation = new ResourceLocation(s);
			ARMOR_TEXTURE_RES_MAP.put(s, resourceLocation);
		}

		return resourceLocation;
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
