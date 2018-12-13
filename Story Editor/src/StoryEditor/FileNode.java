package StoryEditor;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
	
	private File file;
	
	public FileNode(File file) {
		super(file.getName());
		
		this.file = file;
	}
	
	public File getFile() { return file; }
}
