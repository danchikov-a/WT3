package bsuir.server.service;

import bsuir.server.entity.criteria.SearchCriteria;
import bsuir.server.entity.info.Info;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public class CommandHandler extends Thread {
    private final String fullCommand;
    private final ServerLogic serverLogic;

    public CommandHandler(String command, ServerLogic serverLogic) {
        this.fullCommand = command;
        this.serverLogic = serverLogic;
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
                    serverLogic.stopConnection();
                    serverLogic.startConnection();
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
                        serverLogic.sendData("REG Name: " + name + ", Password: " + password + ", allowance: " + allowance + "\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    serverLogic.getClientInfo().setAllowance(allowance);
                    serverLogic.getClientInfo().setName(name);
                } else {
                    try {
                        serverLogic.sendData("TRY AGAIN\n");
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
                serverLogic.getClientCriteria().add(SearchCriteria.Client.name.getEnumName(), name);
                serverLogic.getClientCriteria().add(SearchCriteria.Client.password.getEnumName(), password);
                ServiceFactory factory = ServiceFactory.getInstance();
                ServerService service = factory.getApplianceService();
                Info clientInfo = service.getUser(serverLogic.getClientCriteria());

                if (clientInfo != null) {
                    serverLogic.getClientInfo().setName(clientInfo.getParameters().get(0));
                    serverLogic.getClientInfo().setAllowance(clientInfo.getParameters().get(1));

                    try {
                        serverLogic.sendData("LOGIN Name: " + serverLogic.getClientInfo().getName() + ", allowance: " + serverLogic.getClientInfo().getAllowance() + "\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        serverLogic.sendData("TRY AGAIN\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            case "GETALL" -> {
                if (!serverLogic.getClientInfo().getAllowance().equals("")) {
                    ServiceFactory factory = ServiceFactory.getInstance();
                    ServerService service = factory.getApplianceService();
                    List<Info> studentInfoList = service.getAll();
                    StringBuilder data = new StringBuilder();

                    if (!studentInfoList.isEmpty()) {
                        for (Info studentInfo : studentInfoList) {
                            data.append(studentInfo.toString()).append("\n");
                        }

                        try {
                            serverLogic.sendData(data.toString());
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serverLogic.sendData("TRY AGAIN\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverLogic.sendData("Not enough rights\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            case "GET" -> {
                if (!serverLogic.getClientInfo().getAllowance().equals("")) {
                    int newIndex = fullCommand.indexOf("]", index);
                    String name = fullCommand.substring(index + 1, newIndex);
                    ServiceFactory factory = ServiceFactory.getInstance();
                    ServerService service = factory.getApplianceService();
                    serverLogic.getStudentCriteria().getCriteria().clear();
                    serverLogic.getStudentCriteria().add(SearchCriteria.Student.name.getEnumName(), name);
                    Info studentInfo;
                    studentInfo = service.getStudent(serverLogic.getStudentCriteria());
                    String data;

                    if (studentInfo != null) {
                        data = String.format("Name: %s\n", studentInfo.toString());

                        try {
                            serverLogic.sendData(data);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serverLogic.sendData("TRY AGAIN\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverLogic.sendData("Not enough rights\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            case "EDIT" -> {
                if (serverLogic.getClientInfo().getAllowance().equals("EDIT") || serverLogic.getClientInfo().getAllowance().equals("ADD")) {
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
                    boolean isEdit = service.regStudent(name, newName, averageScore);

                    if (isEdit) {
                        try {
                            serverLogic.sendData(String.format("EDIT Name: %s, newName: %s, averageScore: %s\n",
                                    name, newName, averageScore));
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serverLogic.sendData("TRY AGAIN\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverLogic.sendData("Not enough rights\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            case "ADD" -> {
                if (serverLogic.getClientInfo().getAllowance().equals("ADD")) {
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
                            serverLogic.sendData("ADD Name: " + name + ", AverageScore: " + averageScore + "\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            serverLogic.sendData("TRY AGAIN\n");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        serverLogic.sendData("Not enough rights\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            case "LOGOUT" -> {
                serverLogic.getClientInfo().setName("");
                serverLogic.getClientInfo().setAllowance("");
                try {
                    serverLogic.sendData("Please, Login\n");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
