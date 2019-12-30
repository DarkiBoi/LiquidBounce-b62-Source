// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.debug;

import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import jdk.internal.org.objectweb.asm.Label;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.ClassReader;

public class NashornClassReader extends ClassReader
{
    private final Map<String, List<Label>> labelMap;
    private static String[] type;
    
    public NashornClassReader(final byte[] bytecode) {
        super(bytecode);
        this.labelMap = new HashMap<String, List<Label>>();
        this.parse(bytecode);
    }
    
    List<Label> getExtraLabels(final String className, final String methodName, final String methodDesc) {
        final String key = fullyQualifiedName(className, methodName, methodDesc);
        return this.labelMap.get(key);
    }
    
    private static int readByte(final byte[] bytecode, final int index) {
        return (byte)(bytecode[index] & 0xFF);
    }
    
    private static int readShort(final byte[] bytecode, final int index) {
        return (short)((bytecode[index] & 0xFF) << 8) | (bytecode[index + 1] & 0xFF);
    }
    
    private static int readInt(final byte[] bytecode, final int index) {
        return (bytecode[index] & 0xFF) << 24 | (bytecode[index + 1] & 0xFF) << 16 | (bytecode[index + 2] & 0xFF) << 8 | (bytecode[index + 3] & 0xFF);
    }
    
    private static long readLong(final byte[] bytecode, final int index) {
        final int hi = readInt(bytecode, index);
        final int lo = readInt(bytecode, index + 4);
        return (long)hi << 32 | (long)lo;
    }
    
    private static String readUTF(final int index, final int utfLen, final byte[] bytecode) {
        final int endIndex = index + utfLen;
        final char[] buf = new char[utfLen * 2];
        int strLen = 0;
        int st = 0;
        char cc = '\0';
        int i = index;
        while (i < endIndex) {
            int c = bytecode[i++];
            switch (st) {
                case 0: {
                    c &= 0xFF;
                    if (c < 128) {
                        buf[strLen++] = (char)c;
                        continue;
                    }
                    if (c < 224 && c > 191) {
                        cc = (char)(c & 0x1F);
                        st = 1;
                        continue;
                    }
                    cc = (char)(c & 0xF);
                    st = 2;
                    continue;
                }
                case 1: {
                    buf[strLen++] = (char)(cc << 6 | (c & 0x3F));
                    st = 0;
                    continue;
                }
                case 2: {
                    cc = (char)(cc << 6 | (c & 0x3F));
                    st = 1;
                    continue;
                }
                default: {
                    continue;
                }
            }
        }
        return new String(buf, 0, strLen);
    }
    
    private String parse(final byte[] bytecode) {
        int u = 0;
        final int magic = readInt(bytecode, u);
        u += 4;
        assert magic == -889275714 : Integer.toHexString(magic);
        readShort(bytecode, u);
        u += 2;
        readShort(bytecode, u);
        u += 2;
        final int cpc = readShort(bytecode, u);
        u += 2;
        final ArrayList<Constant> cp = new ArrayList<Constant>(cpc);
        cp.add(null);
        for (int i = 1; i < cpc; ++i) {
            final int tag = readByte(bytecode, u);
            ++u;
            switch (tag) {
                case 7: {
                    cp.add(new IndexInfo(cp, tag, readShort(bytecode, u)));
                    u += 2;
                    break;
                }
                case 9:
                case 10:
                case 11: {
                    cp.add(new IndexInfo2(cp, tag, readShort(bytecode, u), readShort(bytecode, u + 2)));
                    u += 4;
                    break;
                }
                case 8: {
                    cp.add(new IndexInfo(cp, tag, readShort(bytecode, u)));
                    u += 2;
                    break;
                }
                case 3: {
                    cp.add(new DirectInfo<Object>(cp, tag, readInt(bytecode, u)));
                    u += 4;
                    break;
                }
                case 4: {
                    cp.add(new DirectInfo<Object>(cp, tag, Float.intBitsToFloat(readInt(bytecode, u))));
                    u += 4;
                    break;
                }
                case 5: {
                    cp.add(new DirectInfo<Object>(cp, tag, readLong(bytecode, u)));
                    cp.add(null);
                    ++i;
                    u += 8;
                    break;
                }
                case 6: {
                    cp.add(new DirectInfo<Object>(cp, tag, Double.longBitsToDouble(readLong(bytecode, u))));
                    cp.add(null);
                    ++i;
                    u += 8;
                    break;
                }
                case 12: {
                    cp.add(new IndexInfo2(cp, tag, readShort(bytecode, u), readShort(bytecode, u + 2)));
                    u += 4;
                    break;
                }
                case 1: {
                    final int len = readShort(bytecode, u);
                    u += 2;
                    cp.add(new DirectInfo<Object>(cp, tag, readUTF(u, len, bytecode)));
                    u += len;
                    break;
                }
                case 16: {
                    cp.add(new IndexInfo(cp, tag, readShort(bytecode, u)));
                    u += 2;
                    break;
                }
                case 18: {
                    cp.add(new IndexInfo2(cp, tag, readShort(bytecode, u), readShort(bytecode, u + 2)) {
                        @Override
                        public String toString() {
                            return "#" + this.index + ' ' + this.cp.get(this.index2).toString();
                        }
                    });
                    u += 4;
                    break;
                }
                case 15: {
                    final int kind = readByte(bytecode, u);
                    assert kind >= 1 && kind <= 9 : kind;
                    cp.add(new IndexInfo2(cp, tag, kind, readShort(bytecode, u + 1)) {
                        @Override
                        public String toString() {
                            return "#" + this.index + ' ' + this.cp.get(this.index2).toString();
                        }
                    });
                    u += 3;
                    break;
                }
                default: {
                    assert false : tag;
                    break;
                }
            }
        }
        readShort(bytecode, u);
        u += 2;
        final int cls = readShort(bytecode, u);
        u += 2;
        final String thisClassName = cp.get(cls).toString();
        u += 2;
        final int ifc = readShort(bytecode, u);
        u += 2;
        u += ifc * 2;
        final int fc = readShort(bytecode, u);
        u += 2;
        for (int j = 0; j < fc; ++j) {
            u += 2;
            readShort(bytecode, u);
            u += 2;
            u += 2;
            final int ac = readShort(bytecode, u);
            u += 2;
            for (int k = 0; k < ac; ++k) {
                u += 2;
                final int len2 = readInt(bytecode, u);
                u += 4;
                u += len2;
            }
        }
        final int mc = readShort(bytecode, u);
        u += 2;
        for (int l = 0; l < mc; ++l) {
            readShort(bytecode, u);
            u += 2;
            final int methodNameIndex = readShort(bytecode, u);
            u += 2;
            final String methodName = cp.get(methodNameIndex).toString();
            final int methodDescIndex = readShort(bytecode, u);
            u += 2;
            final String methodDesc = cp.get(methodDescIndex).toString();
            final int ac2 = readShort(bytecode, u);
            u += 2;
            for (int m = 0; m < ac2; ++m) {
                final int nameIndex = readShort(bytecode, u);
                u += 2;
                final String attrName = cp.get(nameIndex).toString();
                final int attrLen = readInt(bytecode, u);
                u += 4;
                if ("Code".equals(attrName)) {
                    readShort(bytecode, u);
                    u += 2;
                    readShort(bytecode, u);
                    u += 2;
                    final int len3 = readInt(bytecode, u);
                    u += 4;
                    this.parseCode(bytecode, u, len3, fullyQualifiedName(thisClassName, methodName, methodDesc));
                    u += len3;
                    final int elen = readShort(bytecode, u);
                    u += 2;
                    u += elen * 8;
                    final int ac3 = readShort(bytecode, u);
                    u += 2;
                    for (int k2 = 0; k2 < ac3; ++k2) {
                        u += 2;
                        final int aclen = readInt(bytecode, u);
                        u += 4;
                        u += aclen;
                    }
                }
                else {
                    u += attrLen;
                }
            }
        }
        final int ac = readShort(bytecode, u);
        u += 2;
        for (int i2 = 0; i2 < ac; ++i2) {
            readShort(bytecode, u);
            u += 2;
            final int len2 = readInt(bytecode, u);
            u += 4;
            u += len2;
        }
        return thisClassName;
    }
    
    private static String fullyQualifiedName(final String className, final String methodName, final String methodDesc) {
        return className + '.' + methodName + methodDesc;
    }
    
    private void parseCode(final byte[] bytecode, final int index, final int len, final String desc) {
        final List<Label> labels = new ArrayList<Label>();
        this.labelMap.put(desc, labels);
        boolean wide = false;
        int i = index;
        while (i < index + len) {
            final int opcode = bytecode[i];
            labels.add(new NashornTextifier.NashornLabel(opcode, i - index));
            switch (opcode & 0xFF) {
                case 196: {
                    wide = true;
                    ++i;
                    break;
                }
                case 169: {
                    i += (wide ? 4 : 2);
                    break;
                }
                case 171: {
                    ++i;
                    while ((i - index & 0x3) != 0x0) {
                        ++i;
                    }
                    readInt(bytecode, i);
                    i += 4;
                    final int npairs = readInt(bytecode, i);
                    i += 4;
                    i += 8 * npairs;
                    break;
                }
                case 170: {
                    ++i;
                    while ((i - index & 0x3) != 0x0) {
                        ++i;
                    }
                    readInt(bytecode, i);
                    i += 4;
                    final int lo = readInt(bytecode, i);
                    i += 4;
                    final int hi = readInt(bytecode, i);
                    i += 4;
                    i += 4 * (hi - lo + 1);
                    break;
                }
                case 197: {
                    i += 4;
                    break;
                }
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58: {
                    i += (wide ? 3 : 2);
                    break;
                }
                case 16:
                case 18:
                case 188: {
                    i += 2;
                    break;
                }
                case 17:
                case 19:
                case 20:
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 167:
                case 168:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 187:
                case 189:
                case 192:
                case 193:
                case 198:
                case 199: {
                    i += 3;
                    break;
                }
                case 132: {
                    i += (wide ? 5 : 3);
                    break;
                }
                case 185:
                case 186:
                case 200:
                case 201: {
                    i += 5;
                    break;
                }
                default: {
                    ++i;
                    break;
                }
            }
            if (wide) {
                wide = false;
            }
        }
    }
    
    @Override
    public void accept(final ClassVisitor classVisitor, final Attribute[] attrs, final int flags) {
        super.accept(classVisitor, attrs, flags);
    }
    
    @Override
    protected Label readLabel(final int offset, final Label[] labels) {
        final Label label = super.readLabel(offset, labels);
        label.info = offset;
        return label;
    }
    
    static {
        NashornClassReader.type = new String[] { "<error>", "UTF8", "<error>", "Integer", "Float", "Long", "Double", "Class", "String", "Fieldref", "Methodref", "InterfaceMethodRef", "NameAndType", "<error>", "<error>", "MethodHandle", "MethodType", "<error>", "Invokedynamic" };
    }
    
    private abstract static class Constant
    {
        protected ArrayList<Constant> cp;
        protected int tag;
        
        protected Constant(final ArrayList<Constant> cp, final int tag) {
            this.cp = cp;
            this.tag = tag;
        }
        
        final String getType() {
            String str;
            for (str = NashornClassReader.type[this.tag]; str.length() < 16; str += " ") {}
            return str;
        }
    }
    
    private static class IndexInfo extends Constant
    {
        protected final int index;
        
        IndexInfo(final ArrayList<Constant> cp, final int tag, final int index) {
            super(cp, tag);
            this.index = index;
        }
        
        @Override
        public String toString() {
            return this.cp.get(this.index).toString();
        }
    }
    
    private static class IndexInfo2 extends IndexInfo
    {
        protected final int index2;
        
        IndexInfo2(final ArrayList<Constant> cp, final int tag, final int index, final int index2) {
            super(cp, tag, index);
            this.index2 = index2;
        }
        
        @Override
        public String toString() {
            return super.toString() + ' ' + this.cp.get(this.index2).toString();
        }
    }
    
    private static class DirectInfo<T> extends Constant
    {
        protected final T info;
        
        DirectInfo(final ArrayList<Constant> cp, final int tag, final T info) {
            super(cp, tag);
            this.info = info;
        }
        
        @Override
        public String toString() {
            return this.info.toString();
        }
    }
}
