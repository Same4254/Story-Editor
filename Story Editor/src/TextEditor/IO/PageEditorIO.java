package TextEditor.IO;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.AbstractDocument.LeafElement;

import TextEditor.AttributeCommand;
import TextEditor.PageEditor;

public class PageEditorIO {

	private PageEditor editor;
	
	/*
	 *	Honestly, the only purpose of this class is to move a huge chunk of IO code away from the UI code... the file was getting cluttered  
	 */
	public PageEditorIO(PageEditor editor) {
		this.editor = editor;
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
    public void save() {
//    	File file = new File("res/test.txt");//Setting up a test file
    	if(editor.getFile() == null) {//Save as -> The File has not been created yet...
    		
    	}
    	
    	PrintWriter writer = null;
		try {
			writer = new PrintWriter(editor.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
		LinkedList<AttributeCommand> leafNodes = new LinkedList<>();
    	getLeafNodes(editor.getTextPane().getDocument().getDefaultRootElement(), leafNodes);

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
    	writer.print(editor.getTextPane().getText());
    	
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
//    	blockCaretListener = true;//the caret listener needs not worry of this...
    	editor.getTextPane().setText("");//empty the pane
    	
//    	System.out.println(file);
    	
    	Scanner sc = null;
		try {
			sc = new Scanner(editor.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
		if(!sc.hasNextLine()) {
//			blockCaretListener = false;
			return;
		}
		
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
    		SimpleAttributeSet attr = new SimpleAttributeSet();
    		for(int j = 0; j < attributes.size(); j++) {
    			attr.addAttribute(AttributeCommand.objectMap.get(attributes.get(j)), revisions.get(j));
    			//editor.setAttribute(AttributeCommand.objectMap.get(attributes.get(j)), revisions.get(j));
    		}
    		
    		editor.getTextPane().setCharacterAttributes(attr, true);
    		
    		//Paste the content
    		if(i == differences.length - 1)
    			editor.getTextPane().replaceSelection(contentAssembler.substring(difference.getStartIndex()));
    		else
    			editor.getTextPane().replaceSelection(contentAssembler.substring(difference.getStartIndex(), difference.getEndIndex()));
    	}
    	
    	sc.close();
//    	blockCaretListener = false;//Nothing happend here...
    	
//    	editor.setCaretPosition(contentAssembler.length());//have the caret listener update the gui to the last position in the text
    }
}
