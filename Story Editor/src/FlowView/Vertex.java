package FlowView;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Vertex {
	public static final int offSetX = 10, offSetY = 10;
	public static final int width = 5, height = 5;
	
	private Element element;
	private Rectangle rectangle;
	
	private int centerX, centerY;
	
	public Vertex(Element element) {
		this.element = element;
		rectangle = new Rectangle();
	}

	public void draw(Graphics2D g2d, int index) {
		int[] coords = getLocation(index);
		
		g2d.fillRect(coords[0], coords[1], width, height);
		rectangle.setBounds(coords[0], coords[1], width, height);
		
		centerX = coords[0] + (width / 2);
		centerY = coords[1] + (height / 2);
	}
	
	private int[] getLocation(int index) {
		int x = element.getX();
		int y = element.getY();
		int width = element.getWidth();
		int height = element.getHeight();
		
		int[] toRet = new int[2];
		
		if(index == 0) {//Top Left
			toRet[0] = x - offSetX;
			toRet[1] = y - offSetY;
		} else if(index == 1) {//Top Middle
			toRet[0] = x + (width / 2) - (Vertex.width / 2);
			toRet[1] = y - offSetY;
		} else if(index == 2) {//Top Right
			toRet[0] = x + width + offSetX - Vertex.width;
			toRet[1] = y - offSetY;
		} else if(index == 3) {//Middle Right
			toRet[0] = x + width + offSetX - Vertex.width;
			toRet[1] = y + (height / 2) - (Vertex.height / 2);
		} else if(index == 4) {//Bottom Right
			toRet[0] = x + width + offSetX - Vertex.width;
			toRet[1] = y + offSetY + height - Vertex.height;
		} else if(index == 5) {//Bottom Middle
			toRet[0] = x + (width / 2) - (Vertex.width / 2);
			toRet[1] = y + offSetY + height - Vertex.height;
		} else if(index == 6) {//Bottom Left
			toRet[0] = x - offSetX;
			toRet[1] = y + offSetY + height - Vertex.height;
		} else if(index == 7) {//Middle Left
			toRet[0] = x - offSetX;
			toRet[1] = y + (height / 2) - (Vertex.height / 2);
		}
		
		return toRet;
	}
	
	public Rectangle getRectangle() { return rectangle; }
	
	public int getCenterX() { return centerX; }
	public int getCenterY() { return centerY; }
	
	public Element getElement() { return element; }
}
