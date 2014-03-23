package db;

//STEP 1. Import required packages
import java.sql.*;

abstract public class MySqlOption {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/carconfig";
    
    // Database credentials
    static final String USER = "root";
    static final String PASS = "";
    
    // the first time to connect to the database.
    protected static Connection getConnectionByURL(String DB_URL_gcbu) {
        // STEP 3: Open a connection
        System.out.println("Connecting to a selected database...");
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(DB_URL_gcbu, USER, PASS);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
        System.out.println("Connected database successfully...");
        
        return conn;
    }
    
    protected static Connection getConnection() {
        return getConnectionByURL(DB_URL);
    }
    
    // close the connection;
    protected static void closeConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void close(Statement stmt){
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException se) {
        }// do nothing
    }
    
}
