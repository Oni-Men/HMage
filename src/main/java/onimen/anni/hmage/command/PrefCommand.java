package onimen.anni.hmage.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import onimen.anni.hmage.HMage;
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.module.InterfaceModule;
import onimen.anni.hmage.util.PositionHelper;
import onimen.anni.hmage.util.PositionHelper.PositionType;

public class PrefCommand extends CommandBase implements IClientCommand {

  public static final String SUCCESS_PREFIX = ChatFormatting.GOLD + "[HMage] ";
  public static final String ERROR_PREFIX = ChatFormatting.RED + "[HMage][ERROR] ";

  public static List<String> getPositionTypeTabCompletions() {
    List<String> tabCompletions = new ArrayList<>();

    for (PositionType positionType : PositionType.values()) {
      tabCompletions.add(positionType.getText());
    }

    return tabCompletions;
  }

  @Override
  public String getName() {
    return "hmage";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/hmage <Module Name> <Option Name> <Option Value>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0)
      return;

    if (args.length == 1) {
      switch (args[0].toLowerCase()) {
      case "enable":
        Preferences.enable();
        sendMessage(sender, SUCCESS_PREFIX + "HMage is enabled");
        break;
      case "disable":
        Preferences.disable();
        sendMessage(sender, SUCCESS_PREFIX + "HMage is disabled");
        break;
      default:
        sendMessage(sender, ERROR_PREFIX + "invalid parameter");
      }
    } else if (args.length == 2) {

      switch (args[1]) {
      case "enable":
        getModuleByName(args[0]).setEnable(true);
        break;
      case "disable":
        getModuleByName(args[0]).setEnable(false);
        break;
      }

    } else if (args.length == 3) {

      String key = args[0] + "." + args[1];
      String value = args[2];

      if (args[1].contentEquals("position")) {
        System.out.println("args 2 is position");
        System.out.println("unformatted value: " + value);
        try {
          Integer.parseInt(value);
        } catch (NumberFormatException error) {
          value = String.valueOf(PositionHelper.getPositionBitFromString(value));
        }
        System.out.println("formatted value: " + value);
      }

      List<String> keys = getModuleByName(args[0]).getPreferenceKeys();

      if (keys.contains(args[1])) {
        Preferences.setProperty(key, value);
      } else {
        sendMessage(sender, ERROR_PREFIX + "cannot set property with key: " + key);
      }
    }

    Preferences.save();
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
      BlockPos targetPos) {

    List<String> completions = new ArrayList<>();

    if (args.length == 0) {
      return super.getTabCompletions(server, sender, args, targetPos);
    } else if (args.length == 1) {

      completions.add("enable");
      completions.add("disable");
      completions.addAll(getModuleNames());

    } else if (args.length == 2) {

      if (!args[0].contentEquals("enable") && !args[0].contentEquals("disable")) {

        InterfaceModule module = getModuleByName(args[0]);

        List<String> keys = module.getPreferenceKeys();

        keys.remove("enabled");

        completions.addAll(keys);
        completions.add("enable");
        completions.add("disable");

      }

    } else if (args.length == 3) {

      if (args[1].contentEquals("position")) {
        completions.addAll(Arrays.stream(PositionType.values()).map(t -> t.getText()).collect(Collectors.toList()));
      }

    }

    return completions.stream()
        .filter(c -> c.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
    return false;
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 0;
  }

  private void sendMessage(ICommandSender sender, String msg) {
    sender.sendMessage(new TextComponentString(msg));
  }

  private List<String> getModuleNames() {
    return HMage.getModuleMap().values().stream().map(m -> m.getName()).collect(Collectors.toList());
  }

  private InterfaceModule getModuleByName(String moduleName) {
    return HMage.getModuleMap().get(moduleName);
  }

}
