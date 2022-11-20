package bsuir.server.serverconsole;

import bsuir.server.service.ServerLogic;

import java.io.IOException;
import java.util.Scanner;

public class MsgReader extends Thread{
    private final Scanner scan = new Scanner(System.in);
    private final ServerLogic serverLogic;

    public MsgReader(ServerLogic serverLogic){
        this.serverLogic = serverLogic;
    }

    public void run(){
        String command = scan.next();

        if (command.equals("STOP")) {
            try {
                serverLogic.stopConnection();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
