package de.confuse.command.commands;

import java.io.PrintStream;

import de.confuse.User;
import de.confuse.command.Command;

public class PrefixCommand extends Command {

	public PrefixCommand(String name, String[] alias, String syntax, String arguments, String descriptor)
	{
		super("prefix", new String[] {}, "/prefix", "", "Toggles whether or not your prefix is shown");
	}

	@Override
	public void onCommand(String command, String[] args, String message, User user, PrintStream stream) throws Exception
	{
		
	}

}
