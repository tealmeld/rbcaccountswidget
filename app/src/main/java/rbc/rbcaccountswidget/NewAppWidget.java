package rbc.rbcaccountswidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String TAG = NewAppWidget.class.getSimpleName();

    public static final String ACTION_DATA_RECEIVED = "rbc.rbcaccountswidget.ACTION_DATA_RECEIVED";
    public static final String ACTION_DATA_INCOMING = "rbc.rbcaccountswidget.ACTION_DATA_INCOMING";
    public static final String ACTION_NETWORK_ERROR = "rbc.rbcaccountswidget.ACTION_NETWORK_ERROR";
    public static int randomNumber = 1 + (int)(Math.random()*1000000);

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate called");
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            Intent serviceIntent = new Intent(context, NewAppWidgetUpdateService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetIds[i]);
            context.startService(serviceIntent);

            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            addRefreshButtonOnClickEvent(context, widget);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void addRefreshButtonOnClickEvent(Context context, RemoteViews widget) {
        Intent clickIntent = new Intent(context, NewAppWidgetUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, clickIntent, 0);
        widget.setOnClickPendingIntent(R.id.widget_refreshButton, pendingIntent);
    }

    private void updateWidgetListView(Context context, RemoteViews remoteViews, int appWidgetId) {
        Intent svcIntent = new Intent(context, NewAppWidgetService.class);
        final ComponentName provider = new ComponentName(context, this.getClass());
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
//        svcIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId+randomNumber), null));
        remoteViews.setRemoteAdapter(R.id.listViewWidget, svcIntent);


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);

//        updateAppWidget(context, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        // RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
        switch (intent.getAction()) {
            case ACTION_DATA_RECEIVED:
                onDataReceived(context, remoteViews, appWidgetId);
                break;
            case ACTION_DATA_INCOMING:
                onDataIncoming(remoteViews);
                break;
            case ACTION_NETWORK_ERROR:
                onNetworkError(context, remoteViews);
                break;
            default:
                break;
        }
        final ComponentName provider = new ComponentName(context, this.getClass());
        appWidgetManager.updateAppWidget(provider, remoteViews);
    }

    private void onDataReceived(Context context, RemoteViews remoteViews, int appWidgetId) {
        Log.d(TAG, "onDataReceived() called");
        resetRefreshButton(remoteViews);
        addRefreshButtonOnClickEvent(context, remoteViews);
        updateWidgetListView(context, remoteViews, appWidgetId);
    }

    private void onDataIncoming(RemoteViews remoteViews) {
        Log.d(TAG, "onDataIncoming() called");
        remoteViews.setViewVisibility(R.id.widget_refreshButton, View.INVISIBLE);
        remoteViews.setViewVisibility(R.id.widget_progressBar, View.VISIBLE);
    }

    private void onNetworkError(Context context, RemoteViews remoteViews) {
        Log.d(TAG, "onNetworkError() called");
        resetRefreshButton(remoteViews);
        addRefreshButtonOnClickEvent(context, remoteViews);
    }

    private void resetRefreshButton(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.widget_refreshButton, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.widget_progressBar, View.INVISIBLE);
    }

    /*@Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            NewAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }*/

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

