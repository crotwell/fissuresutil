package edu.sc.seis.fissuresUtil.database;

import java.sql.*;

import org.hsqldb.*;
import org.apache.log4j.*;

/**
 * This class acts an abstract class for database Operations.
 * AbstractDb.java
 *
 *
 * Created: Fri Feb  7 10:45:30 2003
 *
 * @author <a href="mailto:">Srinivasa Telukutla</a>
 * @version
 */

public abstract class AbstractDb {
    public AbstractDb (){

    }

    public Connection getConnection() {
        try {
            if(connection == null) {
                String driverName = new String("org.hsqldb.jdbcDriver");
                Class.forName(driverName).newInstance();
                connection = DriverManager.getConnection("jdbc:hsqldb:"+directoryName+"/"+databaseName, "sa", "");
            }
            return connection;
        } catch(Exception sqle) {
            sqle.printStackTrace();
            return null;
        }

    }

    public abstract void create() throws SQLException;

    protected Connection connection;

    protected String databaseName = "GEE_database";

    protected String directoryName =
        System.getProperty("java.io.tmpdir")+"/"+
        "GEE_cache_"+
        System.getProperty("user.name").replaceAll("\\W","_");

}// AbstractDb
