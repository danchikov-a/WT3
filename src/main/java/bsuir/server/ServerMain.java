package bsuir.server;

import bsuir.server.service.ServerLogic;

import java.io.IOException;

public class ServerMain {
	public static void main(String[] args) throws IOException, InterruptedException {
		ServerLogic serverLogic = new ServerLogic();
		serverLogic.startConnection();
	}
}
