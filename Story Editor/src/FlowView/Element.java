package FlowView;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class Element extends JInternalFrame implements ComponentListener {
	private static final long serialVersionUID = 1L;

	private FlowView parent;
	
	private JSplitPane splitPane;
	private JTextArea textArea;
	
	private FlowView child;
	
	public Element(FlowView parent) {
		super("Element", true, true, true);
		
		this.parent = parent;
		
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		textArea = new JTextArea();
		child = new FlowView();
		
		splitPane.setTopComponent(textArea);
		splitPane.setBottomComponent(child);
		
		setSize(400, 400);
		add(splitPane);
		addComponentListener(this);
		
		setVisible(true);
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		parent.repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {
	
	}
	
	public JTextArea getTextArea() { return textArea; }
}
