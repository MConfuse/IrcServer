package net.aurora.server.command.commands;

import java.io.PrintStream;

import net.aurora.server.Server;
import net.aurora.server.User;
import net.aurora.server.command.Command;

public class UpdateNick extends Command {

	public UpdateNick(String name, String[] alias, String syntax, String arguments, String descriptor)
	{
		super("nick", new String[] {}, "/nick (name)", "name", "Changes your IRC Username!");
	}

	@Override
	public void onCommand(String command, String[] args, String message, User user, PrintStream stream) throws Exception
	{
		/**
		 * TODO: Test this stuff
		 * 
		 * Requires format:
		 * 
		 * /nick MinecraftName:CustomNick
		 * 
		 */
		
		// --- Splits both options ---
		String[] nick = args[0].split(":");
		
		// --- If User is staff the custom nick will be used ---
		if (user.isStaff())
		{
			user.setNickname(nick[1]);
			user.setNicked(true);
			
			// --- Notifies the User about the changed Nick ---
			stream.println(Server.instance.serverNotifications.get("nick") + nick[1]);
		}
		else
		{
			user.setNickname(nick[0]);
			user.setNicked(false);
			
			// --- Notifies the User about the failed Nick ---
			stream.println(Server.instance.serverWarnings.get("nick") + nick[0]);
		}
		
	}

}
