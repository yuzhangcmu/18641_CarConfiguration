package serverlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.concurrent.ArrayBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import client.ClientDefaultSocketClient;
import client.SocketCLientInterface;
import client.SocketClientConstants;

/**
 * Servlet implementation class Option
 */
@WebServlet("/Option")
public class GetOption extends ConnectToServer{
	private static final long serialVersionUID = 1L;
		
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOption() {        
        super();
        //this.dclient = null;
    }
    
    public ClientDefaultSocketClient getDclient() {
        return dclient;
    }
    
    public ArrayBlockingQueue<Object> getQueue() {
        return queue;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        
        if (dclient == null || queue == null) {
            return;
        }
        
        dclient.requestAvaibleAuto();
        
        System.out.printf("Send request to the server to get the avaible automobiles!\n");
        
        // setup a timeout.
        int t = 0;

        while(true) {
            try {
                Thread.sleep(100);
                t++;
                if (t == 20) {
                    // This is 2s.
                    System.out.printf("Time out, just exit the servlet.");
                    break;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
            if (!queue.isEmpty()) {
                String rst = (String)queue.poll();
                if (rst.equals("ok")) {
                    String autoTmp = (String)queue.poll();
                    //pw.println(auto);
                    
                    // it is only [], no automobile.
                    if (autoTmp.length() <= 2) {
                        return;
                    }
                    
                    // cut the [ and ]
                    String auto = autoTmp.substring(1, autoTmp.length()-1);
                    
                    String[] parts = auto.split(", ");
                    
                    StringBuilder html = new StringBuilder();
                    
                    for(int i = 0; i < parts.length; i++) {
                        html.append("<option value=\""+parts[i]+"\">"+parts[i]+"</option>");
                    }
                    
                    pw.println(html);
                    
                    break;
                } else {
                    System.out.printf("Get data from client fail: %s!\n", rst);
                    break;
                }
            }
        }
        
        System.out.printf("autoModel: %s\n", (String)request.getParameter("autoModel"));

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
