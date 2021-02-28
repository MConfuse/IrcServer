package de.confuse;

public class Main {
	
	/** The Server instance */
	static Server server;
	
	// --- Timer ---
	public static long timerCooldown = 5000;
//	public static JSONObject jsonObject = null;

	public static void main(String[] args)
	{
//		try
//		{
//			jsonObject = (JSONObject) read("Config.json");
//		}
//		catch (Exception e2)
//		{
//			try
//			{
//				config("Config.json");
//			}
//			catch (Exception e1)
//			{
//
//				e1.printStackTrace();
//			}
//			
//		}
		
//		if (jsonObject != null)
		{
			long l = /*((Long) jsonObject.get("port")).longValue()*/ 1997;
			String host = "localhost";
			int port = (int) l;
			boolean debug = false;

			Runtime.getRuntime().addShutdownHook(new Thread() {
				
				public void run()
				{
					System.out.println(" ");
					System.out.println("   Shutting down...");
					System.out.println(" ");
					Main.server.getData().disconnect();
					Server.stop();
				}
				
			});

			try
			{
				server = new Server(host, port, debug);
				server.run();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}	// Server running first time
//		else
//		{
//			System.out.println("Config created restart pls!");
//			System.exit(0);
//		}
		
	}

//	public static void config(String filename) throws Exception
//	{
//		JSONObject sampleObject = new JSONObject();
//		sampleObject.put("port", Integer.valueOf(1997));
//		sampleObject.put("Show-IP", "true");
//		Files.write(Paths.get(filename, new String[0]), sampleObject.toJSONString().getBytes(),
//				new java.nio.file.OpenOption[0]);
//	}
//
//	public static Object read(String filename) throws Exception
//	{
//		FileReader reader = new FileReader(filename);
//		JSONParser jsonParser = new JSONParser();
//		return jsonParser.parse(reader);
//	}
	
}
