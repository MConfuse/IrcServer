package de.confuse.irc.handlers;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.confuse.confFile.ConfFileField;
import de.confuse.confFile.ConfFileReader;
import de.confuse.confFile.ConfResult;
import de.confuse.irc.IrcManager;
import de.confuse.irc.interfaces.IrcServerResponse;

public class MessageHandler {

	// --- Command alias ---
	private static List<String> nickAlias = new ArrayList<String>(Arrays.asList("nick", "uname"));
	
	public static void checkOutgoingMessage(String message)
	{
		// Checks if the message is a command
		if (message.startsWith("/"))
		{
			handleCommand(message.substring(1));
			
			return;
		}
		else
		{
			IrcManager.INSTANCE.getOutput().println(message);
		}
		
	}

	/**
	 * Depending on the exception the fitting method in the
	 * {@link IrcServerResponse} Interface will be called.
	 * 
	 * @param message The message to check
	 * @return False if the message does not contain an error message or anything
	 *         background related.
	 */
	public static boolean checkIncomingMessage(String message)
	{
		try
		{
			System.out.println(message);
			
			// --- Server Exceptions ---
			if (message.startsWith("IrcServerException{\"reason\""))
			{
				// Extracting the content
				ConfFileReader reader = new ConfFileReader(message, 1D);
				ConfResult field = reader.getField("IrcServerException");

				String reason = field.getValue("reason");

				// Switch case for all of the current commands and their Interface response
				switch (reason)
				{
					case "Kicked":
						for (IrcServerResponse resp : IrcManager.responseInterfaces)
							resp.kickedFromIrc();
						break;

					case "Banned":
						for (IrcServerResponse resp : IrcManager.responseInterfaces)
							resp.bannedFromIrc();
						break;

					case "Server_Closed":
						for (IrcServerResponse resp : IrcManager.responseInterfaces)
							resp.serverClosed();
						break;

					case "Error":
						for (IrcServerResponse resp : IrcManager.responseInterfaces)
							resp.serverError();
						break;

					case "Cmd_Error":
						for (IrcServerResponse resp : IrcManager.responseInterfaces)
							resp.commandError();
						break;
				}

				return true;
			} // --- Server Warnings ---
			else if (message.startsWith("IrcServerWarning{\"reason\""))
			{
				// Extracting the content
				ConfFileReader reader = new ConfFileReader(message, 1D);
				ConfResult field = reader.getField("IrcServerWarning");

				String reason = field.getValue("reason");

				switch (reason)
				{
					case "NickPermission":
						for (IrcServerResponse resp : IrcManager.responseInterfaces)
							resp.nickPermission();
						break;

				}

				return true;
			} // --- Server Notifications ---
			else if (message.startsWith("IrcServerNotification{\"reason\""))
			{
				// Extracting the content
				ConfFileReader reader = new ConfFileReader(message, 1D);
				ConfResult field = reader.getField("IrcServerNotification");

				String reason = field.getValue("reason");

				switch (reason)
				{
					case "NickChanged":
						for (IrcServerResponse resp : IrcManager.responseInterfaces)
							resp.nickChanged(field.getValue("name"));
						break;

				}

				return true;
			}
			
			return false;
		}
		catch (Exception e)
		{
			// TODO: Handle exceptions?
			e.printStackTrace();
			return true;
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
		PrintStream out = IrcManager.INSTANCE.getOutput();
		IrcManager man = IrcManager.INSTANCE;
		String[] args = message.split(" ");
		String command = args[0];
		
		// --- Nick Command ---
		if (nickAlias.contains(command))
		{
			ConfFileField field = new ConfFileField("data");
			field.put("ingameName", man.getName());
			field.put("customName", args[1]);
			
			out.println("/nick " + field.getFormattedField());
		}
		
		// TODO: Add the actual handler
//		IrcManager.INSTANCE.getOutput().println("/" + message);
	}

}
