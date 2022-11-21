package bsuir.client.service;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final int PORT = 8888;

    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

    public boolean makeConnection() {
        try {
            try {
                clientSocket = new Socket("localhost", PORT);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String word = "CONNECT\n";
                out.write(word);
                out.flush();

                return true;
            } catch (Exception e) {
                System.err.println(e);
                clientSocket.close();
                in.close();
                out.close();
            }
            return false;
        } catch (IOException e) {
            System.err.println(e);
        }

        return false;
    }

    public void sendCommand(String command) throws IOException {
        try {
            out.write(command + "\n");
            out.flush();
        } catch (IOException e) {
            in.close();
            out.close();
            clientSocket.close();
        }
    }

    public String getData() throws IOException {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }

        return "STOP";
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}

