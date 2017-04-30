package api.tasks;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import api.OnLoopJEvent;
import api.PadRestClient;
import cz.msebera.android.httpclient.Header;

public class RegisterDeviceTask implements Task {
    private OnLoopJEvent listener;

    private static final String TAG = "PAD_API";

    public RegisterDeviceTask(OnLoopJEvent listener) {
        this.listener = listener;
    }

    public void execute(RequestParams params) {
        PadRestClient.post("notification/register_device", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                listener.taskCompleted(response);
                Log.i(TAG, "onSuccess: " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                listener.taskFailed(errorResponse);
                Log.e(TAG, "onFailure: " + errorResponse);
            }
        });
    }
}
