package bsuir.client.service;

import bsuir.client.clientconsole.Reader;

import java.io.IOException;

public class ClientLogic {
    private final Client client = new Client();
    private String command = "";
    private boolean serverEnable;

    public void startClient() throws IOException {
        Reader commandReader = new Reader();
        serverEnable = client.makeConnection();

        if (serverEnable) {
            ServerReader serverReader = new ServerReader(client, this);
            serverReader.start();
        }

        while (!command.equals("EXIT") && serverEnable) {
            command = commandReader.getCommand();
            command = command.replaceAll("\\s+", "_");
            System.out.println(command);
            client.sendCommand(command.replaceAll("\\s+", "_") + "\n");
        }
    }

    public void setNonEnable() {
        serverEnable = false;
    }

}
