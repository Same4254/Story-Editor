package Story;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import StoryEditor.FileNode;
import StoryEditor.Window;

public class NewChapterFrame {
	private Window window;
	private JFrame frame;
	
	public NewChapterFrame(Window window, FileNode chaptersNode) {
		this.window = window;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Create a Chapter!");
		
		JPanel editPanel = new JPanel();
		frame.getContentPane().add(editPanel, BorderLayout.CENTER);
		GridBagLayout gbl_editPanel = new GridBagLayout();
		gbl_editPanel.columnWidths = new int[]{0, 0, 0};
		gbl_editPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_editPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_editPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		editPanel.setLayout(gbl_editPanel);
		
		JLabel titleLabel = new JLabel("Title: ");
		GridBagConstraints gbc_titleLabel = new GridBagConstraints();
		gbc_titleLabel.anchor = GridBagConstraints.EAST;
		gbc_titleLabel.insets = new Insets(0, 0, 5, 5);
		gbc_titleLabel.gridx = 0;
		gbc_titleLabel.gridy = 0;
		editPanel.add(titleLabel, gbc_titleLabel);
		
		JTextField titleField = new JTextField();
		GridBagConstraints gbc_titleField = new GridBagConstraints();
		gbc_titleField.insets = new Insets(0, 0, 5, 0);
		gbc_titleField.fill = GridBagConstraints.HORIZONTAL;
		gbc_titleField.gridx = 1;
		gbc_titleField.gridy = 0;
		editPanel.add(titleField, gbc_titleField);
		titleField.setColumns(10);
		
		JLabel summaryLabel = new JLabel("Summary: ");
		GridBagConstraints gbc_summaryLabel = new GridBagConstraints();
		gbc_summaryLabel.insets = new Insets(0, 0, 5, 5);
		gbc_summaryLabel.anchor = GridBagConstraints.EAST;
		gbc_summaryLabel.gridx = 0;
		gbc_summaryLabel.gridy = 2;
		editPanel.add(summaryLabel, gbc_summaryLabel);
		
		JTextArea summaryArea = new JTextArea();
		GridBagConstraints gbc_summaryArea = new GridBagConstraints();
		gbc_summaryArea.fill = GridBagConstraints.BOTH;
		gbc_summaryArea.gridx = 1;
		gbc_summaryArea.gridy = 3;
		editPanel.add(summaryArea, gbc_summaryArea);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		buttonPanel.add(cancelButton, BorderLayout.WEST);
		
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Chapter chapter = new Chapter(chaptersNode.getFile());
				chapter.setTitle(titleField.getText());
				chapter.setSummary(summaryArea.getText());
				chapter.save();
				
				frame.dispose();
				window.getFileTreePanel().reExplore(chaptersNode);
			}
		});
		buttonPanel.add(confirmButton, BorderLayout.EAST);
		
		frame.setSize(400, 400);
		frame.setVisible(true);
	}
}
