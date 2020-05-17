package onimen.anni.hmage.renderer;

import java.util.Iterator;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import onimen.anni.hmage.renderer.layer.HMageLayerCape;

public class HMageRenderPlayer extends RenderPlayer {

  public HMageRenderPlayer(RenderManager renderManager) {
    this(renderManager, false);
  }

  @SuppressWarnings("rawtypes")
  public HMageRenderPlayer(RenderManager renderManager, boolean isSlim) {
    super(renderManager, isSlim);

    for (Iterator iterator = layerRenderers.iterator(); iterator.hasNext();) {
      LayerRenderer<?> layerRenderer = (LayerRenderer<?>) iterator.next();
      if (layerRenderer instanceof LayerBipedArmor) {
        iterator.remove();
        System.out.println("------------------REMOVE DEFAULT RENDERER----------:" + layerRenderer.getClass());
      } else if (layerRenderer instanceof LayerCape) {
        iterator.remove();
        System.out.println("------------------REMOVE DEFAULT RENDERER----------:" + layerRenderer.getClass());
      }
    }

    this.addLayer(new HMageLayerCape(this));
    this.addLayer(new HMageLayerBipedArmor(this));
  }

}
