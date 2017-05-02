package hva.groepje12.quitsmokinghabits.api.tasks;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.PadRestClient;

public class RemoveTimeTask implements Task {
    private OnLoopJEvent listener;

    private static final String TAG = "PAD_API";

    public RemoveTimeTask(OnLoopJEvent listener) {
        this.listener = listener;
    }

    public void execute(RequestParams params) {
        PadRestClient.post("notification/remove_time", params, new JsonHttpResponseHandler() {
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

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                listener.taskFailed(errorResponse.toString());
                Log.e(TAG, "onFailure: " + errorResponse);
            }
        });
    }
}
