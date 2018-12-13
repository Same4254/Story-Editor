package StoryEditor.RightClickMenu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class RightClickMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	public RightClickMenu(String[] names, Lambda... functions) {
		for(int i = 0; i < names.length; i++) {
			JMenuItem menuItem = new JMenuItem(names[i]);
			Lambda function = functions[i];
			
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					function.run();
					RightClickMenu.this.setVisible(false);
				}
			});
		}
	}
	
	public static void main(final String args[]) {
	    JFrame frame = new JFrame("Popup Example");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	    final JTextField textField = new JTextField();
	    frame.add(textField, BorderLayout.NORTH);

	    final JPopupMenu popup = new JPopupMenu();
	    JMenuItem menuItem1 = new JMenuItem("Option 1");
	    popup.add(menuItem1);

	    JMenuItem menuItem2 = new JMenuItem("Option 2");
	    popup.add(menuItem2);

	    
	    ActionListener actionListener = new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	        popup.show(textField, 10,10);
	      }
	    };
	    
	    KeyStroke keystroke =
	      KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0, false);
	    textField.registerKeyboardAction(actionListener, keystroke,
	      JComponent.WHEN_FOCUSED);

	    frame.setSize(250, 150);
	    frame.setVisible(true);
	  }

}
