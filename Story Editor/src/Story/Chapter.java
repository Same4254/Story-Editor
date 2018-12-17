package Story;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Chapter {
	private File chaptersFolder;
	
	private String title, summary;
	
	public Chapter(File chaptersFolder) {
		this.chaptersFolder = chaptersFolder;
	}
		
	public void save() {
		File chapterFolder = new File(chaptersFolder.getPath() + "/" + title);
		if(!chapterFolder.exists())
			chapterFolder.mkdir();
		
		File chapterMetaData = new File(chapterFolder.getPath() + "/" + title + "-Meta");
		if(!chapterMetaData.exists())
			try {
				chapterMetaData.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
		
		File chapterContent = new File(chapterFolder.getPath() + "/" + title + ".chp");
		if(!chapterContent.exists())
			try {
				chapterContent.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(chapterMetaData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		writer.println(title);
		writer.println(summary);
		
		writer.close();
	}
	
	public void read(File file) {
		Scanner sc = null;
		
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(!sc.hasNextLine())
			return;
		
		title = sc.nextLine();
		
		StringBuilder builder = new StringBuilder();
		while(sc.hasNextLine())
			builder.append(sc.nextLine());
		
		summary = builder.toString();
		
		sc.close();
	}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getSummary() { return summary; }
	public void setSummary(String summary) { this.summary = summary; }
}
