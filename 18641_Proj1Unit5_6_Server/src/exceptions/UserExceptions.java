package exceptions;

import java.util.*;

public class UserExceptions extends Exception {
    /*
     * 0: file name error.
     */
    private int errorNumber;
    private String errorMessage;

    private static final long serialVersionUID = -4944252649710757361L;

    // throws a user exception, and get the errorNumber.
    public UserExceptions(int errorNo, String errorMess) {
        super(errorMess);
        this.errorNumber = errorNo;
        this.errorMessage = errorMess;
    }

    // fix problem by inputting from the console
    public String fixProblemReadFromConsole()
    {
        if (errorNumber == 0) {
            // input the new file name from the console
            String filename;
            
            Scanner sc = new Scanner(System.in);
            System.out.println("Please input the right file name:");
            filename = sc.nextLine();
            System.out.println("Got here --> fixProblemReadFromConsole");
            //sc.close();
            return filename;
        } else {
            return null;
        }
    }
    
    public int getErrorNumber(){
        return errorNumber;
    }

    public void printmyproblem() {
   		System.out.printf("UserExceptions [errorno=%d, errormsg=%s]\n", errorNumber, errorMessage); 
    }
}
