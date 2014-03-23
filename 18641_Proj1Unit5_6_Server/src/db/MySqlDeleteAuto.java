package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;
import java.util.Locale;
import java.util.Properties;

import util.FileIO;
import adapter.DeteleAutoDB;
import adapter.EditAutoDB;
import model.Automobile;

//inherit connection method from the MySqlOption class.
public class MySqlDeleteAuto extends MySqlOption 
implements DeteleAutoDB{
    Properties props;
    
    public MySqlDeleteAuto() {
        // read sql from the property file.
        props = (Properties) FileIO.readPropertyFileToObject("db_modify.txt");
        if (props == null) {
            return;
        }
    }
    
    public void delAuto(Automobile auto) {
        if (auto == null) {
            return;
        }
        
        // get connection;
        Connection conn = getConnection();
        
        StringBuilder sb = new StringBuilder();
        
        // Send all output to the Appendable object sb
        Formatter formatter = new Formatter(sb, Locale.US);
        
        // Explicit argument indices may be used to re-order output.
        formatter.format(props.getProperty("delAuto"), auto.getName());
        
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            
            stmt.executeUpdate(sb.toString());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            formatter.close();
            return;
        }
        
        // close stmt and conn.
        close(stmt);
        closeConnection(conn);
        formatter.close();
    }
}
