package net.aurora.server.command.commands;

import java.io.PrintStream;

import net.aurora.server.User;
import net.aurora.server.User.ClientType;
import net.aurora.server.command.Command;

public class SubscribeIrcCommand extends Command {

	public SubscribeIrcCommand()
	{
		super("subscribe", new String[] {}, "subscribe [Client]", "", "Adds you to the specified Client's IRC-Chat");
	}

	@Override
	public void onCommand(String command, String[] args, String message, User user, PrintStream stream) throws Exception
	{
//		if (args.length == 0)
		{
			stream.println("Coming soon ™");
			
		}
		
	}
	
	/**
	 * Returns the Specified client type.
	 * 
	 * @param paramString The name of the {@link ClientType}
	 * @return {@link ClientType} | Null
	 */
	private ClientType getType(String paramString)
	{
//		System.out.println(paramString);
		
		for (ClientType type : ClientType.values())
		{
//			System.out.println(type.name);
			
			if (type.name.equals(paramString))
				return type;
			
		}
		
		return null;
	}

}
