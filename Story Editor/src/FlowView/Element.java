package FlowView;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class Element extends JInternalFrame implements ComponentListener {
	private static final long serialVersionUID = 1L;

	private FlowView parent;
	
//	private JSplitPane splitPane;
	private JTextArea textArea;
	
//	private FlowView child;
	private Vertex[] verticies;
	
	public Element(FlowView parent) {
		super("Element", true, true, true);
		
		this.parent = parent;
		
//		splitPane = new JSplitPane();
//		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		textArea = new JTextArea();
//		child = new FlowView();
		
		JScrollPane scroll = new JScrollPane(); 
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setViewportView(textArea);
		
//		splitPane.setTopComponent(scroll);
//		splitPane.setBottomComponent(child);
//
//		splitPane.setDividerLocation(Integer.MAX_VALUE);
//		splitPane.setResizeWeight(1);
		
		verticies = new Vertex[8];
		for(int i = 0; i < verticies.length; i++)
			verticies[i] = new Vertex(this);
		
		addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
            	parent.removeConnections(Element.this);
            	parent.repaint();
            }
        });
		
		setSize(400, 400);
//		add(splitPane);
		add(textArea);
		addComponentListener(this);
		
		setVisible(true);
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		parent.repaint();
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
	
	public Vertex[] getVerticies() { return verticies; }
	public JTextArea getTextArea() { return textArea; }
}
