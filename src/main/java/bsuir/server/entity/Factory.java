package bsuir.server.entity;

import bsuir.server.entity.info.StudentInfo;
import bsuir.server.entity.info.ClientInfo;
import bsuir.server.entity.info.Info;

import java.util.List;

public class Factory {

    public Info getInfo(List<String> parameters, String type){
        if ("Client".equals(type)) {
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setName(parameters.get(0));
            clientInfo.setAllowance(parameters.get(2));
            return clientInfo;
        }

        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setName(parameters.get(0));
        studentInfo.setAverageScore(parameters.get(1));
        return studentInfo;
    }
}
