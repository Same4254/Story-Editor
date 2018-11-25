package Story;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Character {
	private String name, bio;
	
	private ArrayList<BufferedImage> images;
	
	public Character() {
		this.images = new ArrayList<>();
		this.name = "";
		this.bio = "";
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getBio() { return bio; }
	public void setBio(String bio) { this.bio = bio; }
	
	public ArrayList<BufferedImage> getImages() { return images; }
}
