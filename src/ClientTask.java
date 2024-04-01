import java.io.*;
import java.net.Socket;

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

            //Get the input/output streams for reading/writing data from/to the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            ObjectInputStream msgIn = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream msgOut = new ObjectOutputStream(socket.getOutputStream());

            Message clientMsg = null;

            while (true) {

                try {
                    clientMsg = (Message) msgIn.readObject();
                } catch (ClassNotFoundException e) {
                    // TODO: note bad message and reply as such
                    e.printStackTrace();
                }

                if (clientMsg == null) {
                    System.out.println("bad msg");
                    continue;
                    // TODO: note bad message and reply as such
                }


                Message response = handleMessage(clientMsg);
                msgOut.writeObject(response);
                msgOut.flush();

            }


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
