import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UDPServer {
    String host;
    int port;

    public UDPServer(String host, int port) {
        this.host = host;
        this.port = port;
        UDPserve(this.host,this.port);
    }

    private void UDPserve(String host, int port) {
        DatagramSocket udpSocket;

        try {
            udpSocket = new DatagramSocket(port, InetAddress.getByName(host));
            System.out.println("Server started");
            ByteBuffer buf = ByteBuffer.allocate(8);
            DatagramPacket request;
            DatagramPacket packet;
            InetAddress clientAddress;
            int clientPort;


            for (int i = 0; i < 100; i++) {
                request = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(request);
                clientAddress = request.getAddress();
                clientPort = request.getPort();
                packet = new DatagramPacket(buf.array(), buf.array().length, clientAddress, clientPort);

                XOR(buf);
                if (buf.getLong() != 1) {
                    System.out.println("Error");
                }
                udpSocket.send(packet);

                buf = ByteBuffer.allocate(32);
                request = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(request);
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, clientAddress, clientPort);
                udpSocket.send(packet);

                buf = ByteBuffer.allocate(512);
                request = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(request);
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, clientAddress, clientPort);
                udpSocket.send(packet);

                buf = ByteBuffer.allocate(1024);
                request = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(request);
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, clientAddress, clientPort);
                udpSocket.send(packet);
            }

            for (int j = 0; j < 8192; j++) {
                buf = ByteBuffer.allocate(128);
                request = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(request);
                XOR(buf);
                ByteBuffer response = ByteBuffer.allocate(8);
                XOR(response);
                packet = new DatagramPacket(buf.array(), buf.array().length, request.getAddress(), request.getPort());
                udpSocket.send(packet);
            }
            System.out.println("8192 128-byte packets received");

            for (int j = 0; j < 2048; j++) {
                buf = ByteBuffer.allocate(512);
                request = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(request);
                XOR(buf);
                ByteBuffer response = ByteBuffer.allocate(8);
                XOR(response);
                packet = new DatagramPacket(buf.array(), buf.array().length, request.getAddress(), request.getPort());
                udpSocket.send(packet);
            }
            System.out.println("2048 512-byte packets received");

            for (int j = 0; j < 1024; j++) {
                buf = ByteBuffer.allocate(1024);
                request = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(request);
                XOR(buf);
                ByteBuffer response = ByteBuffer.allocate(8);
                XOR(response);
                packet = new DatagramPacket(buf.array(), buf.array().length, request.getAddress(), request.getPort());
                udpSocket.send(packet);
            }
            System.out.println("1024 1024-byte packets received");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void XOR(ByteBuffer buf) {
        long key = 0x5555555555555555L;
        for (int i = 0; i < buf.array().length; i++) {
            buf.array()[i] = (byte) (buf.array()[i] ^ key);
        }
    }
}
