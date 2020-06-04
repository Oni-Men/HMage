package onimen.anni.hmage.module;

import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.event.DrawWorldBackgroundEvent;
import onimen.anni.hmage.gui.GuiColorPicker;
import onimen.anni.hmage.gui.button.ButtonObject;

public class CustomGuiBackground extends AbstractModule {

  private Consumer<Integer> callback = i -> {
    Preferences.setInt(this.getName() + ".color", i);
    Preferences.save();
  };

  @Override
  public String getName() {
    return "CustomGuiBackground";
  }

  @Override
  public String getDescription() {
    return "GUIの背景を変更します";
  }

  @Override
  public ButtonObject getPreferenceButton() {
    return new ButtonObject() {

      @Override
      public String getTitle() {
        return getName();
      }

      @Override
      public List<String> getDescription() {
        return null;
      }

      @Override
      public String getButtonText() {
        return "Change color";
      }

      @Override
      public void actionPerformed(GuiButton button) {
        int color = Preferences.getInt(getName() + ".color", 0x33000000);
        Minecraft.getMinecraft()
            .displayGuiScreen(new GuiColorPicker(null, callback, color));
      }
    };
  }

  @SubscribeEvent
  public void onDrawWorldBackground(DrawWorldBackgroundEvent event) {
    Minecraft mc = Minecraft.getMinecraft();

    if (mc == null)
      return;

    if (!isEnable() || mc.world == null) { return; }
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

    int c = Preferences.getInt(this.getName() + ".color", 0x33000000);
    float alpha = (float) (c >> 24 & 255) / 255F;
    float red = (float) (c >> 16 & 255) / 255F;
    float green = (float) (c >> 8 & 255) / 255F;
    float blue = (float) (c & 255) / 255F;

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
