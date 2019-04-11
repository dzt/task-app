package petersoboyejo.com.taskapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Task implements Serializable {

    String id, title, notes, dueDate, list, dateAdded;

    public Task(String json) {

        try {
            JSONObject taskObject = new JSONObject(json);
            this.id = taskObject.optString("id");
            this.title = taskObject.optString("title");
            this.notes = taskObject.optString("notes");
            this.dueDate = taskObject.optString("dueDate");
            this.list = taskObject.optString("list");
            this.dateAdded = taskObject.optString("dateAdded");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getList() {
        return list;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getId() {
        return id;
    }
}
