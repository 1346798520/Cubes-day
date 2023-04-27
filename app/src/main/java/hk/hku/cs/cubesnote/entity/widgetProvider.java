package hk.hku.cs.cubesnote.entity;

import static android.content.ContentValues.TAG;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import hk.hku.cs.cubesnote.R;
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
        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final   int  N  =  appWidgetIds.length;


        for  ( int  i  =   0 ; i  <  N; i ++ )
        {
            int  appWidgetId  =  appWidgetIds[i];
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);


            rv.setImageViewResource(R.id.imageView2,R.drawable.cubes);
            // TODO:set update of widget

            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }


}
