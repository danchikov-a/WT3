package bsuir.server.service;

import bsuir.server.entity.criteria.Criteria;
import bsuir.server.entity.criteria.SearchCriteria;
import bsuir.server.entity.info.ClientInfo;
import bsuir.server.serverconsole.Printer;
import bsuir.server.serverconsole.MessageReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerLogicHandler {
    private static final String EMPTY_FIELD = "";
    private static final String EXIT_MESSAGE = "EXIT";

    private final Criteria criteria;
    private final Criteria studentCriteria;
    private final Printer printer;
    private final List<Thread> threads;
    private final Server server;

    private ClientInfo clientInfo;
    private boolean isServerWorking;

    {
        server = new Server(this);
        printer = new Printer();
        studentCriteria = new Criteria(SearchCriteria.Student.getCriteriaName());
        criteria = new Criteria(SearchCriteria.Client.getCriteriaName());
        threads = new ArrayList<>();
    }

    public ServerLogicHandler() {
        Thread consoleReader = new MessageReader(this);
        consoleReader.start();
    }

    public void startConnection() throws InterruptedException, IOException {
        clientInfo = new ClientInfo();
        clientInfo.setName(EMPTY_FIELD);
        clientInfo.setAllowance(EMPTY_FIELD);

        boolean isConnect = false;

        while (!isConnect) {
            isConnect = server.makeConnection();
        }

        sendData("Enter login\n");
        isServerWorking = true;

        while (isServerWorking) {
            String command = server.getCommand();
            Thread newCommand = new ConsoleHandler(command, this);
            newCommand.start();

            if (!command.equals(EXIT_MESSAGE)) {
                threads.add(newCommand);
            }
        }

        server.close();
        System.out.println("STOPPED");
    }

    public void stopConnection() throws InterruptedException, IOException {
        for (Thread thread : threads) {
            thread.join();
        }

        isServerWorking = false;
        server.sendClose();
    }

    public void sendData(String data) throws IOException, InterruptedException {
        server.sendData(data);
    }

    public Criteria getClientCriteria() {
        return criteria;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public Criteria getStudentCriteria() {
        return studentCriteria;
    }

    public Printer getResultPrinter() {
        return printer;
    }
}
