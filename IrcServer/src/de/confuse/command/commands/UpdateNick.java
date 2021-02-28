package de.confuse.command.commands;

import java.io.PrintStream;

import de.confuse.Server;
import de.confuse.User;
import de.confuse.command.Command;
import de.confuse.confFile.ConfFileReader;

public class UpdateNick extends Command {

	public UpdateNick()
	{
		super("nick", new String[] {}, "/nick (name)", "name", "Changes your IRC Username!");
	}

	@Override
	public void onCommand(String command, String[] args, String message, User user, PrintStream stream) throws Exception
	{
		/**
		 * Requires format:
		 * 
		 * /nick data{"ingameName"name,"customName"customName};
		 * 
		 */
		
		ConfFileReader reader = new ConfFileReader(message.substring(message.indexOf(" ")).trim());
		
		try
		{
			// --- If User is staff the custom nick will be used ---
			if (user.isStaff())
			{
				String name = reader.getField("data").getValue("customName");
				
				if (name == null)
					throw new Exception("Name not specified!");
				
				user.setNickname(name);
				user.setNicked(true);
				
				// --- Notifies the User about the changed Nick ---
				stream.println(Server.instance.serverNotifications.get("nick") + name);
			}
			else
			{
				// TODO Test if this is exploitable
				String name = reader.getField("data").getValue("ingameName");
				
				if (name == null)
					throw new Exception("Name not specified!");
				
				user.setNickname(name);
				user.setNicked(false);
				
				// --- Notifies the User about the failed Nick ---
				stream.println(Server.instance.serverWarnings.get("nick") + name);
			}
			
		}
		catch (Exception e)
		{
			stream.println(Server.instance.serverExceptions.get("cmdError"));
		}
		
	}

}
