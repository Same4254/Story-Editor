package FlowView;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import StoryEditor.RightClickMenu.LambdaMenuItem;
import StoryEditor.RightClickMenu.RightClickMenu;

public class FlowView extends JDesktopPane {
	private static final long serialVersionUID = 1L;

	private RightClickMenu rightClickMenu;
	private ArrayList<Connection> connections;
	
	private Point tempDrawOrigin;
	private boolean tempDrawing;
	
	/*
	 * This View will allow the user to plan out what they want to achieve in the story or chapter. 
	 * Essentially, there will be several boxes that the user can move and resize (possibly lock to a grid? Option?). 
	 * They should be able to type in these shapes. Obviously, if the text is to long, scroll pane if it has keyboard focus, 
	 * 		otherwise don't render the scroll bar.
	 * They should be able to click and drag the shapes to what they desire
	 * 		- Brings an issue of z ordering. Simple/Inefficient Solution: Use an arrayList to keep z-order?
	 * There becomes an issue of rendering, the elments have reference to other elements and should draw lines to them,
	 * 		but there needs to be a way to differentiate was has already been rendered so that the same thing isn't rendered twice
	 * 
	 * This shouldn't be screen size dependent, the user should be able to move around the environement
	 * 		- Only render what is actually on the screen.
	 * 
	 * They can also connect these boxes with curved line (bezier?). How will these connect? (certain lock points?)
	 * 		- The lines should be as curvy as the user desires: dynamically add control points to the curve 
	 * Change colors of lines and borders of the shapes (maybe a filter function to gray all colors except for a select few?)
	 * 
	 * Maybe be able to edit a file that directly correlates to that one shpe of detail? 
	 * Or rather maybe be able to highlight what paragraphs pertain to what element in the flow view?
	 * Make this an option? Flow highlight mode...? Needs a better name. This entire thing needs a better name.
	 */
	public FlowView() {
		connections = new ArrayList<>();
		
		rightClickMenu = new RightClickMenu();
		rightClickMenu.setMenuItems(
			new LambdaMenuItem("New Element", () -> {
				FlowView.this.add(new Element(FlowView.this), 0);
			})
		);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				
				if(SwingUtilities.isRightMouseButton(e)) {
					boolean onVertex = false;
					
					for(Component c : getComponents()) {
						if(c instanceof Element) {
							Element element = (Element) c;
							
							for(Vertex v : element.getVerticies()) {
								if(v.getRectangle().contains(e.getPoint())) {
									onVertex = true;
									
									rightClickMenu.setMenuItems(
										new LambdaMenuItem("New Connection", () -> {
											tempDrawOrigin = new Point(v.getCenterX(), v.getCenterY());
										})
									);
									
									break;
								}
							}
						}
					}
					
					rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		for(Component c : getComponents()) {
			if(c instanceof Element) {
				Element element = (Element) c;
				Vertex[] verticies = element.getVerticies();
				
				for(int i = 0; i < verticies.length; i++)
					verticies[i].draw(g2d, i);
			}
		}
		
		for(Connection c : connections)
			c.draw(g2d);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setTitle("Flow");
		
		FlowView flow = new FlowView();
		
		Element e1 = new Element(flow);
		Element e2 = new Element(flow);
		
		flow.add(e1);
		flow.add(e2);

		flow.connections.add(new Connection(e1.getVerticies()[0], e2.getVerticies()[2]));
		
		frame.add(flow);
		frame.setVisible(true);
	}
}