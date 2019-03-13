package StoryEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import Story.NewChapterFrame;
import Story.NewStoryFrame;
import StoryEditor.RightClickMenu.LambdaMenuItem;
import StoryEditor.RightClickMenu.RightClickMenu;
import TextEditor.PageEditor;

public class FileTreePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTree tree;
	
	private FileNode root;
	private Window window;
	
	private RightClickMenu rightClickMenu;
	
	public FileTreePanel(Window window) {
		this.window = window;
		
		setMinimumSize(new Dimension(125, 0));
		
		root = new FileNode(new File("res/Books"));
		exploreNode(root);
		
		tree = new JTree(root);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		
		tree.expandPath(new TreePath(root.getPath()));
		
		rightClickMenu = new RightClickMenu();

		MouseAdapter adapter = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					
					if(path != null) {
						FileNode node = (FileNode) path.getLastPathComponent();
						if(node.getFile().getName().endsWith(".chp"))
							window.getEditor().addChapterTextEditor(node.getFile());
					}
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) { 
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					
					if(path == null) {//No node selected
						rightClickMenu.setMenuItems(
							new LambdaMenuItem("New Story", () -> {
								new NewStoryFrame(FileTreePanel.this.window);
							})
						);
					} else {
						tree.setSelectionPath(path);
						FileNode node = (FileNode) path.getLastPathComponent();
					
						if(node.getFile().getName().equals("Chapters")) {//TODO replace this with some form of enumeration rather than a string
							rightClickMenu.setMenuItems(
//								new LambdaMenuItem("Delete", () -> {
//									delete(node);
//								}),
								
								new LambdaMenuItem("New Chapter", () -> {
									new NewChapterFrame(window, node);
								})
							);
						} else {
							rightClickMenu.setMenuItems(
								new LambdaMenuItem("Delete", () -> {
									delete(node);
								})
							);
						}
					}
					
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
					
					delete(node);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		tree.expandPath(new TreePath(((DefaultMutableTreeNode)root.getChildAt(0).getChildAt(0).getChildAt(3)).getPath()));
		
		JScrollPane scroll = new JScrollPane(tree);
		
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}
	
	public void reExplore() {
		reExplore(root);
	}
	
	public void reExplore(FileNode parent) {
		exploreNode(parent);
		reloadTree();
	}
	
	private void reloadTree() {
	    ArrayList<TreePath> expanded = new ArrayList<>();
	    for (int i = 0; i < tree.getRowCount() - 1; i++) {
	        TreePath currPath = tree.getPathForRow(i);
	        TreePath nextPath = tree.getPathForRow(i + 1);
	        if (currPath.isDescendant(nextPath)) 
	            expanded.add(currPath);
	    }
	    
	    ((DefaultTreeModel) tree.getModel()).reload();
	    for (TreePath path : expanded)
	        tree.expandPath(path);
	}
	
	private void delete(FileNode node) {
		if(JOptionPane.showConfirmDialog(this, "Are you sure you would like to delete: " + node.getFile().getName() + "?", "Delete Confirmation", JOptionPane.YES_NO_OPTION) == 0) {
			if(node.getFile().getName().endsWith(".chp")) {
				int tabCount = window.getEditor().getTabCount();
				for(int i = 0; i < tabCount; i++) {
					Component c = window.getEditor().getComponentAt(i);
					
					if(c instanceof PageEditor) {
						PageEditor pageEditor = (PageEditor) c;
						
						if(pageEditor.getFile().equals(node.getFile())) {
							window.getEditor().removeTabAt(i);
							return;
						}
					}
				}
			}
			
			deleteFile(node.getFile());
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			model.removeNodeFromParent(node);
		}
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
		
		out:
		for(File file : childerenFiles) {
			FileNode temp = new FileNode(file);
			
			for(int i = 0; i < node.getChildCount(); i++)
				if(file.equals(((FileNode) node.getChildAt(i)).getFile()))
					continue out;
			
			exploreNode(temp);
			node.add(temp);
		}
	}
	
	public JTree getTree() { return tree; }
	public FileNode getRootNode() { return root; }
}
