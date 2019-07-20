package TextEditor.Comments;

import java.util.ArrayList;

import TextEditor.PageEditor;

public class CommentManager {
	private PageEditor editor;
	private ArrayList<CommentPanel> comments;
	
	/*
	 * It may be necessary to loop through all the comments. 
	 * Rather than loop through all the attribute sets, this manager will keep track of all the comment panels.
	 * In addition, this class will describe the placement of the panel on the screen.
	 */
	public CommentManager(PageEditor editor) {
		this.editor = editor;
		this.comments = new ArrayList<>();
	}
	
	/*
	 * This method will position the comment panels accordingly. 
	 * However, it is not necessary to run this calculation when the panel is not visible. 
	 * Then again, this means that a panel will neeed to be justified if it turns visible without a resize call to the entire panel.
	 * If that is the case, call the justify function, which takes in a single panel and positions it accordingly.
	 * This function calls the justify method, and therefore will have the same effect
	 */
	public void justifyAllVisible() {
		for(CommentPanel comment : comments)
			if(comment.isVisible()) justify(comment);
	}
	
	/*
	 * Given a comment panel, justify the panel to the correct position on the screen
	 */
	public void justify(CommentPanel comment) {
		comment.setBounds((editor.indexToLocation(comment.getStartPosition().getOffset()).y + 
				         editor.indexToLocation(comment.getStartPosition().getOffset()).y) / 2, 
				editor.indexToLocation(comment.getStartPosition().getOffset()).y - editor.getTextPane().getVisibleRect().y,
				100, 100);
	}
	
	public void addComment(CommentPanel commentPanel) { comments.add(commentPanel); }
	public void removeComment(CommentPanel commentPanel) { comments.remove(commentPanel); }
}
