package onimen.anni.hmage.util;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class ShotbowUtils {

  public static final String[] SHOTBOW_IP_SUFFIXES = new String[] {
      ".shotbow.net",
      ".shotbow.com",
      ".minez.net"
  };

  public static boolean isShotbow(Minecraft mc) {
    if (mc == null) { return false; }

    ServerData currentServerData = mc.getCurrentServerData();

    if (currentServerData == null) { return false; }

    String serverIP = currentServerData.serverIP.toLowerCase();

    if (serverIP == null) {
      return false;
    }

    return Arrays.stream(SHOTBOW_IP_SUFFIXES).anyMatch(suffix -> serverIP.endsWith(suffix));

  }

}
