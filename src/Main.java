
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
    private static int clientCounter = 0;
    private static final int THREAD_COUNT = 8;

    public static void main(String[] args)
    {

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


//    }

}
