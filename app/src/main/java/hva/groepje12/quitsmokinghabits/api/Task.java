package hva.groepje12.quitsmokinghabits.api;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Task {
    public static final String REGISTER_PROFILE = "profile/register";
    public static final String PROFILE_INFORMATION = "profile/get_all_information";

    public static final String REMOVE_TIME = "alarm/remove";
    public static final String ADD_TIME = "alarm/add";

    public static final String ADD_SMOKE_DATA = "smoke_data/add";
    public static final String GET_TILE_DATA = "smoke_data/get_tile_data";

    public static final String ADD_GOAL = "goal/add";
    public static final String REMOVE_GOAL = "goal/remove";

    private static final String TAG = "PAD_API";

    private OnLoopJEvent listener;

    public Task(OnLoopJEvent listener) {
        this.listener = listener;
    }

    public void execute(final String url, RequestParams params) {
        if (params == null) {
            params = new RequestParams();
        }

        params.put("notification_token", FirebaseInstanceId.getInstance().getToken());

        PadRestClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listener.taskCompleted(response);
                Log.i(TAG + " - " + url, "onSuccess: " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.fatalError(responseString);
                Log.e(TAG + " - " + url, "onThrowableFailure: " + responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.taskFailed(errorResponse);
                Log.e(TAG + " - " + url, "onJsonFailure: " + errorResponse);
            }
        });
    }
}
