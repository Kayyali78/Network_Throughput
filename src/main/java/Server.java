import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server {
    int port;

    public Server(int port){
        this.port = port;
        serve(port);
    }

    private void serve(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);
            System.out.println("Waiting for client...");
            Socket client = serverSocket.accept();
            System.out.println("TCP Connected from: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("Connection opened. Input and output streams initialized.");
            ByteBuffer buffer = ByteBuffer.allocate(8);

            for (int i = 0; i < 300; i++) {
                in.read(buffer.array());
                XOR(buffer);
                out.write(buffer.array());

                buffer = ByteBuffer.allocate(32);
                in.read(buffer.array());
                XOR(buffer);
                out.write(buffer.array());

                buffer = ByteBuffer.allocate(512);
                in.read(buffer.array());
                XOR(buffer);
                out.write(buffer.array());

                buffer = ByteBuffer.allocate(1024);
                in.read(buffer.array());
                XOR(buffer);
                out.write(buffer.array());

                ByteBuffer buffer2 = ByteBuffer.allocate(8);
                buffer = ByteBuffer.allocate(128);
                for (int j=0;j<8192;j++) {
                    in.read(buffer.array());
                    XOR(buffer2);
                    out.write(buffer2.array());
                }

                buffer = ByteBuffer.allocate(512);
                for (int j=0;j<2048;j++) {
                    in.read(buffer.array());
                    XOR(buffer2);
                    out.write(buffer2.array());
                }

                buffer = ByteBuffer.allocate(1024);
                for (int j=0;j<1024;j++) {
                    in.read(buffer.array());
                    XOR(buffer2);
                    out.write(buffer2.array());
                }

            }
            try {
                in.close();
                out.close();
                System.out.println("Connection closed.");
            } catch (IOException ex){
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            System.out.println("IO Failure.");
            System.out.println(ex.getMessage());
        }

    }
    private static void XOR(ByteBuffer buffer) {
        long key = 0x5555555555555555L;
        for (int i = 0; i < 8; i++) {
            buffer.put(i, (byte) (buffer.get(i) ^ key));
        }
    }
}
