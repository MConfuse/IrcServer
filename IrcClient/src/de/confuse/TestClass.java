package de.confuse;

import de.confuse.irc.interfaces.IrcMessage;
import de.confuse.irc.interfaces.IrcServerResponse;

public class TestClass implements IrcMessage, IrcServerResponse {

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
	public void commandError()
	{
		System.out.println("Command error!");
	}

	@Override
	public void nickPermission()
	{
		System.out.println("Nick permission!");
		
	}

	@Override
	public void nickChanged(String newName)
	{
		System.out.println("Nick changed to: " + newName + "!");
	}

}
