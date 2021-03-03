package de.confuse.command.commands;

import java.io.PrintStream;

import de.confuse.Server;
import de.confuse.User;
import de.confuse.command.Command;

public class ToggleNickCommand extends Command {

	public ToggleNickCommand()
	{
		super("tnick", new String[] {}, "/tnick", "", "Unnicks the User");
	}

	@Override
	public void onCommand(String command, String[] args, String message, User user, PrintStream stream) throws Exception
	{
		if (!user.isStaff())
		{
			missingPermissions(stream, null);
			return;
		}
		
		Server server = Server.instance;

		user.setNicked(!user.isNicked());

		if (user.isNicked())
			stream.println(server.serverNotifications.get("unnick").getFormattedField());
		else
			stream.println(server.serverNotifications.get("nickChanged").put("name", user.getNickname()).getFormattedField());
	}

}
