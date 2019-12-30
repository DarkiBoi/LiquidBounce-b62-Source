// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.altmanager.sub.altgenerator.thealtening;

import net.minecraft.client.gui.Gui;
import com.thealtening.utilities.SSLVerification;
import net.ccbluex.liquidbounce.ui.GuiPasswordField;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.LiquidFontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import org.jetbrains.annotations.NotNull;
import com.thealtening.domain.User;
import net.minecraft.client.gui.GuiTextField;
import kotlin.Metadata;
import net.minecraft.client.gui.GuiScreen;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0006\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0014J \u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\fH\u0016J\u0018\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0011H\u0014J \u0010\u001a\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u0011H\u0014J\b\u0010\u001c\u001a\u00020\fH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e" }, d2 = { "Lnet/ccbluex/liquidbounce/ui/altmanager/sub/altgenerator/thealtening/GuiTheAltening;", "Lnet/minecraft/client/gui/GuiScreen;", "prevScreen", "(Lnet/minecraft/client/gui/GuiScreen;)V", "apiKeyField", "Lnet/minecraft/client/gui/GuiTextField;", "status", "", "tokenField", "user", "Lcom/thealtening/domain/User;", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "onGuiClosed", "Companion", "LiquidBounce" })
public final class GuiTheAltening extends GuiScreen
{
    private GuiTextField apiKeyField;
    private GuiTextField tokenField;
    private String status;
    private User user;
    private final GuiScreen prevScreen;
    @NotNull
    private static String apiKey;
    public static final Companion Companion;
    
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 105, "Login"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 175, "Generate"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 60, "Back"));
        this.tokenField = new GuiTextField(666, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 80, 200, 20);
        final GuiTextField tokenField = this.tokenField;
        if (tokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        tokenField.func_146195_b(true);
        final GuiTextField tokenField2 = this.tokenField;
        if (tokenField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        tokenField2.func_146203_f(Integer.MAX_VALUE);
        this.apiKeyField = new GuiPasswordField(1337, Fonts.font40, this.field_146294_l / 2 - 100, 150, 200, 20);
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        apiKeyField.func_146203_f(18);
        final GuiTextField apiKeyField2 = this.apiKeyField;
        if (apiKeyField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        apiKeyField2.func_146180_a(GuiTheAltening.apiKey);
        final SSLVerification sslVerification = new SSLVerification();
        sslVerification.verify();
        super.func_73866_w_();
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a(30, 30, this.field_146294_l - 30, this.field_146295_m - 30, Integer.MIN_VALUE);
        this.func_73732_a((FontRenderer)Fonts.font35, "TheAltening", this.field_146294_l / 2, 36, 16777215);
        this.func_73732_a((FontRenderer)Fonts.font35, this.status, this.field_146294_l / 2, this.field_146295_m - 70, 16777215);
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        apiKeyField.func_146194_f();
        final GuiTextField tokenField = this.tokenField;
        if (tokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        tokenField.func_146194_f();
        this.func_73732_a((FontRenderer)Fonts.font40, "§7API-Key:", this.field_146294_l / 2 - 78, 137, 16777215);
        this.func_73732_a((FontRenderer)Fonts.font40, "§7Token:", this.field_146294_l / 2 - 84, 66, 16777215);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(@NotNull final GuiButton button) {
        Intrinsics.checkParameterIsNotNull(button, "button");
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevScreen);
                break;
            }
            case 1: {
                new Thread((Runnable)new GuiTheAltening$actionPerformed.GuiTheAltening$actionPerformed$1(this)).start();
                break;
            }
            case 2: {
                new Thread((Runnable)new GuiTheAltening$actionPerformed.GuiTheAltening$actionPerformed$2(this)).start();
                break;
            }
        }
        super.func_146284_a(button);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevScreen);
            return;
        }
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        if (apiKeyField.func_146206_l()) {
            final GuiTextField apiKeyField2 = this.apiKeyField;
            if (apiKeyField2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            }
            apiKeyField2.func_146201_a(typedChar, keyCode);
        }
        final GuiTextField tokenField = this.tokenField;
        if (tokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        if (tokenField.func_146206_l()) {
            final GuiTextField tokenField2 = this.tokenField;
            if (tokenField2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            }
            tokenField2.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) {
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        apiKeyField.func_146192_a(mouseX, mouseY, mouseButton);
        final GuiTextField tokenField = this.tokenField;
        if (tokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        tokenField.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        final String func_146179_b = apiKeyField.func_146179_b();
        Intrinsics.checkExpressionValueIsNotNull(func_146179_b, "apiKeyField.text");
        GuiTheAltening.apiKey = func_146179_b;
        super.func_146281_b();
    }
    
    public GuiTheAltening(@NotNull final GuiScreen prevScreen) {
        Intrinsics.checkParameterIsNotNull(prevScreen, "prevScreen");
        this.prevScreen = prevScreen;
        this.status = "";
    }
    
    static {
        Companion = new Companion(null);
        GuiTheAltening.apiKey = "";
    }
    
    public static final /* synthetic */ String access$getApiKey$cp() {
        return GuiTheAltening.apiKey;
    }
    
    public static final /* synthetic */ void access$setApiKey$cp(final String <set-?>) {
        GuiTheAltening.apiKey = <set-?>;
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\t" }, d2 = { "Lnet/ccbluex/liquidbounce/ui/altmanager/sub/altgenerator/thealtening/GuiTheAltening$Companion;", "", "()V", "apiKey", "", "getApiKey", "()Ljava/lang/String;", "setApiKey", "(Ljava/lang/String;)V", "LiquidBounce" })
    public static final class Companion
    {
        @NotNull
        public final String getApiKey() {
            return GuiTheAltening.access$getApiKey$cp();
        }
        
        public final void setApiKey(@NotNull final String <set-?>) {
            Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
            GuiTheAltening.access$setApiKey$cp(<set-?>);
        }
        
        private Companion() {
        }
    }
}
