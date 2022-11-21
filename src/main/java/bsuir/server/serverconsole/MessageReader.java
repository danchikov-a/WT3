package bsuir.server.serverconsole;

import bsuir.server.service.ServerLogicHandler;

import java.io.IOException;
import java.util.Scanner;

public class MessageReader extends Thread{
    private final Scanner scan = new Scanner(System.in);
    private final ServerLogicHandler serverLogicHandler;

    public MessageReader(ServerLogicHandler serverLogicHandler){
        this.serverLogicHandler = serverLogicHandler;
    }

    public void run(){
        String command = scan.next();

        if (command.equals("STOP")) {
            try {
                serverLogicHandler.stopConnection();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
