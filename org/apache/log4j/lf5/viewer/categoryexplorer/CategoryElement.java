// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.lf5.viewer.categoryexplorer;

public class CategoryElement
{
    protected String _categoryTitle;
    
    public CategoryElement() {
    }
    
    public CategoryElement(final String title) {
        this._categoryTitle = title;
    }
    
    public String getTitle() {
        return this._categoryTitle;
    }
    
    public void setTitle(final String title) {
        this._categoryTitle = title;
    }
}
