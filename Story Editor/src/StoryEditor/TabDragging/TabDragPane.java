package StoryEditor.TabDragging;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JTabbedPane;

public class TabDragPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	
	private boolean dragging;
	private int dragIndex;
	
	public TabDragPane() {
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(!dragging) {
					int tabNumber = getUI().tabForCoordinate(TabDragPane.this, e.getX(), 10);
					
					if(tabNumber >= 0) {
						dragIndex = tabNumber;
						dragging = true;
					}
				} else {
					int tabNumber = getUI().tabForCoordinate(TabDragPane.this, e.getX(), 10);
					
					if(tabNumber < 0) {
						if(e.getPoint().x < 0)
							tabNumber = 0;
						else
							tabNumber = getTabCount() - 1;
					}
					
					if(tabNumber != dragIndex) {
						moveTab(dragIndex, tabNumber);
						dragIndex = tabNumber;
					}
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				dragging = false;
			}
		});
	}
	
	private void moveTab(int current, int desired) {
		Component component = getComponentAt(current);
		Component tabComponent = getTabComponentAt(current);
		String title = getTitleAt(current);
		removeTabAt(current);
		insertTab(title, null, component, null, desired);
		setTabComponentAt(desired, tabComponent);
		setSelectedIndex(desired);
	}
}
