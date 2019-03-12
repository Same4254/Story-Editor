package TextEditor;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.Position;

public class CommentPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextArea textArea;
	private JButton resolveButton;
	private Position startPosition, endPosition;
	
	public CommentPanel(Position startPosition, Position endPosition) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		
		textArea = new JTextArea();
		resolveButton = new JButton("Resolve");
		
		setLayout(new BorderLayout());
		add(textArea, BorderLayout.CENTER);
		add(resolveButton, BorderLayout.SOUTH);
	}
	
	public Position getStartPosition() { return startPosition; }
	public Position getEndPosition() { return endPosition; }
}
