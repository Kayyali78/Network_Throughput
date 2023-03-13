import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Client {
    String host;
    int port;
    Socket echoSocket = null;
    DataOutputStream out = null;
    DataInputStream in = null;

    public Client(String host, int port){
        this.host = host;
        this.port = port;
        connect(host,port);
    }

    private void connect(String host, int port) {
        try {
            System.out.println("Connecting to " + host + " on port " + port);
            echoSocket = new Socket(host, port);
            System.out.println("Connected to " + host + " on port " + port);
            out = new DataOutputStream(echoSocket.getOutputStream());
            in = new DataInputStream(echoSocket.getInputStream());
            System.out.println("TCP Connected");
            long pack1=0, pack2=0, pack3=0, pack4=0, pack5, pack6, pack7;
            for (int i = 0; i < 100; i++) {

                //8bytes data, 8bytes ack
                ByteBuffer buf = ByteBuffer.allocate(8);
                buf.putLong(1);
                long start = System.nanoTime();
                out.write(buf.array());
                in.read(buf.array());
                long end = System.nanoTime();
                pack1 += (end - start);

                //32bytes data, 32bytes ack
                buf = ByteBuffer.allocate(32);
                start = System.nanoTime();
                out.write(buf.array());
                in.read(buf.array());
                end = System.nanoTime();
                pack2 += (end - start);

                //512bytes data, 512bytes ack
                buf = ByteBuffer.allocate(512);
                start = System.nanoTime();
                out.write(buf.array());
                in.read(buf.array());
                end = System.nanoTime();
                pack3 += (end - start);

                //1024bytes data, 1024bytes ack
                buf = ByteBuffer.allocate(1024);
                start = System.nanoTime();
                out.write(buf.array());
                in.read(buf.array());
                end = System.nanoTime();
                pack4 += (end - start);
            }
            //1Mb data, 8bytes ack
            ByteBuffer respond = ByteBuffer.allocate(8);
            ByteBuffer buf;
            long start = System.nanoTime();

            //1024bytes, 1024 times
            for (int j = 0; j < 1024; j++) {
                buf = ByteBuffer.allocate(1024);
                out.write(buf.array());
                in.read(respond.array());
            }
            long end = System.nanoTime();
            pack5 = ((end - start));

            start = System.nanoTime();

            //512bytes, 2048 times
            for (int j = 0; j < 2048; j++) {
                buf = ByteBuffer.allocate(512);
                out.write(buf.array());
                in.read(respond.array());
            }
            end = System.nanoTime();
            pack6 = ((end - start));

            start = System.nanoTime();

            //256 bytes, 4096 times
            for (int j = 0; j < 4096; j++) {
                buf = ByteBuffer.allocate(256);
                out.write(buf.array());
                in.read(respond.array());
            }
            end = System.nanoTime();
            pack7 = ((end - start));

            try {
                echoSocket.close();
                System.out.println("TCP Communication Closed");
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("TCP Results");
            System.out.println("Average 8 byte time: " + pack1 / 100 + " ns");
            System.out.println("Average 64 byte time: " + pack2 / 100 + " ns");
            System.out.println("Average 256 byte time: " + pack3 / 100 + " ns");
            System.out.println("Average 1024 byte time: " + pack4 / 100 + " ns");
            System.out.println("1024 byte throughput: " + 8 / (pack5 / 1e+9) + "Mbps");
            System.out.println("512 byte throughput: " + 8 / (pack6 / 1e+9) + "Mbps");
            System.out.println("256 byte throughput: " + 8 / (pack7 / 1e+9) + "Mbps");


        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host);
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
