package TextEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import TextEditor.PageKit.PageableEditorKit;

public class BasicEditor extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTextPane textPane;
	private JComboBox<String> fontFamilies;
	private JComboBox<Integer> fontSizes;
	private JToggleButton boldSwitch, italicSwitch, underlineSwitch;
	
	private boolean blockCaretListener;
	
	/**
	 * Small Bugs:
	 * 	   - Double click to select and drag left de-selects the original word
	 * 
	 * Next:
	 * 	   - Some additions such as centering the text, and small other options
	 * 	   - Spruce up the editor a bit... it's ugly
	 * 	   - Begin the actual project, this is just one part 
	 * 
	 * To Do Later:
	 *     - Hot keys to change character properties
	 *     - Spell checker / auto-correct
	 *     - Save to a folder  
	 */
    public BasicEditor () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1700, 1080);
        
        setLayout(new BorderLayout());
        
        textPane = new JTextPane();
//        textPane.setCaret(new SelectionPreservingCaret());
//        textPane.setEditorKit(new WrapEditorKit());
        textPane.setEditorKit(new PageableEditorKit(textPane));
        textPane.setEditable(true);
        
        JScrollPane textScrollPane = new JScrollPane(textPane); 
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(textScrollPane, BorderLayout.CENTER);
        
        JPanel buttons = new JPanel();
        
        fontFamilies = new JComboBox<>();
        fontFamilies.setFocusable(false);
        
        fontFamilies.addItem("Comic Sans MS");
        fontFamilies.addItem("Courier New");
        
        fontFamilies.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String family = (String) fontFamilies.getSelectedItem();
					String toReplace = textPane.getSelectedText();
					
					setFontFamily(family);
					
					if(toReplace == null)
						textPane.replaceSelection(toReplace);
				}
			}
		});

        setFontFamily(fontFamilies.getItemAt(0));
        buttons.add(fontFamilies);
        
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
					int size = (Integer) fontSizes.getSelectedItem();
					String toReplace = textPane.getSelectedText();
					
					setFontSize(size);
					
					if(toReplace == null)
						textPane.replaceSelection(toReplace);
				}
			}
		});
        setFontSize(fontSizes.getItemAt(0));
        
        buttons.add(fontSizes);
        
        boldSwitch = new JToggleButton("Bold");
        boldSwitch.setFocusable(false);
        boldSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean bold = boldSwitch.isSelected();
				String toReplace = textPane.getSelectedText();
				
				setBold(bold);
				
				if(toReplace == null)
					textPane.replaceSelection(toReplace);
			}
		});
        setBold(false);
        
        buttons.add(boldSwitch);
        
        italicSwitch = new JToggleButton("Italics");
        italicSwitch.setFocusable(false);
        italicSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean italic = italicSwitch.isSelected();
				String toReplace = textPane.getSelectedText();
				
				setItalic(italic);
				
				if(toReplace == null)
					textPane.replaceSelection(toReplace);
			}
		});
        setItalic(false);
        
        buttons.add(italicSwitch);
        
        underlineSwitch = new JToggleButton("Underline");
        underlineSwitch.setFocusable(false);
        underlineSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean underline = underlineSwitch.isSelected();
				String toReplace = textPane.getSelectedText();
				
				setUnderline(underline);
				
				if(toReplace == null)
					textPane.replaceSelection(toReplace);
			}
		});
        setUnderline(false);
        
        buttons.add(underlineSwitch);
        
        add(buttons, BorderLayout.NORTH);

        textPane.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(!blockCaretListener && e.getDot() == e.getMark()) {// && e.getDot() != textPane.getDocument().getLength()) {
					updateAttributes();
				}
			}
		});

        setVisible(true);
    }
    
    /**
     * This is a bit of a work around, but surprisingly not as bad as it could be.
     * 
     * SO, there is a slight issue where if you try to change an attribute in the middle of the word the caret 
     * listener will actually take the properties of the next character. Meaning that everything gets shown as the attribute to the right, 
     * but is actually the attribute on the left. 
     *   
     * This is because the getAttribute method looks to the right of the caret. However, it would be more favorable if it looked to the left of the caret in 
     * order to keep consistent attributes, with what is being used in the text pane, and what is shown in the gui.
     * 
     * Thus, this will temporarily move the caret back one, and then call the function, and then move back to the original location. 
     * Fortunately, this will have no visual effect, and allows for the call to the getCharacterAttributes method.
     */
    private void updateAttributes() {
    	blockCaretListener = true;//We must block the listener to avoid endless recursion
    	
    	int caretPosition = textPane.getCaretPosition();
    	int temp = caretPosition;
    	
    	if(caretPosition > 0)//Move the cursor back one unit
    		caretPosition--;
    	
    	textPane.setCaretPosition(caretPosition);
    	
    	fontFamilies.setSelectedItem(getAttribute(StyleConstants.FontFamily));
		fontSizes.setSelectedItem(getAttribute(StyleConstants.FontSize));
		boldSwitch.setSelected((Boolean) getAttribute(StyleConstants.Bold));
		italicSwitch.setSelected((Boolean) getAttribute(StyleConstants.Italic));
		underlineSwitch.setSelected(isUnderline());
		
		textPane.setCaretPosition(temp);
		
		blockCaretListener = false;//unblock the listener
    }
    
    private Object getAttribute(Object constant) {
    	return textPane.getCharacterAttributes().getAttribute(constant);
    }
    
    private void setFontFamily(String family) {
		setAttribute(StyleConstants.FontFamily, family);
    }
    
    private void setFontSize(int size) {
    	setAttribute(StyleConstants.Size, size);
    }
    
    private void setBold(boolean bold) {
    	setAttribute(StyleConstants.Bold, bold);
    }
    
    private void setItalic(boolean italic) {
    	setAttribute(StyleConstants.Italic, italic);
    }
    
    private void setUnderline(boolean underline) {
    	setAttribute(StyleConstants.Underline, underline);
    }
    
    private boolean isUnderline() {
    	Object o = getAttribute(StyleConstants.Underline);
    	return o == null ? false : (Boolean) o;
    }
    
    private void setAttribute(Object name, Object value) {
    	StyleContext context = StyleContext.getDefaultStyleContext();
		AttributeSet set = context.addAttribute(SimpleAttributeSet.EMPTY, name, value);
		
		textPane.setCharacterAttributes(set, false);
    }

    public static void main(String[] args) throws IOException {
        new BasicEditor();
    }
}
