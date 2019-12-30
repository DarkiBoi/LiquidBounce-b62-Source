// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

public final class FormattingInfo
{
    private static final char[] SPACES;
    private static final FormattingInfo DEFAULT;
    private final int minLength;
    private final int maxLength;
    private final boolean leftAlign;
    
    public FormattingInfo(final boolean leftAlign, final int minLength, final int maxLength) {
        this.leftAlign = leftAlign;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
    
    public static FormattingInfo getDefault() {
        return FormattingInfo.DEFAULT;
    }
    
    public boolean isLeftAligned() {
        return this.leftAlign;
    }
    
    public int getMinLength() {
        return this.minLength;
    }
    
    public int getMaxLength() {
        return this.maxLength;
    }
    
    public void format(final int fieldStart, final StringBuffer buffer) {
        final int rawLength = buffer.length() - fieldStart;
        if (rawLength > this.maxLength) {
            buffer.delete(fieldStart, buffer.length() - this.maxLength);
        }
        else if (rawLength < this.minLength) {
            if (this.leftAlign) {
                final int fieldEnd = buffer.length();
                buffer.setLength(fieldStart + this.minLength);
                for (int i = fieldEnd; i < buffer.length(); ++i) {
                    buffer.setCharAt(i, ' ');
                }
            }
            else {
                int padLength;
                for (padLength = this.minLength - rawLength; padLength > 8; padLength -= 8) {
                    buffer.insert(fieldStart, FormattingInfo.SPACES);
                }
                buffer.insert(fieldStart, FormattingInfo.SPACES, 0, padLength);
            }
        }
    }
    
    static {
        SPACES = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        DEFAULT = new FormattingInfo(false, 0, Integer.MAX_VALUE);
    }
}
