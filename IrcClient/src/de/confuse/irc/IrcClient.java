package de.confuse.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.confuse.TestClass;
import de.confuse.irc.IrcManager.ClientType;
import de.confuse.irc.message.IrcMessage;

public class IrcClient {

	public String host;
	public static Scanner sc;
	public int port;
	public static Socket client = null;
	public PrintStream output = null;
	public static String name = "Presti";

	public static void main(String[] args)
	{
		try
		{
			List<IrcMessage> list = new ArrayList<IrcMessage>();
			list.add(new TestClass());
			
			IrcManager man = new IrcManager("localhost", 1997, null, "TestUser", "null", ClientType.AURORA);
			
			new Thread(() ->
			{

				String line = "";
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				try
				{
					while ((line = reader.readLine()) != null)
					{
						if (line.equalsIgnoreCase("end me") || line.equalsIgnoreCase("end") || line.equalsIgnoreCase("SHUT")
								|| line.equalsIgnoreCase("BE GONE UNHOLY"))
						{
							System.out.println("Client offline.");

							man.stopIrc();
							System.exit(0);
							break;
						}
						else
						{
							IrcManager.sendMessage(line);
						}

					}

				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

			}).start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
//		new IrcClient();
	}

	public IrcClient()
	{
		try
		{
			run();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		output.println("login{\"nickname\"Test1,\"password\"yikes,\"type\"Aurora};");
	}

	public void IRC_Conn(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	public void run() throws UnknownHostException, IOException
	{
//		IRC_Conn("irc.janderedev.xyz", 1997);
		IRC_Conn("localhost", 1997);
//		IRC_Conn("79.219.143.56", 1997);

		client = new Socket(host, port);
		System.out.println("Successfully connected to the IRC Server!");
		output = new PrintStream(client.getOutputStream());

		new Thread(new ReceivedMessagesHandler(client.getInputStream())).start();
		new Thread(() ->
		{

			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try
			{
				while ((line = reader.readLine()) != null)
				{
					if (line.equalsIgnoreCase("end me") || line.equalsIgnoreCase("end") || line.equalsIgnoreCase("SHUT")
							|| line.equalsIgnoreCase("BE GONE UNHOLY"))
					{
						System.out.println("Client offline.");

						output.close();
						client.close();
						reader.close();
						System.exit(0);
						break;
					}
					else
					{
						this.output.println(line);
					}

				}

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}).start();
	}

	class ReceivedMessagesHandler implements Runnable {

		private InputStream server;

		public ReceivedMessagesHandler(InputStream server)
		{
			this.server = server;
		}

		public void run()
		{ // das ist der Handler wenn eine Nachricht vom Server eingeht
			Scanner s = new Scanner(server);
			String tmp = "";

			while (s.hasNextLine())
			{
				tmp = s.nextLine();

				try
				{
//					if (tmp.charAt(0) == '[')
//					{
//						tmp = tmp.substring(1, tmp.length() - 1);
//					}
//					else
//					{
					try
					{
						System.out.println("§c" + getTagValue(tmp).replaceAll("->", "§8->§7"));
					}
					catch (Exception ignore)
					{
					}
//					}
				}
				catch (Exception e)
				{
					// e.printStackTrace();
				}

			}

			s.close();
		}

		public String getTagValue(String xml)
		{
			return xml;
		}

	}

}
