/**
 * CMPT 431 Distributed Systems - Assignment 03
 * 
 * GROUP: 
 * Ahsan Naveed (anaveed)
 * Amandeep Sindhar (asindhar)
 */

// a java program for a client
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A command line UDP client. It sends a packet to a server , then gets the date
 * from the server and sets client system date to the server date on success,
 * otherwise it crashes and dumps the exception trace.
 */
class UDPClient {
    public static void main(String args[]) throws Exception {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("127.0.0.1");

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            // prepares time request for server
            String request = "time";
            sendData = request.getBytes();

            // timer starts before sending request
            long startTimer = new Date().getTime();

            // sends time request to the server
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);
            clientSocket.send(sendPacket);

            // gets response from the server
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            // timer ends after getting the response
            long endTimer = new Date().getTime();

            // computes RTT in ms
            long rtt = endTimer - startTimer;

            // computes client's time i.e. tc = ts + dsc
            String serverTime = new String(receivePacket.getData()).trim();
            Long clientTime = Long.parseLong(serverTime) + (rtt / 2);
            System.out.println("Client time without rtt: " + serverTime + " ms");
            System.out.println("rtt: " + rtt + " ms");
            System.out.println("rtt/2: " + rtt / 2 + " ms");
            System.out.println("Client time with rtt: " + clientTime + " ms\n");

            // format time to month:day:hour:min:year.sec
            DateFormat timeFormat = new SimpleDateFormat("MMddHHmmyy.ss");
            String formatedTime = timeFormat.format(new Date(clientTime));

            System.out.println("Server time: " + new Date(Long.parseLong(serverTime)));

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

            // close the connection
            clientSocket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
}