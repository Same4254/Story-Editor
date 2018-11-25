package Story;

import java.util.ArrayList;

public class Story {
	private String title, author;
	private ArrayList<Character> characters;
	
	public Story() {
		this.characters = new ArrayList<>();
		this.title = "";
		this.author = "";
	}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getAuthor() { return author; }
	public void setAuthor(String author) { this.author = author; } 

	public ArrayList<Character> getCharacters() { return characters; }
}
