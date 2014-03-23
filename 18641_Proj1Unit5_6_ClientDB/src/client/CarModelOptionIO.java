package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CarModelOptionIO implements SocketClientConstants{
    // output the object.
    private ObjectOutputStream oos;
    
    // the input object stream.
    private ObjectInputStream ois;
    
    /*
    public CarModelOptionIO(Socket sock, ObjectOutputStream oos, 
            ObjectInputStream ois) throws IOException{
        this.oos = oos;
        this.ois = ois;
        //oos = new ObjectOutputStream(sock.getOutputStream());
        //oos.flush();
        
        //ois = new ObjectInputStream(sock.getInputStream());
    }
    
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
        
        System.out.printf("Read out object %s, !\n",rst);
        
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
    */
}
