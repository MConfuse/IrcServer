package de.confuse.irc.handlers;

import de.confuse.irc.IrcManager;

public class CommandHandler {
	
	public static void checkMessage(String message)
	{
		// Checks if the message is a command
		if (message.startsWith("/"))
		{
			handleCommand(message);
		}
		else
		{
			IrcManager.INSTANCE.getOutput().println(message);
		}
		
	}
	
	/**
	 * This method handles all commands and will format them according to the
	 * command.
	 * 
	 * @param message The whole command
	 */
	private static void handleCommand(String message)
	{
		// TODO: Add the actual handler
		IrcManager.INSTANCE.getOutput().println(message);
	}

}
