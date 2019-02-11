
// a java program for a client
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * A command line TCP client. It connects to a server , then gets the date from
 * the server and sets client system date to the server date on success,
 * otherwise it crashes and dumps the exception trace.
 */
public class TCPClient {
    // initialize socket and io streams
    private Socket socket = null;
    private BufferedReader input = null;

    // Note: Port number can be from 0 to 65535
    // constructor to put ip address and port
    public TCPClient(String ipAddress, int port) {
        // establish a connection
        try {
            // timer starts before sending request
            long startTimer = new Date().getTime();

            socket = new Socket(ipAddress, port);
            System.out.println("Connection successful...\n");

            // gets time from the Server
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String response = input.readLine();
            System.out.println("Server time upon request: " + response + " ms");

            // timer ends after getting the response
            long endTimer = new Date().getTime();

            // computes RTT in ms
            long rtt = endTimer - startTimer;

            // computes client's time i.e. tc = ts + dsc
            long clientTime = Long.parseLong(response) + (rtt / 2);
            System.out.println("Client time without rtt: " + response + " ms");
            System.out.println("rtt: " + rtt + " ms");
            System.out.println("rtt/2: " + rtt / 2 + " ms");
            System.out.println("Client time with rtt: " + clientTime + " ms\n");

            // formats time to month:day:hour:min:year.sec
            DateFormat timeFormat = new SimpleDateFormat("MMddHHmmyy.ss");
            String formatedTime = timeFormat.format(new Date(clientTime));

            System.out.println("Server time: " + new Date(Long.parseLong(response)));

            // sets client time

            // creats list of commands
            List<String> commands = new ArrayList<String>();
            commands.add("sudo");
            commands.add("date");
            commands.add("" + formatedTime);

            // creats the process
            ProcessBuilder pb = new ProcessBuilder(commands);

            // starts the process
            Process process = pb.start();

            // reads the ouput from process input stream
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println("Client time: " + s);
            }
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

        // close the connection
        try {
            input.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        TCPClient tcpClient = new TCPClient("127.0.0.1", 5000);
    }
}