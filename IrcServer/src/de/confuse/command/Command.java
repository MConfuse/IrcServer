package de.confuse.command;

import java.io.PrintStream;

import de.confuse.Server;
import de.confuse.User;
import de.confuse.confFile.ConfFileField;

/**
 * Commands for managing Settings and other stuff on a text basis.
 * 
 * IRC Server Special edition :)!
 * 
 * @version 1.1
 * @author Confuse/xXConfusedJenni#5117
 */
public abstract class Command {

	private String name;
	private String[] alias;
	private String syntax;
	private String arguments;
	private String descriptor;

	/**
	 * Constructor for a new Command.
	 * 
	 * @param name       Name of the Command
	 * @param alias      Alias/Nick/Short of the Command
	 * @param syntax     How to use the Command
	 * @param arguments  All arguments you can input for this Command
	 * @param descriptor Description of what this Command is / what it's used for
	 */
	public Command(String name, String[] alias, String syntax, String arguments, String descriptor)
	{
		this.name = name;
		this.alias = alias;
		this.syntax = syntax;
		this.arguments = arguments;
		this.descriptor = descriptor;
	}

	/**
	 * Called to perform a Command, this is usually called by the
	 * {@link CommandManager#callCommand(String)} method.
	 * 
	 * @param command Name of the Command
	 * @param args    All arguments that were parsed to the Command (Depending on
	 *                the Method everything after the Command name)
	 * @param message The complete Input String containing everything that was
	 *                parsed to the {@link CommandManager#callCommand(String)}
	 *                method
	 * @param user    The User who called the Command
	 * @param stream  The Output Stream of the User who called the Command
	 * @throws Exception Throws an error if the Command fails at some stage of the
	 *                   execution (No need to catch it yourself, to some extend)
	 */
	public abstract void onCommand(String command, String[] args, String message, User user, PrintStream stream)
			throws Exception;
	
	/**
	 * Sends the {@link User} who sent this command request an error message
	 * containing everything they might need.
	 * 
	 * @param stream  The {@link PrintStream} of the sender
	 * @param extra   Any extra text for special cases
	 */
	public void sendErrorMessage(PrintStream stream, String extra)
	{
		ConfFileField field = Server.instance.serverExceptions.get("cmdError");
		
		if (extra != null)
			field.put("extra", extra);

		stream.println(field.put("command", getName()).put("syntax", getSyntax()).getFormattedField());
	}

	/**
	 * Notifies the {@link User} who sent this command request that they do not have
	 * the required permissions to use this command.
	 * 
	 * @param stream The {@link PrintStream} of the sender
	 * @param extra  Any extra text for special cases
	 */
	public void missingPermissions(PrintStream stream, String extra)
	{
		ConfFileField field = Server.instance.serverWarnings.get("commandPermission");
		
		if (extra != null)
			field.put("extra", extra);
		
		stream.println(field.put("command", getName()).getFormattedField());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String[] getAlias()
	{
		return alias;
	}

	public void setAlias(String[] alias)
	{
		this.alias = alias;
	}

	public String getSyntax()
	{
		return syntax;
	}

	public void setSyntax(String errorMessage)
	{
		this.syntax = errorMessage;
	}

	public String getArguments()
	{
		return arguments;
	}

	public void setArguments(String arguments)
	{
		this.arguments = arguments;
	}

	public String getDescriptor()
	{
		return descriptor;
	}

	public void setDescriptor(String descriptor)
	{
		this.descriptor = descriptor;
	}

}
