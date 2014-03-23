package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

import util.FileIO;
import adapter.BuildAuto;
import adapter.EditThread;
import model.Automobile;

public class ServerDefaultSocketClient extends Thread 
implements SocketCLientInterface, SocketClientConstants{    
    private Socket sock;
    private String strHost;
    
    private int iPort;
        
    // output the object.
    private ObjectOutputStream oos;
    
    // the input object stream.
    private ObjectInputStream ois;
    
    public ServerDefaultSocketClient(String strHost, int iPort) {
        setPort(iPort);
        setHost(strHost);
    }
    
    public ServerDefaultSocketClient(Socket socket) {
        this.sock = socket;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setPort(int iPort) {
        this.iPort = iPort;
    }
    
    public void setHost(String strHost) {
        this.strHost = strHost;
    }
    

    @Override
    public boolean openConnection() {
        
        if (sock == null) {
            // create a new socket.
            try {
                sock = new Socket(strHost, iPort);
            } catch (IOException socketError) {
                if (DEBUG) {
                    System.err.println("Unable to connect to " + strHost);
                }
                return false;
            }
        }
        
        // can't get the sock.
        if (sock == null) {
            return false;
        }
        
        return true;
    }
    
    private void carConfigProtocol() throws IOException {
        // read the config file sent from the client.
        
        while(true) {
            int command;
            command = readCommand();

            if (DEBUG) {
                System.out.printf("Receive command: %d\n", command);
            }

            switch (command) {
                case -1:
                    continue;
                    
                case COMMAND_STOP:
                    return;
                    
                // the client send a serilized config file to the server.
                case COMMAND_SEND_FILE:
                    // read the file and save automobile to the Hashmap.
                    readAndSave();

                    // send a "Success" command to the client.
                    setCommand(COMMAND_SRV_CREATE_AUTO_SUSS);
                    oos.flush();
                    break;

                // client want all the automobiles stored in     
                case COMMAND_GET_AVAILABLE_MODELS:
                    

                    // create the list and send back to the client.
                    String str;
                    EditThread eT = new BuildAuto();
                    str = eT.getAvailableAutos();
                    if (str == null) {
                        System.out.printf("Get avaiable autos fail\n");
                        setCommand(COMMAND_SERVER_DONTHAVEAUTO);
                        oos.flush();
                        break;
                    }
                    
                    // send back a list of the available Automobiles
                    //setCommand(COMMAND_GET_AVAILABLE_MODELS);
                    
                    System.out.printf("Write back avaiable auto:%s\n", str);
                    sendMessage(str);
                    oos.flush();
                    break;

                // client selected a model.    
                case COMMAND_SELECT_MODEL:
                    String modelName;
                    try {
                        modelName = (String) getMessage();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return;
                    }
                    
                    System.out.printf("Client want:%s\n", modelName);

                    EditThread eT2 = new BuildAuto();
                    Automobile auto = eT2.getAutomobileByName(modelName);
                    
                    if (auto == null) {
                        System.out.printf("Server don't have this auto:%s\n", modelName);
                        // there is some error.
                        //setCommand(COMMAND_SERVER_DONTHAVEAUTO);
                        //oos.flush();
                        break;
                    }

                    // send back a automobile file.
                    //setCommand(COMMAND_SERVER_SEND_AUTO);
                    sendMessage(auto);
                    oos.flush();
                    
                    System.out.printf("Server send back the auto which client want:%s\n", auto.getName());
                    break;
                    
                case COMMAND_GET_ALL_MODELS:
                    System.out.printf("Client want all the auto mobiles.\n");
                    
                    EditThread eT3 = new BuildAuto();
                    LinkedHashMap<String, Automobile> LHM = eT3.getAutoHashMap();
                    
                    if (LHM == null) {
                        System.out.printf("Server don't have autos\n");
                        // there is some error.
                        //setCommand(COMMAND_SERVER_DONTHAVEAUTO);
                        break;
                    }

                    // send back a automobile file.
                    //setCommand(COMMAND_GET_ALL_MODELS);
                    sendMessage(LHM);
                    oos.flush();
                    
                    System.out.printf("Server send back the autos client want:%s\n", LHM.toString());
                    break;

                default:
                    break;
            }
        }
    }
    
    protected void readAndSave() throws IOException {
        Object propertyObj;
        try {
            propertyObj = getMessage();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        buildAutoFromPropertyFile(propertyObj);
    }
    
    public void buildAutoFromPropertyFile(Object propertyObj) {
        AutoServer auto = new BuildAuto();
        auto.buildAutoFromPropertyFile(propertyObj);
    }
    
    

    @Override
    public void handleSession() {
        if (DEBUG) {
            if (strHost != null) {
                System.out.println("Handling session with " + strHost + ":"
                        + iPort);
            } else {
                System.out.println("Handling session with " + sock.toString());
            }
        }
        
        try {
            carConfigProtocol();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public int readCommand() throws IOException {
        String mess;
        try {
            mess = (String)getMessage();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }

        int command = Integer.parseInt(mess);
        return command;
    }
    
    // send a message to the client.
    protected void sendMessage(Object outputLine) throws IOException {
        if (outputLine == null) {
            return;
        }
        
        oos.writeObject(outputLine);
        //oos.flush();
    }
    
    // read a line from the inputline.
    // read a object from the stream.
    protected Object getMessage() throws IOException, ClassNotFoundException {
        //ois.skip(Long.MAX_VALUE);
        return ois.readObject();
    }
    
    protected void setCommand(int commandNumber) throws IOException {
        String command = Integer.toString(commandNumber);
        System.out.printf("Write command: %d\n", commandNumber);
        oos.writeObject(command);
        //oos.flush();
    }
    

    @Override
    public void closeSession() {
        try {
            //is = null;
            ois.close();
            oos.close();
            sock.close();
        } catch (IOException e) {
            if (DEBUG) {
                System.err.println("Error closeing socket to " + strHost);
            }
        }
    }
    
    public void run() {
        if (openConnection()) {
            handleSession();
            closeSession();
        }
    }
}
