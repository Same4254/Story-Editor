package FlowView;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.xml.parsers.DocumentBuilder;

import StoryEditor.RightClickMenu.LambdaMenuItem;
import StoryEditor.RightClickMenu.RightClickMenu;

public class FlowView extends JDesktopPane implements MouseInputListener {
	private static final long serialVersionUID = 1L;

	private RightClickMenu rightClickMenu;
	private ArrayList<Connection> connections;
	
	//Temporary variables for planning a new connection 
	private Point tempDrawOrigin;
	private Point tempDrawCurrent;
	private Vertex tempVertex;
	private boolean tempDrawing;
	
	//Temporary variables for spanning camera
	private boolean dragging;
	private Point lastDrag;
	
	private File file;
	
	/*
	 * This View will allow the user to plan out what they want to achieve in the story or chapter. 
	 * Essentially, there will be several boxes that the user can move and resize (possibly lock to a grid? Option?). 
	 * They should be able to type in these shapes. Obviously, if the text is to long, scroll pane if it has keyboard focus, 
	 * 		otherwise don't render the scroll bar.
	 * They should be able to click and drag the shapes to what they desire
	 * 		- Brings an issue of z ordering. Simple/Inefficient Solution: Use an arrayList to keep z-order?
	 * There becomes an issue of rendering, the elements have reference to other elements and should draw lines to them,
	 * 		but there needs to be a way to differentiate was has already been rendered so that the same thing isn't rendered twice
	 * 
	 * This shouldn't be screen size dependent, the user should be able to move around the environment
	 * 		- Only render what is actually on the screen.
	 * 
	 * They can also connect these boxes with curved line (bezier?). How will these connect? (certain lock points?)
	 * 		- The lines should be as curvey as the user desires: dynamically add control points to the curve 
	 * Change colors of lines and borders of the shapes (maybe a filter function to gray all colors except for a select few?)
	 * 
	 * Maybe be able to edit a file that directly correlates to that one shape of detail? 
	 * Or rather maybe be able to highlight what paragraphs pertain to what element in the flow view?
	 * Make this an option? Flow highlight mode...? Needs a better name. This entire thing needs a better name.
	 */
	public FlowView(File file) {
		this.file = file;
		
		connections = new ArrayList<>();
		
		rightClickMenu = new RightClickMenu();
		
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	
	public void save() {
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(file);
		} catch(IOException e) {
			e.printStackTrace();
		}

		for(Component c : getComponents()) {
			if(c instanceof Element) {
				Element element = (Element) c;
				
				String text = element.getTextArea().getText();
			}
		}
		
		writer.close();
	}
	
	public void readFile() {
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		for(Component c : getComponents()) {//Draw Vertices
			if(c instanceof Element) {
				Element element = (Element) c;
				Vertex[] verticies = element.getVerticies();
				
				for(int i = 0; i < verticies.length; i++)
					verticies[i].draw(g2d, i);//Include index so the vertex knows where to draw relative to the element
			}
		}
		
		for(Connection c : connections)//Draw the connections
			c.draw(g2d);
		
		if(tempDrawing) {//If the user is planning a new connection, draw it following the mouse
			g2d.drawLine((int) tempDrawOrigin.getX(), (int) tempDrawOrigin.getY(), 
						 (int) tempDrawCurrent.getX(), (int) tempDrawCurrent.getY());
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(tempDrawing) {//If the user is planning a new connection, then store the mouse location
			tempDrawCurrent = e.getPoint();//so repaint knows where the mouse is
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(dragging) {//If the user is spanning the "camera" then don't bring up the right click menu
			dragging = false;
			lastDrag = null;
			return;
		}
		
		if(SwingUtilities.isLeftMouseButton(e) && tempDrawing) {//Assign a new connection if a new one is being planned
			for(Component c : getComponents()) {
				if(c instanceof Element) {
					Element element = (Element) c;
					for(Vertex v : element.getVerticies()) {//See if the mouse collides with any vertices
						if(v.getRectangle().contains(e.getPoint())) {
							Connection connection = new Connection(tempVertex, v);
							
							if(connection.getElement1() != connection.getElement2() && !connections.contains(connection))
								connections.add(connection);//Can't make a connection to the same element
							
							tempVertex = null;
							tempDrawing = false;
							repaint();
							break;
						}
					}
				}
			}
		}
		
		/*
		 * Find where the mouse is right clicking
		 * and create a right click menu for what it is on
		 */
		if(SwingUtilities.isRightMouseButton(e) && !tempDrawing) {
			boolean onVertex = false;
			
			/*
			 * Determine if the cursor is on a vertex
			 * If so, allow the user to plan a new connection
			 */
			out:
			for(Component c : getComponents()) {
				if(c instanceof Element) {
					Element element = (Element) c;
					
					for(Vertex v : element.getVerticies()) {
						if(v.getRectangle().contains(e.getPoint())) {
							onVertex = true;
							
							rightClickMenu.setMenuItems(
								new LambdaMenuItem("New Connection", () -> {
									tempDrawOrigin = new Point(v.getCenterX(), v.getCenterY());
									tempDrawCurrent = e.getPoint();
									tempVertex = v;
									tempDrawing = true;
								})
							);
							
							break out;
						}
					}
					
				}
			}
			
			/*
			 * Not on a vertex
			 * Allow the user to create a new element if they wish
			 */
			if(!onVertex)
				rightClickMenu.setMenuItems(
						new LambdaMenuItem("New Element", () -> {
							Element element = new Element(FlowView.this);
							element.setSize(FlowView.this.getWidth() / 4, FlowView.this.getHeight() / 4);
							element.setLocation(e.getPoint());
							
							FlowView.this.add(element, 0);
						})
					);
			
			rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	/*
	 * Remove any connections related to this element
	 */
	public void removeConnections(Element element) {
		for(int i = connections.size() - 1; i >= 0; i--) 
			if(connections.get(i).contains(element))
				connections.remove(i);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		/*
		 * Span the camera if a drag with right click is happening
		 */
		if(SwingUtilities.isRightMouseButton(e)) {
			dragging = true;
			if(lastDrag == null)//The variable needs a starting point
				lastDrag = e.getPoint();
			
			for(Component c : getComponents()) {//Translate Components
				Point current = c.getLocation();
				current.translate((int) (e.getX() - lastDrag.getX()), (int) (e.getY() - lastDrag.getY()));
				c.setLocation(current);
			}
			
			lastDrag = e.getPoint();//Remember where the mouse was
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setTitle("Flow");
		
		FlowView flow = new FlowView(null);
		
		frame.add(flow);
		frame.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
}