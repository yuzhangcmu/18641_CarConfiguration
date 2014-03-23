package client;

public interface SocketClientConstants {
    int iECHO_PORT = 7;
    int iDAYTIME_PORT = 4444;
    int iSMTP_PORT = 25;
    boolean DEBUG = true;
    
    // define the command to communicate to server.
    int COMMAND_SEND_FILE = 0;    
    
    // successfully create the automobile 
    int COMMAND_SRV_CREATE_AUTO_SUSS = 1;
    
    // when client want to know all the available automible, both client and 
    // server will use this command.
    int COMMAND_GET_AVAILABLE_MODELS = 2;
        
    // the client select a model.
    int COMMAND_SELECT_MODEL = 3;
    
    // the server send back a automobile file
    int COMMAND_SERVER_SEND_AUTO = 4;
        
    // stop the socket.
    int COMMAND_STOP = 5;
    
    // the server send back a automobile file
    int COMMAND_SERVER_DONTHAVEAUTO = 6;
    
    // return all the automobiles.
    int COMMAND_GET_ALL_MODELS = 7;
}
