package de.confuse.irc;

/**
 * This Interface needs to be implemented into every class you want to receive
 * the IRC-Messages in. It is streamlined and the
 * {@link #messageReceived(String)} method will be called for every message this
 * client receives from the IRC-Server.
 * 
 * @version 1
 * @author Confuse/Confuse#5117
 *
 */
public interface IrcMessage {
	
	public void messageReceived(String message);

}
