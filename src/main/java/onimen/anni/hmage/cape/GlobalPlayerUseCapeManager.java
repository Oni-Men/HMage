package onimen.anni.hmage.cape;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.util.ResourceLocation;
import onimen.anni.hmage.HMage;

public class GlobalPlayerUseCapeManager {

  private static Map<UUID, ResourceLocation> capeMap = new HashMap<>();

  /**
   * Cape情報を取得するスケジューラーを開始する。
   */
  public static void loadCape() {
    CapeResourceLoadTask capeResourceLoadTask = new CapeResourceLoadTask();
    capeResourceLoadTask.run();
  }

  /**
   * UUIDに紐づくCapeのリソースを取得する。
   *
   * @param uuid uuid
   * @return Capeのリソース
   */
  public static ResourceLocation getCapeResource(UUID uuid) {
    return capeMap.get(uuid);
  }

  /**
   * Cape情報を再設定する。
   *
   * @param capeMap capeMap
   */
  public static void reload(Map<UUID, ResourceLocation> capeMap) {
    GlobalPlayerUseCapeManager.capeMap = Collections.unmodifiableMap(capeMap);
    HMage.logger.info("loaded cape infomation.");
  }

  public static Map<UUID, ResourceLocation> getCapeMap() {
    return capeMap;
  }

  public static void clear() {
    capeMap = Collections.emptyMap();
  }

}
