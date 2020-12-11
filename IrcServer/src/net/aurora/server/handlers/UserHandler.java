package net.aurora.server.handlers;

import net.aurora.server.Server;
import net.aurora.server.User;
import net.aurora.server.command.CommandManager;

import java.util.Scanner;

/**
 * Handles all incoming traffic by the User. This Class should be created for every User.
 * 
 * @author Confuse / xXConfusedJenni#5117
 */
public class UserHandler implements Runnable {
	
	private Server server;
	private User user;

	/**
	 * Creates a new User Handler for handling the incoming traffic.
	 * 
	 * @param server The {@link Server} which it was created by
	 * @param user The {@link User} Object
	 */
	public UserHandler(Server server, User user)
	{
		this.server = server;
		this.user = user;
	}

	public void run()
	{
		Scanner sc = new Scanner(this.user.getInputStream());
		
		// Has user been terminated?
		while (sc.hasNextLine())
		{
			String message = sc.nextLine();
			message = message.replace("§", "");
			
			if (message.length() > 120)
				message = message.substring(0, 119);
			
			if (message.charAt(0) == '@')
			{
				if (message.contains(" "))
				{
//					System.out.println("private msg : " + message);
					int firstSpace = message.indexOf(" ");
					String userPrivate = message.substring(1, firstSpace);
					
					sendMessageToUser(message.substring(firstSpace + 1, message.length()), this.user, userPrivate);
				}
				
				continue;
			}
			
			if (message.charAt(0) == '/')
			{
				try
				{
					CommandManager.getInstance().callCommand(message.substring(1), this.user, this.user.getOutStream());
				}
				catch (Exception e)
				{
					
					e.printStackTrace();
				}
				
				continue;
			}
			
			broadcastMessageToSpecificClient(message, this.user);
		}

		this.server.removeUser(this.user);
//		this.server.broadcastAllUsers();
		sc.close();
		System.out.println(this.user.getNickname() + " disconnected!");
	}
	
	/**
	 * Sends a Message to all Users that are on the same Client
	 * 
	 * @param msg The Message that should be sent
	 * @param userSender
	 */
	public void broadcastMessageToSpecificClient(String msg, User userSender)
	{
		for (User client : this.server.clients)
		{
//			System.out.println(client.type + "|" +userSender.type);
			
			if (!userSender.type.equals(client.type))
				continue;
			
			client.getOutStream().println(userSender.getNickname() + " -> " + msg);
		}

	}
	
	/**
	 * Used to send a private Message to a user.
	 * 
	 * @param msg        The message
	 * @param userSender The Sender of the message
	 * @param user       Name of the targeted User
	 */
	public void sendMessageToUser(String msg, User userSender, String user)
	{
		for (User client : this.server.clients)
		{
			if (client.getName().equals(user) && client != userSender)
			{
				client.setLastPrivateMessage(userSender);
				
				userSender.getOutStream()
						.println(userSender.getNickname() + " -> " + client.getNickname() + "§8: §7" + msg);
				client.getOutStream().println("(Privat) " + userSender.getNickname() + " -> " + msg);
				
				return;
			}

		}

		userSender.getOutStream().println("User §3" + user + " §7was §cnot §7found!");
	}
	
}
