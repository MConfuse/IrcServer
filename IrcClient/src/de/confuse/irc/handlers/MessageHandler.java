package de.confuse.irc.handlers;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.confuse.confFile.ConfFileField;
import de.confuse.confFile.ConfFileReader;
import de.confuse.confFile.ConfResult;
import de.confuse.irc.IrcManager;
import de.confuse.irc.interfaces.IMessage;
import de.confuse.irc.interfaces.IServerResponse;

/**
 * The general message handler that checks every incoming and outgoing message
 * from and to the IRC-Server. <br>
 * This Class will format commands written by the User to fit the needed format
 * and will handle incoming Server responses using the {@link IServerResponse}
 * Interface as well as notify your code of the normal messages broadcasted by
 * the IRC-Server using the {@link IMessage} Interface.
 * 
 * @version 1
 * @author Confuse/Confuse#5117
 *
 */
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
	 * {@link IServerResponse} Interface will be called.
	 * 
	 * @param message The message to check
	 * @return False if the message does not contain an error message or anything
	 *         background related.
	 */
	public static boolean checkIncomingMessage(String message)
	{
		try
		{
			System.out.println(MessageHandler.class + ":: " + message);
			
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
						for (IServerResponse resp : IrcManager.responseInterfaces)
							resp.kickedFromIrc();
						break;

					case "Banned":
						for (IServerResponse resp : IrcManager.responseInterfaces)
							resp.bannedFromIrc();
						break;

					case "Server_Closed":
						for (IServerResponse resp : IrcManager.responseInterfaces)
							resp.serverClosed();
						break;

					case "Error":
						for (IServerResponse resp : IrcManager.responseInterfaces)
							resp.serverError();
						break;

					case "Cmd_Error":
						for (IServerResponse resp : IrcManager.responseInterfaces)
							resp.commandError(field.getValue("command"), field.getValue("syntax"), field.getValue("extra"));
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
					case "CommandPermission":
						for (IServerResponse resp : IrcManager.responseInterfaces)
							resp.commandPermission(field.getValue("command"), field.getValue("extra"));
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
					case "ChangedNick":
						for (IServerResponse resp : IrcManager.responseInterfaces)
							resp.nickChanged(field.getValue("name"));
						break;
						
					case "UnNicked":
						for (IServerResponse resp : IrcManager.responseInterfaces)
							resp.unNicked();
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
		
		// --- Nick/uName Command ---
		if (nickAlias.contains(command))
		{
			boolean uname = command.equals("uname");

			ConfFileField field = new ConfFileField("data");
			field.put("ingameName", man.getName());
			field.put("customName", uname ? man.getName() : args[1]);

			out.println("/" + (uname ? "uname" : args[0]) + " " + field.getFormattedField());
			return;
		}
		
		// TODO: Add the actual handler
		IrcManager.INSTANCE.getOutput().println("/" + message);
	}

}
