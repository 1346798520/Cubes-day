package hk.hku.cs.cubesnote.ui;

import static android.content.ContentValues.TAG;
import static java.lang.Math.round;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEvent;
import hk.hku.cs.cubesnote.utils.FileIO;

public class widgetProvider extends AppWidgetProvider

{
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

    }


    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);}

    private ArrayList<CubeEvent> eventList;
    private ArrayList<String> infoList;
    private ArrayList<String> subList;
    private Calendar today;
    private Calendar monthLater;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        infoList = new ArrayList<String>();
        subList = new ArrayList<String>();
        today = Calendar.getInstance();
        monthLater = Calendar.getInstance();
        monthLater.add(Calendar.DAY_OF_MONTH, 30);
        eventList = FileIO.readEventsWithin(context, today, monthLater);
        for (CubeEvent event : eventList) {
            if (event.getTreemapConfig() != null) {
                Float emergency = (float) event.getTreemapConfig().getEmergency();
                Float importance = (float) event.getTreemapConfig().getImportance();
                String eventName = event.getTitle();
                if (emergency + importance > 5) {
                    infoList.add(eventName);
                    subList.add("Emergency:" + emergency + "  Importance:" + importance);

                }
            }
        }
        Log.i(TAG, "onUpdate: findtextviews");
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget);
            if (infoList.size() >0){
            views.setTextViewText(R.id.widgetText1,infoList.get(0));
            views.setTextViewText(R.id.widgetSub1,subList.get(0));
            }
            if (infoList.size() >1){
            views.setTextViewText(R.id.widgetText2,infoList.get(1));
            views.setTextViewText(R.id.widgetSub2,subList.get(1));
            }
            if (infoList.size() >2){
            views.setTextViewText(R.id.widgetText3,infoList.get(2));
            views.setTextViewText(R.id.widgetSub3,subList.get(2));
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }}



