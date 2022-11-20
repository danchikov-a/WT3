package bsuir.server.entity.info;

import java.util.ArrayList;
import java.util.List;

public class StudentInfo implements Info {
    private String name;
    private String averageScore;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAverageScore(String averageScore) {
        this.averageScore = averageScore;
    }

    public String getAverageScore() {
        return averageScore;
    }

    @Override
    public String toString(){
        return String.format("Name: %s, AverageScore: %s", getName(), getAverageScore());
    }

    public List<String> getParameters(){
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add(getName());
        parameters.add(getAverageScore());

        return parameters;
    }
}
