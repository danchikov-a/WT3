package bsuir.server.service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8888;
    private Socket clientSocket;
    private ServerSocket server;
    private BufferedReader in;
    private BufferedWriter out;

    private final ServerLogicHandler serverLogicHandler;

    public Server(ServerLogicHandler serverLogicHandler) {
        this.serverLogicHandler = serverLogicHandler;
    }

    public boolean makeConnection() {
        try {
            try {
                server = new ServerSocket(PORT);
                System.out.println("Waiting for clients");
                clientSocket = server.accept();

                try {

                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    String word = in.readLine();

                    if (word.equals("CONNECT")) {
                        System.out.println(word);
                        out.write("CONNECT\n");
                        out.flush();

                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    clientSocket.close();
                    in.close();
                    out.close();
                }

                return false;

            } catch (Exception e) {
                server.close();
            }

            return false;
        } catch (IOException e) {
            System.err.println(e);
        }

        return false;
    }

    public String getCommand() throws IOException, InterruptedException {
        try {
            return in.readLine();
        } catch (IOException e) {
            clientSocket.close();
            in.close();
            out.close();
            serverLogicHandler.startConnection();
        }

        return "";
    }

    public void sendData(String data) throws IOException, InterruptedException {
        try {
            out.write(data + "\n");
            out.flush();
        } catch (IOException e) {
            clientSocket.close();
            in.close();
            out.close();
            serverLogicHandler.startConnection();
        }
    }

    public void sendClose() throws IOException, InterruptedException {
        try {
            out.write("STOP\n");
            out.flush();
        } catch (IOException e) {
            clientSocket.close();
            in.close();
            out.close();
            serverLogicHandler.startConnection();
        }
    }

    public void close() throws IOException {
        in.close();
        out.close();
        server.close();
    }
}
