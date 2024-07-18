import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        out.println(name + " has joined the chat.");

        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
            }
        }).start();

        while (true) {
            String message = scanner.nextLine();
            out.println(name + ": " + message);
        }
    }
}