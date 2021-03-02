package de.confuse.command.commands;

import java.io.PrintStream;

import de.confuse.User;
import de.confuse.command.Command;

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
		builder.append("| IRC by Confuse\n");
		builder.append("| \n");
		builder.append("| /uname - Updates your IRC name\n");
		builder.append("| \n");
		builder.append("|------ Staff Commands -----\n");
		builder.append("| /info (name) - Gives you Info about the User\n");
		builder.append("| /nick (nick) - Changes your Name!\n");
		builder.append("| /hide - Un-/hides your rank!\n");
		builder.append("| /ban (name) - Bans the User\n");
		builder.append("| /kick (name) - Kicks the User\n");
		builder.append("| \n");
		builder.append("\\---------------------------");
		
//		String output = SikePackets.encrypt(builder.toString());
		stream.println(builder);
	}

}
