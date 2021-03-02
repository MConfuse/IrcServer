package de.confuse.irc.interfaces;

/**
 * When implemented, this Interface will add the methods needed to handle all
 * kinds of Server errors and background responses.
 * 
 * @version 1
 * @author Confuse/Confuse#5117
 *
 */
public interface IrcServerResponse {

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
	 * a typos or syntax errors.
	 */
	public void commandError();
	
	// ---			---
	// --- Warnings ---
	// --- 			---

	/**
	 * NOTE: This may get removed later on and streamlined into one with
	 * {@link #commandError()}! <br>
	 * <br>
	 * Called when the "nick" command failed due to insufficient permissions.
	 */
	public void nickPermission();
	
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
}
