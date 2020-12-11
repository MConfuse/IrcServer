package net.aurora.server;

/**
 * En-/decrypting algorithm for the in- and outgoing traffic.
 * 
 * @author Confuse/xXConfusedJenni#5117
 *         {@link https://github.com/MConfuse/IrcServer}
 * @version 1
 */
public class SikePackets {

	public SikePackets()
	{
		;
	}

	/**
	 * Encrypts the String given to the method.
	 * 
	 * @param input The string to encrypt
	 * @return Returns the encrypted String
	 */
	public static String encrypt(String input)
	{
		char[] chars = input.toCharArray();
		StringBuilder builder = new StringBuilder();

		int key = 5;
		int index = 0;
		for (char c : chars)
		{
			if (index == 1)
			{
				key += 19;
				index = 0;
			}
//			System.out.println(key + " " + c);

			c += key;
			builder.append(c);
			index++;
		}

		return builder.toString();
	}

	/**
	 * Decrypts the String given to the method.
	 * 
	 * @param input The encrypted String to be decrypted
	 * @return The decrypted String
	 */
	public static String decrypt(String input)
	{
		StringBuilder builder = new StringBuilder();

		int key = 5;
		int index = 0;
		char[] decrypt = input.toCharArray();
		for (char c : decrypt)
		{
			if (index == 1)
			{
				key += 19;
				index = 0;
			}
			System.out.println((int) c);

			c -= key;
			builder.append(c);
			index++;
		}

		return builder.toString();
	}

}
