// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Objects;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.api.scripting.JSObject;
import java.lang.invoke.MethodHandle;
import java.util.concurrent.Callable;
import java.util.Deque;
import java.util.RandomAccess;
import java.util.AbstractList;

public class ListAdapter extends AbstractList<Object> implements RandomAccess, Deque<Object>
{
    private static final Callable<MethodHandle> ADD_INVOKER_CREATOR;
    private static final Object PUSH;
    private static final Object UNSHIFT;
    private static final Callable<MethodHandle> REMOVE_INVOKER_CREATOR;
    private static final Object POP;
    private static final Object SHIFT;
    private static final Object SPLICE_ADD;
    private static final Callable<MethodHandle> SPLICE_ADD_INVOKER_CREATOR;
    private static final Object SPLICE_REMOVE;
    private static final Callable<MethodHandle> SPLICE_REMOVE_INVOKER_CREATOR;
    final JSObject obj;
    private final Global global;
    
    ListAdapter(final JSObject obj, final Global global) {
        if (global == null) {
            throw new IllegalStateException(ECMAErrors.getMessage("list.adapter.null.global", new String[0]));
        }
        this.obj = obj;
        this.global = global;
    }
    
    public static ListAdapter create(final Object obj) {
        final Global global = Context.getGlobal();
        return new ListAdapter(getJSObject(obj, global), global);
    }
    
    private static JSObject getJSObject(final Object obj, final Global global) {
        if (obj instanceof ScriptObject) {
            return (JSObject)ScriptObjectMirror.wrap(obj, global);
        }
        if (obj instanceof JSObject) {
            return (JSObject)obj;
        }
        throw new IllegalArgumentException("ScriptObject or JSObject expected");
    }
    
    @Override
    public final Object get(final int index) {
        this.checkRange(index);
        return this.getAt(index);
    }
    
    private Object getAt(final int index) {
        return this.obj.getSlot(index);
    }
    
    @Override
    public Object set(final int index, final Object element) {
        this.checkRange(index);
        final Object prevValue = this.getAt(index);
        this.obj.setSlot(index, element);
        return prevValue;
    }
    
    private void checkRange(final int index) {
        if (index < 0 || index >= this.size()) {
            throw invalidIndex(index);
        }
    }
    
    @Override
    public int size() {
        return JSType.toInt32(this.obj.getMember("length"));
    }
    
    @Override
    public final void push(final Object e) {
        this.addFirst(e);
    }
    
    @Override
    public final boolean add(final Object e) {
        this.addLast(e);
        return true;
    }
    
    @Override
    public final void addFirst(final Object e) {
        try {
            this.getDynamicInvoker(ListAdapter.UNSHIFT, ListAdapter.ADD_INVOKER_CREATOR).invokeExact(this.getFunction("unshift"), this.obj, e);
        }
        catch (RuntimeException | Error ex2) {
            final Throwable t2;
            final Throwable ex = t2;
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    @Override
    public final void addLast(final Object e) {
        try {
            this.getDynamicInvoker(ListAdapter.PUSH, ListAdapter.ADD_INVOKER_CREATOR).invokeExact(this.getFunction("push"), this.obj, e);
        }
        catch (RuntimeException | Error ex2) {
            final Throwable t2;
            final Throwable ex = t2;
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    @Override
    public final void add(final int index, final Object e) {
        try {
            if (index < 0) {
                throw invalidIndex(index);
            }
            if (index == 0) {
                this.addFirst(e);
            }
            else {
                final int size = this.size();
                if (index < size) {
                    this.getDynamicInvoker(ListAdapter.SPLICE_ADD, ListAdapter.SPLICE_ADD_INVOKER_CREATOR).invokeExact(this.obj.getMember("splice"), this.obj, index, 0, e);
                }
                else {
                    if (index != size) {
                        throw invalidIndex(index);
                    }
                    this.addLast(e);
                }
            }
        }
        catch (RuntimeException | Error ex2) {
            final Throwable t2;
            final Throwable ex = t2;
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private Object getFunction(final String name) {
        final Object fn = this.obj.getMember(name);
        if (!Bootstrap.isCallable(fn)) {
            throw new UnsupportedOperationException("The script object doesn't have a function named " + name);
        }
        return fn;
    }
    
    private static IndexOutOfBoundsException invalidIndex(final int index) {
        return new IndexOutOfBoundsException(String.valueOf(index));
    }
    
    @Override
    public final boolean offer(final Object e) {
        return this.offerLast(e);
    }
    
    @Override
    public final boolean offerFirst(final Object e) {
        this.addFirst(e);
        return true;
    }
    
    @Override
    public final boolean offerLast(final Object e) {
        this.addLast(e);
        return true;
    }
    
    @Override
    public final Object pop() {
        return this.removeFirst();
    }
    
    @Override
    public final Object remove() {
        return this.removeFirst();
    }
    
    @Override
    public final Object removeFirst() {
        this.checkNonEmpty();
        return this.invokeShift();
    }
    
    @Override
    public final Object removeLast() {
        this.checkNonEmpty();
        return this.invokePop();
    }
    
    private void checkNonEmpty() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
    }
    
    @Override
    public final Object remove(final int index) {
        if (index < 0) {
            throw invalidIndex(index);
        }
        if (index == 0) {
            return this.invokeShift();
        }
        final int maxIndex = this.size() - 1;
        if (index < maxIndex) {
            final Object prevValue = this.get(index);
            this.invokeSpliceRemove(index, 1);
            return prevValue;
        }
        if (index == maxIndex) {
            return this.invokePop();
        }
        throw invalidIndex(index);
    }
    
    private Object invokeShift() {
        try {
            return this.getDynamicInvoker(ListAdapter.SHIFT, ListAdapter.REMOVE_INVOKER_CREATOR).invokeExact(this.getFunction("shift"), this.obj);
        }
        catch (RuntimeException | Error ex2) {
            final Throwable t2;
            final Throwable ex = t2;
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private Object invokePop() {
        try {
            return this.getDynamicInvoker(ListAdapter.POP, ListAdapter.REMOVE_INVOKER_CREATOR).invokeExact(this.getFunction("pop"), this.obj);
        }
        catch (RuntimeException | Error ex2) {
            final Throwable t2;
            final Throwable ex = t2;
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    @Override
    protected final void removeRange(final int fromIndex, final int toIndex) {
        this.invokeSpliceRemove(fromIndex, toIndex - fromIndex);
    }
    
    private void invokeSpliceRemove(final int fromIndex, final int count) {
        try {
            this.getDynamicInvoker(ListAdapter.SPLICE_REMOVE, ListAdapter.SPLICE_REMOVE_INVOKER_CREATOR).invokeExact(this.getFunction("splice"), this.obj, fromIndex, count);
        }
        catch (RuntimeException | Error ex2) {
            final Throwable t2;
            final Throwable ex = t2;
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    @Override
    public final Object poll() {
        return this.pollFirst();
    }
    
    @Override
    public final Object pollFirst() {
        return this.isEmpty() ? null : this.invokeShift();
    }
    
    @Override
    public final Object pollLast() {
        return this.isEmpty() ? null : this.invokePop();
    }
    
    @Override
    public final Object peek() {
        return this.peekFirst();
    }
    
    @Override
    public final Object peekFirst() {
        return this.isEmpty() ? null : this.get(0);
    }
    
    @Override
    public final Object peekLast() {
        return this.isEmpty() ? null : this.get(this.size() - 1);
    }
    
    @Override
    public final Object element() {
        return this.getFirst();
    }
    
    @Override
    public final Object getFirst() {
        this.checkNonEmpty();
        return this.get(0);
    }
    
    @Override
    public final Object getLast() {
        this.checkNonEmpty();
        return this.get(this.size() - 1);
    }
    
    @Override
    public final Iterator<Object> descendingIterator() {
        final ListIterator<Object> it = this.listIterator(this.size());
        return new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return it.hasPrevious();
            }
            
            @Override
            public Object next() {
                return it.previous();
            }
            
            @Override
            public void remove() {
                it.remove();
            }
        };
    }
    
    @Override
    public final boolean removeFirstOccurrence(final Object o) {
        return removeOccurrence(o, this.iterator());
    }
    
    @Override
    public final boolean removeLastOccurrence(final Object o) {
        return removeOccurrence(o, this.descendingIterator());
    }
    
    private static boolean removeOccurrence(final Object o, final Iterator<Object> it) {
        while (it.hasNext()) {
            if (Objects.equals(o, it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }
    
    private static Callable<MethodHandle> invokerCreator(final Class<?> rtype, final Class<?>... ptypes) {
        return new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", rtype, (Class<?>[])ptypes);
            }
        };
    }
    
    private MethodHandle getDynamicInvoker(final Object key, final Callable<MethodHandle> creator) {
        return this.global.getDynamicInvoker(key, creator);
    }
    
    static {
        ADD_INVOKER_CREATOR = invokerCreator(Void.TYPE, Object.class, JSObject.class, Object.class);
        PUSH = new Object();
        UNSHIFT = new Object();
        REMOVE_INVOKER_CREATOR = invokerCreator(Object.class, Object.class, JSObject.class);
        POP = new Object();
        SHIFT = new Object();
        SPLICE_ADD = new Object();
        SPLICE_ADD_INVOKER_CREATOR = invokerCreator(Void.TYPE, Object.class, JSObject.class, Integer.TYPE, Integer.TYPE, Object.class);
        SPLICE_REMOVE = new Object();
        SPLICE_REMOVE_INVOKER_CREATOR = invokerCreator(Void.TYPE, Object.class, JSObject.class, Integer.TYPE, Integer.TYPE);
    }
}
