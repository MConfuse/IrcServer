package de.confuse.handlers;

import java.util.Scanner;

import de.confuse.Main;
import de.confuse.Server;
import de.confuse.User;
import de.confuse.command.CommandManager;
import de.confuse.util.TimeHelper;

/**
 * Handles all incoming traffic by the User. This Class should be created for
 * every User.
 * 
 * @author Confuse / xXConfusedJenni#5117
 */
public class UserHandler implements Runnable {

	// --- User stuff ---
	private Server server;
	private User user;
	
	// --- Chat cool down ---
	private TimeHelper timer = new TimeHelper();
	
	/**
	 * Creates a new User Handler for handling the incoming traffic.
	 * 
	 * @param server The {@link Server} which it was created by
	 * @param user   The {@link User} Object
	 */
	public UserHandler(Server server, User user)
	{
		this.server = server;
		this.user = user;
	}

	public void run()
	{
		Scanner sc = new Scanner(user.getInputStream());
		timer.reset();

		// Has user been terminated?
		while (sc.hasNextLine())
		{
			
			String message = sc.nextLine();
//			String message = SikePackets.decrypt(sc.nextLine());
			message = message.replace("§", "");
			
			/** Checks if the User is still being rate limited */
			if (!timer.hasTimeElapsed(Main.timerCooldown, true) && user.isChatCooldown())
			{
				user.getOutStream().println("§7[§3System§7] §cChat cooldown still active!");
				continue;
			}

			/** Trims the message down to 120 Characters */
			if (message.length() > 120)
				message = message.substring(0, 119);

			/** Checks if the message is either a Broadcast to ALL Clients or Private message */
			if (message.startsWith("@all") && user.isStaff()) // Broadcast
			{
				if (message.contains(" "))
				{
//					System.out.println("broadcast all: " + message);
					broadcastMessageToAll(message.replaceFirst("@all", ""), user);
				}

				continue;
			}
			else if (message.charAt(0) == '@') // Private message
			{
				if (message.contains(" "))
				{
//					System.out.println("private msg : " + message);
					int firstSpace = message.indexOf(" ");
					String userPrivate = message.substring(1, firstSpace);

					sendMessageToUser(message.substring(firstSpace + 1, message.length()), user, userPrivate);
				}

				continue;
			}

			/** Checks if the message is a Command */
			if (message.charAt(0) == '/')
			{
				try
				{
					CommandManager.getInstance().callCommand(message.substring(1), user, user.getOutStream());
				}
				catch (Exception e)
				{

					e.printStackTrace();
				}

				continue;
			}

			/** If nothing above was true, the message will be sent to the Users Clients IRC */
//			message = SikePackets.encrypt(message);
			broadcastMessageToClient(message, user);
		}

		server.removeUser(user);
//		server.broadcastAllUsers();
		sc.close();
		System.out.println(user.getNickname() + " disconnected!");
	}

	/**
	 * Sends a Message to all Users that are on the same Client
	 * 
	 * @param msg        The Message that should be sent
	 * @param userSender
	 */
	public void broadcastMessageToClient(String msg, User userSender)
	{
		for (User client : server.clients)
		{
//			System.out.println(client.type + "|" +userSender.type);

			if (!userSender.clientType.equals(client.clientType) || !client.subscribedIrcs.contains(userSender.clientType))
				continue;

			client.getOutStream().println(userSender.getNickname() + " -> " + msg);
		}

	}
	
	/**
	 * Sends a Message to all Users that are on the same Client
	 * 
	 * @param msg        The Message that should be sent
	 * @param userSender
	 */
	public void broadcastMessageToAll(String msg, User userSender)
	{
		for (User client : server.clients)
		{
//			System.out.println(client.type + "|" +userSender.type);
			
			client.getOutStream().println("§4@all §7(§3" + userSender.clientType.name + "§7) " + userSender.getNickname() + " -> " + msg);
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
		for (User client : server.clients)
		{
			if (client.getName().equals(user) && client != userSender)
			{
				// --- (Should) Encrypt the whole thing LOL ---
				String toSender = ("(Private) " + userSender.getNickname() + " -> " + client.getNickname() + "§8: §7");
				String toReceiver = ("(Privat) " + userSender.getNickname() + " -> ");
//				msg = SikePackets.encrypt(msg);

				client.setLastPrivateMessage(userSender);

				userSender.getOutStream().println(toSender + msg);
				client.getOutStream().println(toReceiver + msg);

				return;
			}

		}

		userSender.getOutStream().println("User §3" + user + " §7was §cnot §7found!");
	}

}
