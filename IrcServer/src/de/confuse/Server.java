package de.confuse;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import de.confuse.User.ClientType;
import de.confuse.command.CommandManager;
import de.confuse.command.commands.HelpCommand;
import de.confuse.command.commands.SubscribeIrcCommand;
import de.confuse.command.commands.ToggleNickCommand;
import de.confuse.command.commands.UpdateNick;
import de.confuse.confFile.ConfFileField;
import de.confuse.confFile.ConfFileReader;
import de.confuse.database.DataBase;
import de.confuse.handlers.UserHandler;

public class Server {

	public static Server instance;
	
	// --- Server interns ---
	private ServerSocket server;
	private DataBase data;
	private final double version = 0.54;
	private int port;

	/** A List of all Users */
	public final List<User> clients;
	
	// --- Managers ---
	private CommandManager commandManager;

	// --- Server Values ---
	@SuppressWarnings("unused")
	private boolean debugmode = false;
	private static boolean running = true;
	
	private final String exception = "IrcServerException";
	private final String warning = "IrcServerWarning";
	private final String notification = "IrcServerNotification";
	
	/** Server Exception HashMap */
	public final ConcurrentHashMap<String, ConfFileField> serverExceptions = new ConcurrentHashMap<String, ConfFileField>();
	/** Server Warnings HashMap */
	public final ConcurrentHashMap<String, ConfFileField> serverWarnings = new ConcurrentHashMap<String, ConfFileField>();
	/** Server Notifications HashMap */
	public final ConcurrentHashMap<String, ConfFileField> serverNotifications = new ConcurrentHashMap<String, ConfFileField>();

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
		serverExceptions.put("kicked", new ConfFileField(exception).put("reason", "Kicked"));
		serverExceptions.put("banned", new ConfFileField(exception).put("reason", "Banned"));
		
		// --- Server Exceptions ---
		serverExceptions.put("close", new ConfFileField(exception).put("reason", "Server_Closed"));
		serverExceptions.put("error", new ConfFileField(exception).put("reason", "Error"));
		serverExceptions.put("cmdError", new ConfFileField(exception).put("reason", "Cmd_Error"));
		
		// --- Warnings (All kinds) ---
		serverWarnings.put("commandPermission", new ConfFileField(warning).put("reason", "CommandPermission"));
		
		// --- Notifications (All kinds) ---
		serverNotifications.put("changedNick", new ConfFileField(notification).put("reason", "ChangedNick"));
		serverNotifications.put("unnick", new ConfFileField(notification).put("reason", "UnNicked"));
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
		commandManager.addCommand(new UpdateNick());
		commandManager.addCommand(new ToggleNickCommand());
		
		
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
				Socket client = server.accept(); // Accepts every connection lMao
				Scanner scanner = new Scanner(client.getInputStream());
				ConfFileReader reader = new ConfFileReader(scanner.nextLine(), 1D);
				
				// Password and Nick
				String nickname = reader.getField("login").getValue("nickname");
				String password = reader.getField("login").getValue("password");
				
				// TODO: Check if the HWID matches with the database
				isVerified("");
				
				// Sets the ClientType
				ClientType type = getType(reader.getField("login").getValue("type"));
				if (type == null || nickname == null)
				{
					PrintStream stream = new PrintStream(client.getOutputStream());
					stream.println("Disconnected!");
					
					client.close();
					continue;
				}
				
				// Logs User in the Console
				System.out.println("New User: " + nickname + " (" + client.getInetAddress().getHostAddress() + ") " + type);
				
				// Adds the User
				User newUser = new User(client, type, nickname, password);
				clients.add(newUser);
				
				// Notify's the Console
				newUser.getOutStream().println("Welcome " + newUser.getNickname());
				newUser.getOutStream().println("Note: This is a beta!");
				newUser.getOutStream().println("Bugs may occur LOL!");
				
				// Creates a new UserHandler
				new Thread(new UserHandler(this, newUser)).start();
			}
			catch (Exception e)
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
	
	private boolean isVerified(String hwid)
	{
		return true;
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
