// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import java.util.Collections;
import java.util.List;
import org.jglr.jchroma.utils.KeyboardKeys;
import com.sun.jna.Structure;
import org.jglr.jchroma.utils.ColorRef;

public class CustomKeyboardEffect extends KeyboardEffect
{
    public static final int ROW_COUNT = 6;
    public static final int COLUMN_COUNT = 22;
    private final CustomStructure struct;
    
    public CustomKeyboardEffect() {
        this(createEmptyArray());
    }
    
    private static ColorRef[][] createEmptyArray() {
        final ColorRef[][] arr = new ColorRef[6][22];
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 22; ++j) {
                arr[i][j] = ColorRef.NULL;
            }
        }
        return arr;
    }
    
    public CustomKeyboardEffect(final ColorRef[][] colors) {
        if (colors.length != 6) {
            throw new IllegalStateException("Colors array must be a 6x22 (row, column) 2D ColorRef array");
        }
        for (int i = 0; i < 6; ++i) {
            final ColorRef[] row = colors[i];
            if (row == null || row.length != 22) {
                throw new IllegalStateException("Colors array must be a 6x22 (row, column) 2D ColorRef array");
            }
        }
        this.struct = new CustomStructure();
        this.setColors(colors);
    }
    
    @Override
    public KeyboardEffectType getType() {
        return KeyboardEffectType.CUSTOM;
    }
    
    @Override
    public Structure createParameter() {
        return this.struct;
    }
    
    public void setColors(final ColorRef[][] colors) {
        for (int j = 0; j < 22; ++j) {
            for (int i = 0; i < 6; ++i) {
                this.setColor(i, j, colors[i][j]);
            }
        }
    }
    
    public void setColor(final int row, final int column, final ColorRef color) {
        this.struct.colors[column + row * 22] = color.getValue();
    }
    
    public void setKeyColor(final int key, final ColorRef color) {
        this.setColor(KeyboardKeys.getRow(key), KeyboardKeys.getColumn(key), color);
    }
    
    public void fill(final ColorRef color) {
        for (int j = 0; j < 22; ++j) {
            for (int i = 0; i < 6; ++i) {
                this.setColor(i, j, color);
            }
        }
    }
    
    public CustomKeyboardEffect combine(final CustomKeyboardEffect other) {
        return new CombinedCustomKeyboardEffect(this, other);
    }
    
    public ColorRef getColor(final int row, final int column) {
        return ColorRef.fromBGR(this.struct.colors[column + row * 22]);
    }
    
    public static class CustomStructure extends Structure implements Structure.ByReference
    {
        public int[] colors;
        
        public CustomStructure() {
            this.colors = new int[132];
        }
        
        protected List getFieldOrder() {
            return Collections.singletonList("colors");
        }
    }
}
