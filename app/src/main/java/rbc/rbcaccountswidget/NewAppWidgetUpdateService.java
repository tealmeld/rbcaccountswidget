package rbc.rbcaccountswidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User on 11/8/2015.
 */
public class NewAppWidgetUpdateService extends Service {
    private static final String TAG = NewAppWidgetUpdateService.class.getSimpleName();
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private AQuery aquery;
    private String remoteJsonUrl = "";

    public static ArrayList<NewAppWidgetListItem> itemList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
                appWidgetId = intent.getIntExtra(
                                AppWidgetManager.EXTRA_APPWIDGET_ID,
                                AppWidgetManager.INVALID_APPWIDGET_ID);
        aquery = new AQuery(getBaseContext());
        fetchDataFromWeb();
        return super.onStartCommand(intent, flags, startId);
    }

    private void fetchDataFromWeb() {
        aquery.ajax(remoteJsonUrl, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                processResult(result);
                super.callback(url, object, status);
            }
        });
    }

    private void processResult(String result) {
        Log.d(TAG, "result: " + result);
        itemList = new ArrayList<NewAppWidgetListItem>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                NewAppWidgetListItem item = new NewAppWidgetListItem();
                item.account = jsonObject.getString("account");
                item.balance = jsonObject.getString("balance");
                item.balance_diff = jsonObject.getString("balance_diff");
                item.time = jsonObject.getString("time");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        populateWidget();
    }

    private void populateWidget() {
        Log.d(TAG, "populateWidget() called");
        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(NewAppWidget.ACTION_DATA_RECEIVED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        sendBroadcast(widgetUpdateIntent);
        this.stopSelf();
    }
}
