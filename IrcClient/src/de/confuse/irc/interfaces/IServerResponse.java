package de.confuse.irc.interfaces;

/**
 * When implemented, this Interface will add the methods needed to handle all
 * kinds of Server errors and background responses. <br>
 * It is recommended to create a custom handler using this Interface and
 * override the needed methods when needed.
 * 
 * @version 1
 * @author Confuse/Confuse#5117
 *
 */
public interface IServerResponse {

	// --- 					 ---
	// --- Client Exceptions ---
	// --- 					 ---

	/**
	 * Called when you were kicked from the IRC.
	 */
	public void kickedFromIrc();
	
	/**
	 * Called when you were banned from the IRC.
	 */
	public void bannedFromIrc();
	
	// --- 					 ---
	// --- Server Exceptions ---
	// ---					 ---
	
	/**
	 * Called when the IRC-Server was closed while still being connected.
	 */
	public void serverClosed();
	
	/**
	 * In the current version this has no use.
	 */
	public void serverError();
	
	/**
	 * Called when there was an error while executing a command. This can be due to
	 * a typos or syntax errors.<br>
	 * <br>
	 * Note:<br>
	 * The "extra" String is null if the message was not set!
	 * 
	 * @param command The command that failed
	 * @param syntax  The command usage
	 * @param extra   Specific information for rare or specific errors || <strong>
	 *                null </strong>
	 */
	public void commandError(String command, String syntax, String extra);
	
	// ---			---
	// --- Warnings ---
	// --- 			---

	/**
	 * Called when the User does not have the required permissions to use the
	 * specified command.
	 * 
	 * @param command The command that failed
	 * @param extra   Specific information for rare or specific errors || <strong>
	 *                null </strong>
	 */
	public void commandPermission(String command, String extra);
	
	// --- 				 ----
	// --- Notifications ----
	// --- 				 ----
	
	/**
	 * Called when your nickname was updated, this can be done by either an
	 * IRC-Administrator or yourself if you have sufficient permissions.
	 * 
	 * @param newName The new alias.
	 */
	public void nickChanged(String newName);
	
	/**
	 * Called when you successfully unnicked yourself.
	 */
	public void unNicked();
}
