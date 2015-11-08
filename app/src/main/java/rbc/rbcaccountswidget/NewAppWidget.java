package rbc.rbcaccountswidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link NewAppWidgetConfigureActivity NewAppWidgetConfigureActivity}
 */
public class NewAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            NewAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = NewAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        views.setBitmap(R.id.chartImageView, "setImageBitmap", GetChartBitmap(context));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static Bitmap GetChartBitmap(Context context){

        XYPlot plot = new XYPlot(context, "");
        //plot.getLayoutParams().height = 100;
        //plot.getLayoutParams().width = 100;
        plot.measure(550, 250);
        plot.layout(0, 0, 550, 250);
        plot.setDrawingCacheEnabled(true);
        //plot.setBackgroundColor(Color.GREEN);
        plot.getTitleWidget().setVisible(false);
        plot.getLegendWidget().setVisible(false);

        plot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getBackgroundPaint().setColor(Color.argb(30,255,255,255));


        // display a week day for each points
        plot.setDomainValueFormat(new Format() {

            private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int day  = ((Number) obj).intValue() +1 ;
                cal.add(Calendar.DATE, day); //minus number would decrement the days

                return dateFormat.format( cal.getTime(), toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;

            }
        });
        // Create a couple arrays of y-values to plot:
        // Number[] series1Numbers = {900, 850, 1450, 1300, 1350};

        // random amount of money just for show
        Number[] series1Numbers = new Number[5];
        for(int i = 0; i < series1Numbers.length; i++) {
            series1Numbers[i] = (Number)(Math.random()*300 + 800);
        }

        //plot.setRangeTopMax(1500);
        //plot.setRangeBottomMin(800);

        plot.setDomainStep(XYStepMode.SUBDIVIDE, series1Numbers.length);
        //plot.setDomainLowerBoundary(700, BoundaryMode.FIXED);
        
        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Series1");                             // Set the display title of the series

        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 56, 163),                   // line color
                Color.rgb(243, 209, 77),                   // point color
                null, null);                                  // fill color (none)
        series1Format.getLinePaint().setStrokeWidth(3);

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);


        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);

        return plot.getDrawingCache();
    }
}

