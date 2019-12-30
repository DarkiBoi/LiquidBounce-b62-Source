// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.chainsaw;

import org.apache.log4j.PropertyConfigurator;
import java.util.Properties;
import java.io.IOException;
import javax.swing.JPanel;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.apache.log4j.Logger;
import javax.swing.JFrame;

public class Main extends JFrame
{
    private static final int DEFAULT_PORT = 4445;
    public static final String PORT_PROP_NAME = "chainsaw.port";
    private static final Logger LOG;
    
    private Main() {
        super("CHAINSAW - Log4J Log Viewer");
        final MyTableModel model = new MyTableModel();
        final JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        final JMenu menu = new JMenu("File");
        menuBar.add(menu);
        try {
            final LoadXMLAction lxa = new LoadXMLAction(this, model);
            final JMenuItem loadMenuItem = new JMenuItem("Load file...");
            menu.add(loadMenuItem);
            loadMenuItem.addActionListener(lxa);
        }
        catch (NoClassDefFoundError e) {
            Main.LOG.info("Missing classes for XML parser", e);
            JOptionPane.showMessageDialog(this, "XML parser not in classpath - unable to load XML events.", "CHAINSAW", 0);
        }
        catch (Exception e2) {
            Main.LOG.info("Unable to create the action to load XML files", e2);
            JOptionPane.showMessageDialog(this, "Unable to create a XML parser - unable to load XML events.", "CHAINSAW", 0);
        }
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        menu.add(exitMenuItem);
        exitMenuItem.addActionListener(ExitAction.INSTANCE);
        final ControlPanel cp = new ControlPanel(model);
        this.getContentPane().add(cp, "North");
        final JTable table = new JTable(model);
        table.setSelectionMode(0);
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Events: "));
        scrollPane.setPreferredSize(new Dimension(900, 300));
        final JPanel details = new DetailPanel(table, model);
        details.setPreferredSize(new Dimension(900, 300));
        final JSplitPane jsp = new JSplitPane(0, scrollPane, details);
        this.getContentPane().add(jsp, "Center");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent aEvent) {
                ExitAction.INSTANCE.actionPerformed(null);
            }
        });
        this.pack();
        this.setVisible(true);
        this.setupReceiver(model);
    }
    
    private void setupReceiver(final MyTableModel aModel) {
        int port = 4445;
        final String strRep = System.getProperty("chainsaw.port");
        if (strRep != null) {
            try {
                port = Integer.parseInt(strRep);
            }
            catch (NumberFormatException nfe) {
                Main.LOG.fatal("Unable to parse chainsaw.port property with value " + strRep + ".");
                JOptionPane.showMessageDialog(this, "Unable to parse port number from '" + strRep + "', quitting.", "CHAINSAW", 0);
                System.exit(1);
            }
        }
        try {
            final LoggingReceiver lr = new LoggingReceiver(aModel, port);
            lr.start();
        }
        catch (IOException e) {
            Main.LOG.fatal("Unable to connect to socket server, quiting", e);
            JOptionPane.showMessageDialog(this, "Unable to create socket on port " + port + ", quitting.", "CHAINSAW", 0);
            System.exit(1);
        }
    }
    
    private static void initLog4J() {
        final Properties props = new Properties();
        props.setProperty("log4j.rootLogger", "DEBUG, A1");
        props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.TTCCLayout");
        PropertyConfigurator.configure(props);
    }
    
    public static void main(final String[] aArgs) {
        initLog4J();
        new Main();
    }
    
    static {
        LOG = Logger.getLogger(Main.class);
    }
}
