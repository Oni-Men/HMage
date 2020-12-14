package onimen.anni.hmage;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import onimen.anni.hmage.event.DrawWorldBackgroundEvent;
import onimen.anni.hmage.event.GetCharWidthEvent;
import onimen.anni.hmage.event.GetLocationCapeEvent;
import onimen.anni.hmage.event.HandleEntityMetadataEvent;
import onimen.anni.hmage.event.LoadFontTextureEvent;
import onimen.anni.hmage.event.PlayParticleEvent;
import onimen.anni.hmage.event.RenderFontEvent;

public class HMageHooks {

  public static boolean onDrawWorldBackground() {
    return MinecraftForge.EVENT_BUS.post(new DrawWorldBackgroundEvent());
  }

  public static ResourceLocation onGetLocationCape(AbstractClientPlayer player) {
    GetLocationCapeEvent event = new GetLocationCapeEvent(player);
    MinecraftForge.EVENT_BUS.post(event);
    return event.getCapeLocation();
  }

  public static boolean onhandleParticles(SPacketParticles particle) {
    PlayParticleEvent event = new PlayParticleEvent(particle);
    MinecraftForge.EVENT_BUS.post(event);
    return event.isCanceled();
  }

  public static RenderFontEvent onRenderFont(char ch, boolean italic, float x, float y) {
    RenderFontEvent event = new RenderFontEvent(ch, italic, x, y);
    MinecraftForge.EVENT_BUS.post(event);
    return event;
  }

  public static ResourceLocation onLoadFontTexture(int page, ResourceLocation resourceLocation) {
    LoadFontTextureEvent event = new LoadFontTextureEvent(page, resourceLocation);
    MinecraftForge.EVENT_BUS.post(event);
    return event.getResourceLocation();
  }

  public static GetCharWidthEvent onGetCharWidth(char ch) {
    GetCharWidthEvent event = new GetCharWidthEvent(ch, 0);
    MinecraftForge.EVENT_BUS.post(event);
    return event;
  }

  public static void onHandleEntityMetadata(SPacketEntityMetadata packet) {
    MinecraftForge.EVENT_BUS.post(new HandleEntityMetadataEvent(packet));
  }
}
