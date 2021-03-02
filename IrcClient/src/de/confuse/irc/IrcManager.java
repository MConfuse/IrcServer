package de.confuse.irc;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import de.confuse.confFile.ConfFileField;
import de.confuse.irc.handlers.CommandHandler;
import de.confuse.irc.handlers.MessageReceivedHandler;
import de.confuse.irc.message.IrcMessage;

/**
 * The IrcManager class is a core class for the IRC-Server. This will handle
 * connecting, sending and disconnecting as well as parse through received
 * messages from the IRC-Server using the {@link IrcMessage} Interface. <br>
 * The {@link IrcMessage} Interface will implement a method that is called for
 * every message this client receives from the IRC-Server.
 * 
 * @version 1
 * @author Confuse/Confuse#5117
 *
 */
public class IrcManager {

	/** The Instance of this class */
	public static IrcManager INSTANCE;

	/**
	 * List that should contain all classes that want to receive the Messages the
	 * {@link MessageReceivedHandler} receives.
	 */
	public static List<IrcMessage> classes;
	/** The Handler for incoming messages */
	public static MessageReceivedHandler handler;
	/** The {@link MessageReceivedHandler} Thread */
	private Thread handlerThread;

	// --- IP and host ---
	/** The Host address of the Server */
	private String host;
	/** The port the Server is on */
	private int port;

	// --- Server stuff ---
	private Socket server;
	private PrintStream output;
	
	// --- User stuff ---
	private String name;
	private String token;
	private ClientType type;

	/**
	 * Creates a new {@link IrcManager} which is the core class of this IRC-System.
	 * This Manager-class will do almost everything server related for you.
	 * 
	 * @param host  The Hosts address
	 * @param port  The port to connect to
	 * @param obj   A {@link List} containing all the {@link Class}es that implement
	 *              the {@link IrcMessage} Interface. Implementing the
	 *              {@link IrcMessage} Interface is necessary so your code is
	 *              notified when the IRC-Server sends a message to your IRC-Client.
	 * @param nick  The default name of the User.
	 * @param token The token to gain access to ranks (put in "null" if it was not
	 *              specified)
	 * @param type  The type of client
	 * @throws UnknownHostException Thrown when the client can't connect to the
	 *                              Server.
	 * @throws IOException          Thrown when there was an error creating the
	 *                              {@link PrintStream}.
	 */
	public IrcManager(String host, int port, List<IrcMessage> obj, String nick, String token, ClientType type) throws UnknownHostException, IOException
	{
		INSTANCE = this;
		classes = obj;

		this.host = host;
		this.port = port;
		this.name = nick;
		this.token = token;
		this.type = type;

		// --- Booting the IRC-Client ---
		initIrc();
	}
	
	/**
	 * Will do the initial boot of the IRC when creating a new Instance of this
	 * class. <br>
	 * To change the {@link #host} or {@link #port} you can use their individual
	 * setters, which are in this class.
	 * 
	 * @throws UnknownHostException Thrown when the client can't connect to the
	 *                              Server.
	 * @throws IOException          Thrown when there was an error creating the
	 *                              {@link PrintStream}.
	 */
	private void initIrc() throws UnknownHostException, IOException
	{
		// --- Connecting to the Server ---
		server = new Socket(host, port);
		output = new PrintStream(server.getOutputStream());
		
		handler = new MessageReceivedHandler(new Scanner(server.getInputStream()));
		handlerThread = new Thread(handler);
		handlerThread.start();
		
		// --- Logging in ---
		login();
	}

	/**
	 * Restarts the IRC-Client and updates the variables.
	 * 
	 * @throws UnknownHostException Thrown when the client can't connect to the
	 *                              Server.
	 * @throws IOException          Thrown when there was an error creating the
	 *                              {@link PrintStream}.
	 */
	public void restartIrc() throws UnknownHostException, IOException
	{
		// --- Closing the old connection ---
		server.close();
		getOutput().close();
		handler.setRunning(false);
//		handlerThread.stop();

		// --- Connecting to the Server ---
		server = new Socket(host, port);
		output = new PrintStream(server.getOutputStream());

		handler = new MessageReceivedHandler(new Scanner(server.getInputStream()));
		handlerThread = new Thread(handler);
		handlerThread.start();
		
		// --- Logging in ---
		login();
	}

	/**
	 * Shuts the IRC-Service down.
	 * 
	 * @throws IOException Thrown when there was an error while closing the Streams.
	 */
	public void stopIrc() throws IOException
	{
		// --- Closing the old connection ---
		server.close();
		getOutput().close();
		handler.setRunning(false);
	}
	
	/**
	 * Will log the user into the IRC-Server. Several variables can be chosen here.
	 * 
	 */
	private void login()
	{
		ConfFileField field = new ConfFileField("login");
		field.put("nickname", name);
		field.put("password", token);
		field.put("type", type.name);
		
		System.out.println(field.getFormattedField());
		getOutput().println(field.getFormattedField());
	}
	
	/**
	 * Sends a message to the IRC-Server. This message will be broadcasted to all
	 * clients if it isn't a command!
	 * 
	 * @param message The message sent to the IRC-Server.
	 */
	public static void sendMessage(String message)
	{
		CommandHandler.checkMessage(message);
	}
	
	public PrintStream getOutput()
	{
		return output;
	}

	public enum ClientType
	{
		AURORA("Aurora"), KOTCLIENT("Kot"), CATSENSE("CatSense");
		
		public final String name;
		
		ClientType(String name)
		{
			this.name = name;
		}

	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

}
