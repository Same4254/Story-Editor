package Util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	public static BufferedImage readImage(String location) {
		try { return ImageIO.read(new File(location)); } 
		catch(IOException e) { e.printStackTrace(); }
		
		return null;
	}
	
	public static BufferedImage readImage(File file) {
		try { return ImageIO.read(file); } 
		catch(IOException e) { e.printStackTrace(); }
		
		return null;
	}
}
