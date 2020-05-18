package onimen.anni.hmage.transformer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.renderer.HMageRenderPlayer;

public class HurtingArmorInjector {

  public static void replaceSkinMap(RenderManager renderManager) {

    Class<? extends RenderManager> c1ass = renderManager.getClass();

    Field[] fields = c1ass.getDeclaredFields();

    for (Field field : fields) {
      int modifiers = field.getModifiers();

      if (!Modifier.isFinal(modifiers)) {
        continue;
      }

      if (!Modifier.isPrivate(modifiers)) {
        continue;
      }

      if (!Map.class.isAssignableFrom(field.getType())) {
        continue;
      }

      HMage.logger.info("Find the skinMap. so we are going to replace RenderPlayer");

      field.setAccessible(true);

      try {
        @SuppressWarnings("unchecked")
        Map<String, RenderPlayer> map = (Map<String, RenderPlayer>) field.get(renderManager);
        map.put("default", new HMageRenderPlayer(renderManager));
        map.put("slim", new HMageRenderPlayer(renderManager, true));
        break;
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }

    }
  }

}
