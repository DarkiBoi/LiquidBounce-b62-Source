// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.JvmField;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00172\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0017B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0011\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0000H\u0096\u0002J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u000e\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0003H\u0016J\u0016\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003J\u001e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0016J \u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003H\u0002R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u000e\u0010\f\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018" }, d2 = { "Lkotlin/KotlinVersion;", "", "major", "", "minor", "(II)V", "patch", "(III)V", "getMajor", "()I", "getMinor", "getPatch", "version", "compareTo", "other", "equals", "", "", "hashCode", "isAtLeast", "toString", "", "versionOf", "Companion", "kotlin-stdlib" })
@SinceKotlin(version = "1.1")
public final class KotlinVersion implements Comparable<KotlinVersion>
{
    private final int version;
    private final int major;
    private final int minor;
    private final int patch;
    public static final int MAX_COMPONENT_VALUE = 255;
    @JvmField
    @NotNull
    public static final KotlinVersion CURRENT;
    public static final Companion Companion;
    
    private final int versionOf(final int major, final int minor, final int patch) {
        final int n = 255;
        boolean b = false;
        Label_0068: {
            if (0 <= major) {
                if (n >= major) {
                    final int n2 = 255;
                    if (0 <= minor) {
                        if (n2 >= minor) {
                            final int n3 = 255;
                            if (0 <= patch) {
                                if (n3 >= patch) {
                                    b = true;
                                    break Label_0068;
                                }
                            }
                        }
                    }
                }
            }
            b = false;
        }
        if (!b) {
            throw new IllegalArgumentException(("Version components are out of range: " + major + '.' + minor + '.' + patch).toString());
        }
        return (major << 16) + (minor << 8) + patch;
    }
    
    @NotNull
    @Override
    public String toString() {
        return new StringBuilder().append(this.major).append('.').append(this.minor).append('.').append(this.patch).toString();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        Object o = other;
        if (!(other instanceof KotlinVersion)) {
            o = null;
        }
        final KotlinVersion kotlinVersion = (KotlinVersion)o;
        if (kotlinVersion != null) {
            final KotlinVersion otherVersion = kotlinVersion;
            return this.version == otherVersion.version;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.version;
    }
    
    @Override
    public int compareTo(@NotNull final KotlinVersion other) {
        Intrinsics.checkParameterIsNotNull(other, "other");
        return this.version - other.version;
    }
    
    public final boolean isAtLeast(final int major, final int minor) {
        return this.major > major || (this.major == major && this.minor >= minor);
    }
    
    public final boolean isAtLeast(final int major, final int minor, final int patch) {
        return this.major > major || (this.major == major && (this.minor > minor || (this.minor == minor && this.patch >= patch)));
    }
    
    public final int getMajor() {
        return this.major;
    }
    
    public final int getMinor() {
        return this.minor;
    }
    
    public final int getPatch() {
        return this.patch;
    }
    
    public KotlinVersion(final int major, final int minor, final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.version = this.versionOf(this.major, this.minor, this.patch);
    }
    
    public KotlinVersion(final int major, final int minor) {
        this(major, minor, 0);
    }
    
    static {
        Companion = new Companion(null);
        CURRENT = new KotlinVersion(1, 3, 21);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0007" }, d2 = { "Lkotlin/KotlinVersion$Companion;", "", "()V", "CURRENT", "Lkotlin/KotlinVersion;", "MAX_COMPONENT_VALUE", "", "kotlin-stdlib" })
    public static final class Companion
    {
        private Companion() {
        }
    }
}
