package onimen.anni.hmage.module;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import onimen.anni.hmage.event.DrawWorldBackgroundEvent;
import onimen.anni.hmage.module.annotation.ColorOption;

public class CustomGuiBackground extends AbstractModule {

  @ColorOption(id = "color", name = "GUI Background Color")
  private int color = 0x4d000000;

  @Override
  public String getId() {
    return "hmage.module.custom-gui-bg";
  }

  public CustomGuiBackground() {
    super();
    super.loadPreferences(this);
  }

  @SubscribeEvent
  public void onDrawWorldBackground(DrawWorldBackgroundEvent event) {
    Minecraft mc = Minecraft.getMinecraft();

    if (mc == null)
      return;

    if (!canBehaivor() || mc.world == null) { return; }
    event.setCanceled(true);

    ScaledResolution sr = new ScaledResolution(mc);
    double w = sr.getScaledWidth_double();
    double h = sr.getScaledHeight_double();

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder builder = tessellator.getBuffer();

    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.shadeModel(7425);
    GlStateManager.disableTexture2D();
    GlStateManager.color(1F, 1F, 1F, 1F);

    float alpha = (float) (color >> 24 & 255) / 255F;
    float red = (float) (color >> 16 & 255) / 255F;
    float green = (float) (color >> 8 & 255) / 255F;
    float blue = (float) (color & 255) / 255F;

    builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

    builder.pos(w, 0, 0).color(red, green, blue, alpha).endVertex();
    builder.pos(0, 0, 0).color(red, green, blue, alpha).endVertex();
    builder.pos(0, h, 0).color(red, green, blue, alpha).endVertex();
    builder.pos(w, h, 0).color(red, green, blue, alpha).endVertex();

    tessellator.draw();

    GlStateManager.shadeModel(7424);
    GlStateManager.popMatrix();
    GlStateManager.enableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.enableTexture2D();
  }
}
