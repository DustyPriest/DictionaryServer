

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientTask implements Runnable {

    private final Socket socket;
    private final ServerDictionary dictionary;
    private final SimpleLogger log;

    public ClientTask(Socket socket, ServerDictionary dictionary, SimpleLogger log) {
        this.socket = socket;
        this.dictionary = dictionary;
        this.log = log;
    }
    @Override
    public void run() {
        try {

            // Get input/output streams for reading/writing data to/from the socket
            ObjectOutputStream msgOut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream msgIn = new ObjectInputStream(socket.getInputStream());

            try {
                NetworkMessage clientMsg;

                while (true) {
                        try {
                            clientMsg = (NetworkMessage) msgIn.readObject();
                            msgOut.writeObject(handleMessage(clientMsg));
                            msgOut.flush();
                        } catch (ClassNotFoundException e) {
                            // stream damaged from bad data, disconnect client
                            log.updateLog("ERROR: Bad data from client, dropping connection");
                            break;
                        }
                }
            } catch (SocketException e) {
                log.updateLog("INFO: Client disconnected");
            }
        } catch (EOFException e) {
            log.updateLog("INFO: Client cancelled connection");
        } catch (IOException e) {
            log.updateLog("ERROR: Client " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                log.decrementClientCounter();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Action client requests
    private NetworkMessage handleMessage(NetworkMessage msg) {
        Status status = msg.getStatus();
        switch (status) {
            case TASK_QUERY:
                log.updateLog("INFO: Client request - query word '" + msg.getData()[0].toUpperCase() + "'");
                return dictionary.searchWord(msg.getData()[0]);
            case TASK_ADD:
                log.updateLog("INFO: Client request - add word '" + msg.getData()[0].toUpperCase() + "'");
                return dictionary.addWord(msg.getData()[0], msg.getData()[1]);
            case TASK_REMOVE:
                log.updateLog("INFO: Client request - remove word '" + msg.getData()[0].toUpperCase() + "'");
                return dictionary.removeWord(msg.getData()[0]);
            case TASK_UPDATE:
                log.updateLog("INFO: Client request - update word '" + msg.getData()[0].toUpperCase() + "'");
                return dictionary.updateWord(msg.getData()[0], msg.getData()[1]);
            default:
                log.updateLog("WARN: Invalid client request");
                return new NetworkMessage(Status.FAILURE_INVALID_INPUT);
        }
    }
}
