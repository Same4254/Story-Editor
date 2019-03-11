package TextEditor.PageKit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class PageableEditorKit extends StyledEditorKit {
	private static final long serialVersionUID = 1L;
	
	PageableViewFactory factory = new PageableViewFactory();
    protected int pageWidth = 800;
    protected int pageHeight = 1000;
    public static int DRAW_PAGE_INSET_X = 150, DRAW_PAGE_INSET_Y = 20;
    protected Insets pageMargins = new Insets(20, 20, 20, 20);

    private JTextPane pane;
    
    private int pageX;

//    private double zoom;
    
    /**
     * Constructs kit instance
     */
    public PageableEditorKit(JTextPane pane) {
    	this.pane = pane;
    	
//    	zoom = 1.0;
    }

    /**
     * Sets page width
     * @param width int width value in pixels
     */
    public void setPageWidth(int width) {
        pageWidth = width;
    }

    /**
     * gets page width
     * @return int width in pixels
     */
    public int getPageWidth() {
        return pageWidth;
    }
    
    public int getPageX() { 
    	return pageX;
    }

    /**
     * Sets page height
     * @param height int height in pixels
     */
    public void setPageHeight(int height) {
        pageHeight = height;
    }

    private void calcDrawInsets() {
    	if(pageWidth > pane.getWidth())
    		DRAW_PAGE_INSET_X = 0;
    	else
    		DRAW_PAGE_INSET_X = (pane.getWidth() / 2) - (pageWidth / 2);
    }
    
    /**
     * Gets page height
     * @return int height in pixels
     */
    public int getPageHeight() {
        return pageHeight;
    }

    /**
     * Sets page margins (distance between page content and page edge.
     * @param margins Insets margins.
     */
    public void setPageMargins(Insets margins) {
        pageMargins = margins;
    }

//    /**
//     * Collateral method to create application GUI
//     * @return JFrame
//     */
//    public JFrame init() {
//        JFrame frame = new JFrame("Pagination");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        final JEditorPane editor = new JEditorPane();
//        editor.setEditorKit(this);
//        JScrollPane scroll = new JScrollPane(editor);
//        frame.getContentPane().add(scroll);
//        frame.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
//
//        return frame;
//    }

    /**
     * Prints pages content
     * @param editor JEditorPane pane with content.
     * @param pp PaginationPrinter Printable implementation.
     */
    protected void print(JEditorPane editor, PaginationPrinter pp) {
        PrinterJob pJob = PrinterJob.getPrinterJob();
        //by default paper is letter
        pJob.setPageable(pp);
        try {
            pJob.print();
        }
        catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        new PageableEditorKit().init().setVisible(true);
//    }

    /**
     * gets kit view factory.
     * @return ViewFactory
     */
    public ViewFactory getViewFactory() {
        return factory;
    }

    /**
     * The view factory class creates custom views for pagination
     * root view (SectionView class) and paragraph (PageableParagraphView class)
     *
     * @author Stanislav Lapitsky
     * @version 1.0
     */
    class PageableViewFactory implements ViewFactory {
        /**
         * Creates view for specified element.
         * @param elem Element parent element
         * @return View created view instance.
         */
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
//                    return new LabelView(elem);
                	return new WrapLabelView(elem);
                }
                else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new PageableParagraphView(elem);
                }
                else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new SectionView(elem, View.Y_AXIS);
                }
                else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                }
                else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }
            // default to text display
            return new LabelView(elem);
        }

    }
    
    class WrapLabelView extends LabelView {
        public WrapLabelView(Element element) { super(element); }

        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }

    /**
     * Root view which perform pagination and paints frame around pages.
     *
     * @author Stanislav Lapitsky
     * @version 1.0
     */
    class SectionView extends BoxView {
        int pageNumber = 0;

        /**
         * Creates view instace
         * @param elem Element
         * @param axis int
         */
        public SectionView(Element elem, int axis) {
            super(elem, axis);
        }

        /**
         * Gets amount of pages
         * @return int
         */
        public int getPageCount() {
            return pageNumber;
        }

        /**
         * Perform layout on the box
         *
         * @param width the width (inside of the insets) >= 0
         * @param height the height (inside of the insets) >= 0
         */
        protected void layout(int width, int height) {
        	calcDrawInsets();
        	
            width = pageWidth - pageMargins.left - pageMargins.right;
            this.setInsets( (short) (DRAW_PAGE_INSET_Y + pageMargins.top), (short) (DRAW_PAGE_INSET_X + pageMargins.left), (short) (DRAW_PAGE_INSET_Y + pageMargins.bottom),
                           (short) (DRAW_PAGE_INSET_X + pageMargins.right));
            super.layout(width, height);
        }

        /**
         * Determines the maximum span for this view along an
         * axis.
         *
         * overriddedn
         */
        public float getMaximumSpan(int axis) {
            return getPreferredSpan(axis);
        }

        /**
         * Determines the minimum span for this view along an
         * axis.
         *
         * overriddedn
         */
        public float getMinimumSpan(int axis) {
            return getPreferredSpan(axis);
        }

        /**
         * Determines the preferred span for this view along an
         * axis.
         * overriddedn
         */
        public float getPreferredSpan(int axis) {
            float span = 0;
            if (axis == View.X_AXIS) {
                span = pageWidth;
            }
            else {
                span = pageHeight * getPageCount();
            }
            return span;
        }

        /**
         * Performs layout along Y_AXIS with shifts for pages.
         *
         * @param targetSpan int
         * @param axis int
         * @param offsets int[]
         * @param spans int[]
         */
        protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
            super.layoutMajorAxis(targetSpan, axis, offsets, spans);
            int totalOffset = 0;
            int n = offsets.length;
            pageNumber = 0;
            for (int i = 0; i < n; i++) {
                offsets[i] = totalOffset;
                View v = getView(i);
                if (v instanceof MultiPageView) {
                    ( (MultiPageView) v).setBreakSpan(0);
                    ( (MultiPageView) v).setAdditionalSpace(0);
                }

                if ( (offsets[i] + spans[i]) > (pageNumber * pageHeight - DRAW_PAGE_INSET_Y * 2 - pageMargins.top - pageMargins.bottom)) {
                    if ( (v instanceof MultiPageView) && (v.getViewCount() > 1)) {
                        MultiPageView multipageView = (MultiPageView) v;
                        int space = offsets[i] - (pageNumber - 1) * pageHeight;
                        int breakSpan = (pageNumber * pageHeight - DRAW_PAGE_INSET_Y * 2 - pageMargins.top - pageMargins.bottom) - offsets[i];
                        multipageView.setBreakSpan(breakSpan);
                        multipageView.setPageOffset(space);
                        multipageView.setStartPageNumber(pageNumber);
                        multipageView.setEndPageNumber(pageNumber);
                        int height = (int) getHeight();

                        int width = ( (BoxView) v).getWidth();
                        if (v instanceof PageableParagraphView) {
                            PageableParagraphView parView = (PageableParagraphView) v;
                            parView.layout(width, height);
                        }

                        pageNumber = multipageView.getEndPageNumber();
                        spans[i] += multipageView.getAdditionalSpace();
                    }
                    else {
                        offsets[i] = pageNumber * pageHeight;
                        pageNumber++;
                    }
                }
                totalOffset = (int) Math.min( (long) offsets[i] + (long) spans[i], Integer.MAX_VALUE);
            }
        }

        /**
         * Paints view content and page frames.
         * @param g Graphics
         * @param a Shape
         */
        public void paint(Graphics graphics, Shape a) {
        	Graphics2D g = (Graphics2D) graphics;
        	
        	calcDrawInsets();
        	
            Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
            
            int panelWidth = alloc.width + 6;
            int panelHeight = alloc.height + 50;
            
            g.setColor(Color.gray);
            g.fillRect(0, 0, panelWidth, panelHeight);

//            g.scale(zoom, zoom);
            
//            g.scale(zoom, zoom);
            
//            int newPageWidth = (int) (zoom * pageWidth);
//            g.translate(pageWidth - newPageWidth, 0);
            
//            AffineTransform transform = g.getTransform();
//            transform.setToScale(zoom, zoom);
//            g.setTransform(transform);
            
            int pageCount = getPageCount();
            Rectangle page = new Rectangle();
            page.x = alloc.x;
            
            pageX = page.x + DRAW_PAGE_INSET_X;
            
            page.y = alloc.y;
            page.height = pageHeight;
            page.width = pageWidth;
            String sC = Integer.toString(pageCount);
            for (int i = 0; i < pageCount; i++) {
                page.y = alloc.y + pageHeight * i;
//                paintPageFrame(g, page);
                g.setColor(Color.white);
                g.fillRect(page.x + DRAW_PAGE_INSET_X, page.y + DRAW_PAGE_INSET_Y, page.width, page.height - (2 * DRAW_PAGE_INSET_Y));
                
                String sN = Integer.toString(i + 1);
                String pageStr = "Page: " + sN;
                pageStr += " of " + sC;

                g.drawString(pageStr, 
                		(page.x + DRAW_PAGE_INSET_X) + (page.width / 2) - (g.getFontMetrics().stringWidth(pageStr) / 2),
                		page.y + page.height - DRAW_PAGE_INSET_Y + 15);
            }
            
            super.paint(g, a);
//            
//            g.setColor(Color.gray);
//            // Fills background of pages
//            int currentWidth = (int) alloc.getWidth();
//            int currentHeight = (int) alloc.getHeight();
//            int x = page.x + DRAW_PAGE_INSET_X;
//            int y = 0;
//            int w = 0;
//            int h = 0;
//            if (pageWidth < currentWidth) {
//                w = currentWidth;
//                h = currentHeight;
//                g.fillRect(page.x + page.width, alloc.y, w, h);
//            }
//            if (pageHeight * pageCount < currentHeight) {
//                w = currentWidth;
//                h = currentHeight;
//                g.fillRect(page.x, alloc.y + page.height * pageCount, w, h);
//            }
            
            
            
//            g.translate(-1920 + (pageWidth / 2), 0);
        }

        /**
         * Paints frame for specified page
         * @param g Graphics
         * @param page Shape page rectangle
         * @param container Rectangle
         */
        public void paintPageFrame(Graphics g, Rectangle page) {
            Color oldColor = g.getColor();

            //frame
            g.setColor(Color.BLACK);
            g.drawLine(page.x + DRAW_PAGE_INSET_X, page.y + DRAW_PAGE_INSET_Y, page.x + page.width - DRAW_PAGE_INSET_X, page.y + DRAW_PAGE_INSET_Y);
            g.drawLine(page.x + DRAW_PAGE_INSET_X, page.y + DRAW_PAGE_INSET_Y, page.x + DRAW_PAGE_INSET_X, page.y + page.height - DRAW_PAGE_INSET_Y);
            g.drawLine(page.x + DRAW_PAGE_INSET_X, page.y + page.height - DRAW_PAGE_INSET_Y, page.x + page.width - DRAW_PAGE_INSET_X, page.y + page.height - DRAW_PAGE_INSET_Y);
            g.drawLine(page.x + page.width - DRAW_PAGE_INSET_X, page.y + DRAW_PAGE_INSET_Y, page.x + page.width - DRAW_PAGE_INSET_X, page.y + page.height - DRAW_PAGE_INSET_Y);

            //shadow
            g.fillRect(page.x + page.width - DRAW_PAGE_INSET_X, page.y + DRAW_PAGE_INSET_Y + 4, 4, page.height - 2 * DRAW_PAGE_INSET_Y);
            g.fillRect(page.x + DRAW_PAGE_INSET_X + 4, page.y + page.height - DRAW_PAGE_INSET_Y, page.width - 2 * DRAW_PAGE_INSET_X, 4);

            g.setColor(oldColor);
        }

    }
    
//    public void changeZoom(int ticks) {
//    	double delta = ticks / 10.0;
//    	zoom += delta;
//    	
//    	System.out.println("Delta Zoom: " + delta);
//    	System.out.println("Zoom: " + zoom);
//    	System.out.println("------------------------------");
//    }

    /**
     * Represents multipage paragraph.
     *
     * @author Stanislav Lapitsky
     * @version 1.0
     */
    class PageableParagraphView extends ParagraphView implements MultiPageView {
        protected int additionalSpace = 0;
        protected int breakSpan = 0;
        protected int pageOffset = 0;
        protected int startPageNumber = 0;
        protected int endPageNumber = 0;

        public PageableParagraphView(Element elem) {
            super(elem);
        }

        public void layout(int width, int height) {
            super.layout(width, height);
        }

        protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
            super.layoutMajorAxis(targetSpan, axis, offsets, spans);
            performMultiPageLayout(targetSpan, axis, offsets, spans);
        }

        /**
         * Layout paragraph's content splitting between pages if needed.
         * Calculates shifts and breaks for parent view (SectionView)
         * @param targetSpan int
         * @param axis int
         * @param offsets int[]
         * @param spans int[]
         */
        public void performMultiPageLayout(int targetSpan, int axis, int[] offsets, int[] spans) {
            if (breakSpan == 0)
                return;
            int space = breakSpan;

            additionalSpace = 0;
            endPageNumber = startPageNumber;
            int topInset = this.getTopInset();
            int offs = 0;
            for (int i = 0; i < offsets.length; i++) {
                if (offs + spans[i] + topInset > space) {
                    int newOffset = endPageNumber * pageHeight;
                    int addSpace = newOffset - (startPageNumber - 1) * pageHeight - pageOffset - offs - topInset;
                    additionalSpace += addSpace;
                    offs += addSpace;
                    for (int j = i; j < offsets.length; j++) {
                        offsets[j] += addSpace;
                    }
                    endPageNumber++;
                    space = (endPageNumber * pageHeight - 2 * DRAW_PAGE_INSET_Y - pageMargins.top - pageMargins.bottom) - (startPageNumber - 1) * pageHeight - pageOffset;
                }
                offs += spans[i];
            }
        }

        /**
         * Gets view's start page number
         * @return page number
         */
        public int getStartPageNumber() {
            return startPageNumber;
        }

        /**
         * Gets view's end page number
         * @return page number
         */
        public int getEndPageNumber() {
            return endPageNumber;
        }

        /**
         * Gets view's extra space (space between pages)
         * @return extra space
         */
        public int getAdditionalSpace() {
            return additionalSpace;
        }

        /**
         * Gets view's break span
         * @return break span
         */
        public int getBreakSpan() {
            return breakSpan;
        }

        /**
         * Gets view's offsets on the page
         * @return offset
         */
        public int getPageOffset() {
            return pageOffset;
        }

        /**
         * Sets view's start page number
         *
         * @param startPageNumber page number
         */
        public void setStartPageNumber(int startPageNumber) {
            this.startPageNumber = startPageNumber;
        }

        /**
         * Sets view's end page number
         *
         * @param endPageNumber page number
         */
        public void setEndPageNumber(int endPageNumber) {
            this.endPageNumber = endPageNumber;
        }

        /**
         * Sets extra space (space between pages)
         *
         * @param additionalSpace additional space
         */
        public void setAdditionalSpace(int additionalSpace) {
            this.additionalSpace = additionalSpace;
        }

        /**
         * Sets view's break span.
         *
         * @param breakSpan break span
         */
        public void setBreakSpan(int breakSpan) {
            this.breakSpan = breakSpan;
        }

        /**
         * Sets view's offset on the page
         *
         * @param pageOffset offset
         */
        public void setPageOffset(int pageOffset) {
            this.pageOffset = pageOffset;
        }
    }
}


//class WrapEditorKit extends StyledEditorKit {
//	private static final long serialVersionUID = 1L;
//	
//	private ViewFactory defaultFactory;
//	
//	public WrapEditorKit() {
//		defaultFactory = new ViewFactory() {
//			@Override
//			public View create(Element elem) {
//				String kind = elem.getName();
//	            if (kind != null) {
//	                if (kind.equals(AbstractDocument.ContentElementName))
//	                    return new WrapLabelView(elem);
//	                else if (kind.equals(AbstractDocument.ParagraphElementName)) 
//	                    return new ParagraphView(elem);
//	                else if (kind.equals(AbstractDocument.SectionElementName)) 
//	                    return new BoxView(elem, View.Y_AXIS);
//	                else if (kind.equals(StyleConstants.ComponentElementName)) 
//	                    return new ComponentView(elem);
//	                else if (kind.equals(StyleConstants.IconElementName)) 
//	                    return new IconView(elem);
//	            }
//	            return new LabelView(elem);
//			}
//		};
//	}
//	
//    public ViewFactory getViewFactory() { return defaultFactory; }
//}
//
//class WrapLabelView extends LabelView {
//    public WrapLabelView(Element element) { super(element); }
//
//    public float getMinimumSpan(int axis) {
//        switch (axis) {
//            case View.X_AXIS:
//                return 0;
//            case View.Y_AXIS:
//                return super.getMinimumSpan(axis);
//            default:
//                throw new IllegalArgumentException("Invalid axis: " + axis);
//        }
//    }
//}
//
//static class SelectionPreservingCaret extends DefaultCaret {
//	private static final long serialVersionUID = 1L;
//
//	/**
//     * The last SelectionPreservingCaret that lost focus
//     */
//    private static SelectionPreservingCaret last = null;
//
//    /**
//     * The last event that indicated loss of focus
//     */
//    private static FocusEvent lastFocusEvent = null;
//
//    public SelectionPreservingCaret() {
//        // The blink rate is set by BasicTextUI when the text component
//        // is created, and is not (re-) set when a new Caret is installed.
//        // This implementation attempts to pull a value from the UIManager,
//        // and defaults to a 500ms blink rate. This assumes that the
//        // look and feel uses the same blink rate for all text components
//        // (and hence we just pull the value for TextArea). If you are
//        // using a look and feel for which this is not the case, you may
//        // need to set the blink rate after creating the Caret.
//        int blinkRate = 500;
//        Object o = UIManager.get("TextArea.caretBlinkRate");
//        if ((o != null) && (o instanceof Integer)) {
//            Integer rate = (Integer) o;
//            blinkRate = rate.intValue();
//        }
//        setBlinkRate(blinkRate);
//    }
//
//    /**
//     * Called when the component containing the caret gains focus. 
//     * DefaultCaret does most of the work, while the subclass checks
//     * to see if another instance of SelectionPreservingCaret previously
//     * had focus.
//     *
//     * @param e the focus event
//     * @see java.awt.event.FocusListener#focusGained
//     */
//    public void focusGained(FocusEvent evt) {
//        super.focusGained(evt);
//
//        // If another instance of SelectionPreservingCaret had focus and
//        // we defered a focusLost event, deliver that event now.
//        if ((last != null) && (last != this)) {
//            last.hide();
//        }
//    }
//
//    /**
//     * Called when the component containing the caret loses focus. Instead
//     * of hiding both the caret and the selection, the subclass only 
//     * hides the caret and saves a (static) reference to the event and this
//     * specific caret instance so that the event can be delivered later
//     * if appropriate.
//     *
//     * @param e the focus event
//     * @see java.awt.event.FocusListener#focusLost
//     */
//    public void focusLost(FocusEvent evt) {
//        setVisible(false);
//        last = this;
//        lastFocusEvent = evt;
//    }
//
//    /**
//     * Delivers a defered focusLost event to this caret.
//     */
//    protected void hide() {
//        if (last == this) {
//            super.focusLost(lastFocusEvent);
//            last = null;
//            lastFocusEvent = null;
//        }
//    }
//}
