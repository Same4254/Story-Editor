package StoryEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument.LeafElement;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import TextEditor.AttributeCommand;
import TextEditor.PageKit.PageableEditorKit;

public class PageEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextPane textPane;
	private JComboBox<String> fontFamilies;
	private JComboBox<Integer> fontSizes;
	private JToggleButton boldSwitch, italicSwitch, underlineSwitch;
	private JButton saveButton;
	
	private File file;
	
	private boolean blockCaretListener;
	
	private PageableEditorKit editorKit;
	
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
	 *     - Save project folder to google drive to allow multi computer devlopment
	 */
    public PageEditor() {
        setLayout(new BorderLayout());
        
        textPane = new JTextPane();
        
        editorKit = new PageableEditorKit(textPane);
        
        textPane.setEditorKit(editorKit);
        textPane.setEditable(true);
        
//        UndoManager undoManager = new UndoManager();
//        textPane.getDocument().addUndoableEditListener(undoManager);
        
        JScrollPane textScrollPane = new JScrollPane(textPane); 
        textScrollPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				PageEditor.this.processMouseWheelEvent(e);
			}
		});
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
        
        saveButton = new JButton("Save");
        saveButton.setFocusable(false);
        
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
        
        buttons.add(saveButton);
        
//        loadButton = new JButton("Load");
//        loadButton.setFocusable(false);
//        
//        loadButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				read(new File("res/test.txt"));
//			}
//		});
//        
//        buttons.add(loadButton);
        
        add(buttons, BorderLayout.NORTH);

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
				boldSwitch.doClick();
			}
		});
        
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK, true), "Italic");
        textPane.getActionMap().put("Italic", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				italicSwitch.doClick();
			}
		});
        
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK, true), "Underline");
        textPane.getActionMap().put("Underline", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				underlineSwitch.doClick();
			}
		});
        
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK, true), "Save");
        textPane.getActionMap().put("Save", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
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
        
        addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
//				if(e.isControlDown()) {
//					editorKit.changeZoom(-e.getWheelRotation());
//					repaint();
//				}
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
     *	Okay, here's the plan:
     * 
     * 	The goal here is to save the contents of the pane with all of the character attributes as well. 
     * 	To do this, we need to establish a few things. Firstly, this will be done in a simple and linear manner. 
     * 	Meaning that every character's attributes will be analyzed before writing it into the file. 
     * 	
     * 	Essentially, this is what will be done.
     * 	The first character's attributes will be marked down, and the caret will slide along the contents of the pane. 
     * 	While doing so, we will also keep track of the index that we are looking at. In addition, when a new character is reached, 
     * 	its attributes will be compared to those of the character behind it. If the attributes happen to be different, then 
     * 	we will mark down the changes, the index of change and continue on. 
     * 
     *  In the end, we wish for there to be a file that contains the following:
     *  	- The number of lines that follow this that are preliminary information 
     *  		(The reason for this number is to avoid the user accidently typing information 
     *  		that this may interpret as pertinent file information).
     *  	- Several lines of information, each containing the 
     *  		indices of application,
     *  		the changes to be applied before writing to the text
     *  	- The actual String content of the page
     */
    private void save() {
//    	File file = new File("res/test.txt");//Setting up a test file
    	if(file == null) {//Save as -> The File has not been created yet...
    		
    	}
    	
    	PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
		LinkedList<AttributeCommand> leafNodes = new LinkedList<>();
    	getLeafNodes(textPane.getDocument().getDefaultRootElement(), leafNodes);

    	AttributeCommand last = leafNodes.getLast();
    	
    	int endIndex = -1;
    	if(last.getAttributes().size() == 0) {//Sometimes, the last element is just blank... This will save the index information in this case
    		endIndex = last.getEndIndex();
    		leafNodes.removeLast();
    	}
    	
    	writer.println(leafNodes.size());
    	
    	int count = 0;//index of the element in the linked-list (avoid random access)
    	for(AttributeCommand node : leafNodes) {
    		if(count == leafNodes.size() - 1 && endIndex > 0)//Check if it is the last (avoids .equals of the node element, this is cheaper... probably)
    			writer.print(node.getStartIndex() + "-" + endIndex + ",");
    		else
    			writer.print(node.getStartIndex() + "-" + node.getEndIndex() + ",");
    		
    		ArrayList<String> attributes = node.getAttributes();
    		ArrayList<Object> values = node.getRevisions();
    		for(int i = 0; i < attributes.size(); i ++) {
    			writer.print(attributes.get(i) + ":" + values.get(i));//attribute:value
    			
    			if(i != attributes.size() - 1)//attribute:value,attribute:value,attribute:value
    				writer.print(",");
    			else
    				writer.println();
    		}
    		
    		count++;
    	}
    	
    	writer.println();//just an empty line to separate info from content
    	writer.print(textPane.getText());
    	
    	writer.close();
    }
    
    /*
     * This recursive function will go through the pane's elements and collect the leaf node elements. 
     * These leaf nodes contain the desired information about the indexes in which to apply certain attributes. 
     * This is considerably more efficient than collecting information from every single character in the text pane. 
     * 
     * However, all new line character get their own leaf node. This means that there will be many MANY more leaf nodes than there has to be. 
     * Thus, as this function goes through the leaf nodes, it will check to see if the last stored node holds the same information. 
     * if so, then the index of the last node will be extended rather than add another element all together.
     * To avoid complications later in the pane's usage, the information will be stored in a separate objects, 
     * and its indexes modified elsewhere, for modifying the actual indexes that will have more negative effects than worth dealing with. 
     */
    private void getLeafNodes(Element element, LinkedList<AttributeCommand> nodes) {
    	if(element.isLeaf()) {
    		AttributeCommand command = new AttributeCommand((LeafElement) element);
    		if(nodes.size() > 0 && command.isEqual(nodes.getLast()))//See if these attributes are already in play
    			nodes.getLast().setEndIndex(command.getEndIndex());
    		else
    			nodes.add(command);
    		return;
    	} 
    	
    	for(int i = 0; i < element.getElementCount(); i++)
    		getLeafNodes(element.getElement(i), nodes);
    }
    
    /*
     *	The first line in the file is the the amount of lines before the actual text of the content (this contains an empty line as well). 
     *	That amount of lines following it will be specific character attribute information to instruct how to write the text at the specified indicies.
     *	Thereafter will be a blank line. 
     *	Then, the text itself will be written down.
     */
    public void read() {
    	blockCaretListener = true;//the caret listener needs not worry of this...
    	textPane.setText("");//empty the pane
    	
//    	System.out.println(file);
    	
    	Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
		if(!sc.hasNextLine())
			return;
		
    	int amountOfLines = Integer.parseInt(sc.nextLine());
    	
    	AttributeCommand[] differences = new AttributeCommand[amountOfLines];
    	for(int i = 0; i < amountOfLines; i++) {//Attribute Differences
    		String[] parts = sc.nextLine().split(",");
    		
    		//Indexes to apply the following information
    		String[] indecies = parts[0].split("-");
    		int startIndex = Integer.parseInt(indecies[0]);
    		int endIndex = Integer.parseInt(indecies[1]);
    		
    		//Store the information
    		AttributeCommand difference = new AttributeCommand();
			difference.setStartIndex(startIndex);
			difference.setEndIndex(endIndex);
    		
    		for(int j = 1; j < parts.length; j++) {
    			String[] revisions = parts[j].split(":");
    			
    			//attribute:value
    			String constant = revisions[0];
    			String value = revisions[1];
    			
    			Object newValue = null;
    			
    			//This is to find out what type of value is being read for this attribute
    			try {
    				newValue = Integer.parseInt(value);//see if it is an int
    			} catch(NumberFormatException e) {//If this exception is thrown, then it is not an integer value
    				if(value.equals("true"))//see if it is boolean true
    					newValue = true;
    				else if(value.equals("false"))//see if it is boolean false
    					newValue = false;
    				else
    					newValue = value;//it is just a string value
    			}
    			
    			//store the information
    			difference.addAttributeRevision(constant, newValue);
    		}
    		
    		differences[i] = difference;
    	}
    	
    	sc.nextLine();// blank line

    	//Start reading the content 
    	StringBuilder contentAssembler = new StringBuilder();
    	
    	while(sc.hasNextLine()) {//Read the content
    		contentAssembler.append(sc.nextLine());
    		if(sc.hasNextLine())//Last line doesn't need a blank new line
    			contentAssembler.append("\n");
    	}

    	//Go through the attribute commands and paste the content with the given attributes
    	for(int i = 0; i < differences.length; i++) {
    		//Get the attributes and their values
    		AttributeCommand difference = differences[i];
    		ArrayList<String> attributes = difference.getAttributes();
    		ArrayList<Object> revisions = difference.getRevisions();
    		
    		//Set all the attributes accordingly, uses hashmap to pull Style constants from a given string
    		for(int j = 0; j < attributes.size(); j++)
    			setAttribute(AttributeCommand.objectMap.get(attributes.get(j)), revisions.get(j));
    		
    		//Paste the content
    		if(i == differences.length - 1)
    			textPane.replaceSelection(contentAssembler.substring(difference.getStartIndex()));
    		else
    			textPane.replaceSelection(contentAssembler.substring(difference.getStartIndex(), difference.getEndIndex()));
    	}
    	
    	sc.close();
    	blockCaretListener = false;//Nothing happend here...
    	
    	textPane.setCaretPosition(contentAssembler.length());//have the caret listener update the gui to the last position in the text
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
    	
    	fontFamilies.setSelectedItem(getAttribute(StyleConstants.FontFamily));
		fontSizes.setSelectedItem(getAttribute(StyleConstants.FontSize));
		boldSwitch.setSelected((Boolean) getAttribute(StyleConstants.Bold));
		italicSwitch.setSelected((Boolean) getAttribute(StyleConstants.Italic));
		underlineSwitch.setSelected((Boolean) getAttribute(StyleConstants.Italic));
		
		textPane.setCaretPosition(temp);
		
		blockCaretListener = false;//unblock the listener
    }
    
    private Object getAttribute(Object constant) { return textPane.getCharacterAttributes().getAttribute(constant); }

    private void setAttribute(Object name, Object value) {
    	StyleContext context = StyleContext.getDefaultStyleContext();
		AttributeSet set = context.addAttribute(SimpleAttributeSet.EMPTY, name, value);
		
		textPane.setCharacterAttributes(set, false);
    }
    
    private void setFontFamily(String family) { setAttribute(StyleConstants.FontFamily, family); }
    private void setFontSize(int size) { setAttribute(StyleConstants.Size, size); }
    private void setBold(boolean bold) { setAttribute(StyleConstants.Bold, bold); }
    private void setItalic(boolean italic) { setAttribute(StyleConstants.Italic, italic); }
    private void setUnderline(boolean underline) { setAttribute(StyleConstants.Underline, underline); }

    public void setFile(File file) { this.file = file; read(); }
    public File getFile() { return this.file; }
    
//    public static void main(String[] args) throws IOException {
//    	System.setProperty("line.separator", "\n");
//    	
//    	JFrame frame = new JFrame();
//    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1700, 1080);
//        
//        frame.add(new PageEditor());
//        frame.setVisible(true);
//    }
}
