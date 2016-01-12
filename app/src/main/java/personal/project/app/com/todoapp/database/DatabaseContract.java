package personal.project.app.com.todoapp.database;

import android.provider.BaseColumns;

/**
 * Created by sichen55 on 1/8/16.
 */
public class DatabaseContract {
    public interface ProjectColumns {
        public String PROJECT_ID = BaseColumns._ID;
        public String PROJECT_TITLE = "project_title";
        public String PROJECT_HOURS = "project_hours";
        public String PROJECT_COLOR = "project_color";
        public String PROJECT_END_TIME = "project_end_time";
    }

    public interface TaskColumns {
        public String TASK_ID = BaseColumns._ID;
        public String TASK_DESC = "task_description";
        public String TASK_HOUR = "task_hour";
        public String TASK_DATE = "task_date";
        public String TASK_PROJECT_ID = "task_project_id";
    }
}
