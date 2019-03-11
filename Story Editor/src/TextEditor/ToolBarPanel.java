package TextEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.text.StyleConstants.CharacterConstants;

public class ToolBarPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JComboBox<String> fontFamilies;
	private JComboBox<Integer> fontSizes;
	private JToggleButton boldSwitch, italicSwitch, underlineSwitch;
	private JButton saveButton;
	
	public ToolBarPanel(PageEditor editor) {
		fontFamilies = new JComboBox<>();
        fontFamilies.setFocusable(false);
        
        fontFamilies.addItem("Comic Sans MS");
        fontFamilies.addItem("Courier New");
        
        fontFamilies.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					editor.setCharacterAttribute(CharacterConstants.FontFamily, fontFamilies.getSelectedItem());
				}
			}
		});
        editor.setCharacterAttribute(CharacterConstants.FontFamily, fontFamilies.getSelectedItem());
        
        add(fontFamilies);
        
        fontSizes = new JComboBox<>();
        fontSizes.setFocusable(false);
        
        fontSizes.addItem(12);
        fontSizes.addItem(14);
        fontSizes.addItem(16);
        fontSizes.addItem(18);
        fontSizes.addItem(32);
        
        fontSizes.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					editor.setCharacterAttribute(CharacterConstants.FontSize, fontSizes.getSelectedItem());
				}
			}
		});
        editor.setCharacterAttribute(CharacterConstants.FontSize, fontSizes.getSelectedItem());
        
        add(fontSizes);
        
        boldSwitch = new JToggleButton("Bold");
        boldSwitch.setFocusable(false);
        boldSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.setCharacterAttribute(CharacterConstants.Bold, (Object)boldSwitch.isSelected());
			}
		});
        editor.setCharacterAttribute(CharacterConstants.Bold, (Object)boldSwitch.isSelected());
        
        add(boldSwitch);
        
        italicSwitch = new JToggleButton("Italics");
        italicSwitch.setFocusable(false);
        italicSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.setCharacterAttribute(CharacterConstants.Italic, (Object)italicSwitch.isSelected());
			}
		});
        editor.setCharacterAttribute(CharacterConstants.Italic, (Object)italicSwitch.isSelected());
        
        add(italicSwitch);
        
        underlineSwitch = new JToggleButton("Underline");
        underlineSwitch.setFocusable(false);
        underlineSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.setCharacterAttribute(CharacterConstants.Underline, (Object)underlineSwitch.isSelected());
			}
		});
        editor.setCharacterAttribute(CharacterConstants.Underline, (Object)underlineSwitch.isSelected());
        
        add(underlineSwitch);
        
        saveButton = new JButton("Save");
        saveButton.setFocusable(false);
        
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.save();
			}
		});
        
        add(saveButton);
	}

	public JComboBox<String> getFontFamilies() { return fontFamilies; }
	public JComboBox<Integer> getFontSizes() { return fontSizes; }
	public JToggleButton getBoldSwitch() { return boldSwitch; }
	public JToggleButton getItalicSwitch() { return italicSwitch; }
	public JToggleButton getUnderlineSwitch() { return underlineSwitch; }
	public JButton getSaveButton() { return saveButton; }
}
