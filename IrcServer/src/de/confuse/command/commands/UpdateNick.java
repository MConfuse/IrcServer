package de.confuse.command.commands;

import java.io.PrintStream;

import de.confuse.Server;
import de.confuse.User;
import de.confuse.command.Command;
import de.confuse.confFile.ConfFileReader;

public class UpdateNick extends Command {

	public UpdateNick()
	{
		// uName is the alias for normal users to update their nick.
		super("nick", new String[] {"uname"}, "/nick (name) || /uname", "name", "Changes your IRC Username!");
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
		
		System.out.println(message);
		System.out.println(message.substring(5));
		
		// Reading the content
		ConfFileReader reader = null;
		if (command.equals("uname"))
			reader = new ConfFileReader(message.substring(6), 1D);
		else
			reader = new ConfFileReader(message.substring(5), 1D);
		
		try
		{
			if (command.equals("uname"))
			{
				System.out.println("Reached: uName");
				String name = reader.getField("data").getValue("ingameName");
				
				user.setNickname(name);
				
				// --- Notifies the User about the changed Nick ---
				stream.println(Server.instance.serverNotifications.get("nickChanged").put("name", user.getNickname())
						.getFormattedField());
				return;
			}
			
			// --- If User is staff the custom nick will be used ---
			if (user.isStaff())
			{
				String name = reader.getField("data").getValue("customName");
				
				if (name == null)
					throw new Exception("Name not specified!");
				
				user.setNickname(name);
				user.setNicked(true);

				// --- Notifies the User about the changed Nick ---
				stream.println(Server.instance.serverNotifications.get("nickChanged").put("name", user.getNickname())
						.getFormattedField());
			}
			else // If user is not staff, the normal name will be updated.
			{
				// TODO Test if this is exploitable
				String name = reader.getField("data").getValue("ingameName");
				
				if (name == null)
					throw new Exception("Name not specified!");
				
				user.setName(name);
				user.setNicked(false);
				
				// --- Notifies the User about the failed Nick ---
				missingPermissions(stream, null);
			}
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			sendErrorMessage(stream, null);
		}
		
	}

}
