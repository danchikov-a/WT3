package bsuir.server;

import bsuir.server.service.ServerLogicHandler;

import java.io.IOException;

public class ServerMain {
	public static void main(String[] args) throws IOException, InterruptedException {
		ServerLogicHandler serverLogicHandler = new ServerLogicHandler();
		serverLogicHandler.startConnection();
	}
}
