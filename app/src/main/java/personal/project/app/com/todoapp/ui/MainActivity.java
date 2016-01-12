package personal.project.app.com.todoapp.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import personal.project.app.com.todoapp.R;
import personal.project.app.com.todoapp.database.DatabaseContract;
import personal.project.app.com.todoapp.database.DatabaseHelper;
import personal.project.app.com.todoapp.model.Project;

public class MainActivity extends AppCompatActivity{

    private final String LOG_TAG = getClass().getSimpleName().toString();
    private DatabaseHelper helper;
    ProjectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DatabaseHelper(MainActivity.this);

        ExpandableListView expandableContactListView = (ExpandableListView) findViewById(R.id.lvExp);

        mAdapter = new ProjectListAdapter(this);

        expandableContactListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_add_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a task");
                final EditText inputField = new EditText(this);
                final EditText hourFiled = new EditText(this);
                hourFiled.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(inputField);
                ll.addView(hourFiled);
                builder.setView(ll);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = inputField.getText().toString();
                        String hourString = hourFiled.getText().toString();

                        if (title.trim().equals("")) {
                            inputField.setError("Title is required!");
                        } else if (hourString.trim().equals("")) {
                            hourFiled.setError("Hour is required!");
                        } else {
                            int hour = Integer.parseInt(hourString);
                            Project newProject = new Project(title);
                            updateUI();
                        }
                    }
                });

                builder.setNegativeButton("Cancel",null);

                builder.create().show();
                return true;

            default:
                return false;
        }
    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskIdView = (TextView) v.findViewById(R.id.task_id);
        String task_id = taskIdView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                DatabaseHelper.Tables.PROJECTS,
                DatabaseContract.ProjectColumns.PROJECT_ID,
                task_id);

        helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }

    public void onAddTimeButtonClick(View view) {
        View v = (View) view.getParent();
        TextView projectIdView = (TextView)v.findViewById(R.id.task_id);
        final String project_id = projectIdView.getText().toString();
        TextView preHourView = (TextView)v.findViewById(R.id.task_hour);
        final int preHour = Integer.parseInt(preHourView.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add hours");

        final EditText titleField = new EditText(this);
        final EditText hourField = new EditText(this);
        hourField.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(titleField);
        ll.addView(hourField);
        builder.setView(ll);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SQLiteDatabase db = helper.getWritableDatabase();

                // insert task table
                String subTitle = titleField.getText().toString();
                int hour = Integer.parseInt(hourField.getText().toString());
                int totalHour = preHour + hour;
                ContentValues values = new ContentValues();
                values.clear();
                values.put(DatabaseContract.TaskColumns.TASK_DESC, subTitle);
                values.put(DatabaseContract.TaskColumns.TASK_HOUR, hour);
                values.put(DatabaseContract.TaskColumns.TASK_PROJECT_ID, project_id);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                values.put(DatabaseContract.TaskColumns.TASK_DATE, sdf.format(new Date()));
                db.insertWithOnConflict(DatabaseHelper.Tables.TASKS, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                // update project
                values.clear();
                values.put(DatabaseContract.ProjectColumns.PROJECT_HOURS, totalHour);
                db.updateWithOnConflict(DatabaseHelper.Tables.PROJECTS, values,
                        DatabaseContract.ProjectColumns.PROJECT_ID + " = ?", new String[]{project_id}, SQLiteDatabase.CONFLICT_IGNORE);

                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }



    // support functions //

    private void updateUI(){
        new ReadAllDataTask().execute();
    }

    private class ReadAllDataTask extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor allData = db.query(DatabaseHelper.Tables.PROJECTS, new String[]{DatabaseContract.ProjectColumns.PROJECT_ID,
                    DatabaseContract.ProjectColumns.PROJECT_TITLE, DatabaseContract.ProjectColumns.PROJECT_HOURS},
                    null, null, null, null, null);
            return allData;
        }
        @Override
        protected void onPostExecute(Cursor data) {
            if (data != null && data.getCount() > 0) {
                List<Project> projects = new ArrayList<Project>();
                data.moveToFirst();
                do {
                    int projectId = data.getInt(data.getColumnIndex(DatabaseContract.ProjectColumns.PROJECT_ID));
                    String projectTitle = data.getString(data.getColumnIndex(DatabaseContract.ProjectColumns.PROJECT_TITLE));
                    int projectHour = data.getInt(data.getColumnIndex(DatabaseContract.ProjectColumns.PROJECT_HOURS));

                    Project newProject = new Project(projectTitle);
                    newProject.setHour(projectHour);
                    projects.add(newProject);
                } while (data.moveToNext());
                mAdapter.updateProjectsData(projects);
            }


        }
    }

}
