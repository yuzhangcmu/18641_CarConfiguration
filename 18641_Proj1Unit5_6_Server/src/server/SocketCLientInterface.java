package server;

import java.io.IOException;

public interface SocketCLientInterface {
    boolean openConnection();
    void handleSession();
    void closeSession();
}
