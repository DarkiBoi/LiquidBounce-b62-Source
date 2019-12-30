// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.font;

import net.minecraft.client.Minecraft;
import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.ArrayList;
import java.awt.Font;
import java.lang.reflect.Field;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.net.URL;
import java.util.Iterator;
import com.google.gson.GsonBuilder;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.JsonParser;
import java.io.File;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Fonts
{
    @FontDetails(fontName = "Roboto Medium", fontSize = 35)
    public static LiquidFontRenderer font35;
    @FontDetails(fontName = "Roboto Medium", fontSize = 40)
    public static LiquidFontRenderer font40;
    @FontDetails(fontName = "Roboto Bold", fontSize = 180)
    public static LiquidFontRenderer fontBold180;
    @FontDetails(fontName = "Minecraft Font")
    public static final FontRenderer minecraftFont;
    public static final List<LiquidFontRenderer> CUSTOM_FONT_RENDERERS;
    
    public static void loadFonts() {
        final long l = System.currentTimeMillis();
        ClientUtils.getLogger().info("Loading Fonts.");
        downloadFonts();
        Fonts.font35 = new LiquidFontRenderer(getFont("Roboto-Medium.ttf", 35));
        Fonts.font40 = new LiquidFontRenderer(getFont("Roboto-Medium.ttf", 40));
        Fonts.fontBold180 = new LiquidFontRenderer(getFont("Roboto-Bold.ttf", 180));
        try {
            final File fontsFile = new File(LiquidBounce.CLIENT.fileManager.fontsDir, "fonts.json");
            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(fontsFile)));
                if (jsonElement instanceof JsonNull) {
                    return;
                }
                final JsonArray jsonArray = (JsonArray)jsonElement;
                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull) {
                        return;
                    }
                    final JsonObject fontObject = (JsonObject)element;
                    Fonts.CUSTOM_FONT_RENDERERS.add(new LiquidFontRenderer(getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt())));
                }
            }
            else {
                fontsFile.createNewFile();
                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)new JsonArray()));
                printWriter.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }
    
    private static void downloadFonts() {
        try {
            final File outputFile = new File(LiquidBounce.CLIENT.fileManager.fontsDir, "roboto.zip");
            if (!outputFile.exists()) {
                ClientUtils.getLogger().info("Downloading fonts...");
                final URL url = new URL("https://ccbluex.github.io/FileCloud/LiquidBounce/Roboto.zip");
                FileUtils.copyURLToFile(url, outputFile);
                ClientUtils.getLogger().info("Extract fonts...");
                unZipIt(outputFile.getPath(), LiquidBounce.CLIENT.fileManager.fontsDir.getPath());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static FontRenderer getFontRenderer(final String name, final int size) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(null);
                if (o instanceof FontRenderer) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);
                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size) {
                        return (FontRenderer)o;
                    }
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for (final LiquidFontRenderer liquidFontRenderer : Fonts.CUSTOM_FONT_RENDERERS) {
            final Font font = liquidFontRenderer.getFont().getFont();
            if (font.getName().equals(name) && font.getSize() == size) {
                return liquidFontRenderer;
            }
        }
        return Fonts.minecraftFont;
    }
    
    public static Object[] getFontDetails(final FontRenderer fontRenderer) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(null);
                if (o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);
                    return new Object[] { fontDetails.fontName(), fontDetails.fontSize() };
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (fontRenderer instanceof LiquidFontRenderer) {
            final Font font = ((LiquidFontRenderer)fontRenderer).getFont().getFont();
            return new Object[] { font.getName(), font.getSize() };
        }
        return null;
    }
    
    public static List<FontRenderer> getFonts() {
        final List<FontRenderer> fonts = new ArrayList<FontRenderer>();
        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);
                final Object fontObj = fontField.get(null);
                if (fontObj instanceof FontRenderer) {
                    fonts.add((FontRenderer)fontObj);
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS);
        return fonts;
    }
    
    public static List<String> getFontStrings() {
        final List<String> fonts = new ArrayList<String>();
        for (final FontRenderer fontRenderer : getFonts()) {
            final Object[] objects = getFontDetails(fontRenderer);
            if (objects == null) {
                continue;
            }
            fonts.add(objects[0] + " - " + objects[1]);
        }
        return fonts;
    }
    
    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidBounce.CLIENT.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, (float)size);
            inputStream.close();
            return awtClientFont;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Font("default", 0, size);
        }
    }
    
    private static void unZipIt(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];
        try {
            final File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }
            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
                final File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();
                final FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int i;
                while ((i = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, i);
                }
                fileOutputStream.close();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static {
        minecraftFont = Minecraft.func_71410_x().field_71466_p;
        CUSTOM_FONT_RENDERERS = new ArrayList<LiquidFontRenderer>();
    }
}
