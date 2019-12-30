// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.NoWhenBranchMatchedException;
import java.util.ArrayDeque;
import kotlin.collections.AbstractIterator;
import kotlin._Assertions;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.io.IOException;
import kotlin.jvm.functions.Function2;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;
import java.io.File;
import kotlin.sequences.Sequence;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\u001a\u001b\u001cB\u0019\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006B\u0089\u0001\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u0012\u0014\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u00128\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\r\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014¢\u0006\u0002\u0010\u0015J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0096\u0002J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0014J\u001a\u0010\u0007\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\bJ \u0010\f\u001a\u00020\u00002\u0018\u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u000b0\rJ\u001a\u0010\n\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u0004¢\u0006\u0002\n\u0000R@\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d" }, d2 = { "Lkotlin/io/FileTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/io/File;", "start", "direction", "Lkotlin/io/FileWalkDirection;", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;)V", "onEnter", "Lkotlin/Function1;", "", "onLeave", "", "onFail", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "f", "Ljava/io/IOException;", "e", "maxDepth", "", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;I)V", "iterator", "", "depth", "function", "DirectoryState", "FileTreeWalkIterator", "WalkState", "kotlin-stdlib" })
public final class FileTreeWalk implements Sequence<File>
{
    private final File start;
    private final FileWalkDirection direction;
    private final Function1<File, Boolean> onEnter;
    private final Function1<File, Unit> onLeave;
    private final Function2<File, IOException, Unit> onFail;
    private final int maxDepth;
    
    @NotNull
    @Override
    public Iterator<File> iterator() {
        return new FileTreeWalkIterator();
    }
    
    @NotNull
    public final FileTreeWalk onEnter(@NotNull final Function1<? super File, Boolean> function) {
        Intrinsics.checkParameterIsNotNull(function, "function");
        return new FileTreeWalk(this.start, this.direction, function, this.onLeave, this.onFail, this.maxDepth);
    }
    
    @NotNull
    public final FileTreeWalk onLeave(@NotNull final Function1<? super File, Unit> function) {
        Intrinsics.checkParameterIsNotNull(function, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, function, this.onFail, this.maxDepth);
    }
    
    @NotNull
    public final FileTreeWalk onFail(@NotNull final Function2<? super File, ? super IOException, Unit> function) {
        Intrinsics.checkParameterIsNotNull(function, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, function, this.maxDepth);
    }
    
    @NotNull
    public final FileTreeWalk maxDepth(final int depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("depth must be positive, but was " + depth + '.');
        }
        return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, this.onFail, depth);
    }
    
    private FileTreeWalk(final File start, final FileWalkDirection direction, final Function1<? super File, Boolean> onEnter, final Function1<? super File, Unit> onLeave, final Function2<? super File, ? super IOException, Unit> onFail, final int maxDepth) {
        this.start = start;
        this.direction = direction;
        this.onEnter = (Function1<File, Boolean>)onEnter;
        this.onLeave = (Function1<File, Unit>)onLeave;
        this.onFail = (Function2<File, IOException, Unit>)onFail;
        this.maxDepth = maxDepth;
    }
    
    public FileTreeWalk(@NotNull final File start, @NotNull final FileWalkDirection direction) {
        Intrinsics.checkParameterIsNotNull(start, "start");
        Intrinsics.checkParameterIsNotNull(direction, "direction");
        this(start, direction, null, null, null, 0, 32, null);
    }
    
    public static final /* synthetic */ Function1 access$getOnEnter$p(final FileTreeWalk $this) {
        return $this.onEnter;
    }
    
    public static final /* synthetic */ Function2 access$getOnFail$p(final FileTreeWalk $this) {
        return $this.onFail;
    }
    
    public static final /* synthetic */ Function1 access$getOnLeave$p(final FileTreeWalk $this) {
        return $this.onLeave;
    }
    
    public static final /* synthetic */ FileWalkDirection access$getDirection$p(final FileTreeWalk $this) {
        return $this.direction;
    }
    
    public static final /* synthetic */ int access$getMaxDepth$p(final FileTreeWalk $this) {
        return $this.maxDepth;
    }
    
    public static final /* synthetic */ File access$getStart$p(final FileTreeWalk $this) {
        return $this.start;
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H&R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\b" }, d2 = { "Lkotlin/io/FileTreeWalk$WalkState;", "", "root", "Ljava/io/File;", "(Ljava/io/File;)V", "getRoot", "()Ljava/io/File;", "step", "kotlin-stdlib" })
    private abstract static class WalkState
    {
        @NotNull
        private final File root;
        
        @Nullable
        public abstract File step();
        
        @NotNull
        public final File getRoot() {
            return this.root;
        }
        
        public WalkState(@NotNull final File root) {
            Intrinsics.checkParameterIsNotNull(root, "root");
            this.root = root;
        }
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005" }, d2 = { "Lkotlin/io/FileTreeWalk$DirectoryState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootDir", "Ljava/io/File;", "(Ljava/io/File;)V", "kotlin-stdlib" })
    private abstract static class DirectoryState extends WalkState
    {
        public DirectoryState(@NotNull final File rootDir) {
            Intrinsics.checkParameterIsNotNull(rootDir, "rootDir");
            super(rootDir);
            if (_Assertions.ENABLED) {
                final boolean directory = rootDir.isDirectory();
                if (_Assertions.ENABLED && !directory) {
                    throw new AssertionError((Object)"rootDir must be verified to be directory beforehand.");
                }
            }
        }
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\r\u000e\u000fB\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0014J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0082\u0010R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010" }, d2 = { "Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;", "Lkotlin/collections/AbstractIterator;", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk;)V", "state", "Ljava/util/ArrayDeque;", "Lkotlin/io/FileTreeWalk$WalkState;", "computeNext", "", "directoryState", "Lkotlin/io/FileTreeWalk$DirectoryState;", "root", "gotoNext", "BottomUpDirectoryState", "SingleFileState", "TopDownDirectoryState", "kotlin-stdlib" })
    private final class FileTreeWalkIterator extends AbstractIterator<File>
    {
        private final ArrayDeque<WalkState> state;
        
        @Override
        protected void computeNext() {
            final File nextFile = this.gotoNext();
            if (nextFile != null) {
                this.setNext(nextFile);
            }
            else {
                this.done();
            }
        }
        
        private final DirectoryState directoryState(final File root) {
            DirectoryState directoryState = null;
            switch (FileTreeWalk$FileTreeWalkIterator$WhenMappings.$EnumSwitchMapping$0[FileTreeWalk.access$getDirection$p(FileTreeWalk.this).ordinal()]) {
                case 1: {
                    directoryState = new TopDownDirectoryState(root);
                    break;
                }
                case 2: {
                    directoryState = new BottomUpDirectoryState(root);
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
            return directoryState;
        }
        
        private final File gotoNext() {
            while (true) {
                final WalkState walkState = this.state.peek();
                if (walkState == null) {
                    return null;
                }
                final WalkState topState = walkState;
                final File file = topState.step();
                if (file == null) {
                    this.state.pop();
                }
                else {
                    if (Intrinsics.areEqual(file, topState.getRoot()) || !file.isDirectory() || this.state.size() >= FileTreeWalk.access$getMaxDepth$p(FileTreeWalk.this)) {
                        return file;
                    }
                    this.state.push(this.directoryState(file));
                }
            }
        }
        
        public FileTreeWalkIterator() {
            this.state = new ArrayDeque<WalkState>();
            if (FileTreeWalk.access$getStart$p(FileTreeWalk.this).isDirectory()) {
                this.state.push(this.directoryState(FileTreeWalk.access$getStart$p(FileTreeWalk.this)));
            }
            else if (FileTreeWalk.access$getStart$p(FileTreeWalk.this).isFile()) {
                this.state.push(new SingleFileState(FileTreeWalk.access$getStart$p(FileTreeWalk.this)));
            }
            else {
                this.done();
            }
        }
        
        @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\r\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\nX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e" }, d2 = { "Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$BottomUpDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "failed", "", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "step", "kotlin-stdlib" })
        private final class BottomUpDirectoryState extends DirectoryState
        {
            private boolean rootVisited;
            private File[] fileList;
            private int fileIndex;
            private boolean failed;
            
            @Nullable
            @Override
            public File step() {
                if (!this.failed && this.fileList == null) {
                    final Function1 access$getOnEnter$p = FileTreeWalk.access$getOnEnter$p(FileTreeWalk.this);
                    if (access$getOnEnter$p != null) {
                        if (!access$getOnEnter$p.invoke(((WalkState)this).getRoot())) {
                            return null;
                        }
                    }
                    this.fileList = ((WalkState)this).getRoot().listFiles();
                    if (this.fileList == null) {
                        final Function2 access$getOnFail$p = FileTreeWalk.access$getOnFail$p(FileTreeWalk.this);
                        if (access$getOnFail$p != null) {
                            final Unit unit = access$getOnFail$p.invoke(((WalkState)this).getRoot(), new AccessDeniedException(((WalkState)this).getRoot(), null, "Cannot list files in a directory", 2, null));
                        }
                        this.failed = true;
                    }
                }
                if (this.fileList != null) {
                    final int fileIndex = this.fileIndex;
                    final File[] fileList = this.fileList;
                    if (fileList == null) {
                        Intrinsics.throwNpe();
                    }
                    if (fileIndex < fileList.length) {
                        final File[] fileList2 = this.fileList;
                        if (fileList2 == null) {
                            Intrinsics.throwNpe();
                        }
                        final int fileIndex2;
                        this.fileIndex = (fileIndex2 = this.fileIndex) + 1;
                        return fileList2[fileIndex2];
                    }
                }
                if (!this.rootVisited) {
                    this.rootVisited = true;
                    return ((WalkState)this).getRoot();
                }
                final Function1 access$getOnLeave$p = FileTreeWalk.access$getOnLeave$p(FileTreeWalk.this);
                if (access$getOnLeave$p != null) {
                    final Unit unit2 = access$getOnLeave$p.invoke(((WalkState)this).getRoot());
                }
                return null;
            }
            
            public BottomUpDirectoryState(final File rootDir) {
                Intrinsics.checkParameterIsNotNull(rootDir, "rootDir");
                super(rootDir);
            }
        }
        
        @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\r" }, d2 = { "Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$TopDownDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "", "step", "kotlin-stdlib" })
        private final class TopDownDirectoryState extends DirectoryState
        {
            private boolean rootVisited;
            private File[] fileList;
            private int fileIndex;
            
            @Nullable
            @Override
            public File step() {
                if (!this.rootVisited) {
                    final Function1 access$getOnEnter$p = FileTreeWalk.access$getOnEnter$p(FileTreeWalk.this);
                    if (access$getOnEnter$p != null) {
                        if (!access$getOnEnter$p.invoke(((WalkState)this).getRoot())) {
                            return null;
                        }
                    }
                    this.rootVisited = true;
                    return ((WalkState)this).getRoot();
                }
                if (this.fileList != null) {
                    final int fileIndex = this.fileIndex;
                    final File[] fileList = this.fileList;
                    if (fileList == null) {
                        Intrinsics.throwNpe();
                    }
                    if (fileIndex >= fileList.length) {
                        final Function1 access$getOnLeave$p = FileTreeWalk.access$getOnLeave$p(FileTreeWalk.this);
                        if (access$getOnLeave$p != null) {
                            final Unit unit = access$getOnLeave$p.invoke(((WalkState)this).getRoot());
                        }
                        return null;
                    }
                }
                Label_0211: {
                    if (this.fileList == null) {
                        this.fileList = ((WalkState)this).getRoot().listFiles();
                        if (this.fileList == null) {
                            final Function2 access$getOnFail$p = FileTreeWalk.access$getOnFail$p(FileTreeWalk.this);
                            if (access$getOnFail$p != null) {
                                final Unit unit2 = access$getOnFail$p.invoke(((WalkState)this).getRoot(), new AccessDeniedException(((WalkState)this).getRoot(), null, "Cannot list files in a directory", 2, null));
                            }
                        }
                        if (this.fileList != null) {
                            final File[] fileList2 = this.fileList;
                            if (fileList2 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (fileList2.length != 0) {
                                break Label_0211;
                            }
                        }
                        final Function1 access$getOnLeave$p2 = FileTreeWalk.access$getOnLeave$p(FileTreeWalk.this);
                        if (access$getOnLeave$p2 != null) {
                            final Unit unit3 = access$getOnLeave$p2.invoke(((WalkState)this).getRoot());
                        }
                        return null;
                    }
                }
                final File[] fileList3 = this.fileList;
                if (fileList3 == null) {
                    Intrinsics.throwNpe();
                }
                final int fileIndex2;
                this.fileIndex = (fileIndex2 = this.fileIndex) + 1;
                return fileList3[fileIndex2];
            }
            
            public TopDownDirectoryState(final File rootDir) {
                Intrinsics.checkParameterIsNotNull(rootDir, "rootDir");
                super(rootDir);
            }
        }
        
        @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\b" }, d2 = { "Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$SingleFileState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootFile", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "visited", "", "step", "kotlin-stdlib" })
        private final class SingleFileState extends WalkState
        {
            private boolean visited;
            
            @Nullable
            @Override
            public File step() {
                if (this.visited) {
                    return null;
                }
                this.visited = true;
                return ((WalkState)this).getRoot();
            }
            
            public SingleFileState(final File rootFile) {
                Intrinsics.checkParameterIsNotNull(rootFile, "rootFile");
                super(rootFile);
                if (_Assertions.ENABLED) {
                    final boolean file = rootFile.isFile();
                    if (_Assertions.ENABLED && !file) {
                        throw new AssertionError((Object)"rootFile must be verified to be file beforehand.");
                    }
                }
            }
        }
    }
}
