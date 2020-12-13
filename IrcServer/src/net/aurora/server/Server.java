package net.aurora.server;

import net.aurora.server.User.ClientType;
import net.aurora.server.command.CommandManager;
import net.aurora.server.command.commands.HelpCommand;
import net.aurora.server.command.commands.SubscribeIrcCommand;
import net.aurora.server.database.DataBase;
import net.aurora.server.handlers.UserHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

	public static Server instance;
	
	// --- Server interns ---
	private ServerSocket server;
	private DataBase data;
	private final double version = 0.4;
	private int port;

	/** A List of all Users */
	public final List<User> clients;
	
	// --- Managers ---
	private CommandManager commandManager;

	// --- Server Values ---
	@SuppressWarnings("unused")
	private boolean debugmode = false;
	private static boolean running = true;
	
	/** Server Exception HashMap */
	public final ConcurrentHashMap<String, String> serverExceptions = new ConcurrentHashMap<String, String>();
	/** Server Warnings HashMap */
	public final ConcurrentHashMap<String, String> serverWarnings = new ConcurrentHashMap<String, String>();
	/** Server Notifications HashMap */
	public final ConcurrentHashMap<String, String> serverNotifications = new ConcurrentHashMap<String, String>();

	/**
	 * Creates a new Server instance
	 * 
	 * @param host  The domain the Server should connect to
	 * @param port  The port
	 * @param debug Debug mode?
	 */
	public Server(String host, int port, boolean debug)
	{
		instance = this;
		
		this.port = port;
		this.clients = new ArrayList<User>();
		this.debugmode = debug;
		
		// --- Client Exceptions ---
		serverExceptions.put("kicked", "IrcServerException:Kicked");
		serverExceptions.put("banned", "IrcServerException:Banned");
		// --- Server Exceptions ---
		serverExceptions.put("closed", "IrcServerException:Server_Closed");
		
		// --- Warnings (All kinds) ---
		serverWarnings.put("nick", "IrcServerWarning:NickPermission-" /** Nickname */); // Notification for non staff
		
		// --- Notifications (All kinds) ---
		serverNotifications.put("nick", "IrcServerNotification:NickChanged-" /** Nickname */);
	}

	/**
	 * Runs the Servers logic.
	 * 
	 * @throws IOException If something goes wrong, you'll know it lol
	 */
	public void run() throws IOException
	{
		System.out.println("Booting the IRC-Server!");
		
		this.server = new ServerSocket(this.port) {
			protected void finalize() throws IOException
			{
				close();
			}

		};

		commandManager = new CommandManager();
		commandManager.addCommand(new HelpCommand());
		commandManager.addCommand(new SubscribeIrcCommand());
		
		
		System.out.println("Server V" + version + " online!");
		System.out.println("Server Port: " + this.port);

		// Runs the Server
		while (running)
		{
			/**
			 * Connection requirements:
			 * 	- Line 1 Name: Name of the Connection
			 *  - Line 2 Client: The Client that is connecting
			 * 	- Line 3 Password: Password if you are staff
			 */
			
			try
			{
				Socket client = this.server.accept(); // Accepts every connection lMao
				Scanner scanner = new Scanner(client.getInputStream());
				String nickname = scanner.nextLine();
				String password = null;
				
				nickname = nickname.replace(",", "");
				nickname = nickname.replace(" ", "_").substring(0, nickname.length() - 1);
				
				// Sets the ClientType
				ClientType type = getType(scanner.nextLine().trim());
				if (type == null)
				{
					PrintStream stream = new PrintStream(client.getOutputStream());
					stream.println("Disconnected!");
					
					client.close();
					continue;
				}
				
				// Retrieves the Token
				password = scanner.nextLine().trim();
				
				// Logs User in the Console
				System.out.println("New User: " + nickname + " (" + client.getInetAddress().getHostAddress() + ") " + type);
				
				// Adds the User
				User newUser = new User(client, type, nickname, password);
				this.clients.add(newUser);
				
				// Notify's the Console
				newUser.getOutStream().println("Welcome " + newUser.getNickname());
				newUser.getOutStream().println("Note: This is a beta!");
				newUser.getOutStream().println("Bugs may occur LOL!");
				
				// Creates a new UserHandler
				new Thread(new UserHandler(this, newUser)).start();
			}
			catch (NoSuchElementException e)
			{
				;
			}
			
		}

	}

	public void broadcastMessages(String msg, User userSender)
	{
		for (User client : this.clients)
		{
			client.getOutStream().println(userSender.getNickname() + " -> " + msg);
		}

	}
	
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

	/**
	 * Returns a list of all active Users.
	 * 
	 */
	public void broadcastAllUsers()
	{
		for (User client : this.clients)
		{
			client.getOutStream().println(this.clients);
		}

	}

	/**
	 * Removes a User using the User's Object
	 * 
	 * @param user
	 */
	public void removeUser(User user)
	{
		this.clients.remove(user);
	}

	/**
	 * Returns the Database.
	 * 
	 * @return {@link DataBase}
	 */
	public DataBase getData()
	{
		return this.data;
	}

	/**
	 * Sets the Database
	 * 
	 * @param data
	 */
	public void setData(DataBase data)
	{
		this.data = data;
	}

	/**
	 * Kills the Server.
	 * 
	 */
	public static void stop()
	{
		running = false;
	}

}
