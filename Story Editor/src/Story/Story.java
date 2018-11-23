package Story;

import java.util.ArrayList;

public class Story {
	private String title, author;
	private String summary;
	
	private ArrayList<Character> characters;
	
	public Story() {
		characters = new ArrayList<>();
	}

	public ArrayList<Character> getCharacters() { return characters; }
}
