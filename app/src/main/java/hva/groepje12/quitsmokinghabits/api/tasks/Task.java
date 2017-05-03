package hva.groepje12.quitsmokinghabits.api.tasks;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.PadRestClient;

public class Task {
    private OnLoopJEvent listener;

    private static final String TAG = "PAD_API";

    public static final String REGISTER_PROFILE = "notification/register_profile";
    public static final String REMOVE_TIME = "notification/remove_time";
    public static final String ADD_TIME = "notification/add_time";

    public Task(OnLoopJEvent listener) {
        this.listener = listener;
    }

    public void execute(String url, RequestParams params) {
        PadRestClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listener.taskCompleted(response);
                Log.i(TAG, "onSuccess: " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.fatalError(responseString);
                Log.e(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.taskFailed(errorResponse);
                Log.e(TAG, "onFailure: " + errorResponse);
            }
        });
    }
}
