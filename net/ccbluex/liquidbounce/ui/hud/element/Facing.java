// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element;

public class Facing
{
    private Horizontal horizontal;
    private Vertical vertical;
    
    public Facing(final Horizontal horizontal, final Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
    
    public Horizontal getHorizontal() {
        return this.horizontal;
    }
    
    public void setHorizontal(final Horizontal horizontal) {
        this.horizontal = horizontal;
    }
    
    public Vertical getVertical() {
        return this.vertical;
    }
    
    public void setVertical(final Vertical vertical) {
        this.vertical = vertical;
    }
    
    @Override
    public String toString() {
        return this.horizontal.getName() + "," + this.vertical.getName();
    }
    
    public enum Horizontal
    {
        LEFT("Left"), 
        MIDDLE("Middle"), 
        RIGHT("Right");
        
        private final String name;
        
        private Horizontal(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Horizontal getByName(final String name) {
            for (final Horizontal horizontal : values()) {
                if (horizontal.getName().equals(name)) {
                    return horizontal;
                }
            }
            return null;
        }
    }
    
    public enum Vertical
    {
        UP("Up"), 
        MIDDLE("Middle"), 
        DOWN("Down");
        
        private final String name;
        
        private Vertical(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Vertical getByName(final String name) {
            for (final Vertical vertical : values()) {
                if (vertical.getName().equals(name)) {
                    return vertical;
                }
            }
            return null;
        }
    }
}
