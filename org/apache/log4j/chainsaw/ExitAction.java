// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.chainsaw;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import javax.swing.AbstractAction;

class ExitAction extends AbstractAction
{
    private static final Logger LOG;
    public static final ExitAction INSTANCE;
    
    private ExitAction() {
    }
    
    public void actionPerformed(final ActionEvent aIgnore) {
        ExitAction.LOG.info("shutting down");
        System.exit(0);
    }
    
    static {
        LOG = Logger.getLogger(ExitAction.class);
        INSTANCE = new ExitAction();
    }
}
