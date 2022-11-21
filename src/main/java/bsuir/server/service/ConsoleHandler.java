package bsuir.server.service;

import bsuir.server.entity.criteria.SearchCriteria;
import bsuir.server.entity.info.Info;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public class ConsoleHandler extends Thread {
    private final String fullCommand;
    private final ServerLogicHandler serverLogicHandler;

    public ConsoleHandler(String command, ServerLogicHandler serverLogicHandler) {
        this.fullCommand = command;
        this.serverLogicHandler = serverLogicHandler;
    }

    public void run() {
        String command;
        System.out.println(fullCommand);
        int index = 0;

        if (fullCommand.contains("[")) {
            index = fullCommand.indexOf("[");
            command = fullCommand.substring(0, index);
        } else {
            command = fullCommand;
        }

        switch (command) {
            case "EXIT" -> {
                try {
                    serverLogicHandler.stopConnection();
                    serverLogicHandler.startConnection();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }

            case "REG" -> {
                int newIndex = fullCommand.indexOf("]", index);
                String name = fullCommand.substring(index + 1, newIndex);
                index = fullCommand.indexOf("[", newIndex);
                newIndex = fullCommand.indexOf("]", index);
                String password = fullCommand.substring(index + 1, newIndex);
                index = fullCommand.indexOf("[", newIndex);
                newIndex = fullCommand.indexOf("]", index);
                String allowance = fullCommand.substring(index + 1, newIndex);
                ServiceFactory factory = ServiceFactory.getInstance();
                ServerService service = factory.getApplianceService();
                boolean isAdded = false;

                try {
                    isAdded = service.addUser(name, password, allowance);
                } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
                    e.printStackTrace();
                }

                if (isAdded) {
                    try {
                        serverLogicHandler.sendData("REG Name: " + name + ", Password: " + password + ", allowance: " + allowance + "\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    serverLogicHandler.getClientInfo().setAllowance(allowance);
                    serverLogicHandler.getClientInfo().setName(name);
                } else {
                    try {
                        serverLogicHandler.sendData("TRY AGAIN\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            case "LOGIN" -> {
                int newIndex = fullCommand.indexOf("]", index);
                String name = fullCommand.substring(index + 1, newIndex);
                index = fullCommand.indexOf("[", newIndex);
                newIndex = fullCommand.indexOf("]", index);
                String password = fullCommand.substring(index + 1, newIndex);
                serverLogicHandler.getClientCriteria().add(SearchCriteria.Client.name.getEnumName(), name);
                serverLogicHandler.getClientCriteria().add(SearchCriteria.Client.password.getEnumName(), password);
                ServiceFactory factory = ServiceFactory.getInstance();
                ServerService service = factory.getApplianceService();
                Info clientInfo = service.getUser(serverLogicHandler.getClientCriteria());

                if (clientInfo != null) {
                    serverLogicHandler.getClientInfo().setName(clientInfo.getParameters().get(0));
                    serverLogicHandler.getClientInfo().setAllowance(clientInfo.getParameters().get(1));

                    try {
                        serverLogicHandler.sendData("LOGIN Name: " + serverLogicHandler.getClientInfo().getName() + ", allowance: " + serverLogicHandler.getClientInfo().getAllowance() + "\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        serverLogicHandler.sendData("TRY AGAIN\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            case "GETALL" -> {
                if (!serverLogicHandler.getClientInfo().getAllowance().equals("")) {
                    ServiceFactory factory = ServiceFactory.getInstance();
                    ServerService service = factory.getApplianceService();
                    List<Info> studentInfoList = service.getAll();
                    StringBuilder data = new StringBuilder();

                    if (!studentInfoList.isEmpty()) {
                        for (Info studentInfo : studentInfoList) {
                            data.append(studentInfo.toString()).append("\n");
                        }

                        try {
                            serverLogicHandler.sendData(data.toString());
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serverLogicHandler.sendData("TRY AGAIN\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverLogicHandler.sendData("Not enough rights\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            case "GET" -> {
                if (!serverLogicHandler.getClientInfo().getAllowance().equals("")) {
                    int newIndex = fullCommand.indexOf("]", index);
                    String name = fullCommand.substring(index + 1, newIndex);
                    ServiceFactory factory = ServiceFactory.getInstance();
                    ServerService service = factory.getApplianceService();
                    serverLogicHandler.getStudentCriteria().getCriteria().clear();
                    serverLogicHandler.getStudentCriteria().add(SearchCriteria.Student.name.getEnumName(), name);
                    Info studentInfo;
                    studentInfo = service.getStudent(serverLogicHandler.getStudentCriteria());
                    String data;

                    if (studentInfo != null) {
                        data = String.format("Name: %s\n", studentInfo.toString());

                        try {
                            serverLogicHandler.sendData(data);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serverLogicHandler.sendData("TRY AGAIN\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverLogicHandler.sendData("Not enough rights\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            case "EDIT" -> {
                if (serverLogicHandler.getClientInfo().getAllowance().equals("EDIT") || serverLogicHandler.getClientInfo().getAllowance().equals("ADD")) {
                    int newIndex = fullCommand.indexOf("]", index);
                    String name = fullCommand.substring(index + 1, newIndex);
                    index = fullCommand.indexOf("[", newIndex);
                    newIndex = fullCommand.indexOf("]", index);
                    String newName = fullCommand.substring(index + 1, newIndex);
                    index = fullCommand.indexOf("[", newIndex);
                    newIndex = fullCommand.indexOf("]", index);
                    String averageScore = fullCommand.substring(index + 1, newIndex);
                    ServiceFactory factory = ServiceFactory.getInstance();
                    ServerService service = factory.getApplianceService();
                    boolean isEdit = service.registerStudent(name, newName, averageScore);

                    if (isEdit) {
                        try {
                            serverLogicHandler.sendData(String.format("EDIT Name: %s, newName: %s, averageScore: %s\n",
                                    name, newName, averageScore));
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serverLogicHandler.sendData("TRY AGAIN\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverLogicHandler.sendData("Not enough rights\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            case "ADD" -> {
                if (serverLogicHandler.getClientInfo().getAllowance().equals("ADD")) {
                    int newIndex = fullCommand.indexOf("]", index);
                    String name = fullCommand.substring(index + 1, newIndex);
                    index = fullCommand.indexOf("[", newIndex);
                    newIndex = fullCommand.indexOf("]", index);
                    String averageScore = fullCommand.substring(index + 1, newIndex);
                    ServiceFactory factory = ServiceFactory.getInstance();
                    ServerService service = factory.getApplianceService();
                    boolean isAdded = false;
                    
                    try {
                        isAdded = service.addStudent(name, averageScore);
                    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
                        e.printStackTrace();
                    }
                    if (isAdded) {
                        try {
                            serverLogicHandler.sendData("ADD Name: " + name + ", AverageScore: " + averageScore + "\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serverLogicHandler.sendData("TRY AGAIN\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverLogicHandler.sendData("Not enough rights\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            case "LOGOUT" -> {
                serverLogicHandler.getClientInfo().setName("");
                serverLogicHandler.getClientInfo().setAllowance("");
                try {
                    serverLogicHandler.sendData("Please, Login\n");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
