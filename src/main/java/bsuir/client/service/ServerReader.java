package bsuir.client.service;

import bsuir.client.clientconsole.Printer;

import java.io.IOException;

public class ServerReader extends Thread {
    private final Client client;
    private String data = "";
    private final Printer printer = new Printer();
    private final ClientLogic clientLogic;

    public ServerReader(Client client, ClientLogic clientLogic) {
        this.clientLogic = clientLogic;
        this.client = client;
    }

    public void run() {
        try {
            while (!data.equals("STOP")) {
                data = client.getData();
                printer.print(data);
            }

            client.sendCommand("STOP");
            client.close();
            clientLogic.setNonEnable();
            printer.print("Break with the server\nEnter any character");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
