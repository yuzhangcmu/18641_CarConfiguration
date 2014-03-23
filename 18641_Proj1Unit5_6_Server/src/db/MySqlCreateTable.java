package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// inherit connection method from the MySqlOption class.
public class MySqlCreateTable extends MySqlOption{
    /**
     * Initialize the database.
     * Create three tables.
     * <p>
     *
     * @param   
     * @param  
     * @return 
     */
    public static void initDatabase() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // STEP 4: Execute a query
            System.out.println("Creating table in given database...");
            conn = getConnectionByURL("jdbc:mysql://localhost:3306/");
            stmt = conn.createStatement();

            /*
             * String sql = "CREATE TABLE REGISTRATION " +
             * "(id INTEGER not NULL, " + 
             * " first VARCHAR(255), " +
             * " last VARCHAR(255), " + 
             * " age INTEGER, " +
             * " PRIMARY KEY ( id ))";
             */
            FileReader file = new 
                    FileReader("db_Schema.txt");
            
            BufferedReader buff = new BufferedReader(file);
            
            boolean eof = false;
            
            StringBuilder sb = new StringBuilder();
            
            while(!eof){
                String line = buff.readLine();
                if(line == null){
                    eof = true;
                    break;
                }
                else{
                    System.out.println(line);
                }
                
                sb.append(line);
                if (sb.length() > 0 && sb.charAt(sb.length()-1) == ';') {
                    stmt.executeUpdate(sb.toString());
                    System.out.println("Created table in given database...");
                    // empty the old string builder.
                    sb = new StringBuilder();
                    
                }
            }
            
            buff.close();
            
            //System.out.println("Created table in given database...");
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            
            close(stmt);
            closeConnection(conn);
            
        }// end try
        System.out.println("Goodbye!");

    }
    
    public static void main(String[] args) {
        initDatabase();
    }
    
}
