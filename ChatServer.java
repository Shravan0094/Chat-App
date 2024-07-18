import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 8888;
    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Chat Server is running...");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new ClientHandler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                clients.add(out);

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter client : clients) {
                        client.println(input);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    clients.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}