package onimen.anni.hmage.module.normal;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import onimen.anni.hmage.event.HandleEntityMetadataEvent;
import onimen.anni.hmage.util.ShotbowUtils;
import onimen.anni.hmage.util.scheduler.SyncScheduledTaskQueue;

public class BodyArrowRemover extends AbstractModule {

  @Override
  public String getId() {
    return "hmage.module.body-arrow-remover";
  }

  @SubscribeEvent
  public void onHandleEntityMetadata(HandleEntityMetadataEvent event) {
    if (!canBehave())
      return;

    Minecraft mc = Minecraft.getMinecraft();

    if (!ShotbowUtils.isShotbow(mc))
      return;

    SPacketEntityMetadata metadata = event.getMetadata();
    Entity entity = mc.world.getEntityByID(metadata.getEntityId());

    if (entity == null || metadata.getDataManagerEntries() == null)
      return;

    if (entity.getEntityId() != mc.player.getEntityId())
      return;

    int currentArrowCount = mc.player.getArrowCountInEntity();

    for (DataEntry<?> dataEntry : metadata.getDataManagerEntries()) {
      if (dataEntry.getKey().getId() == 10) {
        Object value = dataEntry.getValue();

        int nextArrowCount = 0;
        if (value instanceof Integer) {
          nextArrowCount = ((Integer) value).intValue();
        }

        if (nextArrowCount > currentArrowCount) {
          SyncScheduledTaskQueue.addTask(() -> {
            if (mc.player != null)
              mc.player.setArrowCountInEntity(mc.player.getArrowCountInEntity() - 1);
          }, 1000L * 30L);
        }
      }
    }

  }
}
