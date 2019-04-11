package petersoboyejo.com.taskapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class List {

    String id, title, dateAdded;

    public List(String json) {
        try {
            JSONObject taskObject = new JSONObject(json);
            this.id = taskObject.optString("id");
            this.title = taskObject.optString("title");
            this.dateAdded = taskObject.optString("dateAdded");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getTitle() {
        return title;
    }

}
