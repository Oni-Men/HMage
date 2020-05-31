package onimen.anni.hmage.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import onimen.anni.hmage.event.DrawWorldBackgroundEvent;

public class CustomGuiBackground extends AbstractModule {

  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      "hmage:textures/gui/gui_background.png");

  private float r = 0.2F;
  private float g = 0.4F;
  private float b = 0.7F;
  private float a = 0.3F;

  private boolean textured = true;

  @Override
  public String getName() {
    return "CustomGuiBackground";
  }

  @Override
  public String getDescription() {
    return "GUIの背景を変更します";
  }

  @SubscribeEvent
  public void onDrawWorldBackground(DrawWorldBackgroundEvent event) {
    if (!isEnable()) { return; }
    event.setCanceled(true);

    Minecraft mc = Minecraft.getMinecraft();

    if (mc == null)
      return;

    ScaledResolution sr = new ScaledResolution(mc);
    double w = sr.getScaledWidth_double();
    double h = sr.getScaledHeight_double();

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder builder = tessellator.getBuffer();

    GlStateManager.disableAlpha();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.color(r, g, b, a);

    if (textured) {
      mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
      builder.begin(7, DefaultVertexFormats.POSITION_TEX);
    } else {
      GlStateManager.disableTexture2D();
      builder.begin(7, DefaultVertexFormats.POSITION);
    }

    builder.pos(0, h, 0);
    if (textured)
      builder.tex(0, 1);
    builder.endVertex();

    builder.pos(w, h, 0);
    if (textured)
      builder.tex(1, 1);
    builder.endVertex();

    builder.pos(w, 0, 0);
    if (textured)
      builder.tex(0, 1);
    builder.endVertex();

    builder.pos(0, 0, 0);
    if (textured)
      builder.tex(0, 1);
    builder.endVertex();

    tessellator.draw();

    GlStateManager.enableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.enableTexture2D();
  }
}
