package bsuir.client;

import bsuir.client.service.ClientLogic;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        ClientLogic clientLogic = new ClientLogic();
        clientLogic.startClient();
    }
}
