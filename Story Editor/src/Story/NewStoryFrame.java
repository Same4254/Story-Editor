package Story;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Story.Story.Rating;
import StoryEditor.FileNode;
import StoryEditor.Window;

public class NewStoryFrame {
	private Window window;
	private JFrame frame;
	
	public NewStoryFrame(Window window) {
		this.window = window;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Create a Story!");
		
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
		
		JLabel ratingLabel = new JLabel("Rating: ");
		GridBagConstraints gbc_ratingLabel = new GridBagConstraints();
		gbc_ratingLabel.anchor = GridBagConstraints.EAST;
		gbc_ratingLabel.insets = new Insets(0, 0, 5, 5);
		gbc_ratingLabel.gridx = 0;
		gbc_ratingLabel.gridy = 1;
		editPanel.add(ratingLabel, gbc_ratingLabel);
		
		JComboBox<Rating> ratingBox = new JComboBox<>();
		for(Rating rating : Story.Rating.values())
			ratingBox.addItem(rating);
		
		GridBagConstraints gbc_ratingBox = new GridBagConstraints();
		gbc_ratingBox.insets = new Insets(0, 0, 5, 0);
		gbc_ratingBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_ratingBox.gridx = 1;
		gbc_ratingBox.gridy = 1;
		editPanel.add(ratingBox, gbc_ratingBox);
		
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
				Story story = new Story();
				story.setTitle(titleField.getText());
				story.setRating((Rating) ratingBox.getSelectedItem());
				story.setSummary(summaryArea.getText());
				story.save();
				
				frame.dispose();
				window.getFileTreePanel().exploreFiles(new File(Story.saveLocation + "/" + story.getTitle()));
			}
		});
		buttonPanel.add(confirmButton, BorderLayout.EAST);
		
		frame.setSize(400, 400);
		frame.setVisible(true);
	}
}
