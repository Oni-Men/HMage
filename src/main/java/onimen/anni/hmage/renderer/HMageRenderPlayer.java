package onimen.anni.hmage.renderer;

import java.util.Iterator;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
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
      if (layerRenderer instanceof LayerArmorBase) {
        iterator.remove();
        System.out.println("------------------REMOVE DEFAULT RENDERER----------:" + layerRenderer.getClass());
      } else if (layerRenderer instanceof LayerRenderer) {
        iterator.remove();
        System.out.println("------------------REMOVE DEFAULT RENDERER----------:" + layerRenderer.getClass());
      }
    }

    this.addLayer(new HMageLayerCape(this));
    this.addLayer(new HMageLayerBipedArmor(this));
  }

}
