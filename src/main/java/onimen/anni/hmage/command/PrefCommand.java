package onimen.anni.hmage.command;

import java.util.ArrayList;
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
import onimen.anni.hmage.Preferences;
import onimen.anni.hmage.util.PositionHelper;
import onimen.anni.hmage.util.PositionHelper.PositionType;

public class PrefCommand extends CommandBase implements IClientCommand {

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
    return "hmage";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/hmage <setting key> <value>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0)
      return;

    if (args.length == 1) {
      switch (args[0].toLowerCase()) {
      case "enable":
        Preferences.cfg.setProperty("enabled", "true");
        sendMessage(sender, SUCCESS_PREFIX + "enabled");
        break;
      case "disable":
        Preferences.cfg.setProperty("enabled", "false");
        sendMessage(sender, SUCCESS_PREFIX + "disabled");
        break;
      default:
        sendMessage(sender, ERROR_PREFIX + "invalid param");
      }
    } else if (args.length == 2) {

      String key = args[0];
      String value = args[1].toLowerCase();

      boolean isPositionKey = key.endsWith(".position");

      if (isPositionKey) {
        try {
          Integer.parseInt(value);
        } catch (NumberFormatException error) {
          value = String.valueOf(PositionHelper.getPositionBitFromString(value));
        }
      }

      if (Preferences.cfg.containsKey(key)) {
        Preferences.cfg.setProperty(key, value);
        sendMessage(sender, SUCCESS_PREFIX + key + " = " + value);
      } else {
        sendMessage(sender, ERROR_PREFIX + key + " doesn't exist.");
      }
    }

    Preferences.read();
    Preferences.save();
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
      BlockPos targetPos) {

    List<String> tabCompletions = new ArrayList<>();
    String lastArgument = args[args.length - 1];

    if (args.length == 0) {
      return super.getTabCompletions(server, sender, args, targetPos);
    } else if (args.length == 1) {
      List<String> prepare = getOptionDataTabCompletions();
      prepare.add("enable");
      prepare.add("disable");
      tabCompletions.addAll(prepare
          .stream()
          .filter(s -> s.startsWith(lastArgument))
          .collect(Collectors.toList()));
    } else if (args.length == 2) {
      //TODO WANNA MAKE TAB COMPLETION BETTER
      //			if (args[0].toLowerCase().endsWith(".position")) {
      //
      //				List<String> prepare = getPositionTypeTabCompletions();
      //				String[] positionParameters = lastArgument.split("#");
      //
      //				if (positionParameters.length == 0) {
      //					tabCompletions.addAll(prepare.stream().map(e -> "#" + e).collect(Collectors.toList()));
      //				} else {
      //					String lastParameter = positionParameters[positionParameters.length - 1];
      //
      //					tabCompletions.addAll(prepare.stream()
      //							.filter(e -> e.startsWith(lastParameter))
      //							.map(e -> String.join("#",
      //									(CharSequence[]) Arrays.copyOf(positionParameters, positionParameters.length - 1))
      //									+ "#" + e)
      //							.collect(Collectors.toList()));
      //				}
      //
      //			}
    }

    return tabCompletions;
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

}
