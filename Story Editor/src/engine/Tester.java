package engine;

import java.awt.Color;

import engine.logger.Logger;
import engine.logger.LoggingInfo;

public class Tester {
	public static void main(String[] args) {
		Logger.setTitle("Best Game Ever");
		
		Logger.show();
		
		Logger.setClosingMethod(new Tester(), "close");
		
		Logger.addLoggingType(new LoggingInfo("Input", new Color(0, 150, 0), "Input"));
		Logger.addLoggingType(new LoggingInfo("Error", Color.RED, "ERROR"));
		Logger.addLoggingType(new LoggingInfo("Info", new Color(200, 200, 0), ""));
		
		Logger.log("Enter your name: ", "info");
		String name = Logger.getInput();
		Logger.log(name + " is to cool", "error");
	}
	
	public void close() {
		System.out.println("Goodbye");
	}
}
