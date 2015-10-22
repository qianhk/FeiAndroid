import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 15-10-22
 */
public class TestNtp {
    public static void main(String[] args){
        int retry = 2;
        int port = 123;
        int timeout = 3000;

        // get the address and NTP address request
        //
        InetAddress ipv4Addr = null;
        try {
            ipv4Addr = InetAddress.getByName("0.asia.pool.ntp.org");//更多NTP时间服务器参考附注
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        int serviceStatus = -1;
        DatagramSocket socket = null;
        long responseTime = -1;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(timeout); // will force the
            // InterruptedIOException

            for (int attempts = 0; attempts <= retry && serviceStatus != 1; attempts++) {
                try {
                    // Send NTP request
                    //
                    byte[] data = new NtpMessage().toByteArray();
                    DatagramPacket outgoing = new DatagramPacket(data, data.length, ipv4Addr, port);
                    long sentTime = System.currentTimeMillis();
                    socket.send(outgoing);

                    // Get NTP Response
                    //
                    // byte[] buffer = new byte[512];
                    DatagramPacket incoming = new DatagramPacket(data, data.length);
                    socket.receive(incoming);
                    responseTime = System.currentTimeMillis() - sentTime;
                    double destinationTimestamp = (System.currentTimeMillis() / 1000.0) + 2208988800.0;
                    //这里要加2208988800，是因为获得到的时间是格林尼治时间，所以要变成东八区的时间，否则会与与北京时间有8小时的时差

                    // Validate NTP Response
                    // IOException thrown if packet does not decode as expected.
                    NtpMessage msg = new NtpMessage(incoming.getData());
                    double localClockOffset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - destinationTimestamp)) / 2;

                    System.out.println("poll: valid NTP request received the local clock offset is " + localClockOffset + ", responseTime= " + responseTime + "ms");
                    System.out.println("poll: NTP message : " + msg.toString());
                    serviceStatus = 1;
                } catch (InterruptedIOException ex) {
                    // Ignore, no response received.
                }
            }
        } catch (NoRouteToHostException e) {
            System.out.println("No route to host exception for address: " + ipv4Addr);
        } catch (ConnectException e) {
            // Connection refused. Continue to retry.
            e.fillInStackTrace();
            System.out.println("Connection exception for address: " + ipv4Addr);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.println("IOException while polling address: " + ipv4Addr);
        } finally {
            if (socket != null)
                socket.close();
        }

        // Store response time if available
        //
        if (serviceStatus == 1) {
            System.out.println("responsetime=="+responseTime);
        }


    }
}
