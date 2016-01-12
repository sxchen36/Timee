package personal.project.app.com.todoapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import personal.project.app.com.todoapp.R;
import personal.project.app.com.todoapp.model.Project;
import personal.project.app.com.todoapp.model.Task;

/**
 * Created by sichen55 on 1/11/16.
 */
public class ProjectListAdapter extends BaseExpandableListAdapter {

    private final String LOG_TAG = getClass().getSimpleName().toString();
    private MainActivity mActivity;
    protected List<Project> mProjects;

    public ProjectListAdapter(Context context) {
        mActivity = (MainActivity) context;
    }

    public void updateProjectsData(List<Project> projects) {
        this.mProjects = projects;
        this.notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Task> tasks = mProjects.get(groupPosition).getTasks();
        return tasks.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Task task = (Task)this.getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView taskDesc = (TextView) convertView.findViewById(R.id.list_item);
        taskDesc.setText(task.getDesc().trim());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Task> tasks = mProjects.get(groupPosition).getTasks();
        return tasks.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mProjects.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Project project = (Project)getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView head = (TextView) convertView.findViewById(R.id.list_header);
        head.setText(project.getName().trim());

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return mProjects.size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
