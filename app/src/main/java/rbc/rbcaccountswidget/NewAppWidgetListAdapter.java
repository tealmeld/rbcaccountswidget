package rbc.rbcaccountswidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by User on 11/7/2015.
 */
public class NewAppWidgetListAdapter implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = NewAppWidgetListAdapter.class.getSimpleName();
    private static final long BAL_SMALL = 2000;
    private static final long BAL_MED   = 20000;
    private static final long BAL_LRG   = 200000;

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

//    private void populateList() {
//        for (int i = 0; i < 11; i++) {
//            NewAppWidgetListItem item = new NewAppWidgetListItem();
//            item.account = "CHQ" + i + ":";
//            item.balance = "$ 2 50" + i + ".00";
//            item.balance_diff = "+ $ 100";
//            item.time = "5d9h";
//            itemList.add(item);
//        }
//    }

    private void populateList() {
        Log.d(TAG, "populateList() called");
        if (NewAppWidgetUpdateService.itemList != null)
            itemList = (ArrayList<NewAppWidgetListItem>) NewAppWidgetUpdateService.itemList
                    .clone();
        else
            itemList = new ArrayList<>();

    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_list_row);
        NewAppWidgetListItem item = itemList.get(position);
        remoteView.setTextViewText(R.id.widget_list_row_acc_name_textview, item.account);
        remoteView.setTextViewText(R.id.widget_list_row_acc_bal_textview,
                Utils.FormatCurrency(item.balance));

        remoteView.setTextViewText(R.id.widget_list_row_bal_diff_textview,
                Utils.FormatCurrency(item.balance_diff));

        remoteView.setTextViewText(R.id.widget_list_row_time_textview, item.time);

        // color views

        if (item.balance_diff > 0) {
            if (item.balance_diff > BAL_LRG) {
                setColor(remoteView, R.color.colorAccPosXLrg);
            } else if (item.balance_diff > BAL_MED) {
                setColor(remoteView, R.color.colorAccPosLrg);
            } else if (item.balance_diff > BAL_SMALL) {
                setColor(remoteView, R.color.colorAccPosMed);
            } else {
                setColor(remoteView, R.color.colorAccPosSmall);
            }
        } else {
            if (item.balance_diff < -1 * BAL_LRG) {
                setColor(remoteView, R.color.colorAccNegXLrg);
            } else if (item.balance_diff < -1 * BAL_MED) {
                setColor(remoteView, R.color.colorAccNegLrg);
            } else if (item.balance_diff < -1 * BAL_SMALL) {
                setColor(remoteView, R.color.colorAccNegMed);
            } else {
                setColor(remoteView, R.color.colorAccNegSmall);
            }
        }

        return remoteView;
    }

    private void setColor(RemoteViews remoteViews, int color) {
        /*
        remoteViews.setTextColor(R.id.widget_list_row_acc_name_textview,
                ContextCompat.getColor(mContext, color));

        remoteViews.setTextColor(R.id.widget_list_row_acc_bal_textview,
                ContextCompat.getColor(mContext, color));
        */
        remoteViews.setTextColor(R.id.widget_list_row_bal_diff_textview,
                ContextCompat.getColor(mContext, color));
        /*
        remoteViews.setTextColor(R.id.widget_list_row_time_textview,
                ContextCompat.getColor(mContext, color));
        */
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged() called");
        populateList();
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
