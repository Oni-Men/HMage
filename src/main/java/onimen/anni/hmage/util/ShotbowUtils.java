package onimen.anni.hmage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class ShotbowUtils {

  public static final String[] SHOTBOW_IPS = new String[] {
      "us.shotbow.net",
      "eu.shotbow.net",
      "jp.shotbow.net"
  };

  public static boolean isShotbow(Minecraft mc) {
    if (mc == null) { return false; }

    ServerData currentServerData = mc.getCurrentServerData();

    if (currentServerData == null) { return false; }

    String serverIP = currentServerData.serverIP;

    if (serverIP == null) {
      return false;
    }

    //    for (String shotbowIP : SHOTBOW_IPS) {
    //      if (shotbowIP.equalsIgnoreCase(serverIP)) {
    //        return true;
    //      }
    //    }

    return serverIP.endsWith(".shotbow.net");

  }

}
