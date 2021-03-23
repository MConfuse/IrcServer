package de.confuse.confFile;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A class that's usually created by the {@link ConfFileReader} but can also be
 * created manually for special use cases. <br>
 * <br>
 * This method allows for easy data retrieval, after scanning in the values
 * using the constructor, using the {@link #getValue(String)} method. All input
 * values have to follow the .CONFF formatting or else values cannot be
 * retrieved or only partially.
 * 
 * @version 1
 * @author Confuse/Confuse#5117
 *
 */
public class ConfResult {

	/** Represents the name of this Field */
	private final String name;
	/** A HashMap containing all values with their keys */
	private ConcurrentHashMap<String, String> values = new ConcurrentHashMap<String, String>();

	/**
	 * Will take the input values in the .CONFF format and put them into a
	 * {@link ConcurrentHashMap} for easy data retrieval using the
	 * {@link #getValue(String)} method.
	 * 
	 * @param name   The name of this field
	 * @param values A String in the .CONFF format
	 */
	public ConfResult(String name, String values)
	{
		this.name = name;
//		System.out.println(name);
//		System.out.println(values);

		String[] valueArray = values.split(",");

		// If there are more than at least one value it will split them up and add them
		// to the HashMap
		if (valueArray.length > 1)
			for (String string : valueArray)
			{
				String key = string.substring(string.indexOf("\"") + 1, string.lastIndexOf("\""));
				String val = string.substring(string.lastIndexOf("\"") + 1);
//				System.out.println(key + " == " + val);

				this.values.put(key, val);
			}
		else if (!values.equals("")) // Only one value
		{
			String key = values.substring(values.indexOf("\"") + 1, values.lastIndexOf("\""));
			String val = values.substring(values.lastIndexOf("\"") + 1);
//			System.out.println(key + " == " + val);

			this.values.put(key, val);
		}
		else // no value
			;

	}

	/**
	 * Returns a value using the values key. This method is case sensitive!
	 * 
	 * @param key Key (name) of the value
	 * @return String containing the value | Null
	 */
	public String getValue(String key)
	{
		String res = this.values.get(key);

		if (res != null)
		{
			return res;
		}
		else
			return null;
	}

	public ConcurrentHashMap<String, String> getValues()
	{
		return values;
	}

	public void setValues(ConcurrentHashMap<String, String> values)
	{
		this.values = values;
	}

	public String getName()
	{
		return name;
	}

}
