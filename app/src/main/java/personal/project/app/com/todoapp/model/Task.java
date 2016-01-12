package personal.project.app.com.todoapp.model;

/**
 * Created by sichen55 on 1/12/16.
 */
import java.util.Date;
public class Task {
    int id;
    String desc;
    int duration;
    Date createDate;

    public String getDesc(){
        return desc;
    }
}
