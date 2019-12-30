// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.debug;

import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashSet;
import jdk.internal.org.objectweb.asm.Attribute;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.Handle;
import java.util.List;
import jdk.internal.org.objectweb.asm.signature.SignatureVisitor;
import jdk.internal.org.objectweb.asm.signature.SignatureReader;
import jdk.internal.org.objectweb.asm.util.TraceSignatureVisitor;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Label;
import java.util.Iterator;
import jdk.internal.org.objectweb.asm.util.Printer;

public final class NashornTextifier extends Printer
{
    private String currentClassName;
    private Iterator<Label> labelIter;
    private Graph graph;
    private String currentBlock;
    private boolean lastWasNop;
    private boolean lastWasEllipse;
    private static final int INTERNAL_NAME = 0;
    private static final int FIELD_DESCRIPTOR = 1;
    private static final int FIELD_SIGNATURE = 2;
    private static final int METHOD_DESCRIPTOR = 3;
    private static final int METHOD_SIGNATURE = 4;
    private static final int CLASS_SIGNATURE = 5;
    private final String tab = "  ";
    private final String tab2 = "    ";
    private final String tab3 = "      ";
    private Map<Label, String> labelNames;
    private boolean localVarsStarted;
    private NashornClassReader cr;
    private ScriptEnvironment env;
    
    public NashornTextifier(final ScriptEnvironment env, final NashornClassReader cr) {
        this(327680);
        this.env = env;
        this.cr = cr;
    }
    
    private NashornTextifier(final ScriptEnvironment env, final NashornClassReader cr, final Iterator<Label> labelIter, final Graph graph) {
        this(env, cr);
        this.labelIter = labelIter;
        this.graph = graph;
    }
    
    protected NashornTextifier(final int api) {
        super(api);
        this.lastWasNop = false;
        this.lastWasEllipse = false;
        this.localVarsStarted = false;
    }
    
    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        final int major = version & 0xFFFF;
        final int minor = version >>> 16;
        this.currentClassName = name;
        final StringBuilder sb = new StringBuilder();
        sb.append("// class version ").append(major).append('.').append(minor).append(" (").append(version).append(")\n");
        if ((access & 0x20000) != 0x0) {
            sb.append("// DEPRECATED\n");
        }
        sb.append("// access flags 0x").append(Integer.toHexString(access).toUpperCase()).append('\n');
        appendDescriptor(sb, 5, signature);
        if (signature != null) {
            final TraceSignatureVisitor sv = new TraceSignatureVisitor(access);
            final SignatureReader r = new SignatureReader(signature);
            r.accept(sv);
            sb.append("// declaration: ").append(name).append(sv.getDeclaration()).append('\n');
        }
        appendAccess(sb, access & 0xFFFFFFDF);
        if ((access & 0x2000) != 0x0) {
            sb.append("@interface ");
        }
        else if ((access & 0x200) != 0x0) {
            sb.append("interface ");
        }
        else if ((access & 0x4000) == 0x0) {
            sb.append("class ");
        }
        appendDescriptor(sb, 0, name);
        if (superName != null && !"java/lang/Object".equals(superName)) {
            sb.append(" extends ");
            appendDescriptor(sb, 0, superName);
            sb.append(' ');
        }
        if (interfaces != null && interfaces.length > 0) {
            sb.append(" implements ");
            for (final String interface1 : interfaces) {
                appendDescriptor(sb, 0, interface1);
                sb.append(' ');
            }
        }
        sb.append(" {\n");
        this.addText(sb);
    }
    
    @Override
    public void visitSource(final String file, final String debug) {
        final StringBuilder sb = new StringBuilder();
        if (file != null) {
            sb.append("  ").append("// compiled from: ").append(file).append('\n');
        }
        if (debug != null) {
            sb.append("  ").append("// debug info: ").append(debug).append('\n');
        }
        if (sb.length() > 0) {
            this.addText(sb);
        }
    }
    
    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
        final StringBuilder sb = new StringBuilder();
        sb.append("  ").append("outer class ");
        appendDescriptor(sb, 0, owner);
        sb.append(' ');
        if (name != null) {
            sb.append(name).append(' ');
        }
        appendDescriptor(sb, 3, desc);
        sb.append('\n');
        this.addText(sb);
    }
    
    @Override
    public NashornTextifier visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        final StringBuilder sb = new StringBuilder();
        if ((access & 0x20000) != 0x0) {
            sb.append("  ").append("// DEPRECATED\n");
        }
        if (signature != null) {
            sb.append("  ");
            appendDescriptor(sb, 2, signature);
            final TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            final SignatureReader r = new SignatureReader(signature);
            r.acceptType(sv);
            sb.append("  ").append("// declaration: ").append(sv.getDeclaration()).append('\n');
        }
        sb.append("  ");
        appendAccess(sb, access);
        final String prunedDesc = desc.endsWith(";") ? desc.substring(0, desc.length() - 1) : desc;
        appendDescriptor(sb, 1, prunedDesc);
        sb.append(' ').append(name);
        if (value != null) {
            sb.append(" = ");
            if (value instanceof String) {
                sb.append('\"').append(value).append('\"');
            }
            else {
                sb.append(value);
            }
        }
        sb.append(";\n");
        this.addText(sb);
        final NashornTextifier t = this.createNashornTextifier();
        this.addText(t.getText());
        return t;
    }
    
    @Override
    public NashornTextifier visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        this.graph = new Graph(name);
        final List<Label> extraLabels = this.cr.getExtraLabels(this.currentClassName, name, desc);
        this.labelIter = ((extraLabels == null) ? null : extraLabels.iterator());
        final StringBuilder sb = new StringBuilder();
        sb.append('\n');
        if ((access & 0x20000) != 0x0) {
            sb.append("  ").append("// DEPRECATED\n");
        }
        sb.append("  ").append("// access flags 0x").append(Integer.toHexString(access).toUpperCase()).append('\n');
        if (signature != null) {
            sb.append("  ");
            appendDescriptor(sb, 4, signature);
            final TraceSignatureVisitor v = new TraceSignatureVisitor(0);
            final SignatureReader r = new SignatureReader(signature);
            r.accept(v);
            final String genericDecl = v.getDeclaration();
            final String genericReturn = v.getReturnType();
            final String genericExceptions = v.getExceptions();
            sb.append("  ").append("// declaration: ").append(genericReturn).append(' ').append(name).append(genericDecl);
            if (genericExceptions != null) {
                sb.append(" throws ").append(genericExceptions);
            }
            sb.append('\n');
        }
        sb.append("  ");
        appendAccess(sb, access);
        if ((access & 0x100) != 0x0) {
            sb.append("native ");
        }
        if ((access & 0x80) != 0x0) {
            sb.append("varargs ");
        }
        if ((access & 0x40) != 0x0) {
            sb.append("bridge ");
        }
        sb.append(name);
        appendDescriptor(sb, 3, desc);
        if (exceptions != null && exceptions.length > 0) {
            sb.append(" throws ");
            for (final String exception : exceptions) {
                appendDescriptor(sb, 0, exception);
                sb.append(' ');
            }
        }
        sb.append('\n');
        this.addText(sb);
        final NashornTextifier t = this.createNashornTextifier();
        this.addText(t.getText());
        return t;
    }
    
    @Override
    public void visitClassEnd() {
        this.addText("}\n");
    }
    
    @Override
    public void visitFieldEnd() {
    }
    
    @Override
    public void visitParameter(final String name, final int access) {
        final StringBuilder sb = new StringBuilder();
        sb.append("    ").append("// parameter ");
        appendAccess(sb, access);
        sb.append(' ').append((name == null) ? "<no name>" : name).append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitCode() {
    }
    
    @Override
    public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
        final StringBuilder sb = new StringBuilder();
        sb.append("frame ");
        switch (type) {
            case -1:
            case 0: {
                sb.append("full [");
                this.appendFrameTypes(sb, nLocal, local);
                sb.append("] [");
                this.appendFrameTypes(sb, nStack, stack);
                sb.append(']');
                break;
            }
            case 1: {
                sb.append("append [");
                this.appendFrameTypes(sb, nLocal, local);
                sb.append(']');
                break;
            }
            case 2: {
                sb.append("chop ").append(nLocal);
                break;
            }
            case 3: {
                sb.append("same");
                break;
            }
            case 4: {
                sb.append("same1 ");
                this.appendFrameTypes(sb, 1, stack);
                break;
            }
            default: {
                assert false;
                break;
            }
        }
        sb.append('\n');
        sb.append('\n');
        this.addText(sb);
    }
    
    private StringBuilder appendOpcode(final StringBuilder sb, final int opcode) {
        final Label next = this.getNextLabel();
        if (next instanceof NashornLabel) {
            final int bci = next.getOffset();
            if (bci != -1) {
                final String bcis = "" + bci;
                for (int i = 0; i < 5 - bcis.length(); ++i) {
                    sb.append(' ');
                }
                sb.append(bcis);
                sb.append(' ');
            }
            else {
                sb.append("       ");
            }
        }
        return sb.append("    ").append(NashornTextifier.OPCODES[opcode].toLowerCase());
    }
    
    private Label getNextLabel() {
        return (this.labelIter == null) ? null : this.labelIter.next();
    }
    
    @Override
    public void visitInsn(final int opcode) {
        if (opcode == 0) {
            if (this.lastWasEllipse) {
                this.getNextLabel();
                return;
            }
            if (this.lastWasNop) {
                this.getNextLabel();
                this.addText("          ...\n");
                this.lastWasEllipse = true;
                return;
            }
            this.lastWasNop = true;
        }
        else {
            final boolean b = false;
            this.lastWasEllipse = b;
            this.lastWasNop = b;
        }
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append('\n');
        this.addText(sb);
        this.checkNoFallThru(opcode, null);
    }
    
    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ').append((opcode == 188) ? NashornTextifier.TYPES[operand] : Integer.toString(operand)).append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitVarInsn(final int opcode, final int var) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ').append(var).append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ');
        appendDescriptor(sb, 0, type);
        sb.append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ');
        appendDescriptor(sb, 0, owner);
        sb.append('.').append(name).append(" : ");
        appendDescriptor(sb, 1, desc);
        sb.append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ');
        appendDescriptor(sb, 0, owner);
        sb.append('.').append(name);
        appendDescriptor(sb, 3, desc);
        sb.append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitInvokeDynamicInsn(final String name, final String desc, final Handle bsm, final Object... bsmArgs) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 186).append(' ');
        sb.append(name);
        appendDescriptor(sb, 3, desc);
        for (int len = sb.length(), i = 0; i < 80 - len; ++i) {
            sb.append(' ');
        }
        sb.append(" [");
        appendHandle(sb, bsm);
        if (bsmArgs.length == 0) {
            sb.append("none");
        }
        else {
            for (final Object cst : bsmArgs) {
                if (cst instanceof String) {
                    appendStr(sb, (String)cst);
                }
                else if (cst instanceof Type) {
                    sb.append(((Type)cst).getDescriptor()).append(".class");
                }
                else if (cst instanceof Handle) {
                    appendHandle(sb, (Handle)cst);
                }
                else if (cst instanceof Integer) {
                    final int c = (int)cst;
                    final int pp = c >> 11;
                    if (pp != 0) {
                        sb.append(" pp=").append(pp);
                    }
                    sb.append(NashornCallSiteDescriptor.toString(c & 0x7FF));
                }
                else {
                    sb.append(cst);
                }
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
        }
        sb.append("]\n");
        this.addText(sb);
    }
    
    private static final boolean noFallThru(final int opcode) {
        switch (opcode) {
            case 167:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 191: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    private void checkNoFallThru(final int opcode, final String to) {
        if (noFallThru(opcode)) {
            this.graph.setNoFallThru(this.currentBlock);
        }
        if (this.currentBlock != null && to != null) {
            this.graph.addEdge(this.currentBlock, to);
        }
    }
    
    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ');
        final String to = this.appendLabel(sb, label);
        sb.append('\n');
        this.addText(sb);
        this.checkNoFallThru(opcode, to);
    }
    
    private void addText(final Object t) {
        this.text.add(t);
        if (this.currentBlock != null) {
            this.graph.addText(this.currentBlock, t.toString());
        }
    }
    
    @Override
    public void visitLabel(final Label label) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        final String name = this.appendLabel(sb, label);
        sb.append(" [bci=");
        sb.append(label.info);
        sb.append("]");
        sb.append("\n");
        this.graph.addNode(name);
        if (this.currentBlock != null && !this.graph.isNoFallThru(this.currentBlock)) {
            this.graph.addEdge(this.currentBlock, name);
        }
        this.currentBlock = name;
        this.addText(sb);
    }
    
    @Override
    public void visitLdcInsn(final Object cst) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 18).append(' ');
        if (cst instanceof String) {
            appendStr(sb, (String)cst);
        }
        else if (cst instanceof Type) {
            sb.append(((Type)cst).getDescriptor()).append(".class");
        }
        else {
            sb.append(cst);
        }
        sb.append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitIincInsn(final int var, final int increment) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 132).append(' ');
        sb.append(var).append(' ').append(increment).append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 170).append(' ');
        for (int i = 0; i < labels.length; ++i) {
            sb.append("      ").append(min + i).append(": ");
            final String to = this.appendLabel(sb, labels[i]);
            this.graph.addEdge(this.currentBlock, to);
            sb.append('\n');
        }
        sb.append("      ").append("default: ");
        this.appendLabel(sb, dflt);
        sb.append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 171).append(' ');
        for (int i = 0; i < labels.length; ++i) {
            sb.append("      ").append(keys[i]).append(": ");
            final String to = this.appendLabel(sb, labels[i]);
            this.graph.addEdge(this.currentBlock, to);
            sb.append('\n');
        }
        sb.append("      ").append("default: ");
        final String to2 = this.appendLabel(sb, dflt);
        this.graph.addEdge(this.currentBlock, to2);
        sb.append('\n');
        this.addText(sb.toString());
    }
    
    @Override
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        final StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 197).append(' ');
        appendDescriptor(sb, 1, desc);
        sb.append(' ').append(dims).append('\n');
        this.addText(sb);
    }
    
    @Override
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
        final StringBuilder sb = new StringBuilder();
        sb.append("    ").append("try ");
        final String from = this.appendLabel(sb, start);
        sb.append(' ');
        this.appendLabel(sb, end);
        sb.append(' ');
        final String to = this.appendLabel(sb, handler);
        sb.append(' ');
        appendDescriptor(sb, 0, type);
        sb.append('\n');
        this.addText(sb);
        this.graph.setIsCatch(to, type);
        this.graph.addTryCatch(from, to);
    }
    
    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        final StringBuilder sb = new StringBuilder();
        if (!this.localVarsStarted) {
            this.text.add("\n");
            this.localVarsStarted = true;
            this.graph.addNode("vars");
            this.currentBlock = "vars";
        }
        sb.append("    ").append("local ").append(name).append(' ');
        for (int len = sb.length(), i = 0; i < 25 - len; ++i) {
            sb.append(' ');
        }
        String label = this.appendLabel(sb, start);
        for (int j = 0; j < 5 - label.length(); ++j) {
            sb.append(' ');
        }
        label = this.appendLabel(sb, end);
        for (int j = 0; j < 5 - label.length(); ++j) {
            sb.append(' ');
        }
        sb.append(index).append("    ");
        appendDescriptor(sb, 1, desc);
        sb.append('\n');
        if (signature != null) {
            sb.append("    ");
            appendDescriptor(sb, 2, signature);
            final TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            final SignatureReader r = new SignatureReader(signature);
            r.acceptType(sv);
            sb.append("    ").append("// declaration: ").append(sv.getDeclaration()).append('\n');
        }
        this.addText(sb.toString());
    }
    
    @Override
    public void visitLineNumber(final int line, final Label start) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<line ");
        sb.append(line);
        sb.append(">\n");
        this.addText(sb.toString());
    }
    
    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        final StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append("    ").append("max stack  = ").append(maxStack);
        sb.append(", max locals = ").append(maxLocals).append('\n');
        this.addText(sb.toString());
    }
    
    private void printToDir(final Graph g) {
        if (this.env._print_code_dir != null) {
            final File dir = new File(this.env._print_code_dir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new RuntimeException(dir.toString());
            }
            int uniqueId = 0;
            File file;
            do {
                final String fileName = g.getName() + ((uniqueId == 0) ? "" : ("_" + uniqueId)) + ".dot";
                file = new File(dir, fileName);
                ++uniqueId;
            } while (file.exists());
            try (final PrintWriter pw = new PrintWriter(new FileOutputStream(file))) {
                pw.println(g);
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Override
    public void visitMethodEnd() {
        if ((this.env._print_code_func == null || this.env._print_code_func.equals(this.graph.getName())) && this.env._print_code_dir != null) {
            this.printToDir(this.graph);
        }
    }
    
    protected NashornTextifier createNashornTextifier() {
        return new NashornTextifier(this.env, this.cr, this.labelIter, this.graph);
    }
    
    private static void appendDescriptor(final StringBuilder sb, final int type, final String desc) {
        if (desc != null) {
            if (type == 5 || type == 2 || type == 4) {
                sb.append("// signature ").append(desc).append('\n');
            }
            else {
                appendShortDescriptor(sb, desc);
            }
        }
    }
    
    private String appendLabel(final StringBuilder sb, final Label l) {
        if (this.labelNames == null) {
            this.labelNames = new HashMap<Label, String>();
        }
        String name = this.labelNames.get(l);
        if (name == null) {
            name = "L" + this.labelNames.size();
            this.labelNames.put(l, name);
        }
        sb.append(name);
        return name;
    }
    
    private static void appendHandle(final StringBuilder sb, final Handle h) {
        switch (h.getTag()) {
            case 1: {
                sb.append("getfield");
                break;
            }
            case 2: {
                sb.append("getstatic");
                break;
            }
            case 3: {
                sb.append("putfield");
                break;
            }
            case 4: {
                sb.append("putstatic");
                break;
            }
            case 9: {
                sb.append("interface");
                break;
            }
            case 7: {
                sb.append("special");
                break;
            }
            case 6: {
                sb.append("static");
                break;
            }
            case 5: {
                sb.append("virtual");
                break;
            }
            case 8: {
                sb.append("new_special");
                break;
            }
            default: {
                assert false;
                break;
            }
        }
        sb.append(" '");
        sb.append(h.getName());
        sb.append("'");
    }
    
    private static void appendAccess(final StringBuilder sb, final int access) {
        if ((access & 0x1) != 0x0) {
            sb.append("public ");
        }
        if ((access & 0x2) != 0x0) {
            sb.append("private ");
        }
        if ((access & 0x4) != 0x0) {
            sb.append("protected ");
        }
        if ((access & 0x10) != 0x0) {
            sb.append("final ");
        }
        if ((access & 0x8) != 0x0) {
            sb.append("static ");
        }
        if ((access & 0x20) != 0x0) {
            sb.append("synchronized ");
        }
        if ((access & 0x40) != 0x0) {
            sb.append("volatile ");
        }
        if ((access & 0x80) != 0x0) {
            sb.append("transient ");
        }
        if ((access & 0x400) != 0x0) {
            sb.append("abstract ");
        }
        if ((access & 0x800) != 0x0) {
            sb.append("strictfp ");
        }
        if ((access & 0x1000) != 0x0) {
            sb.append("synthetic ");
        }
        if ((access & 0x8000) != 0x0) {
            sb.append("mandated ");
        }
        if ((access & 0x4000) != 0x0) {
            sb.append("enum ");
        }
    }
    
    private void appendFrameTypes(final StringBuilder sb, final int n, final Object[] o) {
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                sb.append(' ');
            }
            if (o[i] instanceof String) {
                final String desc = (String)o[i];
                if (desc.startsWith("[")) {
                    appendDescriptor(sb, 1, desc);
                }
                else {
                    appendDescriptor(sb, 0, desc);
                }
            }
            else if (o[i] instanceof Integer) {
                switch ((int)o[i]) {
                    case 0: {
                        appendDescriptor(sb, 1, "T");
                        break;
                    }
                    case 1: {
                        appendDescriptor(sb, 1, "I");
                        break;
                    }
                    case 2: {
                        appendDescriptor(sb, 1, "F");
                        break;
                    }
                    case 3: {
                        appendDescriptor(sb, 1, "D");
                        break;
                    }
                    case 4: {
                        appendDescriptor(sb, 1, "J");
                        break;
                    }
                    case 5: {
                        appendDescriptor(sb, 1, "N");
                        break;
                    }
                    case 6: {
                        appendDescriptor(sb, 1, "U");
                        break;
                    }
                    default: {
                        assert false;
                        break;
                    }
                }
            }
            else {
                this.appendLabel(sb, (Label)o[i]);
            }
        }
    }
    
    private static void appendShortDescriptor(final StringBuilder sb, final String desc) {
        if (desc.charAt(0) == '(') {
            for (int i = 0; i < desc.length(); ++i) {
                if (desc.charAt(i) == 'L') {
                    int slash = i;
                    while (desc.charAt(i) != ';') {
                        ++i;
                        if (desc.charAt(i) == '/') {
                            slash = i;
                        }
                    }
                    sb.append(desc.substring(slash + 1, i)).append(';');
                }
                else {
                    sb.append(desc.charAt(i));
                }
            }
        }
        else {
            final int lastSlash = desc.lastIndexOf(47);
            final int lastBracket = desc.lastIndexOf(91);
            if (lastBracket != -1) {
                sb.append(desc, 0, lastBracket + 1);
            }
            sb.append((lastSlash == -1) ? desc : desc.substring(lastSlash + 1));
        }
    }
    
    private static void appendStr(final StringBuilder sb, final String s) {
        sb.append('\"');
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (c == '\n') {
                sb.append("\\n");
            }
            else if (c == '\r') {
                sb.append("\\r");
            }
            else if (c == '\\') {
                sb.append("\\\\");
            }
            else if (c == '\"') {
                sb.append("\\\"");
            }
            else if (c < ' ' || c > '\u007f') {
                sb.append("\\u");
                if (c < '\u0010') {
                    sb.append("000");
                }
                else if (c < '\u0100') {
                    sb.append("00");
                }
                else if (c < '\u1000') {
                    sb.append('0');
                }
                sb.append(Integer.toString(c, 16));
            }
            else {
                sb.append(c);
            }
        }
        sb.append('\"');
    }
    
    @Override
    public Printer visitAnnotationDefault() {
        throw new AssertionError();
    }
    
    @Override
    public Printer visitClassAnnotation(final String arg0, final boolean arg1) {
        return this;
    }
    
    @Override
    public void visitClassAttribute(final Attribute arg0) {
        throw new AssertionError();
    }
    
    @Override
    public Printer visitFieldAnnotation(final String arg0, final boolean arg1) {
        throw new AssertionError();
    }
    
    @Override
    public void visitFieldAttribute(final Attribute arg0) {
        throw new AssertionError();
    }
    
    @Override
    public Printer visitMethodAnnotation(final String arg0, final boolean arg1) {
        return this;
    }
    
    @Override
    public void visitMethodAttribute(final Attribute arg0) {
        throw new AssertionError();
    }
    
    @Override
    public Printer visitParameterAnnotation(final int arg0, final String arg1, final boolean arg2) {
        throw new AssertionError();
    }
    
    @Override
    public void visit(final String arg0, final Object arg1) {
        throw new AssertionError();
    }
    
    @Override
    public Printer visitAnnotation(final String arg0, final String arg1) {
        throw new AssertionError();
    }
    
    @Override
    public void visitAnnotationEnd() {
    }
    
    @Override
    public Printer visitArray(final String arg0) {
        throw new AssertionError();
    }
    
    @Override
    public void visitEnum(final String arg0, final String arg1, final String arg2) {
        throw new AssertionError();
    }
    
    @Override
    public void visitInnerClass(final String arg0, final String arg1, final String arg2, final int arg3) {
        throw new AssertionError();
    }
    
    private static class Graph
    {
        private final LinkedHashSet<String> nodes;
        private final Map<String, StringBuilder> contents;
        private final Map<String, Set<String>> edges;
        private final Set<String> hasPreds;
        private final Set<String> noFallThru;
        private final Map<String, String> catches;
        private final Map<String, Set<String>> exceptionMap;
        private final String name;
        private static final String LEFT_ALIGN = "\\l";
        private static final String COLOR_CATCH = "\"#ee9999\"";
        private static final String COLOR_ORPHAN = "\"#9999bb\"";
        private static final String COLOR_DEFAULT = "\"#99bb99\"";
        private static final String COLOR_LOCALVARS = "\"#999999\"";
        
        Graph(final String name) {
            this.name = name;
            this.nodes = new LinkedHashSet<String>();
            this.contents = new HashMap<String, StringBuilder>();
            this.edges = new HashMap<String, Set<String>>();
            this.hasPreds = new HashSet<String>();
            this.catches = new HashMap<String, String>();
            this.noFallThru = new HashSet<String>();
            this.exceptionMap = new HashMap<String, Set<String>>();
        }
        
        void addEdge(final String from, final String to) {
            Set<String> edgeSet = this.edges.get(from);
            if (edgeSet == null) {
                edgeSet = new LinkedHashSet<String>();
                this.edges.put(from, edgeSet);
            }
            edgeSet.add(to);
            this.hasPreds.add(to);
        }
        
        void addTryCatch(final String tryNode, final String catchNode) {
            Set<String> tryNodes = this.exceptionMap.get(catchNode);
            if (tryNodes == null) {
                tryNodes = new HashSet<String>();
                this.exceptionMap.put(catchNode, tryNodes);
            }
            if (!tryNodes.contains(tryNode)) {
                this.addEdge(tryNode, catchNode);
            }
            tryNodes.add(tryNode);
        }
        
        void addNode(final String node) {
            assert !this.nodes.contains(node);
            this.nodes.add(node);
        }
        
        void setNoFallThru(final String node) {
            this.noFallThru.add(node);
        }
        
        boolean isNoFallThru(final String node) {
            return this.noFallThru.contains(node);
        }
        
        void setIsCatch(final String node, final String exception) {
            this.catches.put(node, exception);
        }
        
        String getName() {
            return this.name;
        }
        
        void addText(final String node, final String text) {
            StringBuilder sb = this.contents.get(node);
            if (sb == null) {
                sb = new StringBuilder();
            }
            for (int i = 0; i < text.length(); ++i) {
                switch (text.charAt(i)) {
                    case '\n': {
                        sb.append("\\l");
                        break;
                    }
                    case '\"': {
                        sb.append("'");
                        break;
                    }
                    default: {
                        sb.append(text.charAt(i));
                        break;
                    }
                }
            }
            this.contents.put(node, sb);
        }
        
        private static String dottyFriendly(final String name) {
            return name.replace(':', '_');
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("digraph " + dottyFriendly(this.name) + " {");
            sb.append("\n");
            sb.append("\tgraph [fontname=courier]\n");
            sb.append("\tnode [style=filled,color=\"#99bb99\",fontname=courier]\n");
            sb.append("\tedge [fontname=courier]\n\n");
            for (final String node : this.nodes) {
                sb.append("\t");
                sb.append(node);
                sb.append(" [");
                sb.append("id=");
                sb.append(node);
                sb.append(", label=\"");
                String c = this.contents.get(node).toString();
                if (c.startsWith("\\l")) {
                    c = c.substring("\\l".length());
                }
                final String ex = this.catches.get(node);
                if (ex != null) {
                    sb.append("*** CATCH: ").append(ex).append(" ***\\l");
                }
                sb.append(c);
                sb.append("\"]\n");
            }
            for (final String from : this.edges.keySet()) {
                for (final String to : this.edges.get(from)) {
                    sb.append("\t");
                    sb.append(from);
                    sb.append(" -> ");
                    sb.append(to);
                    sb.append("[label=\"");
                    sb.append(to);
                    sb.append("\"");
                    if (this.catches.get(to) != null) {
                        sb.append(", color=red, style=dashed");
                    }
                    sb.append(']');
                    sb.append(";\n");
                }
            }
            sb.append("\n");
            for (final String node : this.nodes) {
                sb.append("\t");
                sb.append(node);
                sb.append(" [shape=box");
                if (this.catches.get(node) != null) {
                    sb.append(", color=\"#ee9999\"");
                }
                else if ("vars".equals(node)) {
                    sb.append(", shape=hexagon, color=\"#999999\"");
                }
                else if (!this.hasPreds.contains(node)) {
                    sb.append(", color=\"#9999bb\"");
                }
                sb.append("]\n");
            }
            sb.append("}\n");
            return sb.toString();
        }
    }
    
    static class NashornLabel extends Label
    {
        final Label label;
        final int bci;
        final int opcode;
        
        NashornLabel(final Label label, final int bci) {
            this.label = label;
            this.bci = bci;
            this.opcode = -1;
        }
        
        NashornLabel(final int opcode, final int bci) {
            this.opcode = opcode;
            this.bci = bci;
            this.label = null;
        }
        
        Label getLabel() {
            return this.label;
        }
        
        @Override
        public int getOffset() {
            return this.bci;
        }
        
        @Override
        public String toString() {
            return "label " + this.bci;
        }
    }
}
