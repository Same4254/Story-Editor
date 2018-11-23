package TextEditor.PageKit;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JEditorPane;

import TextEditor.PageKit.PageableEditorKit.SectionView;

/**
 *
 *
 * @author Stanislav Lapitsky
 * @version 1.0
 */

public class PaginationPrinter implements Printable, Pageable {
    private PageFormat pageFormat;
    private JEditorPane editorPane;
    boolean isPaginated = false;
    PageableEditorKit kit = null;

    public PaginationPrinter(PageFormat pageFormat, JEditorPane pane) {
        this.pageFormat = pageFormat;
        this.editorPane = pane;

        if (pane.getEditorKit() instanceof PageableEditorKit) {
            isPaginated = true;

            kit = (PageableEditorKit) pane.getEditorKit();
            kit.setPageWidth( (int) pageFormat.getWidth() + PageableEditorKit.DRAW_PAGE_INSET_X * 2);
            kit.setPageHeight( (int) pageFormat.getHeight() + PageableEditorKit.DRAW_PAGE_INSET_Y * 2);
            int top = (int) pageFormat.getImageableY();
            int left = (int) pageFormat.getImageableX();
            int bottom = (int) (pageFormat.getHeight() - pageFormat.getImageableHeight() - top);
            int right = (int) (pageFormat.getWidth() - pageFormat.getImageableWidth() - left);
            kit.setPageMargins(new Insets(top, left, bottom, right));

            pane.revalidate();
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Graphics2D g2d = (Graphics2D) g;
        if (isPaginated) {
            g2d.translate(0, -kit.getPageHeight() * pageIndex);
            
            g2d.translate( -PageableEditorKit.DRAW_PAGE_INSET_X, -PageableEditorKit.DRAW_PAGE_INSET_Y);
            Shape oldClip=g2d.getClip();
            
            g2d.setClip(0,kit.getPageHeight() * pageIndex,kit.getPageWidth(), kit.getPageHeight());
            editorPane.printAll(g2d);
            
            g2d.setClip(oldClip);
            if (pageIndex < getPageCount()) {
                return PAGE_EXISTS;
            }
        }
        else {
            if (pageIndex == 0) {
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                editorPane.printAll(g);
                return PAGE_EXISTS;
            }
        }
        return NO_SUCH_PAGE;
    }

    public int getPageCount() {
        if (isPaginated) {
            return ( (SectionView) editorPane.getUI().getRootView(editorPane).getView(0)).getPageCount();
        }
        return 1;
    }

    public int getNumberOfPages() {
        return getPageCount();
    }

    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return pageFormat;
    }

    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return this;
    }
}
