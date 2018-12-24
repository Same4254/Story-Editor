package FlowView;

import java.awt.Graphics2D;

public class Connection {
	private Vertex vertex1, vertex2;

	public Connection(Vertex vertex1, Vertex vertex2) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawLine(vertex1.getCenterX(), vertex1.getCenterY(), vertex2.getCenterX(), vertex2.getCenterY());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Connection) {
			Connection other = (Connection) obj;
			
			if(other.vertex1 == vertex1 && other.vertex2 == vertex2)
				return true;
		}
		
		return false;
	}
	
	public boolean contains(Element element) {
		if(element == getElement1() || element == getElement2()) 
			return true;
		return false;
	}
	
	public Element getElement1() { return vertex1.getElement(); }
	public Element getElement2() { return vertex2.getElement(); }

	public Vertex getVertex1() { return vertex1; }
	public Vertex getVertex2() { return vertex2; }
}
