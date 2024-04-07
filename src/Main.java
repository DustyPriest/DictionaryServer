// Main
// Entry point for dictionary server
// Author: Hugo Akindele-Obe

import java.io.FileNotFoundException;
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
        // USAGE: java -jar DictionaryServer.jar <port> <dictionary.json>
        if (!parseArgs(args)) {
            System.out.println("Usage: java -jar DictionaryServer.jar <port> <dictionary.json>");
            System.exit(0);
        }

        ServerGUI serverGUI = new ServerGUI(address, port);

        // load dictionary file to memory
        ServerDictionary dictionary;
        try {
            dictionary = ServerDictionary.setInstance(args[1], serverGUI);
        } catch (IllegalArgumentException | FileNotFoundException e) {
            serverGUI.updateLog("FATAL: Invalid dictionary file, shutting down...");
            e.printStackTrace();
            serverGUI.showErrorPopup("Invalid dictionary file, program will exit");
            System.exit(0);
            return;
        }

        // create worker threads; allocated per client connection
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

    // checks cmd line arguments match the requirements & assigns the port
    private static boolean parseArgs(String[] args) {
        if (args.length != 2) {
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
