package Story;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Story {
	public static enum Rating {
		Teen("Teen"), Everyone("Everyone"), Unrated("Unrated");
		
		private String name;
		private Rating(String name) {
			this.name = name;
		}
	
		public String toString() { return this.name; }
		public String getName() { return this.name; }
	}
	
	public static String saveLocation = "res/Books";
	
	private String title, summary;
	private Rating rating;
	
	private File directory, storyFile;
	
	public Story() {
		this.title = "";
		this.summary = "";
		this.rating = Rating.Unrated;
	}
	
	public Story(File directory) { 
		initFiles(directory);
	}
	
	private void initFiles(File directory) {
		this.directory = directory;
		
		File chapterFolder = new File(directory.getPath() + "/Chapters");
		File characterFolder = new File(directory.getPath() + "/Characters");
		storyFile = new File(directory.getPath() + "/" + directory.getName() + ".stry");
		
		if(!directory.exists())
			directory.mkdir();
		if(!chapterFolder.exists())
			chapterFolder.mkdir();
		if(!characterFolder.exists())
			characterFolder.mkdir();
		if(!storyFile.exists())
			try {
				storyFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		read(storyFile);
	}
	
	/*
	 * Name
	 * Rating
	 * Summary
	 */
	private void read(File file) {
		Scanner sc = null;
		
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(!sc.hasNextLine())
			return;
		
		this.title = sc.nextLine();
		this.rating = Rating.valueOf(sc.nextLine());
		
		StringBuilder builder = new StringBuilder();
		while(sc.hasNextLine())
			builder.append(sc.nextLine());
		this.summary = builder.toString();
		
		sc.close();
	}
	
	public void save() {
		if(storyFile == null)
			initFiles(new File(saveLocation + "/" + title));
		
		if(!storyFile.exists())
			try {
				storyFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(storyFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		writer.println(this.title);
		writer.println(this.rating);
		writer.println(this.summary);
		
		writer.close();
	}
	
	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getSummary() { return this.summary; }
	public void setSummary(String summary) { this.summary = summary; }
	
	public Rating getRating() { return this.rating; }
	public void setRating(Rating rating) { this.rating = rating; }
	
//	public static void main(String[] args) {
//		Story story = new Story(new File("res/Books/Book 3"));
//		story.setRating(Rating.Teen);
//		story.setSummary("Things and stuff");
//		
////		story.save();
//	}
}
