// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal;

public class IntDeque
{
    private int[] deque;
    private int nextFree;
    
    public IntDeque() {
        this.deque = new int[16];
        this.nextFree = 0;
    }
    
    public void push(final int value) {
        if (this.nextFree == this.deque.length) {
            final int[] newDeque = new int[this.nextFree * 2];
            System.arraycopy(this.deque, 0, newDeque, 0, this.nextFree);
            this.deque = newDeque;
        }
        this.deque[this.nextFree++] = value;
    }
    
    public int pop() {
        final int[] deque = this.deque;
        final int nextFree = this.nextFree - 1;
        this.nextFree = nextFree;
        return deque[nextFree];
    }
    
    public int peek() {
        return this.deque[this.nextFree - 1];
    }
    
    public int getAndIncrement() {
        return this.deque[this.nextFree - 1]++;
    }
    
    public int decrementAndGet() {
        final int[] deque = this.deque;
        final int n = this.nextFree - 1;
        return --deque[n];
    }
    
    public boolean isEmpty() {
        return this.nextFree == 0;
    }
}
