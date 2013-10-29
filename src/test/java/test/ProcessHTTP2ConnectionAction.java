package test;

public class ProcessHTTP2ConnectionAction {
	/**
	 * Note : Messages from the HTTP endpoint come from the local bank
	 */
	public String process(String message) {
		String retMessage = "error";
		System.out.println(message);
		
		if (!message.equals("/favicon.ico")) {
			if (message.equals("/connectBob")) {
				retMessage = "connect_BobBank";
				System.out.println("CONNECTION: Bob");
			} else if (message.equals("/connectAlice")) {
				retMessage = "connect_AliceBank";
				System.out.println("CONNECTION: Alice");
			} else if (message.equals("/release")) {
				retMessage = "release";
				System.out.println("RELEASE CONNECTION");
			} else {					
				System.out.println("CONNECTION WS: Error, wrong path");
			}
		}		
		return retMessage;
	}
	
}
