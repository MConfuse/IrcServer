package de.confuse;

import de.confuse.irc.IrcMessage;

public class TestClass implements IrcMessage {

	public TestClass()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void messageReceived(String message)
	{
		System.out.println("DebugClass: " + message);
	}

}
