
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;

public class Main {

    // Declare the port number
    private static int port = 3005;

    // Identifies the user number connected
    // TODO: implement client counter properly
    private static int clientCounter = 0;
    private static final int THREAD_COUNT = 8;

    public static void main(String[] args)
    {
        // USAGE: java Main <port> <dictionary.json>
        if (!checkArgs(args)) {
            System.out.println("Usage: java Main <port> <dictionary.json>");
            System.exit(0);
        }

        ServerDictionary dictionary = new ServerDictionary("dictionary.json");
        ThreadPool threadPool = new ThreadPool(THREAD_COUNT);

        try(ServerSocket serverSocket = new ServerSocket(port))
        {
            System.out.println("Waiting for client connection...");

            // Wait for connections.
            while(true)
            {
                Socket clientSocket = serverSocket.accept();
                clientCounter++;
                System.out.println("Client "+ clientCounter +": Applying for connection!");

                // add connection to task list
                threadPool.addTask(new ClientTask(clientSocket, dictionary));
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static boolean checkArgs(String[] args)
    {
        if (args.length != 2)
        {
            return false;
        }
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static void decrementClientCounter() {
        clientCounter--;
    }


}
