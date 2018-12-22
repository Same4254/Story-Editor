package FlowView;

public class Connection {
	private Vertex vertex1, vertex2;

	public Connection(Vertex vertex1, Vertex vertex2) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
	}
	
	public Element getElement1() { return vertex1.getElement(); }
	public Element getElement2() { return vertex2.getElement(); }

	public Vertex getVertex1() { return vertex1; }
	public Vertex getVertex2() { return vertex2; }
}
