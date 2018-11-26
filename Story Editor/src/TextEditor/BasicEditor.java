package TextEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument.LeafElement;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
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
	private JButton saveButton;
	private JButton loadButton;
	
	private boolean blockCaretListener;
	
	/**
	 * Small Bugs:
	 * 	   - Double click to select and drag left de-selects the original word
	 * 	   - why us underline null? 
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
	 *     - Implement undo and re-do.
	 */
    public BasicEditor() {
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
        
        saveButton = new JButton("Save");
        saveButton.setFocusable(false);
        
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
        
        buttons.add(saveButton);
        
        loadButton = new JButton("Load");
        loadButton.setFocusable(false);
        
        loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				read(new File("res/test.txt"));
			}
		});
        
        buttons.add(loadButton);
        
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
     *  
     *  TODO
     *  	- This saves A LOT of extra work for itself. For some reason new line characters get their own node, and every node is saved individually.
     *  		 creating a lot of fluff in the file, and more for the program to loop through later on.
     */
    private void save() {
    	File file = new File("res/test.txt");//Setting up a test file
    	PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	LinkedList<LeafElement> leafNodes = new LinkedList<>();
    	getLeafNodes(textPane.getDocument().getDefaultRootElement(), leafNodes);

    	LeafElement last = leafNodes.getLast();
    	
    	int endIndex = -1;
    	if(last.getAttributeCount() == 0) {
    		endIndex = last.getEndOffset();
    		leafNodes.removeLast();
    	}
    	
    	writer.println(leafNodes.size());
    	
    	int count = 0;//index of the element in the linked-list (avoid random access)
    	for(LeafElement node : leafNodes) {
    		Enumeration<?> e = node.getAttributeNames();
    		
    		if(count == leafNodes.size() - 1 && endIndex > 0)//Check if it is the last (avoids .equals of the node element, this is cheaper... probably)
    			writer.print(node.getStartOffset() + "-" + endIndex + ",");
    		else
    			writer.print(node.getStartOffset() + "-" + node.getEndOffset() + ",");
    		
    		while(e.hasMoreElements()) {
    			Object o = e.nextElement();
    			writer.print(o + ":" + node.getAttribute(o));
    			
    			if(e.hasMoreElements())
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
    
    private void getLeafNodes(Element element, LinkedList<LeafElement> nodes) {
    	if(element.isLeaf()) {
    		nodes.add((LeafElement) element);
    		return;
    	} 
    	
    	for(int i = 0; i < element.getElementCount(); i++)
    		getLeafNodes(element.getElement(i), nodes);
    }
    
    /*
     *	The first line in the file is the the amount of lines before the actual text of the content (this contains an empty line as well). 
     *	
     */
    public void read(File file) {
    	blockCaretListener = true;
    	textPane.setText("");//empty the pane
    	
    	Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	int amountOfLines = Integer.parseInt(sc.nextLine());
    	
    	AttributeCommand[] differences = new AttributeCommand[amountOfLines];
    	for(int i = 0; i < amountOfLines; i++) {//Attribute Differences
    		String[] parts = sc.nextLine().split(",");
    		
    		String[] indecies = parts[0].split("-");
    		int startIndex = Integer.parseInt(indecies[0]);
    		int endIndex = Integer.parseInt(indecies[1]);
    		
    		AttributeCommand difference = new AttributeCommand();
			difference.setStartIndex(startIndex);
			difference.setEndIndex(endIndex);
    		
    		for(int j = 1; j < parts.length; j++) {
    			String[] revisions = parts[j].split(":");
    			
    			String constant = revisions[0];
    			String value = revisions[1];
    			
    			Object newValue = null;
    			
    			try {
    				newValue = Integer.parseInt(value);
    			} catch(NumberFormatException e) {
    				if(value.equals("true"))
    					newValue = true;
    				else if(value.equals("false"))
    					newValue = false;
    				else
    					newValue = value;
    			}
    			
    			difference.addAttributeRevision(constant, newValue);
    		}
    		
    		differences[i] = difference;
    	}
    	
    	sc.nextLine();// blank line

    	StringBuilder contentAssembler = new StringBuilder();
    	
    	while(sc.hasNextLine()) {
    		contentAssembler.append(sc.nextLine());
    		contentAssembler.append("\n");
    	}

    	for(int i = 0; i < differences.length; i++) {
    		AttributeCommand difference = differences[i];
    		
    		ArrayList<String> attributes = difference.getAttributes();
    		ArrayList<Object> revisions = difference.getRevisions();
    		
    		for(int j = 0; j < attributes.size(); j++)
    			setAttribute(AttributeCommand.objectMap.get(attributes.get(j)), revisions.get(j));
    		
    		if(i == differences.length - 1)
    			textPane.replaceSelection(contentAssembler.substring(difference.getStartIndex()));
    		else
    			textPane.replaceSelection(contentAssembler.substring(difference.getStartIndex(), difference.getEndIndex()));
    	}
    	
    	sc.close();
    	blockCaretListener = false;
    	
    	textPane.setCaretPosition(contentAssembler.length());//have the caret listener update the gui
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
    	
    	if(caretPosition > 0) {//Move the cursor back to the last text
    		caretPosition--;
    		
//    		String s = null;
//    		while(!(s = textPane.getText().substring(caretPosition, caretPosition + 1)).equals("\n") || !s.equals("\r")) {
//    			if(caretPosition == 0)
//    				break;
//    			
//    			caretPosition--;
//    		}
    	}
    	
    	textPane.setCaretPosition(caretPosition);
    	
    	fontFamilies.setSelectedItem(getAttribute(StyleConstants.FontFamily));
		fontSizes.setSelectedItem(getAttribute(StyleConstants.FontSize));
		boldSwitch.setSelected((Boolean) getAttribute(StyleConstants.Bold));
		italicSwitch.setSelected((Boolean) getAttribute(StyleConstants.Italic));
		underlineSwitch.setSelected(isUnderline());
		
		textPane.setCaretPosition(temp);
		
		blockCaretListener = false;//unblock the listener
    }
    
    private AttributeSet getAttributeSetAtCaret() {
    	int caretPosition = textPane.getCaretPosition();
    	int temp = caretPosition;
    	
    	if(caretPosition > 0) {//Move the cursor back to the last text
    		caretPosition--;
    		
    		String s = null;
    		while(!(s = textPane.getText().substring(caretPosition, caretPosition + 1)).equals("\n") || !s.equals("\r")) {
    			if(caretPosition == 0)
    				break;
    			
    			caretPosition--;
    		}
    	}
    	
    	AttributeSet set = textPane.getCharacterAttributes();
    	textPane.setCaretPosition(temp);
    	return set;
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
    	System.setProperty("line.separator", "\n");
        new BasicEditor();
    }
}
