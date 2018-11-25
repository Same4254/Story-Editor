package engine.logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.HashMap;

import javax.swing.ImageIcon;

import engine.frame.Window;

public class Logger {
	private static final Logger instance = new Logger();
	private static final Object sync = new Object();
	
	private ArrayDeque<String> inputLog;
	private HashMap<String, LoggingInfo> loggingMap;
	
	private Window window;
	private Method closeMethod;
	private Object closeObj;
	
	
	private Logger() {
		inputLog = new ArrayDeque<>();
		loggingMap = new HashMap<>();
		window = new Window(this);
	}
	
	public void addInput(String input) {
		inputLog.push(input);
		loggingMap.get("input").addToWindow(window, input);
		synchronized (sync) { sync.notifyAll(); }
	}
	
	public void closeLogger() {
		try { closeMethod.invoke(closeObj); } 
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
//	------------------------------------------------ Static ----------------------------------------------------- \\
	
	public static void addLoggingType(LoggingInfo info) {
		instance.loggingMap.put(info.getType(), info);
	}
	
	public static boolean hasNext() { return !instance.inputLog.isEmpty(); }
	
	public synchronized static String getInput() {
		if(instance.inputLog.isEmpty()) {
			try { synchronized (sync) { sync.wait(); } } 
			catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		return instance.inputLog.pop();
	}
	
	public static void log(String text, String type) {
		instance.loggingMap.get(type.toLowerCase()).addToWindow(instance.window, text);
	}
	
	public static void show() {
		instance.window.setVisible(true);
	}
	
	public static void setClosingMethod(Object obj, String methodName) {
		instance.closeObj = obj;
		try { instance.closeMethod = obj.getClass().getMethod(methodName); } 
		catch (NoSuchMethodException | SecurityException e) { e.printStackTrace(); }
	}
	
	public static void setTitle(String title) {
		instance.window.setTitle(title);
	}
	
	public static void setIcon(String path) {
		instance.window.setIconImage(new ImageIcon(instance.getClass().getResource(path)).getImage());
	}
	
	public static void setTitleImage(String path) {
		instance.window.setTitleImage(instance.getClass().getResource(path));
	}
	
	public static void close() {
		instance.closeLogger();
		instance.window.setVisible(false);
		instance.window.dispose();
	}
}
