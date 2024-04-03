

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
                            System.out.println("Client: " + clientMsg.getStatus());
                            msgOut.writeObject(handleMessage(clientMsg));
                            msgOut.flush();
                        } catch (ClassNotFoundException e) {
                            System.out.println("bad data from client");
                            // TODO: fix stream when broken
                        }
                }
            } catch (SocketException e) {
                // TODO: add to server log
                System.out.println("Client disconnected");
            }
            Main.decrementClientCounter();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Action client requests
    private NetworkMessage handleMessage(NetworkMessage msg) {
        Status status = msg.getStatus();
        return switch (status) {
            case TASK_QUERY -> dictionary.searchWord(msg.getData()[0]);
            case TASK_ADD -> dictionary.addWord(msg.getData()[0], msg.getData()[1]);
            case TASK_REMOVE -> dictionary.removeWord(msg.getData()[0]);
            case TASK_UPDATE -> dictionary.updateWord(msg.getData()[0], msg.getData()[1]);
            default -> new NetworkMessage(Status.FAILURE_INVALID_INPUT);
        };
    }
}
