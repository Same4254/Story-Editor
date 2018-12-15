package StoryEditor.RightClickMenu;

public class LambdaMenuItem {
	private String name;
	private Lambda lambda;
	
	public LambdaMenuItem(String name, Lambda lambda) {
		this.name = name;
		this.lambda = lambda;
	}
	
	public String getName() { return name; }
	public Lambda getLambda() { return lambda; }
	
	public void setName(String name) { this.name = name; } 
}
