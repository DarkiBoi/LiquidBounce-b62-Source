// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidinstruction;

import java.awt.Component;
import javax.swing.JLabel;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class LiquidInstruction
{
    public static void main(final String[] args) {
        final JFrame jFrame = new JFrame("LiquidBounce | Install Instruction.");
        jFrame.setDefaultCloseOperation(3);
        jFrame.setLayout(new BorderLayout());
        jFrame.setResizable(false);
        jFrame.setAlwaysOnTop(true);
        final JLabel jLabel = new JLabel("<html><h1>LiquidBounce install Instruction</h1><h3>1.) Download Forge for Minecraft 1.8.9 from <a href=\"https://minecraftforge.net/\">https://minecraftforge.net/</a><br>2.) Install Forge<br>3.) Open up your Minecraft folder<br>4.) Create a folder called 'mods'<br>5.) Drag and drop this file into it<br>6.) Run the Minecraft launcher and select the Forge profile</h3></html>");
        jFrame.add(jLabel, "Center");
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
