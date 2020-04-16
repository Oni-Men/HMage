package onimen.anni.hmage.command;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import onimen.anni.hmage.Preferences;

public class PrefCommand extends CommandBase implements IClientCommand {

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
			String prefix = ChatFormatting.GOLD + "HMage is ";
			switch (args[0]) {
			case "enable":
				Preferences.cfg.setProperty("enabled", "true");
				sender.sendMessage(new TextComponentString(prefix + "enabled"));
				break;
			case "disable":
				Preferences.cfg.setProperty("enabled", "false");
				sender.sendMessage(new TextComponentString(prefix + "disabled"));
			}
			Preferences.read();
			return;
		}

		if (args.length == 2) {

			String prefix = ChatFormatting.GOLD + "[HMage]";
			String errorPrefix = ChatFormatting.RED + "[Hmage]Error: ";

			String key = args[0];
			String value = args[1];

			if (Preferences.cfg.containsKey(key)) {
				Preferences.cfg.setProperty(key, value);

				sender.sendMessage(new TextComponentString(prefix + key + " setted " + value));
			} else {
				sender.sendMessage(new TextComponentString(errorPrefix + key + " doesn't exist."));
			}
			Preferences.read();
		}
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		return false;
	}

}
