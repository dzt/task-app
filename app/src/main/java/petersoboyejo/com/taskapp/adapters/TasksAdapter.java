package petersoboyejo.com.taskapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import petersoboyejo.com.taskapp.R;
import petersoboyejo.com.taskapp.models.Task;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {

    private List<Task> tasks;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, notes;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.task_title);
            notes = (TextView) view.findViewById(R.id.task_notes);
        }
    }


    public TasksAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task taskObj = tasks.get(position);
        holder.title.setText(taskObj.getTitle());
        holder.notes.setText(taskObj.getNotes());
        if (taskObj.getNotes().equals("")) {
            holder.notes.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}