package de.confuse.irc.handlers;

import java.util.Scanner;

import de.confuse.irc.IrcManager;
import de.confuse.irc.interfaces.IMessage;

public class MessageReceivedHandler implements Runnable {

	/** The Scanner that scans the Server's output stream */
	private Scanner scanner;
	/** Indicates whether or not this Thread is running */
	private boolean running = true;

	/**
	 * Will start a new handler for all incoming messages from the IRC-Server.
	 * 
	 * @param scanner The {@link Scanner} for the Servers OutputStream
	 */
	public MessageReceivedHandler(Scanner scanner)
	{
		this.scanner = scanner;
	}

	@Override
	public void run()
	{
		String tmp = "";

		while (scanner.hasNextLine() && running)
		{
			tmp = scanner.nextLine();

			try
			{
				try
				{
//					System.out.println(tmp);
//					System.out
//							.println("[MessageHandler notification] §c" + getTagValue(tmp).replaceAll("->", "§8->§7"));

					if (!MessageHandler.checkIncomingMessage(tmp))
						for (IMessage clazz : IrcManager.messageInterfaces)
							clazz.messageReceived(tmp);

				}
				catch (Exception ignore)
				{
					;
				}
				
			}
			catch (Exception e)
			{
				;
			}

		}

		scanner.close();
	}
	
	private String getTagValue(String xml)
	{
		return xml;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

}
