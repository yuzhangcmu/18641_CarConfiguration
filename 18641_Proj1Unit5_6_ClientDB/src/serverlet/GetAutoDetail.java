package serverlet;

import adapter.BuildAuto;
import adapter.EditThread;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Automobile;
import model.OptionSet;

/**
 * Servlet implementation class GetAutoDetail
 */
@WebServlet("/GetAutoDetail")
public class GetAutoDetail extends ConnectToServer {
	private static final long serialVersionUID = 12L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAutoDetail() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        
        System.out.print("Enter servlet GetAutoDetail.\n");
        
        if (dclient == null || queue == null) {
            return;
        }
        
	    
        String autoName = request.getParameter("autoname");
        System.out.printf("The selected auto:%s\n", autoName);
        
        //ClientDefaultSocketClient dclient = gO.getDclient();
        
        dclient.getAuto(autoName);
        
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
            
            
            if (queue.size() != 0) {
                String rst = (String)queue.poll();
                if (rst == "ok") {
                    Automobile auto = (Automobile)queue.poll();
                    
                    // print the auto to the web. 
                    addAutoToWeb(auto, pw);
                    
                    //System.out.printf("html: %s", pw);
                    break;
                } else {
                    System.out.printf("Get error data: %s\n", rst);
                    break;
                }
            }
        }                        
	}
	
	private void addAutoToWeb(Automobile auto, PrintWriter pw){
	    if (auto == null) {
            System.out.printf("The auto is null.\n");
            return;
        }
        
        for(int i = 0; i < auto.getOptionSetNumber(); i++) {
            OptionSet opset = auto.getOptionSet(i);
            
            StringBuilder html = new StringBuilder();
          
            html.append("<div>");
            
            // add the table.
            html.append("<label for=\"" + opset.getName() + "\">" + opset.getName() +":</label>");
            
            html.append("<select name=");
            html.append("\"" + opset.getName() + "\"");
            html.append("id=\"" + opset.getName() + "\">");
            
            for (int j = 0; j < opset.getOptionNumber(); j++) {
                String optName = opset.getOptionWithIndex(j).getName();
                html.append("<option value=\""+optName+"\">"+optName+"</option>");
            }
            html.append("</select>");
            html.append("</div>");
            
            pw.println(html);
            
        }
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    if (dclient == null || queue == null) {
            return;
        }
	    
	    response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        
        pw.println("<head>");
        pw.println("<title>Car Configuration Proof</title>");
        pw.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"index.css\">");
        pw.println("</head>");
                
        EditThread eT = new BuildAuto();
        String autoName = (String)request.getParameter("autoModel");
        Automobile aT = eT.getAutomobileByName(autoName);
        if (aT == null) {
            return;
        }
        
        pw.println("<legend>Here is what you selected</legend>");

        pw.println("<table width=\"413\" height=\"251\" border=\"1\">");
        
        pw.printf("<tr>");
        pw.printf("<th width=\"178\" scope=\"row\">%s</th>", aT.getName());
        pw.printf("<td width=\"124\" align=\"center\">base price</td>");
        pw.printf("<td width=\"124\" align=\"center\">%f</td>", aT.getBasePrice());
        pw.println("</tr>");
        
        int optNo = aT.getOptionSetNumber();
        
        for(int i = 0; i< optNo; i++) {
            pw.printf("<tr>");
            String optName = aT.getOptionSet(i).getName();
            pw.printf("<th scope=\"row\">%s</th>", optName);
            
            String choice = (String)request.getParameter(optName);
            pw.printf("<td align=\"center\">%s</td>", choice);
            
            //System.out.printf("choice: %s\n", choice);
            
            // set the choice.
            aT.setOptionChoice(optName, choice);
            
            //total += price;
            pw.printf("<td>%f</td>", aT.getOptionChoicePrice(optName));
            pw.println("</tr>");
        }
        
        pw.printf("<tr>");
        pw.printf("<th width=\"178\" scope=\"row\">Total Cost</th>");
        pw.printf("<td>&nbsp;</td>");
        pw.printf("<td width=\"124\" align=\"center\">%f</td>", aT.getTotalPrice());
        pw.println("</tr>");
        
        pw.println("</table>");
	}

}
