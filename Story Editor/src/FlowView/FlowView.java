package FlowView;

import javax.swing.JPanel;

public class FlowView extends JPanel {
	private static final long serialVersionUID = 1L;

	/*
	 * This View will allow the user to plan out what they want to achieve in the story or chapter. 
	 * Essentially, there will be several boxes that the user can move and resize (possibly lock to a grid? Option?). 
	 * They should be able to type in these shapes. Obviously, if the text is to long, scroll pane if it has keyboard focus, 
	 * 		otherwise don't render the scroll bar.
	 * They should be able to click and drag the shapes to what they desire
	 * 		- Brings an issue of z ordering. Simple/Inefficient Solution: Use an arrayList to keep z-order?
	 * There becomes an issue of rendering, the elments have reference to other elements and should draw lines to them,
	 * 		but there needs to be a way to differentiate was has already been rendered so that the same thing isn't rendered twice
	 * 
	 * This shouldn't be screen size dependent, the user should be able to move around the environement
	 * 		- Only render what is actually on the screen.
	 * 
	 * They can also connect these boxes with curved line (bezier?). How will these connect? (certain lock points?)
	 * 		- The lines should be as curvy as the user desires: dynamically add control points to the curve 
	 * Change colors of lines and borders of the shapes (maybe a filter function to gray all colors except for a select few?)
	 * 
	 * Maybe be able to edit a file that directly correlates to that one shpe of detail? 
	 * Or rather maybe be able to highlight what paragraphs pertain to what element in the flow view?
	 * Make this an option? Flow highlight mode...? Needs a better name. This entire thing needs a better name.
	 */
	public FlowView() {
		
	}
}
