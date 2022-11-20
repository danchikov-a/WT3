package bsuir.server.entity.info;

import java.util.ArrayList;
import java.util.List;

public class ClientInfo implements Info {
    private String name;
    private String allowance;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getAllowance() {
        return allowance;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Allowance: %s", getName(), getAllowance());
    }

    public List<String> getParameters() {
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add(getName());
        parameters.add(getAllowance());

        return parameters;
    }
}
