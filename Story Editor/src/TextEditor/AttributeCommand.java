package TextEditor;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;

public class AttributeCommand {
	public static final Object[] constantsToCheck = {
		StyleConstants.FontFamily,
		StyleConstants.FontSize,
		StyleConstants.Bold,
		StyleConstants.Italic,
		StyleConstants.Underline
	};
	
	public static HashMap<String, Object> objectMap;
	
	static {
		objectMap = new HashMap<>();
		
		for(Object constant : constantsToCheck)
			objectMap.put(constant.toString(), constant);
	}
	
	private int startIndex, endIndex;
	private ArrayList<String> attributes;
	private ArrayList<Object> newRevision;
	
	public AttributeCommand(AttributeSet set1, AttributeSet set2, int startIndex) {
		this.attributes = new ArrayList<>();
		this.newRevision = new ArrayList<>();
		this.startIndex = startIndex;
		
		if(set1 == null) {
			for(Object constant : constantsToCheck) {
				attributes.add(constant.toString());
				newRevision.add(set2.getAttribute(constant));
			}
		} else {
			for(Object constant : constantsToCheck) {
				Object value1 = set1.getAttribute(constant);
				Object value2 = set2.getAttribute(constant);
				
				if(!value1.equals(value2)) {
					attributes.add(constant.toString());
					newRevision.add(set2.getAttribute(constant));
				}
			}
		}
	}
	
	public AttributeCommand() {
		attributes = new ArrayList<>();
		newRevision = new ArrayList<>();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(startIndex + "-" + endIndex + ",");
		for(int i = 0; i < attributes.size(); i++) {
			builder.append(attributes.get(i) + ":" + newRevision.get(i));
			
			if(i != attributes.size() - 1)
				builder.append(",");
		}
		
		return builder.toString();
	}
	
	public int getStartIndex() { return startIndex; }
	public void setStartIndex(int startIndex) { this.startIndex = startIndex; }
	
	public int getEndIndex() { return endIndex; }
	public void setEndIndex(int endIndex) { this.endIndex = endIndex; }
	
	public void addAttributeRevision(String attribute, Object value) {
		attributes.add(attribute);
		newRevision.add(value);
	}
	
	public ArrayList<String> getAttributes() { return attributes; }
	public ArrayList<Object> getRevisions() { return newRevision; }
}
