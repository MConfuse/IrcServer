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
import de.confuse.command.commands.UpdateNick;
import de.confuse.confFile.ConfFileReader;
import de.confuse.database.DataBase;
import de.confuse.handlers.UserHandler;

public class Server {

	public static Server instance;
	
	// --- Server interns ---
	private ServerSocket server;
	private DataBase data;
	private final double version = 0.5;
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
		
		// TODO: Update this to ConfFields so it's not as bad of a mess lOl
		
		// --- Client Exceptions ---
//		serverExceptions.put("kicked", "IrcServerException:Kicked");
		serverExceptions.put("kicked", "IrcServerException{\"reason\"Kicked};");
//		serverExceptions.put("banned", "IrcServerException:Banned");
		serverExceptions.put("banned", "IrcServerException{\"reason\"Banned};");
		// --- Server Exceptions ---
//		serverExceptions.put("closed", "IrcServerException:Server_Closed");
		serverExceptions.put("close", "IrcServerException{\"reason\"Server_Closed};");
//		serverExceptions.put("error", "IrcServerException:Error");
		serverExceptions.put("error", "IrcServerException{\"reason\"Error};");
//		serverExceptions.put("cmdError", "IrcServerException:Cmd_Error"); // Sent when command failed
		serverExceptions.put("cmdError", "IrcServerException{\"reason\"Cmd_Error};");
		
		// --- Warnings (All kinds) ---
//		serverWarnings.put("nick", "IrcServerWarning:NickPermission-" /** Nickname */); // Notification for non staff
		serverWarnings.put("nick", "IrcServerWarning{\"reason\"NickPermission};");
		
		// --- Notifications (All kinds) ---
//		serverNotifications.put("nick", "IrcServerNotification:NickChanged-" /** Nickname */);
		serverNotifications.put("nick", "IrcServerNotification{\"reason\"NickChanged,\"");
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
