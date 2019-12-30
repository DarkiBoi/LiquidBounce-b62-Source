// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import org.jglr.jchroma.utils.KeyboardKeys;
import org.jglr.jchroma.utils.ColorRef;

public class ProgressKeyboardEffect extends CustomKeyboardEffect
{
    private final int[] keys;
    private int currentValue;
    private ColorRef inRangeColor;
    private ColorRef outsideRangeColor;
    private int minimumValue;
    private int maximumValue;
    private int[] modelToView;
    
    public ProgressKeyboardEffect(final int from, final int to) {
        this(createFromToArray(from, to));
    }
    
    private static int[] createFromToArray(final int from, final int to) {
        final int[] result = new int[to - from + 1];
        for (int i = 0; i < result.length; ++i) {
            result[i] = from + i;
        }
        return result;
    }
    
    public ProgressKeyboardEffect(final int[] keys) {
        this.keys = keys;
        this.modelToView = createFromToArray(0, keys.length);
        this.minimumValue = 0;
        this.maximumValue = keys.length;
        this.inRangeColor = new ColorRef(255, 255, 255);
        this.outsideRangeColor = new ColorRef(0, 0, 0);
        this.setCurrentValue(this.maximumValue);
    }
    
    public ColorRef getOutsideRangeColor() {
        return this.outsideRangeColor;
    }
    
    public void setOutsideRangeColor(final ColorRef outsideRangeColor) {
        this.outsideRangeColor = outsideRangeColor;
    }
    
    public ColorRef getInRangeColor() {
        return this.inRangeColor;
    }
    
    public void setInRangeColor(final ColorRef inRangeColor) {
        this.inRangeColor = inRangeColor;
    }
    
    public int getMinimumValue() {
        return this.minimumValue;
    }
    
    public void setMinimumValue(final int minimumValue) {
        this.minimumValue = minimumValue;
        this.updateModelToView();
    }
    
    private void updateModelToView() {
        this.modelToView = new int[this.maximumValue - this.minimumValue + 1];
        final float scale = this.keys.length / (float)this.modelToView.length;
        for (int i = 0; i < this.modelToView.length; ++i) {
            this.modelToView[i] = (int)(scale * i);
        }
    }
    
    public int getMaximumValue() {
        return this.maximumValue;
    }
    
    public void setMaximumValue(final int maximumValue) {
        this.maximumValue = maximumValue;
        this.updateModelToView();
    }
    
    public int getCurrentValue() {
        return this.currentValue;
    }
    
    public void setCurrentValue(int currentValue) {
        if (currentValue < this.minimumValue) {
            currentValue = this.minimumValue;
        }
        if (currentValue > this.maximumValue) {
            currentValue = this.maximumValue;
        }
        this.currentValue = currentValue;
        for (int i = this.minimumValue; i < this.maximumValue; ++i) {
            final int index = this.modelToView[i - this.minimumValue];
            final int key = this.keys[index];
            final int row = KeyboardKeys.getRow(key);
            final int column = KeyboardKeys.getColumn(key);
            if (i <= currentValue) {
                this.setColor(row, column, this.inRangeColor);
            }
            else {
                this.setColor(row, column, this.outsideRangeColor);
            }
        }
    }
}
