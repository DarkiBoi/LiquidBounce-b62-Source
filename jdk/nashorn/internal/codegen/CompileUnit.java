// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;
import java.util.Set;
import java.util.IdentityHashMap;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.ir.FunctionNode;
import java.util.Map;
import java.io.Serializable;

public final class CompileUnit implements Comparable<CompileUnit>, Serializable
{
    private static final long serialVersionUID = 1L;
    private final transient String className;
    private transient ClassEmitter classEmitter;
    private transient long weight;
    private transient Class<?> clazz;
    private transient Map<FunctionNode, RecompilableScriptFunctionData> functions;
    private transient boolean isUsed;
    private static int emittedUnitCount;
    
    CompileUnit(final String className, final ClassEmitter classEmitter, final long initialWeight) {
        this.functions = new IdentityHashMap<FunctionNode, RecompilableScriptFunctionData>();
        this.className = className;
        this.weight = initialWeight;
        this.classEmitter = classEmitter;
    }
    
    static Set<CompileUnit> createCompileUnitSet() {
        return new TreeSet<CompileUnit>();
    }
    
    static void increaseEmitCount() {
        ++CompileUnit.emittedUnitCount;
    }
    
    public static int getEmittedUnitCount() {
        return CompileUnit.emittedUnitCount;
    }
    
    public boolean isUsed() {
        return this.isUsed;
    }
    
    public boolean hasCode() {
        return this.classEmitter.getMethodCount() - this.classEmitter.getInitCount() - this.classEmitter.getClinitCount() > 0;
    }
    
    public void setUsed() {
        this.isUsed = true;
    }
    
    public Class<?> getCode() {
        return this.clazz;
    }
    
    void setCode(final Class<?> clazz) {
        this.clazz = Objects.requireNonNull(clazz);
        this.classEmitter = null;
    }
    
    void addFunctionInitializer(final RecompilableScriptFunctionData data, final FunctionNode functionNode) {
        this.functions.put(functionNode, data);
    }
    
    public boolean isInitializing(final RecompilableScriptFunctionData data, final FunctionNode functionNode) {
        return this.functions.get(functionNode) == data;
    }
    
    void initializeFunctionsCode() {
        for (final Map.Entry<FunctionNode, RecompilableScriptFunctionData> entry : this.functions.entrySet()) {
            entry.getValue().initializeCode(entry.getKey());
        }
    }
    
    Collection<FunctionNode> getFunctionNodes() {
        return Collections.unmodifiableCollection((Collection<? extends FunctionNode>)this.functions.keySet());
    }
    
    void addWeight(final long w) {
        this.weight += w;
    }
    
    public boolean canHold(final long w) {
        return this.weight + w < Splitter.SPLIT_THRESHOLD;
    }
    
    public ClassEmitter getClassEmitter() {
        return this.classEmitter;
    }
    
    public String getUnitClassName() {
        return this.className;
    }
    
    private static String shortName(final String name) {
        return (name == null) ? null : ((name.lastIndexOf(47) == -1) ? name : name.substring(name.lastIndexOf(47) + 1));
    }
    
    @Override
    public String toString() {
        final String methods = (this.classEmitter != null) ? this.classEmitter.getMethodNames().toString() : "<anon>";
        return "[CompileUnit className=" + shortName(this.className) + " weight=" + this.weight + '/' + Splitter.SPLIT_THRESHOLD + " hasCode=" + methods + ']';
    }
    
    @Override
    public int compareTo(final CompileUnit o) {
        return this.className.compareTo(o.className);
    }
}
