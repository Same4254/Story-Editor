package StoryEditor;

import java.awt.Component;
import java.io.File;

import javax.swing.JTabbedPane;

import TextEditor.PageEditor;

public class Editor extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	private StoryEditor storyEditor;
	
	public Editor(StoryEditor storyEditor) {
		this.storyEditor = storyEditor;
		
	}
	
	public void addChapterTextEditor(File chapter) {
		int tabCount = getTabCount();
		for(int i = 0; i < tabCount; i++) {
			Component c = getComponentAt(i);
			
			if(c instanceof PageEditor) {
				PageEditor pageEditor = (PageEditor) c;
				
				if(pageEditor.getFile().equals(chapter)) {
					setSelectedIndex(i);
					
					return;
				}
			}
		}
		
		PageEditor pageEditor = new PageEditor();
		pageEditor.setFile(chapter);
		
		addTab(chapter.getName(), pageEditor);
		setSelectedIndex(getTabCount() - 1);
	}
}
