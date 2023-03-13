public class Main {

    private final static String helptext = "Syntax: java -jar Network_Throughput.jar (-server PORT | -client HOST PORT | -UDPserver HOST PORT | -UDPclient HOST PORT)";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(helptext);
        } else {
            switch  (args[0]) {
                case "-server":
                    new Server(Integer.parseInt(args[1]));
                    break;
                case "-client":
                    new Client(args[1], Integer.parseInt(args[2]));
                    break;
                case "-UDPserver":
                    new UDPServer(args[1], Integer.parseInt(args[2]));
                    break;
                case "-UDPclient":
                    new UDPClient(args[1], Integer.parseInt(args[2]));
                    break;
                default:
                    System.out.println(helptext);
                    break;
            }
        }
    }
}
