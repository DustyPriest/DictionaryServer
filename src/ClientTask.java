

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientTask implements Runnable {

    private Socket socket;
    private ServerDictionary dictionary;

    public ClientTask(Socket socket, ServerDictionary dictionary) {
        this.socket = socket;
        this.dictionary = dictionary;
    }
    @Override
    public void run() {
        try {

            // Input and Output streams; base input stream used to check for available bytes
            InputStream in = socket.getInputStream();
            ObjectInputStream msgIn = new ObjectInputStream(in);
            ObjectOutputStream msgOut = new ObjectOutputStream(socket.getOutputStream());

            try {
                // TODO: send msg before timeout
                socket.setSoTimeout(10000);

                Message clientMsg = null;

                while (true) {

                    if (in.available() > 0) {
                        try {
                            clientMsg = (Message) msgIn.readObject();
                            System.out.println("Client: " + clientMsg.getStatus());
                            msgOut.writeObject(handleMessage(clientMsg));
                            msgOut.flush();
                        } catch (ClassNotFoundException e) {
                            System.out.println("bad data from client");
                            // TODO: fix stream when broken
                        }
                    }

                }
            } catch (SocketException e) {
                System.out.println("Client disconnected");
            }
            Main.decrementClientCounter();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Message handleMessage(Message msg) {
        Status status = msg.getStatus();
        switch(status) {
            case TASK_QUERY:
                return new Message(Status.SUCCESS_WORD_FOUND, "word");
            case TASK_ADD:
                return new Message(Status.SUCCESS_WORD_ADDED);
            case TASK_REMOVE:
                return new Message(Status.SUCCESS_WORD_REMOVED);
            case TASK_UPDATE:
                return new Message(Status.SUCCESS_WORD_UPDATED);
            default:
                return new Message(Status.FAILURE_INVALID_INPUT);
        }
    }
}
