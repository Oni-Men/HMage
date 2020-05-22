package onimen.anni.hmage.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.cape.CapeResourceLoadTask;
import onimen.anni.hmage.cape.GlobalPlayerUseCapeManager;
import onimen.anni.hmage.util.PositionHelper.PositionType;

public class DebugCommand extends CommandBase implements IClientCommand {

  public static final String SUCCESS_PREFIX = ChatFormatting.GOLD + "[HMage] ";
  public static final String ERROR_PREFIX = ChatFormatting.RED + "[HMage][ERROR] ";

  public static List<String> getOptionDataTabCompletions() {

    List<String> tabCompletions = new ArrayList<>();

    tabCompletions.addAll(Preferences.statusArmorOption.getTabCompletionsList());
    tabCompletions.addAll(Preferences.statusEffectOption.getTabCompletionsList());
    tabCompletions.addAll(Preferences.arrowCounterOption.getTabCompletionsList());
    tabCompletions.addAll(Preferences.cpsCounterOption.getTabCompletionsList());

    return tabCompletions;
  }

  public static List<String> getPositionTypeTabCompletions() {
    List<String> tabCompletions = new ArrayList<>();

    for (PositionType positionType : PositionType.values()) {
      tabCompletions.add(positionType.getText());
    }

    return tabCompletions;
  }

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
