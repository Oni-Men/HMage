package onimen.anni.hmage.observer;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import onimen.anni.hmage.HMage;

public class AnniObserverMap {

  private static AnniObserverMap instance = null;

  public static AnniObserverMap getInstance() {
    if (instance == null)
      instance = new AnniObserverMap();
    return instance;
  }

  @Nullable
  private String playingServerName;
  private final Map<String, AnniObserver> anniObserverMap;

  private AnniObserverMap() {
    anniObserverMap = Maps.newHashMap();
  }

  public void setAnniObserver(String serverName, GamePhase phase) {
    setAnniObserver(serverName, phase, false);
  }

  public void setAnniObserver(String serverName, GamePhase phase, boolean force) {
    playingServerName = serverName;

    boolean canPutNewObserver = false;

    if (anniObserverMap.containsKey(serverName)) {
      AnniObserver anniObserver = anniObserverMap.get(serverName);
      if (anniObserver != null && anniObserver.getGameInfo().getGamePhase().getValue() > phase.getValue()) {
        canPutNewObserver = true;
      }
    } else {
      canPutNewObserver = true;
    }
    canPutNewObserver = force ? true : canPutNewObserver;

    if (canPutNewObserver) {
      anniObserverMap.put(serverName, new AnniObserver(Minecraft.getMinecraft()));
      HMage.logger.info("New Annihilation Observer created");
    }

    getAnniObserver().onJoinGame();
    HMage.logger.info("Playing server name: " + playingServerName);
  }

  public void unsetAnniObserver() {
    HMage.logger.info("Stop observing anni: " + playingServerName);
    getAnniObserver().onLeaveGame();
    this.playingServerName = null;
  }

  @Nullable
  public AnniObserver getAnniObserver() {
    if (this.playingServerName == null)
      return null;
    return anniObserverMap.get(playingServerName);
  }

}
