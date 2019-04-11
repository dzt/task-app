package petersoboyejo.com.taskapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class LocalDatabase {

    static final public String fetchTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("taskPrefs", MODE_PRIVATE);
        String tasksArr = prefs.getString("tasks", "");
        return tasksArr;
    }

    static final public String fetchLists(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("taskPrefs", MODE_PRIVATE);
        String listsArr = prefs.getString("lists", "");
        return listsArr;
    }

    static final public String getTask(Context context, String taskID) {
        SharedPreferences prefs = context.getSharedPreferences("taskPrefs", MODE_PRIVATE);
        String tasksArr = prefs.getString("tasks", "");
        try {
            JSONArray arr = new JSONArray(tasksArr);
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                String id = obj.getString("id");
                if (id.equals(taskID)) {
                    return obj.toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static final public void deleteTask(Context context, String taskID) {
        SharedPreferences prefs = context.getSharedPreferences("taskPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray newArr = new JSONArray();
        try {
            JSONArray arr = new JSONArray(fetchTasks(context));
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                String id = obj.getString("id");
                if (!id.equals(taskID)) {
                    newArr.put(obj);
                }
            }
            editor.putString("tasks", newArr.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static final public void updateTask(Context context, String taskID, String key, String value) {

        SharedPreferences prefs = context.getSharedPreferences("taskPrefs", MODE_PRIVATE);
        String tasksArr = prefs.getString("tasks", "");
        SharedPreferences.Editor editor = prefs.edit();

        JSONArray newArr = new JSONArray();
        try {
            JSONArray arr = new JSONArray(tasksArr);
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                String id = obj.getString("id");
                if (id.equals(taskID)) {
                    obj.put(key, value);
                }
                newArr.put(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putString("tasks", newArr.toString());
        editor.commit();

    }

}
