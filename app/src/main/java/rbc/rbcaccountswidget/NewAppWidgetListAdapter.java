package rbc.rbcaccountswidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by User on 11/7/2015.
 */
public class NewAppWidgetListAdapter implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = NewAppWidgetListAdapter.class.getSimpleName();
    private ArrayList<NewAppWidgetListItem> itemList = new ArrayList<>();
    private Context mContext = null;
    private int mAppWidgetId;

    public NewAppWidgetListAdapter(Context context, Intent intent) {
        Log.d(TAG, "constructor called");
        this.mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        populateList();
    }

    private void populateList() {
        for (int i = 0; i < 11; i++) {
            NewAppWidgetListItem item = new NewAppWidgetListItem();
            item.account = "CHQ" + i + ":";
            item.balance = "$ 2 50" + i + ".00";
            item.balance_diff = "+ $ 100";
            item.time = "5d9h";
            itemList.add(item);
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_list_row);
        NewAppWidgetListItem item = itemList.get(position);
        remoteView.setTextViewText(R.id.widget_list_row_acc_name_textview, item.account);
        remoteView.setTextViewText(R.id.widget_list_row_acc_bal_textview, item.balance);
        remoteView.setTextViewText(R.id.widget_list_row_bal_diff_textview, item.balance_diff);
        remoteView.setTextViewText(R.id.widget_list_row_time_textview, item.time);
        return remoteView;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
