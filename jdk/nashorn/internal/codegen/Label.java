// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import jdk.nashorn.internal.codegen.types.Type;
import java.io.Serializable;

public final class Label implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static int nextId;
    private final String name;
    private transient Stack stack;
    private transient jdk.internal.org.objectweb.asm.Label label;
    private final int id;
    private transient boolean reachable;
    private transient boolean breakTarget;
    private String str;
    
    public Label(final String name) {
        this.name = name;
        this.id = Label.nextId++;
    }
    
    public Label(final Label label) {
        this.name = label.name;
        this.id = label.id;
    }
    
    jdk.internal.org.objectweb.asm.Label getLabel() {
        if (this.label == null) {
            this.label = new jdk.internal.org.objectweb.asm.Label();
        }
        return this.label;
    }
    
    Stack getStack() {
        return this.stack;
    }
    
    void joinFrom(final Stack joinOrigin) {
        this.reachable = true;
        if (this.stack == null) {
            this.stack = joinOrigin.clone();
        }
        else {
            this.stack.joinFrom(joinOrigin, this.breakTarget);
        }
    }
    
    void joinFromTry(final Stack joinOrigin, final boolean isOptimismHandler) {
        this.reachable = true;
        if (this.stack == null) {
            if (!isOptimismHandler) {
                (this.stack = joinOrigin.cloneWithEmptyStack()).undefineLocalVariables(this.stack.firstTemp, false);
            }
        }
        else {
            assert !isOptimismHandler;
            this.stack.joinFromTry(joinOrigin);
        }
    }
    
    void markAsBreakTarget() {
        this.breakTarget = true;
    }
    
    boolean isBreakTarget() {
        return this.breakTarget;
    }
    
    void onCatch() {
        if (this.stack != null) {
            this.stack = this.stack.cloneWithEmptyStack();
        }
    }
    
    void markAsOptimisticCatchHandler(final Stack currentStack, final int liveLocalCount) {
        (this.stack = currentStack.cloneWithEmptyStack()).markAsOptimisticCatchHandler(liveLocalCount);
    }
    
    void markAsOptimisticContinuationHandlerFor(final Label afterConsumeStackLabel) {
        this.stack = afterConsumeStackLabel.stack.cloneWithEmptyStack();
    }
    
    boolean isReachable() {
        return this.reachable;
    }
    
    boolean isAfter(final Label other) {
        return this.label.getOffset() > other.label.getOffset();
    }
    
    @Override
    public String toString() {
        if (this.str == null) {
            this.str = this.name + '_' + this.id;
        }
        return this.str;
    }
    
    static {
        Label.nextId = 0;
    }
    
    static final class Stack implements Cloneable
    {
        static final int NON_LOAD = -1;
        Type[] data;
        int[] localLoads;
        int sp;
        List<Type> localVariableTypes;
        int firstTemp;
        BitSet symbolBoundary;
        
        Stack() {
            this.data = new Type[8];
            this.localLoads = new int[8];
            this.localVariableTypes = new ArrayList<Type>(8);
            this.symbolBoundary = new BitSet();
        }
        
        boolean isEmpty() {
            return this.sp == 0;
        }
        
        int size() {
            return this.sp;
        }
        
        void clear() {
            this.sp = 0;
        }
        
        void push(final Type type) {
            if (this.data.length == this.sp) {
                final Type[] newData = new Type[this.sp * 2];
                final int[] newLocalLoad = new int[this.sp * 2];
                System.arraycopy(this.data, 0, newData, 0, this.sp);
                System.arraycopy(this.localLoads, 0, newLocalLoad, 0, this.sp);
                this.data = newData;
                this.localLoads = newLocalLoad;
            }
            this.data[this.sp] = type;
            this.localLoads[this.sp] = -1;
            ++this.sp;
        }
        
        Type peek() {
            return this.peek(0);
        }
        
        Type peek(final int n) {
            final int pos = this.sp - 1 - n;
            return (pos < 0) ? null : this.data[pos];
        }
        
        Type[] getTopTypes(final int count) {
            final Type[] topTypes = new Type[count];
            System.arraycopy(this.data, this.sp - count, topTypes, 0, count);
            return topTypes;
        }
        
        int[] getLocalLoads(final int from, final int to) {
            final int count = to - from;
            final int[] topLocalLoads = new int[count];
            System.arraycopy(this.localLoads, from, topLocalLoads, 0, count);
            return topLocalLoads;
        }
        
        int getUsedSlotsWithLiveTemporaries() {
            int usedSlots = this.firstTemp;
            int i = this.sp;
            while (i-- > 0) {
                final int slot = this.localLoads[i];
                if (slot != -1) {
                    final int afterSlot = slot + this.localVariableTypes.get(slot).getSlots();
                    if (afterSlot <= usedSlots) {
                        continue;
                    }
                    usedSlots = afterSlot;
                }
            }
            return usedSlots;
        }
        
        void joinFrom(final Stack joinOrigin, final boolean breakTarget) {
            assert this.isStackCompatible(joinOrigin);
            if (breakTarget) {
                this.firstTemp = Math.min(this.firstTemp, joinOrigin.firstTemp);
            }
            else {
                assert this.firstTemp == joinOrigin.firstTemp;
            }
            final int[] otherLoads = joinOrigin.localLoads;
            int firstDeadTemp = this.firstTemp;
            for (int i = 0; i < this.sp; ++i) {
                final int localLoad = this.localLoads[i];
                if (localLoad != otherLoads[i]) {
                    this.localLoads[i] = -1;
                }
                else if (localLoad >= firstDeadTemp) {
                    firstDeadTemp = localLoad + this.localVariableTypes.get(localLoad).getSlots();
                }
            }
            this.undefineLocalVariables(firstDeadTemp, false);
            assert this.isVariablePartitioningEqual(joinOrigin, firstDeadTemp);
            this.mergeVariableTypes(joinOrigin, firstDeadTemp);
        }
        
        private void mergeVariableTypes(final Stack joinOrigin, final int toSlot) {
            final ListIterator<Type> it1 = this.localVariableTypes.listIterator();
            final Iterator<Type> it2 = joinOrigin.localVariableTypes.iterator();
            for (int i = 0; i < toSlot; ++i) {
                final Type thisType = it1.next();
                final Type otherType = it2.next();
                if (otherType == Type.UNKNOWN) {
                    it1.set(Type.UNKNOWN);
                }
                else if (thisType != otherType) {
                    if (thisType.isObject() && otherType.isObject()) {
                        it1.set(Type.OBJECT);
                    }
                    else {
                        assert thisType == Type.UNKNOWN;
                    }
                }
            }
        }
        
        void joinFromTry(final Stack joinOrigin) {
            this.firstTemp = Math.min(this.firstTemp, joinOrigin.firstTemp);
            assert this.isVariablePartitioningEqual(joinOrigin, this.firstTemp);
            this.mergeVariableTypes(joinOrigin, this.firstTemp);
        }
        
        private int getFirstDeadLocal(final List<Type> types) {
            int i = types.size();
            final ListIterator<Type> it = types.listIterator(i);
            while (it.hasPrevious() && it.previous() == Type.UNKNOWN) {
                --i;
            }
            while (!this.symbolBoundary.get(i - 1)) {
                ++i;
            }
            return i;
        }
        
        private boolean isStackCompatible(final Stack other) {
            if (this.sp != other.sp) {
                return false;
            }
            for (int i = 0; i < this.sp; ++i) {
                if (!this.data[i].isEquivalentTo(other.data[i])) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean isVariablePartitioningEqual(final Stack other, final int toSlot) {
            final BitSet diff = other.getSymbolBoundaryCopy();
            diff.xor(this.symbolBoundary);
            return diff.previousSetBit(toSlot - 1) == -1;
        }
        
        void markDeadLocalVariables(final int fromSlot, final int slotCount) {
            final int localCount = this.localVariableTypes.size();
            if (fromSlot >= localCount) {
                return;
            }
            final int toSlot = Math.min(fromSlot + slotCount, localCount);
            this.invalidateLocalLoadsOnStack(fromSlot, toSlot);
            for (int i = fromSlot; i < toSlot; ++i) {
                this.localVariableTypes.set(i, Type.UNKNOWN);
            }
        }
        
        List<Type> getLocalVariableTypesCopy() {
            return (List<Type>)((ArrayList)this.localVariableTypes).clone();
        }
        
        BitSet getSymbolBoundaryCopy() {
            return (BitSet)this.symbolBoundary.clone();
        }
        
        List<Type> getWidestLiveLocals(final List<Type> lvarTypes) {
            final List<Type> widestLiveLocals = new ArrayList<Type>(lvarTypes);
            boolean keepNextValue = true;
            final int size = widestLiveLocals.size();
            int i = size - 1;
            while (i-- > 0) {
                if (this.symbolBoundary.get(i)) {
                    keepNextValue = true;
                }
                final Type t = widestLiveLocals.get(i);
                if (t != Type.UNKNOWN) {
                    if (keepNextValue) {
                        if (t == Type.SLOT_2) {
                            continue;
                        }
                        keepNextValue = false;
                    }
                    else {
                        widestLiveLocals.set(i, Type.UNKNOWN);
                    }
                }
            }
            widestLiveLocals.subList(Math.max(this.getFirstDeadLocal(widestLiveLocals), this.firstTemp), widestLiveLocals.size()).clear();
            return widestLiveLocals;
        }
        
        String markSymbolBoundariesInLvarTypesDescriptor(final String lvarDescriptor) {
            final char[] chars = lvarDescriptor.toCharArray();
            int j = 0;
            for (int i = 0; i < chars.length; ++i) {
                final char c = chars[i];
                final int nextj = j + CodeGeneratorLexicalContext.getTypeForSlotDescriptor(c).getSlots();
                if (!this.symbolBoundary.get(nextj - 1)) {
                    chars[i] = Character.toLowerCase(c);
                }
                j = nextj;
            }
            return new String(chars);
        }
        
        Type pop() {
            assert this.sp > 0;
            final Type[] data = this.data;
            final int sp = this.sp - 1;
            this.sp = sp;
            return data[sp];
        }
        
        public Stack clone() {
            try {
                final Stack clone = (Stack)super.clone();
                clone.data = this.data.clone();
                clone.localLoads = this.localLoads.clone();
                clone.symbolBoundary = this.getSymbolBoundaryCopy();
                clone.localVariableTypes = this.getLocalVariableTypesCopy();
                return clone;
            }
            catch (CloneNotSupportedException e) {
                throw new AssertionError("", e);
            }
        }
        
        private Stack cloneWithEmptyStack() {
            final Stack stack = this.clone();
            stack.sp = 0;
            return stack;
        }
        
        int getTopLocalLoad() {
            return this.localLoads[this.sp - 1];
        }
        
        void markLocalLoad(final int slot) {
            this.localLoads[this.sp - 1] = slot;
        }
        
        void onLocalStore(final Type type, final int slot, final boolean onlySymbolLiveValue) {
            if (onlySymbolLiveValue) {
                final int fromSlot = (slot == 0) ? 0 : (this.symbolBoundary.previousSetBit(slot - 1) + 1);
                final int toSlot = this.symbolBoundary.nextSetBit(slot) + 1;
                for (int i = fromSlot; i < toSlot; ++i) {
                    this.localVariableTypes.set(i, Type.UNKNOWN);
                }
                this.invalidateLocalLoadsOnStack(fromSlot, toSlot);
            }
            else {
                this.invalidateLocalLoadsOnStack(slot, slot + type.getSlots());
            }
            this.localVariableTypes.set(slot, type);
            if (type.isCategory2()) {
                this.localVariableTypes.set(slot + 1, Type.SLOT_2);
            }
        }
        
        private void invalidateLocalLoadsOnStack(final int fromSlot, final int toSlot) {
            for (int i = 0; i < this.sp; ++i) {
                final int localLoad = this.localLoads[i];
                if (localLoad >= fromSlot && localLoad < toSlot) {
                    this.localLoads[i] = -1;
                }
            }
        }
        
        void defineBlockLocalVariable(final int fromSlot, final int toSlot) {
            this.defineLocalVariable(fromSlot, toSlot);
            assert this.firstTemp < toSlot;
            this.firstTemp = toSlot;
        }
        
        int defineTemporaryLocalVariable(final int width) {
            final int fromSlot = this.getUsedSlotsWithLiveTemporaries();
            this.defineLocalVariable(fromSlot, fromSlot + width);
            return fromSlot;
        }
        
        void defineTemporaryLocalVariable(final int fromSlot, final int toSlot) {
            this.defineLocalVariable(fromSlot, toSlot);
        }
        
        private void defineLocalVariable(final int fromSlot, final int toSlot) {
            assert !this.hasLoadsOnStack(fromSlot, toSlot);
            assert fromSlot < toSlot;
            this.symbolBoundary.clear(fromSlot, toSlot - 1);
            this.symbolBoundary.set(toSlot - 1);
            final int lastExisting = Math.min(toSlot, this.localVariableTypes.size());
            for (int i = fromSlot; i < lastExisting; ++i) {
                this.localVariableTypes.set(i, Type.UNKNOWN);
            }
            for (int i = lastExisting; i < toSlot; ++i) {
                this.localVariableTypes.add(i, Type.UNKNOWN);
            }
        }
        
        void undefineLocalVariables(final int fromSlot, final boolean canTruncateSymbol) {
            final int lvarCount = this.localVariableTypes.size();
            assert lvarCount == this.symbolBoundary.length();
            assert !this.hasLoadsOnStack(fromSlot, lvarCount);
            if (canTruncateSymbol) {
                if (fromSlot > 0) {
                    this.symbolBoundary.set(fromSlot - 1);
                }
            }
            else {
                assert !(!this.symbolBoundary.get(fromSlot - 1));
            }
            if (fromSlot < lvarCount) {
                this.symbolBoundary.clear(fromSlot, lvarCount);
                this.localVariableTypes.subList(fromSlot, lvarCount).clear();
            }
            this.firstTemp = Math.min(fromSlot, this.firstTemp);
            assert this.symbolBoundary.length() == this.localVariableTypes.size();
            assert this.symbolBoundary.length() == fromSlot;
        }
        
        private void markAsOptimisticCatchHandler(final int liveLocalCount) {
            this.undefineLocalVariables(liveLocalCount, true);
            this.firstTemp = liveLocalCount;
            this.localVariableTypes.subList(this.firstTemp, this.localVariableTypes.size()).clear();
            assert this.symbolBoundary.length() == this.firstTemp;
            final ListIterator<Type> it = this.localVariableTypes.listIterator();
            while (it.hasNext()) {
                final Type type = it.next();
                if (type == Type.BOOLEAN) {
                    it.set(Type.INT);
                }
                else {
                    if (!type.isObject() || type == Type.OBJECT) {
                        continue;
                    }
                    it.set(Type.OBJECT);
                }
            }
        }
        
        boolean hasLoadsOnStack(final int fromSlot, final int toSlot) {
            for (int i = 0; i < this.sp; ++i) {
                final int load = this.localLoads[i];
                if (load >= fromSlot && load < toSlot) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public String toString() {
            return "stack=" + Arrays.toString(Arrays.copyOf(this.data, this.sp)) + ", symbolBoundaries=" + String.valueOf(this.symbolBoundary) + ", firstTemp=" + this.firstTemp + ", localTypes=" + String.valueOf(this.localVariableTypes);
        }
    }
}
