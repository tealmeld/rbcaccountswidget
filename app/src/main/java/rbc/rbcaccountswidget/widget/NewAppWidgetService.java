package rbc.rbcaccountswidget.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by User on 11/7/2015.
 */
public class NewAppWidgetService extends RemoteViewsService {

    private static final String TAG = NewAppWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory called");
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new NewAppWidgetListAdapter(this.getApplicationContext(), intent));
    }


}
