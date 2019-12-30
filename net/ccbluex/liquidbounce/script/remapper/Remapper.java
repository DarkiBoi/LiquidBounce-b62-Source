// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.remapper;

import net.ccbluex.liquidbounce.LiquidBounce;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import kotlin.TypeCastException;
import java.util.List;
import java.nio.charset.Charset;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import java.util.HashMap;
import java.io.File;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\r\u001a\u00020\fH\u0002J\u001a\u0010\u000e\u001a\u00020\u00052\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u00102\u0006\u0010\u0011\u001a\u00020\u0005J\"\u0010\u0012\u001a\u00020\u00052\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u00102\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005RR\u0010\u0003\u001aF\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00040\u0004j*\u0012\u0004\u0012\u00020\u0005\u0012 \u0012\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005`\u0006`\u0006X\u0082\u0004¢\u0006\u0002\n\u0000RR\u0010\u0007\u001aF\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00040\u0004j*\u0012\u0004\u0012\u00020\u0005\u0012 \u0012\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005`\u0006`\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0014" }, d2 = { "Lnet/ccbluex/liquidbounce/script/remapper/Remapper;", "", "()V", "fields", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "methods", "srgFile", "Ljava/io/File;", "srgName", "loadSrg", "", "parseSrg", "remapField", "clazz", "Ljava/lang/Class;", "name", "remapMethod", "desc", "LiquidBounce" })
public final class Remapper
{
    private static final String srgName = "stable_22";
    private static final File srgFile;
    private static final HashMap<String, HashMap<String, String>> fields;
    private static final HashMap<String, HashMap<String, String>> methods;
    public static final Remapper INSTANCE;
    
    public final void loadSrg() {
        if (!Remapper.srgFile.exists()) {
            Remapper.srgFile.createNewFile();
            ClientUtils.getLogger().info("[Remapper] Downloading stable_22 srg...");
            final File srgFile = Remapper.srgFile;
            final String content = NetworkUtils.readContent("https://ccbluex.github.io/FileCloud/LiquidBounce/srg/mcp-stable_22.srg");
            Intrinsics.checkExpressionValueIsNotNull(content, "NetworkUtils.readContent\u2026ce/srg/mcp-$srgName.srg\")");
            FilesKt__FileReadWriteKt.writeText$default(srgFile, content, null, 2, null);
            ClientUtils.getLogger().info("[Remapper] Downloaded stable_22.");
        }
        ClientUtils.getLogger().info("[Remapper] Loading srg...");
        this.parseSrg();
        ClientUtils.getLogger().info("[Remapper] Loaded srg.");
    }
    
    private final void parseSrg() {
        final Iterable $receiver$iv = FilesKt__FileReadWriteKt.readLines$default(Remapper.srgFile, null, 1, null);
        for (final Object element$iv : $receiver$iv) {
            final String it = (String)element$iv;
            final int n = 0;
            final List args = StringsKt__StringsKt.split$default(it, new String[] { " " }, false, 0, 6, null);
            if (StringsKt__StringsJVMKt.startsWith$default(it, "FD:", false, 2, null)) {
                final String name = args.get(1);
                final String srg = args.get(2);
                final String s = name;
                final int beginIndex = 0;
                final int lastIndexOf$default = StringsKt__StringsKt.lastIndexOf$default(name, '/', 0, false, 6, null);
                final String s2 = s;
                if (s2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring = s2.substring(beginIndex, lastIndexOf$default);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                final String className = StringsKt__StringsJVMKt.replace$default(substring, '/', '.', false, 4, null);
                final String s3 = name;
                final int beginIndex2 = StringsKt__StringsKt.lastIndexOf$default(name, '/', 0, false, 6, null) + 1;
                final String s4 = s3;
                if (s4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring2 = s4.substring(beginIndex2);
                Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.String).substring(startIndex)");
                final String fieldName = substring2;
                final String s5 = srg;
                final int beginIndex3 = StringsKt__StringsKt.lastIndexOf$default(srg, '/', 0, false, 6, null) + 1;
                final String s6 = s5;
                if (s6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring3 = s6.substring(beginIndex3);
                Intrinsics.checkExpressionValueIsNotNull(substring3, "(this as java.lang.String).substring(startIndex)");
                final String fieldSrg = substring3;
                if (!Remapper.fields.containsKey(className)) {
                    Remapper.fields.put(className, new HashMap<String, String>());
                }
                final HashMap<String, String> value = Remapper.fields.get(className);
                if (value == null) {
                    Intrinsics.throwNpe();
                }
                Intrinsics.checkExpressionValueIsNotNull(value, "fields[className]!!");
                value.put(fieldSrg, fieldName);
            }
            else {
                if (!StringsKt__StringsJVMKt.startsWith$default(it, "MD:", false, 2, null)) {
                    continue;
                }
                final String name = args.get(1);
                final String desc = args.get(2);
                final String srg2 = args.get(3);
                final String s7 = name;
                final int beginIndex4 = 0;
                final int lastIndexOf$default2 = StringsKt__StringsKt.lastIndexOf$default(name, '/', 0, false, 6, null);
                final String s8 = s7;
                if (s8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring4 = s8.substring(beginIndex4, lastIndexOf$default2);
                Intrinsics.checkExpressionValueIsNotNull(substring4, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                final String className2 = StringsKt__StringsJVMKt.replace$default(substring4, '/', '.', false, 4, null);
                final String s9 = name;
                final int beginIndex5 = StringsKt__StringsKt.lastIndexOf$default(name, '/', 0, false, 6, null) + 1;
                final String s10 = s9;
                if (s10 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring5 = s10.substring(beginIndex5);
                Intrinsics.checkExpressionValueIsNotNull(substring5, "(this as java.lang.String).substring(startIndex)");
                final String methodName = substring5;
                final String s11 = srg2;
                final int beginIndex6 = StringsKt__StringsKt.lastIndexOf$default(srg2, '/', 0, false, 6, null) + 1;
                final String s12 = s11;
                if (s12 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring6 = s12.substring(beginIndex6);
                Intrinsics.checkExpressionValueIsNotNull(substring6, "(this as java.lang.String).substring(startIndex)");
                final String methodSrg = substring6;
                if (!Remapper.methods.containsKey(className2)) {
                    Remapper.methods.put(className2, new HashMap<String, String>());
                }
                final HashMap<String, String> value2 = Remapper.methods.get(className2);
                if (value2 == null) {
                    Intrinsics.throwNpe();
                }
                Intrinsics.checkExpressionValueIsNotNull(value2, "methods[className]!!");
                value2.put(methodSrg + desc, methodName);
            }
        }
    }
    
    @NotNull
    public final String remapField(@NotNull final Class<?> clazz, @NotNull final String name) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        Intrinsics.checkParameterIsNotNull(name, "name");
        if (!Remapper.fields.containsKey(clazz.getName())) {
            return name;
        }
        final HashMap<String, String> value = Remapper.fields.get(clazz.getName());
        if (value == null) {
            Intrinsics.throwNpe();
        }
        final String orDefault = value.getOrDefault(name, name);
        Intrinsics.checkExpressionValueIsNotNull(orDefault, "fields[clazz.name]!!.getOrDefault(name, name)");
        return orDefault;
    }
    
    @NotNull
    public final String remapMethod(@NotNull final Class<?> clazz, @NotNull final String name, @NotNull final String desc) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(desc, "desc");
        if (!Remapper.methods.containsKey(clazz.getName())) {
            return name;
        }
        final HashMap<String, String> value = Remapper.methods.get(clazz.getName());
        if (value == null) {
            Intrinsics.throwNpe();
        }
        final String orDefault = value.getOrDefault(name + desc, name);
        Intrinsics.checkExpressionValueIsNotNull(orDefault, "methods[clazz.name]!!.ge\u2026efault(name + desc, name)");
        return orDefault;
    }
    
    private Remapper() {
    }
    
    static {
        INSTANCE = new Remapper();
        srgFile = new File(LiquidBounce.CLIENT.fileManager.dir, "mcp-stable_22.srg");
        fields = new HashMap<String, HashMap<String, String>>();
        methods = new HashMap<String, HashMap<String, String>>();
    }
}
