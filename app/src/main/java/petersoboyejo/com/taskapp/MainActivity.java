package petersoboyejo.com.taskapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import petersoboyejo.com.taskapp.fragments.TasksFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Tasks");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                this.addTask();
                break;
            case R.id.action_add_list:
                this.addList();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public String[] getListNames() {

        SharedPreferences destinationsPreferences = getSharedPreferences("taskPrefs", MODE_PRIVATE);
        String listsArr = destinationsPreferences.getString("lists", "");
        JSONArray arr = null;

        try {
            arr = new JSONArray(listsArr);
            String[] names = new String[arr.length()];
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                names[i] = obj.getString("title");
            }
            return names;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Task");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.create_task, null, false);
        final EditText input = viewInflated.findViewById(R.id.input_task_name);
        final Spinner spinner = viewInflated.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        this.getListNames()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences prefs = getSharedPreferences("taskPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                if (!prefs.contains("tasks")) {
                    editor.putString("tasks", new JSONArray().toString());
                    editor.commit();
                }

                if (input.getText().toString().toLowerCase() != "all") {
                    String tasksArr = prefs.getString("tasks", "");

                    try {

                        JSONArray arr = new JSONArray(tasksArr);
                        JSONObject obj = new JSONObject();

                        obj.put("id", UUID.randomUUID().toString());
                        obj.put("title", input.getText().toString());
                        obj.put("notes", "");
                        obj.put("dueDate", null);
                        obj.put("list", spinner.getSelectedItem().toString());

                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                        obj.put("dateAdded", sdf.format(new Date()));

                        arr.put(obj);

                        editor.putString("tasks", arr.toString());
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MainActivity.this.loadViewPager();
                }

                dialog.dismiss();

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();
    }

    public void addList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create List");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.create_list, null, false);
        final EditText input = viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences prefs = getSharedPreferences("taskPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                if (!prefs.contains("lists")) {
                    editor.putString("lists", new JSONArray().toString());
                    editor.commit();
                }

                if (input.getText().toString().toLowerCase() != "all") {
                    String listsArr = prefs.getString("lists", "");

                    try {

                        JSONArray arr = new JSONArray(listsArr);
                        JSONObject obj = new JSONObject();

                        obj.put("id", UUID.randomUUID().toString());
                        obj.put("title", input.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                        obj.put("dateAdded", sdf.format(new Date()));
                        arr.put(obj);
                        editor.putString("lists", arr.toString());
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MainActivity.this.loadViewPager();
                }

                dialog.dismiss();

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        this.loadViewPager();
    }

    public void loadViewPager() {

        JSONArray arr = null;
        SharedPreferences prefs = getSharedPreferences("taskPrefs", MODE_PRIVATE);
        String listsArr = prefs.getString("lists", "");
        String tasksArr = prefs.getString("tasks", "");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        TasksFragment allFragment = new TasksFragment();
        bundle.putString("json", tasksArr);
        allFragment.setArguments(bundle);
        adapter.addFragment(allFragment, "All");

        try {
            arr = new JSONArray(listsArr);
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                String title = obj.getString("title");
                TasksFragment fragment = new TasksFragment();
                Bundle fragmentBundle = new Bundle();
                fragmentBundle.putString("json", filterTasks(tasksArr, title));
                fragment.setArguments(fragmentBundle);
                adapter.addFragment(fragment, title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewPager.setAdapter(adapter);

    }

    public String filterTasks(String json, String list) {
        JSONArray newArr = new JSONArray();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                String taskList = obj.getString("list");
                if (taskList.equals(list)) {
                    newArr.put(obj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newArr.toString();
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List < Fragment > mFragmentList = new ArrayList < > ();
        private final List < String > mFragmentTitleList = new ArrayList < > ();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}