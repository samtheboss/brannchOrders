/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branchtransfes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samue
 */
public class dbConnection {

    public Connection conn;
    Properties properties = new Properties();
    InputStream inputStream;
    OutputStream outputStream = null;

    String url, user, pass, host, port, db;

    public Connection sqlServerconnection() {
//        user = "corebak";
//        pass = "corebak2012";         
//        String serverName = "192.168.1.9";
//        String dbName = "phaMADB";     
        loadPropertiseFile();
        url = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + db + ";encrypt=true;trustServerCertificate=true";
        System.out.println(url);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection successful");

            return conn;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public Connection db2connection() {
        url = "jdbc:db2://169.239.252.252:50023/salmat";
        user = "salama";
        pass = "salamadb@2021";
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection successful");
            return conn;
        } catch (ClassNotFoundException | SQLException ex) {

            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public void loadPropertiseFile() {
        try {
            inputStream = new FileInputStream("setting.properties");
            properties.load(inputStream);
            host = properties.getProperty("host");
            port = properties.getProperty("port");
            db = properties.getProperty("db");
            user = properties.getProperty("user");
            pass = properties.getProperty("password");
            // url = "jdbc:db2://" + properties.getProperty("host") + ":" + properties.getProperty("port") + "/" + properties.getProperty("db");

        } catch (IOException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
