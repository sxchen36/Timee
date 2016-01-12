package personal.project.app.com.todoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by sichen55 on 1/8/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Timee Database";

    // Tables
    public interface Tables {
        String PROJECTS = "Projects";
        String TASKS = "Tasks";

    }

    // Table create statements
    // TO DO table create statement
    private static final String CREATE_TABLE_PROJECT = "CREATE TABLE "
            + Tables.PROJECTS + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.ProjectColumns.PROJECT_TITLE + " TEXT NOT NULL,"
            + DatabaseContract.ProjectColumns.PROJECT_HOURS + " INTEGER,"
            + DatabaseContract.ProjectColumns.PROJECT_END_TIME + " DATETIME,"
            + DatabaseContract.ProjectColumns.PROJECT_COLOR + " TEXT"
            + ")";

    private static final String CREATE_TABLE_TASK = "CREATE TABLE "
            + Tables.TASKS + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.TaskColumns.TASK_DESC+ " TEXT NOT NULL,"
            + DatabaseContract.TaskColumns.TASK_HOUR + " INTEGER,"
            + DatabaseContract.TaskColumns.TASK_DATE + " DATETIME,"
            + DatabaseContract.TaskColumns.TASK_PROJECT_ID + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PROJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TASKS);
        onCreate(db);
    }

}
