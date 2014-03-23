package serverlet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import client.ClientDefaultSocketClient;
import client.SocketClientConstants;

/**
 * Servlet implementation class ConnectToServer
 */
@WebServlet("/ConnectToServer")
public class ConnectToServer extends HttpServlet 
implements SocketClientConstants{
	private static final long serialVersionUID = 1L;
	
	protected static ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(100);
    protected static ClientDefaultSocketClient dclient = openSocketofCLient(queue);
    
    public static ClientDefaultSocketClient openSocketofCLient(ArrayBlockingQueue<Object> queue) {
        String strLocalHost = "";
        try {
            strLocalHost = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.err.println("Unable to find local host");
            
        }
        
        ClientDefaultSocketClient dclient = new ClientDefaultSocketClient(strLocalHost, iDAYTIME_PORT,
                queue, 1);
        
        dclient.start();
        
        //WAITING_WEB_INPUT
        System.out.printf("Wait for the client to start!\n");
        
        /*
        while(dclient.getSocketState() == -1 ) {
            //System.out.printf("wait for the result!\n");
        }*/
        
        while(true) {
            if (!queue.isEmpty()) {
                String mess = (String)queue.poll();               
                if (mess.equals("ok")) {
                    break;
                }
            }
        }
        
        System.out.printf("The client is ready!\n");
            
        return dclient;
    }
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConnectToServer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
