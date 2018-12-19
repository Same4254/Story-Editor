package TextEditor;

import java.awt.Component;
import java.io.File;

import StoryEditor.PageEditor;
import StoryEditor.Window;
import StoryEditor.TabDragging.TabComponent;
import StoryEditor.TabDragging.TabDragPane;

public class ChapterEditor extends TabDragPane {
	private static final long serialVersionUID = 1L;

	private Window window;
	
	public ChapterEditor(Window window) {
		this.window = window;
			
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
		pageEditor.setFile(new File(chapter.getPath()));
		
		addTab(chapter.getName(), pageEditor);
		
		setSelectedIndex(getTabCount() - 1);
		setTabComponentAt(getTabCount() - 1, new TabComponent(this, chapter.getName()));
	}
}
