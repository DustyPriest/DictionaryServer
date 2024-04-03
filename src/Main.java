import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    // Declare the port number
    private static int port;
    private static final String address = "localhost";
    private static final int NUM_THREADS = 8;

    public static void main(String[] args)
    {
        // USAGE: java Main <port> <dictionary.json>
        if (!checkArgs(args)) {
            System.out.println("Usage: java Main <port> <dictionary.json>");
            System.exit(0);
        }
        port = Integer.parseInt(args[0]);

        ServerGUI serverGUI = new ServerGUI(address, port);

        ServerDictionary dictionary = new ServerDictionary("dictionary.json", serverGUI);
        ThreadPool threadPool = new ThreadPool(NUM_THREADS);

        try(ServerSocket serverSocket = new ServerSocket(port))
        {
            serverGUI.updateLog("INFO: Server started on port "+port);

            // Wait for connections.
            while(true)
            {
                Socket clientSocket = serverSocket.accept();
                serverGUI.incrementClientCounter();
                serverGUI.updateLog("INFO: Client "+ serverGUI.getClientCount() +" applying for connection!");

                // add connection to task list
                threadPool.addTask(new ClientTask(clientSocket, dictionary, serverGUI));
                if (serverGUI.getClientCount() > NUM_THREADS) {
                    serverGUI.updateLog("WARN: All worker threads occupied, client in queue");
                }
            }

        }
        catch (IOException e) {
            serverGUI.updateLog("FATAL: Server connection error, shutting down...");
            e.printStackTrace();
            serverGUI.showErrorPopup("Server connection error, program will exit");
            System.exit(0);
        } catch (IllegalArgumentException e) {
            serverGUI.updateLog("FATAL: Invalid port number, shutting down...");
            e.printStackTrace();
            serverGUI.showErrorPopup("Invalid port number, program will exit");
            System.exit(0);
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


}
