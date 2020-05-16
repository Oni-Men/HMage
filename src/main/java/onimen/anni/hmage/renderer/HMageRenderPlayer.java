package onimen.anni.hmage.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class HMageRenderPlayer extends RenderPlayer {

  public HMageRenderPlayer(RenderManager renderManager) {
    this(renderManager, false);
  }

  @SuppressWarnings("rawtypes")
  public HMageRenderPlayer(RenderManager renderManager, boolean isSlim) {
    super(renderManager, isSlim);

    for (int i = 0; i < this.layerRenderers.size(); i++) {
      LayerRenderer<AbstractClientPlayer> layer = this.layerRenderers.get(i);
      if (layer instanceof LayerArmorBase) {
        if ((LayerArmorBase) layer instanceof LayerBipedArmor) {
          this.layerRenderers.remove(layer);
          System.out.println("------------------REMOVE DEFAULT ARMOR RENDERER--------------------");
        }
      }
    }

    this.addLayer(new HMageLayerBipedArmor(this));
  }


}
