/**
 * CMPT 431 Distributed Systems - Assignment 03
 * 
 * GROUP: 
 * Ahsan Naveed (anaveed)
 * Amandeep Sindhar (asindhar)
 */

// a java program for a server
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Date;

/**
 * A TCP server that runs on port 5000. When a client connects, it sends the
 * client the current date and time, then closes the connection with that
 * client.
 */
public class TCPServer {
    // initialize sockets and input stream
    private Socket socket = null; // for communication with the client
    private ServerSocket server = null; // waits for the client requests (when a client makes a new Socket())
    private PrintWriter output = null;

    // constructor with the port
    public TCPServer(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println(String.format("Server is listening on %d", port));

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted...");

            // sends date to the client
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println(Long.toString(new Date().getTime()));

            System.out.println("Closing connection");

            // close connection
            socket.close();
            output.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        TCPServer tcpServer = new TCPServer(5000);
    }
}