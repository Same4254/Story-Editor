package Story;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Character {
	private Story story;
	
	private ArrayList<BufferedImage> images;
	private String name, bio;
	
	public Character(Story story) {
		this.story = story;
		
		this.images = new ArrayList<>();
		this.name = "";
		this.bio = "";
	}
	
	public void write() {
		
	}
	
	public void read(File file) {
		
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getBio() { return bio; }
	public void setBio(String bio) { this.bio = bio; }
	
	public ArrayList<BufferedImage> getImages() { return images; }
	public void addImage(BufferedImage image) { images.add(image); }
}
