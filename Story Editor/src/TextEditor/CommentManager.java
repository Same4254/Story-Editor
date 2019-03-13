package TextEditor;

import java.util.ArrayList;

public class CommentManager {
	private PageEditor editor;
	private ArrayList<CommentPanel> comments;
	
	public CommentManager(PageEditor editor) {
		this.editor = editor;
		this.comments = new ArrayList<>();
	}
	
	public void justifyAllVisible() {
		for(CommentPanel comment : comments)
			if(comment.isVisible()) justify(comment);
	}
	
	/*
	 * Given a comment, justify the panel to the correct position on the screen
	 */
	public void justify(CommentPanel comment) {
		comment.setBounds((editor.indexToLocation(comment.getStartPosition().getOffset()).y + 
				         editor.indexToLocation(comment.getStartPosition().getOffset()).y) / 2, 
				editor.indexToLocation(comment.getStartPosition().getOffset()).y - editor.getTextPane().getVisibleRect().y,
				100, 100);
	}
	
	public void addComment(CommentPanel commentPanel) {
		comments.add(commentPanel);
	}
}
