package onimen.anni.hmage.module;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import onimen.anni.hmage.cape.GlobalPlayerUseCapeManager;
import onimen.anni.hmage.cape.SPPlayerUseCape;
import onimen.anni.hmage.event.GetLocationCapeEvent;

public class HMageCape extends AbstractModule {

  @Override
  public String getName() {
    return "HMageCape";
  }

  @Override
  public String getDescription() {
    return "HMage Mod使用者間で見ることのできるCapeを表示します。この設定は自分のみに影響します。";
  }

  @SubscribeEvent
  public void onGetLocationCape(GetLocationCapeEvent event) {
    if (!isEnable()) { return; }

    //capeのリソースを取得
    UUID uniqueID = event.getPlayer().getUniqueID();
    boolean isMe = uniqueID.equals(Minecraft.getMinecraft().player.getUniqueID());
    ResourceLocation locationCape = isMe ? SPPlayerUseCape.getResourceLocation()
        : GlobalPlayerUseCapeManager.getCapeResource(uniqueID);

    event.setCapeLocation(locationCape);
  }
}
