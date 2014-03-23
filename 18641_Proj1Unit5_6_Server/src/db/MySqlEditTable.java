package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;
import java.util.Locale;
import java.util.Properties;

import util.FileIO;
import adapter.EditAutoDB;
import model.Automobile;

//inherit connection method from the MySqlOption class.
public class MySqlEditTable extends MySqlOption 
implements EditAutoDB{
    Properties props;
    
    public MySqlEditTable() {
        // read sql from the property file.
        props = (Properties) FileIO.readPropertyFileToObject("db_modify.txt");
        if (props == null) {
            return;
        }
    }
    
    public void displayAutoTable(){
        // get connection;
        Connection conn = getConnection();
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            
            // get sql from the file.
            String sql = props.getProperty("displayAuto");;
            ResultSet rs = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while(rs.next()){
               //Retrieve by column name
               int id  = rs.getInt("id");
               String name = rs.getString("name");
               String make = rs.getString("make");
               String model = rs.getString("model");
               float base_price = rs.getFloat("base_price");
                              
               //Display values
               System.out.print("id: " + id);
               System.out.print(", name: " + name);
               System.out.print(", make: " + make);
               System.out.print(", model: " + model);
               System.out.println(", base_price: " + base_price);
            }
            rs.close();
            
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        // close stmt and conn.
        close(stmt);
        closeConnection(conn);
    }
    
    public void addAuto(Automobile auto) {
        if (auto == null) {
            return;
        }
        
        // get connection;
        Connection conn = getConnection();
        
        StringBuilder sb = new StringBuilder();
        
        // Send all output to the Appendable object sb
        Formatter formatter = new Formatter(sb, Locale.US);

        // Explicit argument indices may be used to re-order output.
        formatter.format(props.getProperty("addAuto"), 
                auto.getName(),
                auto.getMake(),
                auto.getModel(),
                auto.getBasePrice()
                );
        
        formatter.close();
        
        
        Statement stmt = null;
        
        try {
            // create the automobile in the automobile table.
            stmt = conn.createStatement();
            //stmt.executeUpdate(sb.toString());
            
            // get the genereated ID.
            int auto_id = 0;
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS); 
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                auto_id = rs.getInt(1);
            }
            
            if (auto_id == 0) {
                System.out.printf("Error to get autoid.\n");
                return;
            }
            
            // add all the optionsets to the optionset table.
            for(int i = 0; i < auto.getOptionSetNumber(); i++) {
                StringBuilder sbTmp = new StringBuilder();
                formatter = new Formatter(sbTmp, Locale.US);
                // Explicit argument indices may be used to re-order output.
                formatter.format(props.getProperty("addOptionSet"),
                        auto.getOptionSetNameByIndex(i),
                        auto_id
                        );
                formatter.close();
                stmt.executeUpdate(sbTmp.toString(), Statement.RETURN_GENERATED_KEYS);
                int optionSetId = 0;
                rs = stmt.getGeneratedKeys();
                if (rs.next()){
                    optionSetId = rs.getInt(1);
                }
                
                if (optionSetId == 0) {
                    System.out.printf("Error to get optionsetID.\n");
                    return;
                }
                
                for(int j = 0; j < auto.getOptionNumberByIndex(i); j++) {
                    sbTmp = new StringBuilder();
                    formatter = new Formatter(sbTmp, Locale.US);
                    // Explicit argument indices may be used to re-order output.
                    formatter.format(props.getProperty("addOption"),
                            auto.getOptionNameByIndex(i,j),
                            auto.getOptionPriceByIndex(i, j),
                            optionSetId
                            );
                    
                    stmt.executeUpdate(sbTmp.toString(), Statement.RETURN_GENERATED_KEYS);
                    formatter.close();
                }
            }
            
           
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        // close stmt and conn.
        close(stmt);
        closeConnection(conn);
    }

    @Override
    public void updateOptionSetName(String Modelname, String OptionSetname,
            String newName) {
        // can't input null pointer.
        if (Modelname == null || OptionSetname == null || newName == null) {
            return;
        }
        
        // get connection;
        Connection conn = getConnection();
        
        StringBuilder sb = new StringBuilder();
        
        // Send all output to the Appendable object sb
        Formatter formatter = new Formatter(sb, Locale.US);
        
        // first find the automobile id.
        formatter.format(props.getProperty("update_find_auto"), Modelname);
        formatter.close();
        
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());
            
            int autoId = 0;
            int num = 0;
            while(rs.next()){
                //Retrieve by column name
                autoId = rs.getInt("id");
                num ++;
            }
            
            if (num != 1) {
                System.out.printf("Find wrong number of rows:%d.\n", num);
                return;
            }
            
            sb = new StringBuilder();
            // Send all output to the Appendable object sb
            formatter = new Formatter(sb, Locale.US);
            formatter.format(props.getProperty("UPDATE_AUTO_OPTION_NAME"), 
                    newName,autoId,OptionSetname);
            
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

    @Override
    public void updateOptionPrice(String Modelname, String optionSetName,
            String Option, float newprice) {
     // can't input null pointer.
        if (Modelname == null || optionSetName == null || Option == null) {
            return;
        }
        
        // get connection;
        Connection conn = getConnection();
        
        StringBuilder sb = new StringBuilder();
        
        // Send all output to the Appendable object sb
        Formatter formatter = new Formatter(sb, Locale.US);
        
        // first find the automobile id.
        formatter.format(props.getProperty("update_find_auto"), Modelname);
        formatter.close();
        
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());
            
            int autoId = 0;
            int num = 0;
            while(rs.next()){
                //Retrieve by column name
                autoId = rs.getInt("id");
                num ++;
            }
            
            if (num != 1) {
                System.out.printf("Find wrong number of rows:%d.\n", num);
                return;
            }
            
            sb = new StringBuilder();
            // Send all output to the Appendable object sb
            formatter = new Formatter(sb, Locale.US);
            
            // findout the id of the option set.
            formatter.format(props.getProperty("update_find_optionset"), 
                    autoId,optionSetName);
            
            rs = stmt.executeQuery(sb.toString());
            int optionsetId = 0;
            num = 0;
            while(rs.next()){
                //Retrieve by column name
                optionsetId = rs.getInt("id");
                num ++;
            }
            if (num != 1) {
                System.out.printf("Find wrong number of rows:%d.\n", num);
                return;
            }
            
            sb = new StringBuilder();
            // Send all output to the Appendable object sb
            formatter.close();
            formatter = new Formatter(sb, Locale.US);
            
            // set the option price.
            String s = props.getProperty("UPDATE_AUTO_OPTION_PRICE");
            formatter.format(s, 
                    (float)newprice,optionsetId,Option);
            
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
