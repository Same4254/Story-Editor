package StoryEditor.RightClickMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class RightClickMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	public RightClickMenu() {
		super();
	}
	
	/*
	 * This will set the menu items to be the following labels with the corresponding function
	 * Clears the menu of all items and sets it to only these
	 */
	public void setMenuItems(LambdaMenuItem... items) {
		int numberOfItems = getComponentCount();
		for(int i = numberOfItems - 1; i >= 0; i--)
			remove(i);
		addMenuItems(items);
	}
	
	/*
	 * Adds menu functions with the given labels and functions
	 */
	public void addMenuItems(LambdaMenuItem... items) {
		for(int i = 0; i < items.length; i++) {
			JMenuItem menuItem = new JMenuItem(items[i].getName());
			Lambda function = items[i].getLambda();
			
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					function.run();
					RightClickMenu.this.setVisible(false);
				}
			});
			
			add(menuItem);
		}
	}
}
