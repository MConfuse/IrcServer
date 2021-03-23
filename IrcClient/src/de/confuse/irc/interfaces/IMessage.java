package de.confuse.irc.interfaces;

import de.confuse.irc.handlers.MessageReceivedHandler;

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
public interface IMessage {
	
	/**
	 * Called when the {@link MessageReceivedHandler} received a message from the
	 * IRC-Server.
	 * 
	 * @param message The message that was received
	 */
	public void messageReceived(String message);

}
