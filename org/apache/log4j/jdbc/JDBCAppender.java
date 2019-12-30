// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.jdbc;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import java.util.Iterator;
import java.util.Collection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.spi.LoggingEvent;
import java.util.ArrayList;
import java.sql.Connection;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;

public class JDBCAppender extends AppenderSkeleton implements Appender
{
    protected String databaseURL;
    protected String databaseUser;
    protected String databasePassword;
    protected Connection connection;
    protected String sqlStatement;
    protected int bufferSize;
    protected ArrayList buffer;
    protected ArrayList removes;
    private boolean locationInfo;
    
    public JDBCAppender() {
        this.databaseURL = "jdbc:odbc:myDB";
        this.databaseUser = "me";
        this.databasePassword = "mypassword";
        this.connection = null;
        this.sqlStatement = "";
        this.bufferSize = 1;
        this.locationInfo = false;
        this.buffer = new ArrayList(this.bufferSize);
        this.removes = new ArrayList(this.bufferSize);
    }
    
    public boolean getLocationInfo() {
        return this.locationInfo;
    }
    
    public void setLocationInfo(final boolean flag) {
        this.locationInfo = flag;
    }
    
    public void append(final LoggingEvent event) {
        event.getNDC();
        event.getThreadName();
        event.getMDCCopy();
        if (this.locationInfo) {
            event.getLocationInformation();
        }
        event.getRenderedMessage();
        event.getThrowableStrRep();
        this.buffer.add(event);
        if (this.buffer.size() >= this.bufferSize) {
            this.flushBuffer();
        }
    }
    
    protected String getLogStatement(final LoggingEvent event) {
        return this.getLayout().format(event);
    }
    
    protected void execute(final String sql) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        try {
            con = this.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
            this.closeConnection(con);
        }
    }
    
    protected void closeConnection(final Connection con) {
    }
    
    protected Connection getConnection() throws SQLException {
        if (!DriverManager.getDrivers().hasMoreElements()) {
            this.setDriver("sun.jdbc.odbc.JdbcOdbcDriver");
        }
        if (this.connection == null) {
            this.connection = DriverManager.getConnection(this.databaseURL, this.databaseUser, this.databasePassword);
        }
        return this.connection;
    }
    
    public void close() {
        this.flushBuffer();
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        }
        catch (SQLException e) {
            this.errorHandler.error("Error closing connection", e, 0);
        }
        this.closed = true;
    }
    
    public void flushBuffer() {
        this.removes.ensureCapacity(this.buffer.size());
        for (final LoggingEvent logEvent : this.buffer) {
            try {
                final String sql = this.getLogStatement(logEvent);
                this.execute(sql);
            }
            catch (SQLException e) {
                this.errorHandler.error("Failed to excute sql", e, 2);
            }
            finally {
                this.removes.add(logEvent);
            }
        }
        this.buffer.removeAll(this.removes);
        this.removes.clear();
    }
    
    public void finalize() {
        this.close();
    }
    
    public boolean requiresLayout() {
        return true;
    }
    
    public void setSql(final String s) {
        this.sqlStatement = s;
        if (this.getLayout() == null) {
            this.setLayout(new PatternLayout(s));
        }
        else {
            ((PatternLayout)this.getLayout()).setConversionPattern(s);
        }
    }
    
    public String getSql() {
        return this.sqlStatement;
    }
    
    public void setUser(final String user) {
        this.databaseUser = user;
    }
    
    public void setURL(final String url) {
        this.databaseURL = url;
    }
    
    public void setPassword(final String password) {
        this.databasePassword = password;
    }
    
    public void setBufferSize(final int newBufferSize) {
        this.bufferSize = newBufferSize;
        this.buffer.ensureCapacity(this.bufferSize);
        this.removes.ensureCapacity(this.bufferSize);
    }
    
    public String getUser() {
        return this.databaseUser;
    }
    
    public String getURL() {
        return this.databaseURL;
    }
    
    public String getPassword() {
        return this.databasePassword;
    }
    
    public int getBufferSize() {
        return this.bufferSize;
    }
    
    public void setDriver(final String driverClass) {
        try {
            Class.forName(driverClass);
        }
        catch (Exception e) {
            this.errorHandler.error("Failed to load driver", e, 0);
        }
    }
}
