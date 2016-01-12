package personal.project.app.com.todoapp.model;

import java.util.List;

/**
 * Created by sichen55 on 1/12/16.
 */

public class Project {
    String name;
    List<Task> tasks;
    private int hour;

    public Project(String name) {
        this.name = name;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public String getName() {
        return name;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
