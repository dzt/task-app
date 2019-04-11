package petersoboyejo.com.taskapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import petersoboyejo.com.taskapp.DetailActivity;
import petersoboyejo.com.taskapp.adapters.TasksAdapter;
import petersoboyejo.com.taskapp.models.Task;
import petersoboyejo.com.taskapp.utils.RecyclerTouchListener;

public class TasksFragment extends Fragment {

    ArrayList<Task> tasks = new ArrayList<>();
    JSONArray arr;

    public TasksFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = new RecyclerView(getContext());

        this.init(getArguments().getString("json"));
        Log.d("TASKS: ", getArguments().getString("json"));

        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(new TasksAdapter(tasks));

        rv.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Task task = tasks.get(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("TASK_CHOICE", task);
                startActivity(intent);

            }
            @Override public void onLongClick(View view, int position) {}
        }));

        return rv;
    }

    private void init(String json) {
        tasks.clear(); // avoids duplication of items in the recycler view
        try {
            arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                tasks.add(new Task(obj.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}