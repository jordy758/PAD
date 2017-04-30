package api;

import org.json.JSONObject;

public interface OnLoopJEvent {

    public void taskCompleted(JSONObject results);

    public void taskFailed(JSONObject results);

}
