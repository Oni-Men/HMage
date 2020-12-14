package onimen.anni.hmage;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Session;
import onimen.anni.hmage.observer.data.GameInfo;

public class HMageDiscordHandler {

  public static final HMageDiscordHandler INSTANCE = new HMageDiscordHandler();
  private static final String DISCORD_APPLICATION_ID = "724639729651417279";

  private DiscordRPC client;
  private DiscordEventHandlers handlers;

  private long previousUpdate = 0;

  private HMageDiscordHandler() {
    client = DiscordRPC.INSTANCE;
    handlers = new DiscordEventHandlers();
    client.Discord_Initialize(DISCORD_APPLICATION_ID, handlers, true, "");
  }

  public boolean checkInterval() {
    long now = System.currentTimeMillis();
    if (now - previousUpdate > 15000) {
      previousUpdate = now;
      return true;
    }
    return false;
  }

  public void clearPresence() {
    client.Discord_ClearPresence();
  }

  public void updatePresenceWithGameInfo(GameInfo gameInfo) {
    if (!Preferences.enableDiscordRPC)
      return;

    if (!Preferences.showAnniGameInfoOnDiscordRPC)
      return;

    DiscordRichPresence presence = new DiscordRichPresence();

    String name = gameInfo.getMapName();
    if (name == null) {
      name = HMage.anniObserverMap.getPlayingServerName().replaceAll("§.", "");
    }

    presence.details = String.format("%s - %s", name, gameInfo.getGamePhase().getText());
    presence.state = gameInfo.getMeTeamColor().getColorName();

    client.Discord_UpdatePresence(presence);
  }

  public void updatePresenceWithNormal() {
    if (!Preferences.enableDiscordRPC)
      return;

    if (!this.checkInterval())
      return;

    Minecraft minecraft = Minecraft.getMinecraft();

    if (minecraft == null)
      return;

    DiscordRichPresence presence = new DiscordRichPresence();
    ServerData server = minecraft.getCurrentServerData();
    Session session = minecraft.getSession();

    boolean singleplayer = minecraft.world == null ? false : !minecraft.world.isRemote;

    String serverAddress = Preferences.showServerAddressOnDiscordRPC
        ? (server != null ? server.serverIP.toLowerCase() : "")
        : "";

    presence.startTimestamp = HMage.startMilliTime / 1000;
    presence.details = singleplayer ? "Singleplayer" : serverAddress;
    presence.state = session == null || !Preferences.showUserNameOnDiscordRPC ? "" : session.getUsername();

    client.Discord_UpdatePresence(presence);
  }
}
