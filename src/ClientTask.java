import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
            // input stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            // output stream
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            System.out.println("CLIENT: "+input.readUTF());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
