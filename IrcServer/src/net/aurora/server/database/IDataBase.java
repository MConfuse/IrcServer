package net.aurora.server.database;

public interface IDataBase {

	void connect();

	boolean isConnected();

	void disconnect();

	void query(String paramString);
}
