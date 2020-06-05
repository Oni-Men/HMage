package onimen.anni.hmage.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import onimen.anni.hmage.renderer.layer.HMageLayerBipedArmor;

public class HMageRenderPlayer extends RenderPlayer {

  public HMageRenderPlayer(RenderManager renderManager) {
    this(renderManager, false);
  }

  public HMageRenderPlayer(RenderManager renderManager, boolean isSlim) {
    super(renderManager, isSlim);

    for (int i = 0; i < layerRenderers.size(); i++) {
      LayerRenderer<?> layerRenderer = (LayerRenderer<?>) layerRenderers.get(i);
      if (layerRenderer instanceof LayerBipedArmor) {
        setLayer(i, new HMageLayerBipedArmor(this));
        //} else if (layerRenderer instanceof LayerCape) {
        //setLayer(i, new HMageLayerCape(this));
      }
    }
  }

  @SuppressWarnings("unchecked")
  public <V extends EntityLivingBase, U extends LayerRenderer<V>> void setLayer(int index, U layer) {
    this.layerRenderers.set(index, (LayerRenderer<AbstractClientPlayer>) layer);
  }
}
