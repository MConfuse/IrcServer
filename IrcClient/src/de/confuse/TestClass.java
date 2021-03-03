package de.confuse;

import de.confuse.irc.interfaces.IMessage;
import de.confuse.irc.interfaces.IServerResponse;

public class TestClass implements IMessage, IServerResponse {

	public TestClass()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void messageReceived(String message)
	{
//		System.out.println("DebugClass: " + message);
	}

	@Override
	public void kickedFromIrc()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bannedFromIrc()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverClosed()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverError()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commandError(String command, String syntax, String extra)
	{
		System.out.println("Command error! | " + command + " - " + syntax);
	}

	@Override
	public void commandPermission(String command, String extra)
	{
		System.out.println("Command permission missing! " + command + " || " + extra);
		
	}

	@Override
	public void nickChanged(String newName)
	{
		System.out.println("Nick changed to: " + newName + "!");
	}

	@Override
	public void unNicked()
	{
		System.out.println("Unnicked!");
	}

}
