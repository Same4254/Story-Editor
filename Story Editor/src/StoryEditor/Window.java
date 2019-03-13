package StoryEditor;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import Util.ImageUtils;

/**
 * This is the main class where the story editing will originate from. 
 * To start with, this will offer a frame with a text editor, tabs of different chapters and a file tree of all the chapters to edit
 * 
 * @author Sam
 */
public class Window extends JSplitPane {
	private static final long serialVersionUID = 1L;
	
	private FileTreePanel fileTree;
	private ChapterEditor editor;
	
	public Window() {

	}
	
	private Window init() {
		fileTree = new FileTreePanel(this);
		editor = new ChapterEditor(this);
		
		setTopComponent(fileTree);
		setBottomComponent(editor);
		
		return this;
	}
	
	public FileTreePanel getFileTreePanel() { return fileTree; }
	public ChapterEditor getEditor() { return editor; }
	
	public static void main(String[] args) {
		System.setProperty("line.separator", "\n");
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 800);
		frame.setTitle("Story Editor");
		frame.setIconImage(ImageUtils.readImage("res/book.png"));
		
		frame.add(new Window().init());
		frame.setVisible(true);
	}
}
