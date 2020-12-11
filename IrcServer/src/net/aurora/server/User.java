package net.aurora.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {

	// --- User end Stuff ---
	private PrintStream streamOut;
	private InputStream streamIn;
	private String nickname;
	private String name;
	private final Socket client;
	private final String password;
	private final List<String> devPasswords;
	private final List<String> adminPasswords;
	private final List<String> staffPasswords;
	private final List<String> friendPasswords;
	public final UserType userType;
	public final ClientType clientType;
	
	// --- User settings ---
	//    --- Staff Settings ---
	private boolean chatCooldown = true;
	
	//    --- Normal Settings ---
	private boolean fullname;
	
	// --- Last private message ---
	private User lastPrivateMessage = null;
	
	// --- Management / Staff permissions ---
	public List<ClientType> subscribedIrcs = new ArrayList<ClientType>();

	// --- User ID ---
	private static int nbUser = 0;
	private int userId;

	/**
	 * Creates a new User! Essential for this whole thing to work.
	 * 
	 * @param client   The {@link Socket} from the User
	 * @param name     The Users name
	 * @param password The Users password if said User is a Staff member
	 * @throws IOException If there was an error creating the tunnel back to the
	 *                     Client
	 */
	public User(Socket client, ClientType type, String name, String password) throws IOException
	{
		password = password.trim();
		this.streamOut = new PrintStream(client.getOutputStream());
		this.streamIn = client.getInputStream();
		this.client = client;
		this.clientType = type;
		this.subscribedIrcs.add(type);
		this.nickname = name;
		this.name = name;
		this.password = password;
		this.userId = nbUser;
		nbUser++;

		devPasswords = Arrays.asList(/* Token for the Developer Rank */ 		"ndDPA563+WMNTwhd18#OWrfhp3oe5eop+WOADJ2687jjowu+g3erwp13FM2SEI7O");
		adminPasswords = Arrays.asList(/* Token for the Administrator Rank */ 	"wAifb29#wEIAW6pu8gv+bawDWAWDKD5478#WAH56pwroitvmawLDWAI4Geo4u+fg");
		staffPasswords = Arrays.asList(/* Token for the Staff Rank */			"lDIAOWFMEI45+OFfkvn+9qaiweDLWP0LUB137#WQvmaoiDLWAnvsq#woDPAWDFwp");
		friendPasswords = Arrays.asList(/* Token for the Friend Rank */ 		"vgiwLDWZ+ivh#bwibvealoqLK1+9QCQDRnvnmLi#rzghbDWJAU+198hfgiwq92WM");

		userType = isStaff(password);
	}

	/**
	 * Sets the rank of the User using the password.
	 * 
	 * @param password Password
	 * @return Staff
	 */
	private UserType isStaff(String password)
	{
		if (devPasswords.contains(password))
			return UserType.DEV;

		if (adminPasswords.contains(password))
			return UserType.ADMIN;

		if (staffPasswords.contains(password))
			return UserType.STAFF;

		if (friendPasswords.contains(password))
			return UserType.FRIEND;
		
		if (isVerified(name))
			return UserType.VERIFIED;

		return UserType.USER;
	}
	
	/**
	 * TODO: Add a database to actually check if the User is actually Verified
	 * 
	 * @param name Name of the User.
	 * @return
	 */
	public boolean isVerified(String name)
	{
		return false;
	}
	
	public enum ClientType
	{
		AURORA("Aurora"), KOTCLIENT("Kot"), VOYAGER("Voyager");
		
		public final String name;
		
		ClientType(String name)
		{
			this.name = name;
		}
		
	}

	public enum UserType
	{
		DEV("§7[§4Dev§7]§4"), ADMIN("§7[§3Admin§7]§3"), STAFF("§7[§6Staff§7]§6"), FRIEND("§7[§2Friend§7]§2"), VERIFIED("§7[Verified]"), USER("");

		public final String prefix;

		UserType(String prefix)
		{
			this.prefix = prefix;
		}

	}

	public PrintStream getOutStream()
	{
		return this.streamOut;
	}

	public InputStream getInputStream()
	{
		return this.streamIn;
	}

	public String getNickname()
	{
		return userType != UserType.USER ? userType.prefix + " " + this.nickname : this.nickname;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Socket getClient()
	{
		return client;
	}

	public String getPassword()
	{
		return password;
	}

	public boolean isChatCooldown()
	{
		return chatCooldown;
	}

	public void setChatCooldown(boolean chatCooldown)
	{
		this.chatCooldown = chatCooldown;
	}

	public boolean isFullname()
	{
		return fullname;
	}

	public void setFullname(boolean fullname)
	{
		this.fullname = fullname;
	}

	public User getLastPrivateMessage()
	{
		return lastPrivateMessage;
	}

	public void setLastPrivateMessage(User lastPrivateMessage)
	{
		this.lastPrivateMessage = lastPrivateMessage;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

}
