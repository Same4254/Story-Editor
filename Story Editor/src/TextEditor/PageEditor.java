package TextEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyleContext.NamedStyle;

import StoryEditor.RightClickMenu.LambdaMenuItem;
import StoryEditor.RightClickMenu.RightClickMenu;
import TextEditor.Comments.CommentManager;
import TextEditor.Comments.CommentPanel;
import TextEditor.IO.PageEditorIO;
import TextEditor.PageKit.PageableEditorKit;

public class PageEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	private File file;
	
	private boolean blockCaretListener;
	
	private ToolBarPanel toolBar;
	
	private JLayeredPane layeredPane;
	
	private TopLayerPanel topLayer;
	
	private JScrollPane scrollPane;
	private JTextPane textPane;

	private PageableEditorKit editorKit;
	private PageEditorIO io;
	
	private RightClickMenu rightClickMenu;
	
	private CommentManager commentManager;
	
	/**
	 * Comments:
	 * 	   - Selection over many indexes
	 * 			- Removal
	 * 			- Commenting on something with a comment already 
	 *     - Removal by resolution
	 *     - 
	 * 
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
//        setLayout(new BorderLayout());
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
        
        toolBar = new ToolBarPanel(this);
        
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);//(buttons, BorderLayout.NORTH);
        
        scrollPane = new JScrollPane(textPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        
        topLayer = new TopLayerPanel(this);
        
        layeredPane = new JLayeredPane();
        layeredPane.add(scrollPane, new Integer(0));
        layeredPane.add(topLayer, new Integer(1));

        add(layeredPane, BorderLayout.CENTER);

        layeredPane.addComponentListener(new ComponentAdapter() {
        	@Override
        	public void componentResized(ComponentEvent e) {
        		super.componentResized(e);
        		
        		scrollPane.setSize(layeredPane.getSize());
        		topLayer.setSize(layeredPane.getSize());
        	}
		});
        
        textPane.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(!blockCaretListener && e.getDot() == e.getMark()) 
					updateAttributes();
			}
		});
        
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK, true), "Bold");
        textPane.getActionMap().put("Bold", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				toolBar.getBoldSwitch().doClick();
			}
		});
        
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK, true), "Italic");
        textPane.getActionMap().put("Italic", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				toolBar.getItalicSwitch().doClick();
			}
		});
        
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK, true), "Underline");
        textPane.getActionMap().put("Underline", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				toolBar.getUnderlineSwitch().doClick();
			}
		});
        
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK, true), "Save");
        textPane.getActionMap().put("Save", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				toolBar.getSaveButton().doClick();
			}
		});
        
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK | Event.SHIFT_MASK, true), "Comment");
        textPane.getActionMap().put("Comment", new AbstractAction() {
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
        
        commentManager = new CommentManager(this);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
        	commentManager.justifyAllVisible();
        });
        
        ((DefaultStyledDocument) textPane.getDocument()).setDocumentFilter(new DocumentFilter() {
        	@Override
        	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        		if(length == 1) {
        			//Check if a comment needs to also be removed
	        		Object value = textPane.getStyledDocument().getCharacterElement(offset).getAttributes().getAttribute("Comment");
	        		if(value instanceof CommentPanel) {
	        			CommentPanel panel = (CommentPanel) value;
	        			if(Math.abs(panel.getStartPosition().getOffset() - panel.getEndPosition().getOffset()) == 1) {
	        				commentManager.removeComment(panel);
	        				layeredPane.remove(panel);
	        			}
	        		}
        		}
        		
        		super.remove(fb, offset, length);
        	}
        });
        
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
    
    /*
     * Given two indexes, create a comment panel and show it to the user.
     * 
     * The indexes: any order. 2, 5 or 5, 2 will work. This function will take the min and max of the two.  
     * 		(These represent the indexes of characters in the text)
     * If these indexes represent a bad location in the document, then this method will have no effect.
     * 
     * This method will highlight the text between the indexes (inclusive) in yellow.
     * In addition, a panel will be created to allow the user to write text. This panel will be added to the comment manager.
     * A reference to this panel will be added as a attribute to the text, allowing a character to reference the comment panel if necessary
     */
    public void addComment(int index0, int index1) {
    	NamedStyle sas = new StyleContext().new NamedStyle();
    	sas.addChangeListener(e -> {
    		Object value = sas.getAttribute("Comment");
    		if(value instanceof CommentPanel) {
    			System.out.println("Change Listener Picked up");
    		}
    	});
    	
	    StyleConstants.setBackground(sas, Color.YELLOW);
	    
	    Position p0 = null, p1 = null;
		try {
			p0 = textPane.getDocument().createPosition(Math.min(index0, index1));
			p1 = textPane.getDocument().createPosition(Math.max(index0, index1));
		} catch (BadLocationException e) {
			e.printStackTrace();
			return;
		}
	    
//	    StyleConstants.setComponent(sas, new CommentPanel(p0, p1));
		CommentPanel commentPanel = new CommentPanel(p0, p1);
		commentPanel.setBorder(new LineBorder(Color.BLACK));
		
		commentManager.addComment(commentPanel);
		commentManager.justify(commentPanel);
		
//		topLayer.add(commentPanel);
//		topLayer.repaint();//layered pane needs to paint this component (con of null manager)
//		topLayer.revalidate();
//		topLayer.validate();
		
		sas.addAttribute("Comment", commentPanel);
		
	    ((DefaultStyledDocument) textPane.getDocument()).setCharacterAttributes(
	    		Math.min(index0,  index1), 
	    		Math.abs(index0 - index1), 
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
    	
    	toolBar.getFontFamilies().setSelectedItem(getFontFamily());
		toolBar.getFontSizes().setSelectedItem(getFontSize());
		toolBar.getBoldSwitch().setSelected(isBold());
		toolBar.getItalicSwitch().setSelected(isItalic());
		toolBar.getUnderlineSwitch().setSelected(isUnderline());
		
//		if(textPane.getCharacterAttributes().getAttribute("Comment") instanceof CommentPanel) {
//			CommentPanel panel = (CommentPanel) textPane.getCharacterAttributes().getAttribute("Comment");
//			
//			System.out.println("Thingngngnn");
//			
//			if(panel.getStartPosition().getOffset() == panel.getEndPosition().getOffset()) {
//				System.out.println("Here In the attributes");
//			}
//		}
		
		textPane.setCaretPosition(temp);
		
		blockCaretListener = false;//unblock the listener
    }
    
    public Rectangle indexToLocation(int index) {
    	try {
			return textPane.getUI().modelToView(textPane, index);
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    protected Object getAttribute(Object constant) { return textPane.getCharacterAttributes().getAttribute(constant); }
    protected void setCharacterAttribute(Object constant, Object value) {
    	SimpleAttributeSet attr = new SimpleAttributeSet();
    	attr.addAttribute(constant, value);
    	textPane.setCharacterAttributes(attr, false);
    }
    
    private Object getFontFamily() { return getAttribute(StyleConstants.FontFamily); }
    private Object getFontSize() { return getAttribute(StyleConstants.FontSize); }
    private boolean isBold() { return (Boolean) getAttribute(StyleConstants.Bold); }
    private boolean isItalic() { return (Boolean) getAttribute(StyleConstants.Italic); }
    private boolean isUnderline() { 
    	Object value = getAttribute(StyleConstants.Underline);
    	
    	if(value == null)
    		return false;
    	return (Boolean) value;
    }

    public ToolBarPanel getToolBarPanel() { return toolBar; }
    public JLayeredPane getLayeredPane() { return layeredPane; }

    public TopLayerPanel getTopLayerPanel() { return topLayer; }
    
    public JScrollPane getScrollPane() { return scrollPane; }
    public JTextPane getTextPane() { return textPane; }    
    
    public PageableEditorKit getEditorKit() { return editorKit; }
    
    protected void save() {
    	blockCaretListener = true;
		io.save();
		blockCaretListener = false;
    }
    
    public File getFile() { return this.file; }
    public void setFile(File file) { 
    	this.file = file; 
    	
    	blockCaretListener = true;
    	io.read(); 
    	blockCaretListener = false;
    	
    	textPane.setCaretPosition(textPane.getText().length());
    }
}
