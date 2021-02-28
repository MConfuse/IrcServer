package de.confuse.command.commands;

import java.io.PrintStream;

import de.confuse.User;
import de.confuse.User.ClientType;
import de.confuse.command.Command;

public class SubscribeIrcCommand extends Command {

	public SubscribeIrcCommand()
	{
		super("subscribe", new String[] {}, "subscribe [Client]", "", "Adds you to the specified Client's IRC-Chat");
	}

	@Override
	public void onCommand(String command, String[] args, String message, User user, PrintStream stream) throws Exception
	{
		if (args.length == 1 && args[0].equals(""))
		{
			StringBuilder builder = new StringBuilder();
			
			builder.append("/---------------------------\n");
			builder.append("| \n");
			builder.append("| Rules:\n");
			builder.append("|   - You cannot unsubscribe from your\n");
			builder.append("|     Clients IRC.\n");
			builder.append("| \n");
			builder.append("| /subscribe list - Shows the available Clients\n");
			builder.append("| \n");
			builder.append("\\---------------------------");
			
			stream.println(builder);
			return;
		}
		else
		{
			if (args[0].equalsIgnoreCase("list"))
			{
				StringBuilder builder = new StringBuilder();
				
				builder.append("/---------------------------\n");
				builder.append("| \n");
				builder.append("| Clients you can Subscribe to:\n");
				
				for (ClientType type : User.ClientType.values())
					builder.append("|   - " + type.name + "\n");
				
				builder.append("| \n");
				builder.append("\\---------------------------");
				
				stream.println(builder);
				return;
			}
			
			ClientType type = getType(args[0]);
			
			if (type == null)
			{
				// Nothing found
				stream.println("§7[§3System§7] §cThere is no such ClientType: " + args[0] + ".");
				stream.println("§7[§3System§7] §c/" + getName() + " for more info!");
				
				return;
			}
			else if (type == user.clientType)
			{
				// Type is the same as Client
				stream.println("§7[§3System§7] §cYou cannot unsubscribe from your Client's IRC!");
				stream.println("§7[§3System§7] §c/" + getName() + " for more info!");
				
				return;
			}
			else if (user.subscribedIrcs.contains(type))
			{
				user.subscribedIrcs.remove(type);
				
				stream.println("§7[§3System§7] Successfully §cunsubscribed §7from §5" + type.name + "'s §7IRC!");
				
				return;
			}
			else if (!user.subscribedIrcs.contains(type))
			{
				user.subscribedIrcs.add(type);
				
				stream.println("§7[§3System§7] Successfully §asubscribed §7to §5" + type.name + "'s §7IRC!");
				
				return;
			}
			else
			{
				stream.println("Something");
			}
			
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
			
			if (type.name.equalsIgnoreCase(paramString))
				return type;
			
		}
		
		return null;
	}

}
