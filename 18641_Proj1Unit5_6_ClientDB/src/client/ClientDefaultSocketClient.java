package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Scanner;




import java.util.concurrent.ArrayBlockingQueue;

import adapter.BuildAuto;
import adapter.EditThread;
import model.Automobile;
//import server.BuildCarModelOptions;
import util.FileIO;
import exceptions.UserExceptions;

public class ClientDefaultSocketClient extends Thread 
implements SocketCLientInterface, SocketClientConstants{
    //private BufferedReader reader;
    //private BufferedWriter writer;
    private Socket sock;
    private String strHost;
    private int iPort;
    
    // output the object.
    private ObjectOutputStream oos;
    
    // the input object stream.
    private ObjectInputStream ois;
            
    // wait for the user to choose 'a' or 'b'
    private static final int WAITING_USER_SEL_MENU = 0;
    
    // wait for the server to response OK for the command of send
    // properties file to the server.
    private static final int WAITING_SRV_RESPONCE_OK = 1;
    
    // wait for the server to return a list of all the automobiles.
    private static final int WAITING_SRV_RETURN_LIST = 2;
    
    // wait for the user to return option set.
    private static final int WAITING_SRV_RETURN_AUTO = 3;
    
    // wait for the user to return option set.
    private static final int WAITING_USR_OPSET = 4;
    
    // wait for the user to return option set.
    private static final int WAITING_USR_OPTION = 5;
    
    // wait for the user to return option set.
    private static final int STOP = 6;
    
    private static final int WAITING_WEB_ALL_AUTOS = 7;
    
    //private int state = WAITING_USER_SEL_MENU;
    private int state = -1;
    
    //private CarModelOptionIO carIO;
    
    private ArrayBlockingQueue<Object> queue;
    
    // from web or console
    private int inputType;
        
    public ClientDefaultSocketClient(String strHost, int iPort) {
        this(strHost, iPort, null, 0);
    }
    
    public int getSocketState() {
        return state;
    }    
    
    // use a queue to communicate with servlet.
    public ClientDefaultSocketClient(String strHost, int iPort, ArrayBlockingQueue<Object> queue,
            int inputType) {
        setPort(iPort);
        setHost(strHost);
        //carIO = new CarModelOptionIO
        
        this.queue = queue;
        this.inputType = inputType;
    }
    
    //public void 
    
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
        
        try {
            OutputStream os = sock.getOutputStream();
            oos = new ObjectOutputStream(os);
            oos.flush();
            
            InputStream is = sock.getInputStream();
            
            ois = new ObjectInputStream(is);
            
            //carIO = new CarModelOptionIO(sock, oos, ois);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return true;
    }
    
    private void carConfigProtol(Scanner sc) throws IOException {
        if (sc == null /*|| carIO == null*/) {
            return;
        }
        
        while(true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (state == WAITING_USER_SEL_MENU) {
                if ( inputType == 1) {
                    //don't receive the input from console.
                    continue;
                }
                getUserChoice(sc);
                
            } else if (state == WAITING_SRV_RESPONCE_OK) {
                int bCommand = readCommand();
                if (DEBUG) {
                    System.out.printf("Receive command: %d\n", bCommand);
                }
                
                // the client send a serilized config file to the server.
                if (bCommand == COMMAND_SRV_CREATE_AUTO_SUSS){
                    System.out
                    .println("Receive server response. "
                            + "Server create the Car Model Object successfully!");
                    state = WAITING_USER_SEL_MENU;
                }
                
            } else if (state == WAITING_SRV_RETURN_LIST) {
                //int bCommand = readCommand();
                int bCommand = COMMAND_GET_AVAILABLE_MODELS;
                if (DEBUG) {
                    System.out.printf("Receive command: %d\n", bCommand);
                }
                
                // get the reply of the server that tell the client the available 
                // automobiles.
                if (bCommand == COMMAND_GET_AVAILABLE_MODELS){
                    System.out
                    .println("Receive server response "
                            + ", all the avaliable Automobiles are listed:");
                    String rst;
                    
                    try {
                        rst = (String) getMessage();
                        System.out.println(rst);
                        
                        if (queue != null) {
                            queue.offer("ok");
                            
                            boolean succ = queue.offer(rst);
                            if (succ == true) {
                                System.out.printf("put rst into queue success: %s\n", queue.peek());
                            } else {
                                System.out.printf("put rst into queue fail: %s\n", rst);
                            }
                            // can exit the socket now.
                            state = STOP;
                            continue;
                        }
                        
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        state = WAITING_USER_SEL_MENU;
                        continue;
                    }
                    
                    System.out
                    .println("Please select a Automobile name:");
                    
                    // send message to the server to get the return object.
                    String select = sc.nextLine();
                    getAuto(select);
                    
                }
            } else if (state == WAITING_SRV_RETURN_AUTO) {
                //int bCommand = readCommand();
                int bCommand = COMMAND_SERVER_SEND_AUTO;
                if (DEBUG) {
                    System.out.printf("Receive command: %d\n", bCommand);
                }
                
                // get the reply of the server that tell the client the available 
                // automobiles.
                if (bCommand != COMMAND_SERVER_SEND_AUTO){
                    System.out.print("Command is not I want, continue.");
                    setCommand(COMMAND_STOP);
                    
                    boolean succ = queue.offer("fail!");
                    if (succ == true) {
                        System.out.printf("put fail into queue success: %s\n", queue.peek());
                    } else {
                        System.out.printf("put fail into queue fail: \n");
                    }
                    
                    return;
                    //continue;
                }
                
                // readout the automobile
                Automobile auto;
                try {
                    auto = (Automobile)getMessage();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
                
                // add the automobile into the client.
                EditThread eT = new BuildAuto();
                LinkedHashMap<String, Automobile> mH = eT.getAutoHashMap();
                mH.put(auto.getName(), auto);
                
                                
                auto.print();
                
                // when get message from the web, just stop the socket and finish.
                if (queue != null) {
                    queue.offer("ok");
                    
                    boolean succ = queue.offer((Object)auto);
                    if (succ == true) {
                        System.out.printf("put auto into queue success: %s\n", queue.peek());
                    } else {
                        System.out.printf("put auto into queue fail: %s\n", auto);
                    }
                    
                    // can exit the socket now.
                    System.out.printf("Write automobile to the queue: %s.\n", auto.getName());
                    state = STOP;
                    continue;
                } else {
                    System.out.print("The queue is null!\n");
                }
                
                
                String optSet, option;
                
                do {
                    System.out
                    .println("Please select optionSet name you want to "
                            + "config.(type \"exit\" to exit.)");
                    
                    optSet = sc.nextLine();
                    if (optSet.equals("exit")) {
                        break;
                    }
                    
                    System.out
                    .println("Please select option you want."
                            + "(type \"exit\" to exit.)");
                    
                    option = sc.nextLine();
                    if (optSet.equals("exit")) {
                        break;
                    }
                    
                    auto.setOptionChoice(optSet, option);
                }while (true);
                
                auto.printChoice();
                state = STOP;

            } else if (state == WAITING_WEB_ALL_AUTOS) {
                // wait server to replay all the avaible autos.
                //int bCommand = readCommand();
                int bCommand = COMMAND_GET_ALL_MODELS;
                if (DEBUG) {
                    System.out.printf("Receive command: %d\n", bCommand);
                }
                
                // get the reply of the server that tell the client the available 
                // automobiles.
                if (bCommand != COMMAND_GET_ALL_MODELS){
                    System.out.print("Command is not I want, continue.");
                    setCommand(COMMAND_STOP);
                    return;
                    //continue;
                }
                
                LinkedHashMap<String, Automobile> mHServer = null;
                
                // readout the automobile
                try {
                    LinkedHashMap<String, Automobile> message = 
                            (LinkedHashMap<String, Automobile>)getMessage();
                    mHServer = message;
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
                
                // add the automobile into the client.
                EditThread eT = new BuildAuto();
                LinkedHashMap<String, Automobile> mH = eT.getAutoHashMap();
                mH.putAll(mHServer);
                
                System.out.println("Print the new LHM in client:");
                System.out.println(mH.toString());
                
                // when get message from the web, just stop the socket and finish.
                if (queue != null) {
                    // create the list and send back to the client.
                    String str;
                    str = eT.getAvailableAutos();
                    if (str == null) {
                        System.out.printf("Get avaiable autos fail\n");
                        break;
                    }
                    
                    queue.offer("ok");
                    
                    //queue.offer((Object)auto);
                    boolean succ = queue.offer((Object)str);
                    if (succ == true) {
                        System.out.printf("put autos into queue success: %s\n", queue.peek());
                    } else {
                        System.out.printf("put autos into queue fail: %s\n", str);
                    }
                    
                    // can exit the socket now.
                    System.out.printf("Write all the autos the queue: %s.\n", str);
                    state = STOP;
                    continue;
                } else {
                    System.out.print("The queue is null!\n");
                }
                
            } else if (state == WAITING_USR_OPTION) {
                
            } else if (state == STOP) {
                // stop the application
                // stop the socket.
                if (inputType == 1) {
                    continue;
                    // wait for the web to get the data.
                    /*
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }*/
                }
                setCommand(COMMAND_STOP);
                return;
            }
        }
    }

    @Override
    public void handleSession() {
        if (DEBUG) {
            System.out.println("Handling session with " + strHost + ":" + iPort);
        }
        
        Scanner sc = new Scanner(System.in);
        //CarModelOptionIO carIO = null;
                        
        try {
            carConfigProtol(sc);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }                
    }
   
    // upload the properties file to the server.
    private boolean upLoadPropertiesFile(Scanner sc) throws IOException {
        System.out
        .println("Please input the file path and name(eg. Prius.properties):");

        //  get the input textfile.
        String fileName = sc.nextLine();
        
        if (fileName == null) {
            return false;
        }
        
        Properties props = new Properties();
        FileInputStream in = null;
        
        try {
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        
        props.load(in);
        
        // serialize the pros to a file.
        //CarModelOptionIO carIO = new CarModelOptionIO(sock,oos,ois);
        setCommand(COMMAND_SEND_FILE);
        sendMessage(props);
        
        return true;
    }
    
    private String getUserChoice(Scanner sc) throws IOException {
        String choice;
        /*
        if (carIO == null) {
            return null;
        }*/
        
        System.out
                .println("Please input 'a' or 'b' to tell me what do you want to do:");
        System.out.println("a: Upload a text file or Properties file.");
        System.out.println("b: Configure a car.");
        //System.out.println("exit: exit the application.");
        choice = sc.nextLine();
        if (choice.equals("a")) {

            // upload a properties file to the server.
            if (upLoadPropertiesFile(sc) == true) {
                state = WAITING_SRV_RESPONCE_OK;
            }
        } else if (choice.equals("b")) {
            requestAvaibleAuto();
        } else if (choice.equals("exit")) {
            state = STOP;
            
            
        } else {
            System.out.println("Input error! Please input again.");
            state = WAITING_USER_SEL_MENU;
        }
        
        return choice;
    }
    
    public void requestAllTheAutos() throws IOException {
        setCommand(COMMAND_GET_ALL_MODELS);
        System.out
                .println("Please wait for a while to get the response of the server. Get all autos.");
        
        // set to the state to wait for all the autos.
        state = WAITING_WEB_ALL_AUTOS;  
    }
    
    public void requestAvaibleAuto() throws IOException {
        // send a command to the server to tell server to return automobile list.
        setCommand(COMMAND_GET_AVAILABLE_MODELS);
        System.out
                .println("Please wait for a while to get the response of the server.");
        
        state = WAITING_SRV_RETURN_LIST;        
    }
    
    public void getAuto(String autoName) throws IOException {
        // send a command to the server to tell server to return automobile list.
        setCommand(COMMAND_SELECT_MODEL);
        sendMessage(autoName);
        
        System.out
        .println("Please wait for a while to get the response of the server.");
        state = WAITING_SRV_RETURN_AUTO;
    }
    
    public Automobile getAutoWeb(String autoName) throws IOException {
        EditThread eT = new BuildAuto();
        Automobile aT = eT.getAutomobileByName(autoName);
        
        if (aT == null) {
            System.out.printf("client don't have this mobile\n");
        }
        
        state = STOP;
        return aT;
    }
/*    
    public CarModelOptionIO(Socket sock, ObjectOutputStream oos, 
            ObjectInputStream ois) throws IOException{
        this.oos = oos;
        this.ois = ois;
    }
*/    
    // set the command to transfer to the server.
    public void setCommand(int commandNumber) throws IOException {
        String command = Integer.toString(commandNumber);
        oos.writeObject(command);
        oos.flush();
    }
    
    public int readCommand() throws IOException {
        String mess;
        try {
            mess = (String)getMessage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
        
        if (mess == null) {
            System.out.print("No command read out now, it is null!\n");
            return -1;
        }

        int command = Integer.parseInt(mess);
        return command;
    }
    
    // read a object from the stream.
    public Object getMessage() throws IOException, ClassNotFoundException {
        //ois.skip(Long.MAX_VALUE);
        
        System.out.println("Enter getMessge!");
        
        //ois.skip(Long.MAX_VALUE);
        Object rst = ois.readObject();
        
        System.out.printf("Read out object %s !\n",rst);
        
        if (rst == null) {
            System.out.print("No object read out, it is null!\n");
        }
        
        return rst;
    }
    
    // send a message to the client.
    protected void sendMessage(Object outputLine) throws IOException {
        if (outputLine == null) {
            System.out.printf("sendMessage: Invalid input: null.\n");
            return;
        }
        
        oos.writeObject(outputLine);
        oos.flush();
    }

    @Override
    public void closeSession() {
        try {
            oos.close();
            ois.close();
            sock.close();
        } catch (IOException e) {
            if (DEBUG) {
                System.err.println("Error closeing socket to " + strHost);
            }
        }
    }
    
    public void run() {
        if (openConnection()) {
            state = WAITING_USER_SEL_MENU;
            
            // tell the servlet it is ok!
            if (queue != null) {
                queue.offer("ok");
                System.out.println("client is ok to start.");
            }
            
            handleSession();
            closeSession();
        }
    }
    
    public static void main (String arg[]) {
        // debug main; does daytime on local host
        String strLocalHost = "";
        try {
            strLocalHost = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.err.println("Unable to find local host");
            
        }
        
        ClientDefaultSocketClient dclient = new ClientDefaultSocketClient(strLocalHost, iDAYTIME_PORT);
        dclient.start();
    }
    
}
