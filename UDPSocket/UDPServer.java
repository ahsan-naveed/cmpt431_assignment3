import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;

/**
 * A UDP server that runs on port 5000. When a client sends a packet, it
 * responds with the current date and time and then stops responding.
 */
class UDPServer {
    public static void main(String args[]) throws Exception {
        try {
            DatagramSocket serverSocket = new DatagramSocket(5000);

            System.out.println("Server is listening on port " + 5000 + "...");
            System.out.println("Waiting for client request...");

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                serverSocket.receive(receivePacket);

                // gets client ip address and port number
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                // prints client information
                System.out.println("Request from: " + IPAddress + ":" + port);

                // prints server response
                Date date = new Date();
                System.out.println("Responded with: " + date.getTime() + " ms");
                sendData = Long.toString(date.getTime()).getBytes();

                // sends server current dateTime
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                // breaks the loop once rersponse is sent successfully
                break;
            }

            // close the connection
            System.out.println("Connection closed...");
            serverSocket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
}