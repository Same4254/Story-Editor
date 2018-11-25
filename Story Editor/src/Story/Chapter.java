package Story;

public class Chapter {
	private String name;
	private String readerSummary;
	
	public Chapter() {
		this.name = "";
		this.readerSummary = "";
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getReaderSummary() { return readerSummary; }
	public void setReaderSummary(String readerSummary) { this.readerSummary = readerSummary; }
}