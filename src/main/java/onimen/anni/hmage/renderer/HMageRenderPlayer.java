package onimen.anni.hmage.renderer;

import java.util.Iterator;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import onimen.anni.hmage.renderer.layer.HMageLayerBipedArmor;
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

    setLayer(0, new HMageLayerBipedArmor(this));
    setLayer(4, new HMageLayerCape(this));
  }

  @SuppressWarnings("unchecked")
  public <V extends EntityLivingBase, U extends LayerRenderer<V>> void setLayer(int index, U layer) {
    this.layerRenderers.set(index, (LayerRenderer<AbstractClientPlayer>) layer);
  }
}
