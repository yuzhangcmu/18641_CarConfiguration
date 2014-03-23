package server;

import java.io.IOException;
import java.net.ServerSocket;

import db.MySqlCreateTable;
import adapter.BuildAuto;

public class CarMultiServer implements SocketClientConstants{
    private ServerSocket serverSocket;
    
    public CarMultiServer () {
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(iDAYTIME_PORT);
            
        } catch (IOException e) {
            System.err.printf("Could not listen on port: %d.\n", iDAYTIME_PORT);
            System.exit(1);
        }
    }
    
    public void runServer() {
        ServerDefaultSocketClient clientSocket = null;
        boolean listening = true;
        try {
            while (listening){
                clientSocket = new ServerDefaultSocketClient(serverSocket.accept());
                clientSocket.start();
            }
            
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        // read two automobiles to the hash map.
        BuildAuto buildAuto = new BuildAuto();
        buildAuto.buildAuto("testFile1.txt", 0);
        buildAuto.buildAuto("testFile2.txt", 0);
        buildAuto.buildAuto("testFile3.txt", 0);
        
        // Here we initialize the database.
        MySqlCreateTable.initDatabase();
        
        CarMultiServer server = new CarMultiServer();
        server.runServer();
    }

}
