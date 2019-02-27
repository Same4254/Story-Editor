package TextEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import StoryEditor.RightClickMenu.LambdaMenuItem;
import StoryEditor.RightClickMenu.RightClickMenu;
import TextEditor.PageKit.PageableEditorKit;

public class PageEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	private JComboBox<String> fontFamilies;
	private JComboBox<Integer> fontSizes;
	private JToggleButton boldSwitch, italicSwitch, underlineSwitch;
	private JButton saveButton;
	
	private File file;
	
	private boolean blockCaretListener;
	
	private JTextPane textPane;
	private PageableEditorKit editorKit;
	private PageEditorIO io;
	
	private RightClickMenu rightClickMenu;
	
	/**
	 * Small Bugs:
	 * 	   - Double click to select and drag left de-selects the original word
	 * 	   - Undo manager will undo hot key presses, but that doesn't update the gui
	 * 
	 * Next:
	 * 	   - Some additions such as centering the text, and small other options. More attributes
	 * 	   - Spruce up the editor... it's ugly
	 * 	   - Be able to add Images 
	 * 
	 * To Do Later:
	 *     - Hot keys to change character properties
	 *     		- Hot key panel to view and edit hot keys...?
	 *     - Spell checker / auto-correct / grammar check
	 *     - Implement undo and re-do.
	 *     		- Change undo and re-do limit (options menu?)
	 *     - Export as docx?
	 *     - Import docx files
	 *     - Save project folder to google drive to allow multi-computer development
	 */
    public PageEditor() {
        setLayout(new BorderLayout());
        textPane = new JTextPane();
        io = new PageEditorIO(this);
        editorKit = new PageableEditorKit(textPane);
        
        textPane.setEditorKit(editorKit);
        textPane.setEditable(true);
        
//        UndoManager undoManager = new UndoManager();
//        textPane.getDocument().addUndoableEditListener(undoManager);
        
//        JScrollPane textScrollPane = new JScrollPane(textPane); 
//        textScrollPane.addMouseWheelListener(new MouseWheelListener() {
//			@Override
//			public void mouseWheelMoved(MouseWheelEvent e) {
//				PageEditor.this.processMouseWheelEvent(e);
//			}
//		});
//        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        add(textScrollPane, BorderLayout.CENTER);
        
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
        
        saveButton = new JButton("Save");
        saveButton.setFocusable(false);
        
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blockCaretListener = true;
				io.save();
				blockCaretListener = false;
			}
		});
        
        buttons.add(saveButton);
        
        add(buttons, BorderLayout.NORTH);
        add(textPane, BorderLayout.CENTER);

        textPane.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(!blockCaretListener && e.getDot() == e.getMark()) 
					updateAttributes();
			}
		});
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK, true), "Bold");
        getActionMap().put("Bold", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				boldSwitch.doClick();
			}
		});
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK, true), "Italic");
        getActionMap().put("Italic", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				italicSwitch.doClick();
			}
		});
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK, true), "Underline");
        getActionMap().put("Underline", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				underlineSwitch.doClick();
			}
		});
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK, true), "Save");
        getActionMap().put("Save", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveButton.doClick();
//				save();
			}
		});
        
        //not working
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, true), "Comment");
        getActionMap().put("Comment", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				addComment(textPane.getCaret().getDot(), textPane.getCaret().getMark());
			}
		});
        
        rightClickMenu = new RightClickMenu();
        
        textPane.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		super.mouseReleased(e);
        		
        		if(e.getButton() == MouseEvent.BUTTON3) {
        			if(textPane.getCaret().getMark() != textPane.getCaret().getDot()) {
        				rightClickMenu.setMenuItems(
        					new LambdaMenuItem("Comment", () -> {
        						addComment(textPane.getCaret().getDot(), textPane.getCaret().getMark());
        					})
        				);
        				
        				rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
        			}
        		}
        	}
        });
        
//        addMouseListener(new MouseAdapter() {
//        	@Override
//        	public void mouseWheelMoved(MouseWheelEvent e) {
//        		System.out.println("Here");
//        		if(e.isControlDown()) {
//        			System.out.println("In");
//        			editorKit.changeZoom(e.getWheelRotation());
//        		}
//        	}
//		});
        
//        addMouseWheelListener(new MouseWheelListener() {
//			@Override
//			public void mouseWheelMoved(MouseWheelEvent e) {
//				if(e.isControlDown()) {
//					editorKit.changeZoom(-e.getWheelRotation());
//					repaint();
//				}
//			}
//		});
        
//        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK, true), "Undo");
//        textPane.getActionMap().put("Undo", new AbstractAction() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(undoManager.canUndo())
//					undoManager.undo();
//			}
//		});
//        
//        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK, true), "Redo");
//        textPane.getActionMap().put("Redo", new AbstractAction() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(undoManager.canRedo())
//					undoManager.redo();
//			}
//		});
    }
    
    public void addComment(int index1, int index2) {
    	SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setBackground(sas, Color.YELLOW);
	    ((DefaultStyledDocument) textPane.getDocument()).setCharacterAttributes(
	    		Math.min(index1,  index2), 
	    		Math.abs(index1 - index2), 
	    		sas, false);//(start, length, sas, false);
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
    	
    	if(caretPosition > 0) //Move the cursor back to the last text
    		caretPosition--;
    	
    	textPane.setCaretPosition(caretPosition);
    	
    	fontFamilies.setSelectedItem(getFontFamily());
		fontSizes.setSelectedItem(getFontSize());
		boldSwitch.setSelected(isBold());
		italicSwitch.setSelected(isItalic());
		underlineSwitch.setSelected(isUnderline());
		
		textPane.setCaretPosition(temp);
		
		blockCaretListener = false;//unblock the listener
    }
    
    private Object getAttribute(Object constant) { return textPane.getCharacterAttributes().getAttribute(constant); }

    protected void setAttribute(Object name, Object value) {
    	StyleContext context = StyleContext.getDefaultStyleContext();
		AttributeSet set = context.addAttribute(SimpleAttributeSet.EMPTY, name, value);
		
		textPane.setCharacterAttributes(set, false);
    }
    
    private void setFontFamily(String family) { setAttribute(StyleConstants.FontFamily, family); }
    private void setFontSize(int size) { setAttribute(StyleConstants.Size, size); }
    private void setBold(boolean bold) { setAttribute(StyleConstants.Bold, bold); }
    private void setItalic(boolean italic) { setAttribute(StyleConstants.Italic, italic); }
    private void setUnderline(boolean underline) { setAttribute(StyleConstants.Underline, underline); }
    
    private String getFontFamily() { return getAttribute(StyleConstants.FontFamily).toString(); }
    private Object getFontSize() { return getAttribute(StyleConstants.FontSize); }
    private boolean isBold() { return (Boolean) getAttribute(StyleConstants.Bold); }
    private boolean isItalic() { return (Boolean) getAttribute(StyleConstants.Italic); }
    private boolean isUnderline() { 
    	Object value = getAttribute(StyleConstants.Underline);
    	
    	if(value == null)
    		return false;
    	return (Boolean) value;
    }

    
    public JTextPane getTextPane() { return textPane; }
    public File getFile() { return this.file; }
    public void setFile(File file) { 
    	this.file = file; 
    	
    	blockCaretListener = true;
    	io.read(); 
    	blockCaretListener = false;
    	
    	textPane.setCaretPosition(textPane.getText().length());
    }
}
