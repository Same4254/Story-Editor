package StoryEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import StoryEditor.RightClickMenu.Lambda;
import StoryEditor.RightClickMenu.RightClickMenu;

public class FileTreePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTree tree;
	
	private FileNode root;
	private Window window;
	
	private RightClickMenu rightClickMenu;
	
	public FileTreePanel(Window window) {
		this.window = window;
		
		setMinimumSize(new Dimension(125, 0));
		
		root = new FileNode(new File("res/Chapters"));
		exploreNode(root);
		
		tree = new JTree(root);
		tree.setRootVisible(true);
		tree.setShowsRootHandles(true);
		
		tree.expandPath(new TreePath(root.getPath()));
		
		rightClickMenu = new RightClickMenu(new String[] { "Test" }, new Lambda() {
			@Override
			public void run() {
				System.out.println("Here");
			}
		});
		
		MouseAdapter adapter = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					
					if(path != null) {
						FileNode node = (FileNode) path.getLastPathComponent();
						window.getEditor().addChapterTextEditor(node.getFile());
					}
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) { 
					rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		
		tree.addMouseListener(adapter);
		
		tree.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					FileNode node = (FileNode) tree.getLastSelectedPathComponent();
					
					if(node == null)
						return;
					
					
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		JScrollPane scroll = new JScrollPane(tree);
		
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}
	
	private void deleteFile(File file) {
	    File[] contents = file.listFiles();
	    if(contents != null) {
	        for(File f : contents) {
	            if(!Files.isSymbolicLink(f.toPath())) 
	                deleteFile(f);
	        }
	    }
	    
	    file.delete();
	}
	
//	public void changeRoot(Game game) {
//		if(game.isSuprivised())
//			root = new FileNode(new File("res/" + game + "/Populations"), FileNode.FileType.GENERATION);
//		else
//			root = new FileNode(new File("res/" + game + "/Populations"), FileNode.FileType.POPULATION_TYPE);
//		
//		exploreNode(root);
//		
//		tree.clearSelection();
//		
//		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
//		model.setRoot(root);
//		
//		tree.expandPath(new TreePath(root.getPath()));
//	}

	public FileNode addFile(File file, FileNode parent) {
		FileNode node = new FileNode(file);
		
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.insertNodeInto(node, parent, parent.getChildCount());
		
		return node;
	}
	
	public void addAll(File[] files, FileNode parent) {
		FileNode[] nodes = new FileNode[files.length];
		
		for(int i = 0; i < files.length; i++)
			nodes[i] = new FileNode(files[i]);
		
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		for(FileNode node : nodes)
			model.insertNodeInto(node, parent, parent.getChildCount());
	}
	
	private void exploreNode(FileNode node) {
		File rootFile = node.getFile();
		File[] childerenFiles = rootFile.listFiles();
		
		if(childerenFiles == null)
			return;
		
		for(File file : childerenFiles) {
			FileNode temp = new FileNode(file);
			
			exploreNode(temp);
			node.add(temp);
		}
	}
	
	public JTree getTree() { return tree; }
	public FileNode getRootNode() { return root; }
}
