// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.altmanager;

import java.awt.Color;
import net.minecraft.client.gui.GuiSlot;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;
import java.io.IOException;
import java.util.Iterator;
import java.io.File;
import net.ccbluex.liquidbounce.ui.altmanager.sub.altgenerator.thealtening.GuiTheAltening;
import net.ccbluex.liquidbounce.ui.altmanager.sub.GuiChangeName;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import net.ccbluex.liquidbounce.ui.altmanager.sub.GuiDirectLogin;
import net.ccbluex.liquidbounce.ui.altmanager.sub.altgenerator.mcleaks.GuiMCLeaks;
import java.util.Random;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.ui.altmanager.sub.GuiAdd;
import net.minecraft.util.Session;
import net.mcleaks.MCLeaks;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import com.google.gson.JsonElement;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import net.ccbluex.liquidbounce.utils.misc.JsonUtils;
import com.thealtening.AltService;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiList altsList;
    private String status;
    private static final Map<String, Boolean> GENERATORS;
    public static final AltService altService;
    
    public GuiAltManager(final GuiScreen prevGui) {
        this.status = "§7Idle...";
        this.prevGui = prevGui;
    }
    
    public static void loadGenerators() {
        try {
            final JsonElement jsonElement = JsonUtils.JSON_PARSER.parse(NetworkUtils.readContent("https://ccbluex.github.io/FileCloud/LiquidBounce/generators.json"));
            if (jsonElement.isJsonObject()) {
                final JsonObject jsonObject = jsonElement.getAsJsonObject();
                final Boolean b;
                jsonObject.entrySet().forEach(stringJsonElementEntry -> b = GuiAltManager.GENERATORS.put(stringJsonElementEntry.getKey(), ((JsonElement)stringJsonElementEntry.getValue()).getAsBoolean()));
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load enabled generators.", throwable);
        }
    }
    
    public void func_73866_w_() {
        (this.altsList = new GuiList(this)).func_148134_d(7, 8);
        this.altsList.func_148144_a(-1, false, 0, 0);
        final int j = 22;
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l - 80, j + 24, 70, 20, "Add"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 80, j + 48, 70, 20, "Remove"));
        this.field_146292_n.add(new GuiButton(7, this.field_146294_l - 80, j + 72, 70, 20, "Import"));
        this.field_146292_n.add(new GuiButton(8, this.field_146294_l - 80, j + 96, 70, 20, "Copy"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l - 80, this.field_146295_m - 65, 70, 20, "Back"));
        this.field_146292_n.add(new GuiButton(3, 5, j + 24, 90, 20, "Login"));
        this.field_146292_n.add(new GuiButton(4, 5, j + 48, 90, 20, "Random"));
        this.field_146292_n.add(new GuiButton(6, 5, j + 72, 90, 20, "Direct Login"));
        this.field_146292_n.add(new GuiButton(88, 5, j + 96, 90, 20, "Change Name"));
        if (GuiAltManager.GENERATORS.getOrDefault("mcleaks", false)) {
            this.field_146292_n.add(new GuiButton(5, 5, j + 120, 90, 20, "MCLeaks"));
        }
        if (GuiAltManager.GENERATORS.getOrDefault("thealtening", false)) {
            this.field_146292_n.add(new GuiButton(9, 5, j + 144, 90, 20, "TheAltening"));
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        this.altsList.func_148128_a(mouseX, mouseY, partialTicks);
        Fonts.font40.drawCenteredString("AltManager", this.field_146294_l / 2, 6, 16777215);
        Fonts.font35.drawCenteredString(LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.size() + " Alts", this.field_146294_l / 2, 18, 16777215);
        Fonts.font35.drawCenteredString(this.status, this.field_146294_l / 2, 32, 16777215);
        Fonts.font35.func_78276_b("§7User: §a" + (MCLeaks.isAltActive() ? MCLeaks.getSession().getUsername() : this.field_146297_k.func_110432_I().func_111285_a()), 6, 6, 16777215);
        Fonts.font35.func_78276_b("§7Type: §a" + ((GuiAltManager.altService.getCurrentService() == AltService.EnumAltService.THEALTENING) ? "TheAltening" : (MCLeaks.isAltActive() ? "MCLeaks" : ((this.field_146297_k.func_110432_I().func_152428_f() == Session.Type.LEGACY) ? "Cracked" : "Premium"))), 6, 15, 16777215);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(final GuiButton button) throws IOException {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiAdd(this));
                break;
            }
            case 2: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.func_148127_b()) {
                    LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.remove(this.altsList.getSelectedSlot());
                    LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.accountsConfig);
                    this.status = "§aThe account was removed.";
                    break;
                }
                this.status = "§cSelect an account.";
                break;
            }
            case 3: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.func_148127_b()) {
                    final MinecraftAccount minecraftAccount;
                    final Thread thread = new Thread(() -> {
                        minecraftAccount = LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.get(this.altsList.getSelectedSlot());
                        this.status = "§cLogging in...";
                        this.status = login(minecraftAccount);
                        return;
                    }, "AltLogin");
                    thread.start();
                    break;
                }
                this.status = "§cSelect an account.";
                break;
            }
            case 4: {
                if (LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.size() <= 0) {
                    this.status = "§cList is empty.";
                    return;
                }
                final int randomInteger = new Random().nextInt(LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.size());
                if (randomInteger < this.altsList.func_148127_b()) {
                    this.altsList.selectedSlot = randomInteger;
                }
                final MinecraftAccount minecraftAccount2;
                final Thread thread2 = new Thread(() -> {
                    minecraftAccount2 = LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.get(randomInteger);
                    this.status = "§cLogging in...";
                    this.status = login(minecraftAccount2);
                    return;
                }, "AltLogin");
                thread2.start();
                break;
            }
            case 5: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiMCLeaks(this));
                break;
            }
            case 6: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiDirectLogin(this));
                break;
            }
            case 7: {
                final JFileChooser fileChooser = new JFileChooser() {
                    @Override
                    protected JDialog createDialog(final Component parent) throws HeadlessException {
                        final JDialog jDialog = super.createDialog(parent);
                        jDialog.setAlwaysOnTop(true);
                        return jDialog;
                    }
                };
                if (fileChooser.showOpenDialog(null) == 0) {
                    final File file = fileChooser.getSelectedFile();
                    final FileReader fileReader = new FileReader(file);
                    final BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        final String[] accountData = line.split(":");
                        boolean alreadyAdded = false;
                        for (final MinecraftAccount registeredMinecraftAccount : LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts) {
                            if (registeredMinecraftAccount.getName().equalsIgnoreCase(accountData[0])) {
                                alreadyAdded = true;
                                break;
                            }
                        }
                        if (!alreadyAdded) {
                            if (accountData.length > 1) {
                                LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.add(new MinecraftAccount(accountData[0], accountData[1]));
                            }
                            else {
                                LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.add(new MinecraftAccount(accountData[0]));
                            }
                        }
                    }
                    fileReader.close();
                    bufferedReader.close();
                    LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.accountsConfig);
                    this.status = "§aAccounts was imported.";
                    break;
                }
                break;
            }
            case 8: {
                if (this.altsList.getSelectedSlot() == -1 || this.altsList.getSelectedSlot() >= this.altsList.func_148127_b()) {
                    this.status = "§cSelect an account.";
                    break;
                }
                final MinecraftAccount minecraftAccount3 = LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.get(this.altsList.getSelectedSlot());
                if (minecraftAccount3 == null) {
                    break;
                }
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(minecraftAccount3.getName() + ":" + minecraftAccount3.getPassword()), null);
                this.status = "§aCopied Account into your clipboard.";
                break;
            }
            case 88: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiChangeName(this));
                break;
            }
            case 9: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiTheAltening(this));
                break;
            }
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        switch (keyCode) {
            case 1: {
                this.field_146297_k.func_147108_a(this.prevGui);
                return;
            }
            case 200: {
                int i = this.altsList.getSelectedSlot() - 1;
                if (i < 0) {
                    i = 0;
                }
                this.altsList.func_148144_a(i, false, 0, 0);
                break;
            }
            case 208: {
                int i = this.altsList.getSelectedSlot() + 1;
                if (i >= this.altsList.func_148127_b()) {
                    i = this.altsList.func_148127_b() - 1;
                }
                this.altsList.func_148144_a(i, false, 0, 0);
                break;
            }
            case 28: {
                this.altsList.func_148144_a(this.altsList.getSelectedSlot(), true, 0, 0);
                break;
            }
        }
        super.func_73869_a(typedChar, keyCode);
    }
    
    public void func_146274_d() throws IOException {
        super.func_146274_d();
        this.altsList.func_178039_p();
    }
    
    public static String login(final MinecraftAccount minecraftAccount) {
        if (minecraftAccount == null) {
            return "";
        }
        if (GuiAltManager.altService.getCurrentService() != AltService.EnumAltService.MOJANG) {
            try {
                GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
            }
            catch (NoSuchFieldException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                ClientUtils.getLogger().error("Something went wrong while trying to switch alt service.", (Throwable)e);
            }
        }
        if (minecraftAccount.getPassword() == null) {
            LoginUtils.loginCracked(minecraftAccount.getName());
            MCLeaks.remove();
            return "§cYour name is now §8" + minecraftAccount.getName() + "§c.";
        }
        final LoginUtils.LoginResult result = LoginUtils.login(minecraftAccount.getName(), minecraftAccount.getPassword());
        if (result == LoginUtils.LoginResult.LOGGED) {
            MCLeaks.remove();
            final String userName = Minecraft.func_71410_x().func_110432_I().func_111285_a();
            minecraftAccount.setAccountName(userName);
            LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.accountsConfig);
            return "§cYour name is now §f§l" + userName + "§c.";
        }
        if (result == LoginUtils.LoginResult.WRONG_PASSWORD) {
            return "§cWrong password.";
        }
        if (result == LoginUtils.LoginResult.NO_CONTACT) {
            return "§cCannot contact authentication server.";
        }
        if (result == LoginUtils.LoginResult.INVALID_ACCOUNT_DATA) {
            return "§cInvaild username or password.";
        }
        if (result == LoginUtils.LoginResult.MIGRATED) {
            return "§cAccount migrated.";
        }
        return "";
    }
    
    static {
        GENERATORS = new HashMap<String, Boolean>();
        altService = new AltService();
    }
    
    private class GuiList extends GuiSlot
    {
        private int selectedSlot;
        
        GuiList(final GuiScreen prevGui) {
            super(Minecraft.func_71410_x(), prevGui.field_146294_l, prevGui.field_146295_m, 40, prevGui.field_146295_m - 40, 30);
        }
        
        protected boolean func_148131_a(final int id) {
            return this.selectedSlot == id;
        }
        
        int getSelectedSlot() {
            if (this.selectedSlot > LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.size()) {
                this.selectedSlot = -1;
            }
            return this.selectedSlot;
        }
        
        protected int func_148127_b() {
            return LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.size();
        }
        
        protected void func_148144_a(final int var1, final boolean doubleClick, final int var3, final int var4) {
            this.selectedSlot = var1;
            if (doubleClick) {
                if (GuiAltManager.this.altsList.getSelectedSlot() != -1 && GuiAltManager.this.altsList.getSelectedSlot() < GuiAltManager.this.altsList.func_148127_b()) {
                    final MinecraftAccount minecraftAccount;
                    final Thread thread = new Thread(() -> {
                        minecraftAccount = LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.get(GuiAltManager.this.altsList.getSelectedSlot());
                        GuiAltManager.this.status = "§cLogging in...";
                        GuiAltManager.this.status = "§c" + GuiAltManager.login(minecraftAccount);
                        return;
                    }, "AltManagerLogin");
                    thread.start();
                }
                else {
                    GuiAltManager.this.status = "§cSelect an account.";
                }
            }
        }
        
        public void setSelectedSlot(final int selectedSlot) {
            this.selectedSlot = selectedSlot;
        }
        
        protected void func_180791_a(final int id, final int x, final int y, final int var4, final int var5, final int var6) {
            final MinecraftAccount minecraftAccount = LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.get(id);
            Fonts.font40.drawCenteredString((minecraftAccount.getAccountName() == null) ? minecraftAccount.getName() : minecraftAccount.getAccountName(), this.field_148155_a / 2, y + 2, Color.WHITE.getRGB(), true);
            Fonts.font40.drawCenteredString(minecraftAccount.isCracked() ? "Cracked" : ((minecraftAccount.getAccountName() == null) ? "Premium" : minecraftAccount.getName()), this.field_148155_a / 2, y + 15, minecraftAccount.isCracked() ? Color.GRAY.getRGB() : ((minecraftAccount.getAccountName() == null) ? Color.GREEN.getRGB() : Color.LIGHT_GRAY.getRGB()), true);
        }
        
        protected void func_148123_a() {
        }
    }
}
