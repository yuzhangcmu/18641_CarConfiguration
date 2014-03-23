package scale;

import model.Automobile;
import adapter.EditThread;

public class EditOptions extends Thread {
    String id;
    private int optionId;
    
    private EditThread editThread;

    public EditOptions(String id, EditThread editThread, int optionId) {
        /* set the thread ID. */
        super(id);

        this.id = id;
        this.editThread = editThread;
        
        this.optionId = optionId;

        /* set the autoMobile class */
        //this.autoMobile = autoMobile;
    }
    
    /*
     * get the option choice.
     * */
    private String threadGetModuleOptionChoice(String moduleName, String optionSetName) {
        if (editThread == null){
            return null;
        }
        
        Automobile operateAuto = editThread.getAutomobileByName(moduleName);
        if (operateAuto == null){
            return null;
        }
        
        return operateAuto.getOptionChoice(optionSetName);
    }

    /*
     * set the option choice.
     * */
    private void threadSetModuleOptionChoice(String moduleName, String optionSetName,
            String optionName) {
        if (editThread == null){
            return;
        }
        
        Automobile operateAuto = editThread.getAutomobileByName(moduleName);
        if (operateAuto != null){
            operateAuto.setOptionChoice(optionSetName,
                    optionName);
        }
    }
    
    private void runT1() {
        if (editThread != null){
            System.out.print("Thread T1 began to run.\n");
            System.out
                    .print("Try to select Color Option to be 'Fort Knox Gold Clearcoat Metallic'\n");
            
            threadSetModuleOptionChoice("Focus Wagon ZTW",
                    "Color", "Fort Knox Gold Clearcoat Metallic");
        }
    }

    private void runT2() {
        if (editThread != null){
            System.out.print("Thread T2 began to run.\n");
            
            long t0 = System.currentTimeMillis();
            System.out.printf("System time: %d\n", t0);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out
                    .print("T2 sleep 1s over, try to access \"getOptionChoice\".\n");
            
            

            System.out.print("Still waiting......\n");

            System.out.printf("T2, get operation choice:%s\n",
                    threadGetModuleOptionChoice("Focus Wagon ZTW", "Color"));
            
            long t1 = System.currentTimeMillis();
            System.out.printf("System time: %d\n", t1);
            System.out.printf("Time passed: %ds\n", (t1 - t0)/1000);
        }
    }
    
    private void runT3() {
        if (editThread != null){
            System.out.print("Thread T3 began to run.\n");
            System.out
            .print("Try to select [Color] Option to be 'Fort Knox Gold Clearcoat Metallic'\n");

            threadSetModuleOptionChoice("Focus Wagon ZTW",
                    "Color", "Fort Knox Gold Clearcoat Metallic");

            System.out.printf("T3, get operation choice:%s\n",
                    threadGetModuleOptionChoice("Focus Wagon ZTW", "Color"));
        }
    }
    
    private void runT4() {
        if (editThread != null){
            System.out.print("Thread T4 began to run.\n");
            
            System.out
            .print("Try to select [Color] Option to be 'CD Silver Clearcoat Metallic'\n");
            
            threadSetModuleOptionChoice("Focus Wagon ZTW",
                    "Color", "CD Silver Clearcoat Metallic");

            System.out.printf("T4, get operation choice:%s\n",
                    threadGetModuleOptionChoice("Focus Wagon ZTW", "Color"));
        }
    }
    

    public void run() {
        if (optionId == 1) {
            runT1();

        } else if (optionId == 2) {
            runT2();
        } else if (optionId == 3) {
            runT3();
            
        } else if (optionId == 4) {
            runT4();
        }
    }
}
