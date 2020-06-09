package onimen.anni.hmage.command;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import onimen.anni.hmage.cape.CapeResourceLoadTask;
import onimen.anni.hmage.cape.GlobalPlayerUseCapeManager;
import onimen.anni.hmage.observer.AnniObserver;
import onimen.anni.hmage.observer.AnniObserverMap;
import onimen.anni.hmage.observer.data.GameInfo;

public class DebugCommand extends CommandBase implements IClientCommand {

  @Override
  public String getName() {
    return "dhmage";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/dhmage [reload|clear|show]";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0) { return; }

    switch (args[0]) {
    case "reload":
      new CapeResourceLoadTask().run();
      break;
    case "anni":
      AnniObserver anniObserver = AnniObserverMap.getInstance().getAnniObserver();
      if (anniObserver == null) { return; }
      GameInfo gameInfo = anniObserver.getGameInfo();
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String json = gson.toJson(gameInfo);
      String[] split = json.split("\n");
      for (String string : split) {
        sender.sendMessage(new TextComponentString(string));
      }
      break;
    case "clear":
      GlobalPlayerUseCapeManager.clear();
      break;
    case "show":
      Map<UUID, ResourceLocation> capeMap = GlobalPlayerUseCapeManager.getCapeMap();
      for (Entry<UUID, ResourceLocation> entry : capeMap.entrySet()) {
        sender.sendMessage(new TextComponentString(entry.getKey() + "@" + entry.getValue().getResourcePath()));
      }
      break;
    default:
      sender.sendMessage(new TextComponentString("unknown paramater.:" + args[0]));
      break;
    }
  }

  @Override
  public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
    return false;
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 0;
  }

}
