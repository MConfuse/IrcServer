package de.confuse.confFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A new type of File writer specifically for .CONFF files. <br>
 * This writer will take all added {@link ConfFileField}s and create a new File
 * following the .CONFF formatting.
 * 
 * @version 1
 * @author Confuse/xXConfusedJenni#5117
 *
 */
public class ConfFileWriter {

	/** The target file which the System will use to save the data in */
	private File file;
	/** A list containing all fields */
	private List<ConfFileField> fields = new ArrayList<ConfFileField>();
	/** Whether or not .CONFF should be added to the end of the saved File */
	private final boolean fileType;

	/**
	 * Creates a new {@link ConfFileWriter} object that is needed to write a file in
	 * the .CONFF file format.
	 * 
	 * @param file        The file where the ConfFile should be written to
	 * @param addFileType Whether or not .CONFF should be added at the end of the
	 *                    saved file
	 */
	public ConfFileWriter(File file, final boolean addFileType)
	{
		this.file = file;
		this.fileType = addFileType;
	}

	/**
	 * Method to write all added {@link ConfFileField}s to the {@link File} that was
	 * specified in the constructor ({@link #ConfFileWriter(File)}).
	 * 
	 * @throws IOException This will only be thrown when there was an error
	 *                     creating/writing to the file specified in the constructor
	 */
	public void writeFile() throws IOException
	{
		PrintWriter writer = new PrintWriter(new FileWriter(file.getAbsolutePath() + (fileType ? ".conff" : "")));

		for (ConfFileField field : fields)
		{
			writer.append(field.getFormattedField());
		}

		writer.close();
	}

	/**
	 * Used to add {@link ConfFileField}s to this {@link ConfFileWriter}s instance.
	 * Adding {@link ConfFileField}s is necessary to write the data to the file
	 * later on, using the {@link #writeFile()} method.
	 * 
	 * @param fields All {@link ConfFileField}s that should be added
	 */
	public void addFields(ConfFileField... fields)
	{
		this.fields.addAll(Arrays.asList(fields));
	}

}
