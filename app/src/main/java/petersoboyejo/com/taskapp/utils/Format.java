package petersoboyejo.com.taskapp.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Format {
    // Returns an array with just a single key value
    static final public String[] narrow(String jsonArr, String key) {
        try {
            JSONArray arr = new JSONArray(jsonArr);
            String[] newArr = new String[arr.length()];
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                newArr[i] = obj.getString(key);
            }
            return newArr;
        } catch (JSONException e) {
            return null;
        }
    }
}
