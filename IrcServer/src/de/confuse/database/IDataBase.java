package de.confuse.database;

public interface IDataBase {

	void connect();

	boolean isConnected();

	void disconnect();

	void query(String paramString);
}
