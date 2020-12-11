package net.aurora.server.command.commands;

import java.io.PrintStream;

import net.aurora.server.User;
import net.aurora.server.command.Command;

public class HelpCommand extends Command {

	public HelpCommand()
	{
		super("Help", new String[] {}, "How did you even manage that?", "", "Lists a bunch of commands to help you out!");
	}

	@Override
	public void onCommand(String command, String[] args, String message, User user, PrintStream stream) throws Exception
	{
		StringBuilder builder = new StringBuilder();
		builder.append("/---------------------------\n");
		builder.append("| \n");
		builder.append("| /info (name) - Gives you Info about the User\n");
		builder.append("| /nick (nick) - Changes your Name!\n");
		builder.append("| /hide - Un-/hides your rank!\n");
		builder.append("| /ban (name) - Bans the User\n");
		builder.append("| /kick (name) - Kicks the User\n");
		builder.append("| \n");
		builder.append("\\---------------------------");
		stream.println(builder.toString());
	}

}
