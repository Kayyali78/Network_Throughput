import java.net.*;
import java.nio.ByteBuffer;

public class UDPClient {
    String host;
    int port;

    public UDPClient(String host, int port){
        this.host = host;
        this.port = port;
        UDPclient(this.host,this.port);
    }

    private void UDPclient(String host, int port){
        DatagramSocket udpSocket;
        long pack1=0, pack2=0, pack3=0, pack4=0, pack5, pack6, pack7;

        try {
            udpSocket = new DatagramSocket(port);

            InetAddress address = InetAddress.getByName(host);
            ByteBuffer buf;
            DatagramPacket packet;
            DatagramPacket response;

            for (int i = 0; i < 100; i++) {

                //8 bytes of data, 8 bytes of ack
                buf = ByteBuffer.allocate(8);
                buf.putLong(1);
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                response = new DatagramPacket(buf.array(), buf.array().length);
                long start = System.nanoTime();
                udpSocket.send(packet);
                udpSocket.receive(response);
                long end = System.nanoTime();
                pack1 += end - start;

                //32 bytes of data, 32 bytes of ack
                buf = ByteBuffer.allocate(32);
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                response = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(response);
                end = System.nanoTime();
                pack2 += end - start;

                //512 bytes of data, 512 bytes of ack
                buf = ByteBuffer.allocate(512);
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                response = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(response);
                end = System.nanoTime();
                pack3 += end - start;

                //1024 bytes of data, 1024 bytes of ack
                buf = ByteBuffer.allocate(1024);
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                response = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(response);
                end = System.nanoTime();
                pack4 += end - start;
            }

            //8192 bytes of data, 8 bytes of ack
            buf = ByteBuffer.allocate(128);
            long start = System.nanoTime();
            for (int j = 0; j < 8192; j++) {
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                ByteBuffer reply = ByteBuffer.allocate(8);
                response = new DatagramPacket(reply.array(), reply.array().length);
                udpSocket.receive(response);
                XOR(reply);
            }
            long end = System.nanoTime();
            pack5 = end - start;

            //2048 bytes of data, 8 bytes of ack
            buf = ByteBuffer.allocate(512);
            for (int j = 0; j < 2048; j++) {
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                ByteBuffer reply = ByteBuffer.allocate(8);
                packet = new DatagramPacket(reply.array(), reply.array().length);
                udpSocket.receive(packet);
                XOR(reply);
            }
            end = System.nanoTime();
            pack6 = end - start;

            //512 bytes of data, 8 bytes of ack
            buf = ByteBuffer.allocate(1024);
            for (int j = 0; j < 1024; j++) {
                XOR(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                ByteBuffer reply = ByteBuffer.allocate(8);
                packet = new DatagramPacket(reply.array(), reply.array().length);
                udpSocket.receive(packet);
                XOR(reply);
            }
            end = System.nanoTime();
            pack7 = end - start;

            System.out.println("UDP Results:");
            System.out.println("Average 8 byte time: " + pack1 / 100 + " ns");
            System.out.println("Average 64 byte time: " + pack2 / 100 + " ns");
            System.out.println("Average 256 byte time: " + pack3 / 100 + " ns");
            System.out.println("Average 1024 byte time: " + pack4 / 100 + " ns");
            System.out.println("1024 byte throughput: " + 8/(pack5/1e+9) + " Mbps");
            System.out.println("512 byte throughput: " + 8/(pack6/1e+9) + " Mbps");
            System.out.println("256 byte throughput: " + 8/(pack7/1e+9) + " Mbps");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            private void XOR (ByteBuffer buf){
                long key = 0x5555555555555555L;
                for (int i = 0; i < buf.array().length; i++) {
                    buf.array()[i] = (byte) (buf.array()[i] ^ key);
                }
            }
}
