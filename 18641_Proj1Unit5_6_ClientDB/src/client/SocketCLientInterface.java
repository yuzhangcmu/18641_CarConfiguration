package client;

public interface SocketCLientInterface {
    boolean openConnection();
    void handleSession();
    void closeSession();
}
