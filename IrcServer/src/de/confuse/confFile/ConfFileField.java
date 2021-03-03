package de.confuse.confFile;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a field. Within the .CONFF formatting a field is given
 * a name upon creation and contains all values associated with this name. <br>
 * Using this class will make it significantly easier to use the .CONFF
 * formatting and systems due to the easy to use method to add values with their
 * keys using the {@link #put(String, String)} method and retrieve a fully
 * formatted {@link String} using the {@link #getFormattedField()} method.
 * 
 * @version 1.1
 * @author Confuse/Confuse#5117
 *
 */
public class ConfFileField {

	/** The name of this field */
	private final String name;
	/** All keys and their values currently contained within this field */
	private List<String> content = new ArrayList<String>();

	/**
	 * Adds a new Field to the File. <br>
	 * Fields are an essential part of this System, they contain the actual values.
	 * <br>
	 * <br>
	 * Note: Having characters like the following ones within the key or value might
	 * cause the retrieval to fail or not return the correct values: "" , { }
	 * 
	 * @param name Name of the Field
	 */
	public ConfFileField(final String name)
	{
		this.name = name;
	}

	/**
	 * This method is used to easily add new values with their specific key.<br>
	 * This will also return this {@link ConfFileField}s Instance in case you want
	 * to add something else in one line.<br>
	 * <br>
	 * Note: Having characters like the following ones within the key or value might
	 * cause the retrieval to fail or not return the correct values: "" , { }
	 * 
	 * @param key   The key to retrieve the assigned value
	 * @param value The actual value that can be obtained using the assigned key
	 * @return This {@link ConfFileField}s instance
	 */
	public ConfFileField put(String key, String value)
	{
		content.add("\"" + key + "\"" + value);
		
		return this;
	}

	/**
	 * Method used by the {@link ConfFileWriter} to write the final file. Pretty
	 * much useless to the normal user.
	 * 
	 * @return String with the ConfFile formatting
	 */
	public String getFormattedField()
	{
		StringBuilder builder = new StringBuilder();

		// Creates the actual content of the field
		int index = 1;
		for (String string : content)
		{
			builder.append(string + (index == content.size() ? "" : ","));
			index++;
		}

		return name + "{" + builder.toString() + "};\r";
	}

}
