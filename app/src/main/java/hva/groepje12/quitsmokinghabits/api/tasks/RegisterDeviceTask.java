package hva.groepje12.quitsmokinghabits.api.tasks;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.PadRestClient;
import cz.msebera.android.httpclient.Header;

public class RegisterDeviceTask implements Task {
    private OnLoopJEvent listener;

    private static final String TAG = "PAD_API";

    public RegisterDeviceTask(OnLoopJEvent listener) {
        this.listener = listener;
    }

    public void execute(RequestParams params) {
        PadRestClient.post("notification/register_profile", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                listener.taskCompleted(response);
                Log.i(TAG, "onSuccess: " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                listener.taskFailed(responseString);
                Log.e(TAG, "onFailure: " + responseString);
            }
        });
    }
}
