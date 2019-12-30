// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.lf5.viewer.categoryexplorer;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JTree;
import javax.swing.Icon;
import javax.swing.tree.DefaultTreeCellEditor;

public class CategoryImmediateEditor extends DefaultTreeCellEditor
{
    private CategoryNodeRenderer renderer;
    protected Icon editingIcon;
    
    public CategoryImmediateEditor(final JTree tree, final CategoryNodeRenderer renderer, final CategoryNodeEditor editor) {
        super(tree, renderer, editor);
        this.editingIcon = null;
        (this.renderer = renderer).setIcon(null);
        renderer.setLeafIcon(null);
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        super.editingIcon = null;
    }
    
    public boolean shouldSelectCell(final EventObject e) {
        boolean rv = false;
        if (e instanceof MouseEvent) {
            final MouseEvent me = (MouseEvent)e;
            final TreePath path = this.tree.getPathForLocation(me.getX(), me.getY());
            final CategoryNode node = (CategoryNode)path.getLastPathComponent();
            rv = node.isLeaf();
        }
        return rv;
    }
    
    public boolean inCheckBoxHitRegion(final MouseEvent e) {
        final TreePath path = this.tree.getPathForLocation(e.getX(), e.getY());
        if (path == null) {
            return false;
        }
        final CategoryNode node = (CategoryNode)path.getLastPathComponent();
        boolean rv = false;
        final Rectangle bounds = this.tree.getRowBounds(this.lastRow);
        final Dimension checkBoxOffset = this.renderer.getCheckBoxOffset();
        bounds.translate(this.offset + checkBoxOffset.width, checkBoxOffset.height);
        rv = bounds.contains(e.getPoint());
        return true;
    }
    
    protected boolean canEditImmediately(final EventObject e) {
        boolean rv = false;
        if (e instanceof MouseEvent) {
            final MouseEvent me = (MouseEvent)e;
            rv = this.inCheckBoxHitRegion(me);
        }
        return rv;
    }
    
    protected void determineOffset(final JTree tree, final Object value, final boolean isSelected, final boolean expanded, final boolean leaf, final int row) {
        this.offset = 0;
    }
}
