package engine.logger;

import java.awt.Color;

import engine.frame.Window;

public class LoggingInfo {
	private String type;
	private Color color;
	private String heading;
	
	public LoggingInfo(String type, Color color, String heading) {
		this.type = type.toLowerCase();
		this.color = color;
		this.heading = heading == null || heading.length() > 0 ? heading : null;
	}
	
	public void addToWindow(Window window, String text) {
		window.addString((heading != null ? "[" + heading + "] " : "") + text, color);
	}

	public String getType() { return type; }
	public Color getColor() { return color; }
	public String getHeading() { return heading; }
}
