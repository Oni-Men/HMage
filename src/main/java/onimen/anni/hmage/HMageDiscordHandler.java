package onimen.anni.hmage;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import onimen.anni.hmage.observer.data.GameInfo;

public class HMageDiscordHandler {

  public static final HMageDiscordHandler INSTANCE = new HMageDiscordHandler();
  private static final String DISCORD_APPLICATION_ID = "724639729651417279";

  private DiscordRPC client;
  private DiscordEventHandlers handlers;

  private HMageDiscordHandler() {
    client = DiscordRPC.INSTANCE;
    handlers = new DiscordEventHandlers();
    client.Discord_Initialize(DISCORD_APPLICATION_ID, handlers, true, "");
  }

  public void updatePresenceWithGameInfo(GameInfo gameInfo) {
    DiscordRichPresence presence = new DiscordRichPresence();

    presence.startTimestamp = gameInfo.getGameTimestamp() / 1000;
    presence.details = String.format("%s - %s", gameInfo.getMapName(), gameInfo.getGamePhase().getText());
    presence.state = gameInfo.getMeTeamColor().getColorName();

    client.Discord_UpdatePresence(presence);
  }

  public void updatePresenceWithNormal() {
    DiscordRichPresence presence = new DiscordRichPresence();

    String state = "-";
    String details = "-";

    ServerData server = Minecraft.getMinecraft().getCurrentServerData();
    if (server != null) {
      details = server.serverIP.toLowerCase();
    }

    EntityPlayerSP player = Minecraft.getMinecraft().player;

    if (player != null) {
      state = player.getName();
    }

    presence.startTimestamp = HMage.startMilliTime / 1000;
    presence.details = details;
    presence.state = state;

    client.Discord_UpdatePresence(presence);
  }
}
