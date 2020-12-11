package net.aurora.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBase implements IDataBase {
	
	String Username = null;
	String Passwort = null;
	String Database = null;
	String Host = null;
	int Port = 3306;
	Connection con = null;

	public DataBase(String user, String pw, String Host, int port, String database)
	{
		this.Username = user;
		this.Passwort = pw;
		this.Host = Host;
		this.Port = port;
		this.Database = database;
		connect();
	}

	public void connect()
	{
		try
		{
			this.con = DriverManager.getConnection("jdbc:mysql://" + this.Host + ":" + this.Port + "/" + this.Database,
					this.Username, this.Passwort);

			PreparedStatement stmt = this.con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `Messages` ( `ID` INT NOT NULL AUTO_INCREMENT , `Date` DATE NOT NULL , `User` VARCHAR(25) NOT NULL , `msg` VARCHAR(500) NOT NULL , PRIMARY KEY  (`ID`)) ENGINE = InnoDB;");

			stmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}

	public boolean isConnected()
	{
		return (this.con != null);
	}

	public void disconnect()
	{
		if (isConnected())
		{
			try
			{
				this.con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
		}
		
	}

	public void query(String qry)
	{
		if (!isConnected())
			connect();

		try
		{
			PreparedStatement stmt = this.con.prepareStatement(qry);
			stmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
}
