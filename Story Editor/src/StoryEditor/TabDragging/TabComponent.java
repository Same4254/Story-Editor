package StoryEditor.TabDragging;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import TextEditor.ChapterEditor;

public class TabComponent	extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private JLabel label;
	private JButton closingButton;
	
	public TabComponent(ChapterEditor editor, String title) {
		this.title = title;
		
		setOpaque(false);
		
		label = new JLabel(title);
		closingButton = new JButton("X");
		
		closingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.removeTabAt(editor.indexOfTab(title));
			}
		});
		
		setLayout(new BorderLayout(0, 0));
		
		add(label, BorderLayout.WEST);
		add(closingButton, BorderLayout.EAST);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut, BorderLayout.CENTER);
	}
	
	public void setString(String title) { 
		this.title = title; 
		label.setText(title);
	}
	
	public String getTitle() { return title; }
}
