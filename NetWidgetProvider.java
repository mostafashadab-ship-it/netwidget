package com.example.netwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class NetWidgetProvider extends AppWidgetProvider {

    static final String PREFS = "netwidget";
    static final String KEY_MODE = "mode"; // 0 = 5G/4G, 1 = 5G/4G/2G

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        refresh(context, manager, ids);
    }

    static void refreshAll(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, NetWidgetProvider.class));
        refresh(context, manager, ids);
    }

    static void refresh(Context context, AppWidgetManager manager, int[] ids) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int mode = sp.getInt(KEY_MODE, 0);

        // The mode shown is the one you should pick on the next tap.
        String title = mode == 0 ? "5G / 4G" : "5G / 4G / 2G";
        String hint = mode == 0 ? "Tap, then choose: NR/LTE" : "Tap, then choose: NR/LTE/GSM";

        Intent tap = new Intent(context, TapActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                context, 0, tap,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        for (int id : ids) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
            rv.setTextViewText(R.id.title, title);
            rv.setTextViewText(R.id.hint, hint);
            rv.setOnClickPendingIntent(R.id.root, pi);
            manager.updateAppWidget(id, rv);
        }
    }
}
