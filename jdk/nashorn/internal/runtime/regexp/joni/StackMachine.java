// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import java.lang.ref.WeakReference;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;

abstract class StackMachine extends Matcher implements StackType
{
    protected static final int INVALID_INDEX = -1;
    protected StackEntry[] stack;
    protected int stk;
    protected final int[] repeatStk;
    protected final int memStartStk;
    protected final int memEndStk;
    static final ThreadLocal<WeakReference<StackEntry[]>> stacks;
    
    protected StackMachine(final Regex regex, final char[] chars, final int p, final int end) {
        super(regex, chars, p, end);
        this.stack = (StackEntry[])(regex.stackNeeded ? fetchStack() : null);
        final int n = regex.numRepeat + (regex.numMem << 1);
        this.repeatStk = (int[])((n > 0) ? new int[n] : null);
        this.memStartStk = regex.numRepeat - 1;
        this.memEndStk = this.memStartStk + regex.numMem;
    }
    
    private static StackEntry[] allocateStack() {
        final StackEntry[] stack = new StackEntry[64];
        stack[0] = new StackEntry();
        return stack;
    }
    
    private void doubleStack() {
        final StackEntry[] newStack = new StackEntry[this.stack.length << 1];
        System.arraycopy(this.stack, 0, newStack, 0, this.stack.length);
        this.stack = newStack;
    }
    
    private static StackEntry[] fetchStack() {
        WeakReference<StackEntry[]> ref = StackMachine.stacks.get();
        StackEntry[] stack = ref.get();
        if (stack == null) {
            ref = new WeakReference<StackEntry[]>(stack = allocateStack());
            StackMachine.stacks.set(ref);
        }
        return stack;
    }
    
    protected final void init() {
        if (this.stack != null) {
            this.pushEnsured(1, this.regex.codeLength - 1);
        }
        if (this.repeatStk != null) {
            for (int i = 1; i <= this.regex.numMem; ++i) {
                this.repeatStk[i + this.memStartStk] = (this.repeatStk[i + this.memEndStk] = -1);
            }
        }
    }
    
    protected final StackEntry ensure1() {
        if (this.stk >= this.stack.length) {
            this.doubleStack();
        }
        StackEntry e = this.stack[this.stk];
        if (e == null) {
            e = (this.stack[this.stk] = new StackEntry());
        }
        return e;
    }
    
    protected final void pushType(final int type) {
        this.ensure1().type = type;
        ++this.stk;
    }
    
    private void push(final int type, final int pat, final int s, final int prev) {
        final StackEntry e = this.ensure1();
        e.type = type;
        e.setStatePCode(pat);
        e.setStatePStr(s);
        e.setStatePStrPrev(prev);
        ++this.stk;
    }
    
    protected final void pushEnsured(final int type, final int pat) {
        final StackEntry e = this.stack[this.stk];
        e.type = type;
        e.setStatePCode(pat);
        ++this.stk;
    }
    
    protected final void pushAlt(final int pat, final int s, final int prev) {
        this.push(1, pat, s, prev);
    }
    
    protected final void pushPos(final int s, final int prev) {
        this.push(1280, -1, s, prev);
    }
    
    protected final void pushPosNot(final int pat, final int s, final int prev) {
        this.push(3, pat, s, prev);
    }
    
    protected final void pushStopBT() {
        this.pushType(1536);
    }
    
    protected final void pushLookBehindNot(final int pat, final int s, final int sprev) {
        this.push(2, pat, s, sprev);
    }
    
    protected final void pushRepeat(final int id, final int pat) {
        final StackEntry e = this.ensure1();
        e.type = 1792;
        e.setRepeatNum(id);
        e.setRepeatPCode(pat);
        e.setRepeatCount(0);
        ++this.stk;
    }
    
    protected final void pushRepeatInc(final int sindex) {
        final StackEntry e = this.ensure1();
        e.type = 768;
        e.setSi(sindex);
        ++this.stk;
    }
    
    protected final void pushMemStart(final int mnum, final int s) {
        final StackEntry e = this.ensure1();
        e.type = 256;
        e.setMemNum(mnum);
        e.setMemPstr(s);
        e.setMemStart(this.repeatStk[this.memStartStk + mnum]);
        e.setMemEnd(this.repeatStk[this.memEndStk + mnum]);
        this.repeatStk[this.memStartStk + mnum] = this.stk;
        this.repeatStk[this.memEndStk + mnum] = -1;
        ++this.stk;
    }
    
    protected final void pushMemEnd(final int mnum, final int s) {
        final StackEntry e = this.ensure1();
        e.type = 33280;
        e.setMemNum(mnum);
        e.setMemPstr(s);
        e.setMemStart(this.repeatStk[this.memStartStk + mnum]);
        e.setMemEnd(this.repeatStk[this.memEndStk + mnum]);
        this.repeatStk[this.memEndStk + mnum] = this.stk;
        ++this.stk;
    }
    
    protected final void pushMemEndMark(final int mnum) {
        final StackEntry e = this.ensure1();
        e.type = 33792;
        e.setMemNum(mnum);
        ++this.stk;
    }
    
    protected final int getMemStart(final int mnum) {
        int level = 0;
        int stkp = this.stk;
        while (stkp > 0) {
            --stkp;
            final StackEntry e = this.stack[stkp];
            if ((e.type & 0x8000) != 0x0 && e.getMemNum() == mnum) {
                ++level;
            }
            else {
                if (e.type != 256 || e.getMemNum() != mnum) {
                    continue;
                }
                if (level == 0) {
                    break;
                }
                --level;
            }
        }
        return stkp;
    }
    
    protected final void pushNullCheckStart(final int cnum, final int s) {
        final StackEntry e = this.ensure1();
        e.type = 12288;
        e.setNullCheckNum(cnum);
        e.setNullCheckPStr(s);
        ++this.stk;
    }
    
    protected final void pushNullCheckEnd(final int cnum) {
        final StackEntry e = this.ensure1();
        e.type = 20480;
        e.setNullCheckNum(cnum);
        ++this.stk;
    }
    
    protected final void popOne() {
        --this.stk;
    }
    
    protected final StackEntry pop() {
        switch (this.regex.stackPopLevel) {
            case 0: {
                return this.popFree();
            }
            case 1: {
                return this.popMemStart();
            }
            default: {
                return this.popDefault();
            }
        }
    }
    
    private StackEntry popFree() {
        StackEntry e;
        do {
            final StackEntry[] stack = this.stack;
            final int stk = this.stk - 1;
            this.stk = stk;
            e = stack[stk];
        } while ((e.type & 0xFF) == 0x0);
        return e;
    }
    
    private StackEntry popMemStart() {
        StackEntry e;
        while (true) {
            final StackEntry[] stack = this.stack;
            final int stk = this.stk - 1;
            this.stk = stk;
            e = stack[stk];
            if ((e.type & 0xFF) != 0x0) {
                break;
            }
            if (e.type != 256) {
                continue;
            }
            this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
            this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
        }
        return e;
    }
    
    private StackEntry popDefault() {
        StackEntry e;
        while (true) {
            final StackEntry[] stack = this.stack;
            final int stk = this.stk - 1;
            this.stk = stk;
            e = stack[stk];
            if ((e.type & 0xFF) != 0x0) {
                break;
            }
            if (e.type == 256) {
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
            }
            else if (e.type == 768) {
                this.stack[e.getSi()].decreaseRepeatCount();
            }
            else {
                if (e.type != 33280) {
                    continue;
                }
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
            }
        }
        return e;
    }
    
    protected final void popTilPosNot() {
        while (true) {
            --this.stk;
            final StackEntry e = this.stack[this.stk];
            if (e.type == 3) {
                break;
            }
            if (e.type == 256) {
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemStart();
            }
            else if (e.type == 768) {
                this.stack[e.getSi()].decreaseRepeatCount();
            }
            else {
                if (e.type != 33280) {
                    continue;
                }
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemStart();
            }
        }
    }
    
    protected final void popTilLookBehindNot() {
        while (true) {
            --this.stk;
            final StackEntry e = this.stack[this.stk];
            if (e.type == 2) {
                break;
            }
            if (e.type == 256) {
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
            }
            else if (e.type == 768) {
                this.stack[e.getSi()].decreaseRepeatCount();
            }
            else {
                if (e.type != 33280) {
                    continue;
                }
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
            }
        }
    }
    
    protected final int posEnd() {
        int k = this.stk;
        StackEntry e;
        while (true) {
            --k;
            e = this.stack[k];
            if ((e.type & 0x10FF) != 0x0) {
                e.type = 2560;
            }
            else {
                if (e.type == 1280) {
                    break;
                }
                continue;
            }
        }
        e.type = 2560;
        return k;
    }
    
    protected final void stopBtEnd() {
        int k = this.stk;
        StackEntry e;
        while (true) {
            --k;
            e = this.stack[k];
            if ((e.type & 0x10FF) != 0x0) {
                e.type = 2560;
            }
            else {
                if (e.type == 1536) {
                    break;
                }
                continue;
            }
        }
        e.type = 2560;
    }
    
    protected final int nullCheck(final int id, final int s) {
        int k = this.stk;
        StackEntry e;
        do {
            --k;
            e = this.stack[k];
        } while (e.type != 12288 || e.getNullCheckNum() != id);
        return (e.getNullCheckPStr() == s) ? 1 : 0;
    }
    
    protected final int nullCheckMemSt(final int id, final int s) {
        return -this.nullCheck(id, s);
    }
    
    protected final int getRepeat(final int id) {
        int level = 0;
        int k = this.stk;
        while (true) {
            --k;
            final StackEntry e = this.stack[k];
            if (e.type == 1792) {
                if (level == 0 && e.getRepeatNum() == id) {
                    break;
                }
                continue;
            }
            else if (e.type == 2048) {
                --level;
            }
            else {
                if (e.type != 2304) {
                    continue;
                }
                ++level;
            }
        }
        return k;
    }
    
    protected final int sreturn() {
        int level = 0;
        int k = this.stk;
        StackEntry e;
        while (true) {
            --k;
            e = this.stack[k];
            if (e.type == 2048) {
                if (level == 0) {
                    break;
                }
                --level;
            }
            else {
                if (e.type != 2304) {
                    continue;
                }
                ++level;
            }
        }
        return e.getCallFrameRetAddr();
    }
    
    static {
        stacks = new ThreadLocal<WeakReference<StackEntry[]>>() {
            @Override
            protected WeakReference<StackEntry[]> initialValue() {
                return new WeakReference<StackEntry[]>(allocateStack());
            }
        };
    }
}
