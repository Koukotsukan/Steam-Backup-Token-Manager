package com.qianshui.steamguardhelper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class TokenWidget extends AppWidgetProvider {
    private static final String CLICK_ACTION = "com.qianshui.steamguardhelperwidget.CLICK";
    List<String> codeList;
    int currentProgress;
    static boolean refreshLock;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.token_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        refreshLock = false;
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(CLICK_ACTION) && !refreshLock) {
            refreshLock = true;
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.token_widget);
            codeList = new ArrayList<>();
            SharedPreferences preferencesDataList = context.getSharedPreferences("codeList", 0);
            int codeNumbers = preferencesDataList.getInt("codeNumbers", 0);
            for (int i = 0; i < codeNumbers; i++) {
                String code = preferencesDataList.getString("item_" + i, null);
                codeList.add(code);
            }
            currentProgress = 15;
            CountDownTimer mCountDownTimer = new CountDownTimer(15000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    currentProgress--;
                    Log.d("DATA", "Current Progress: " + currentProgress);
                    remoteViews.setProgressBar(R.id.progressBar, 15, currentProgress, false);
                    appWidgetManager.updateAppWidget(new ComponentName(context, TokenWidget.class), remoteViews);
                }

                @Override
                public void onFinish() {
                    currentProgress = 15;
                    remoteViews.setTextViewText(R.id.appwidget_text, "点击获取");
                    remoteViews.setProgressBar(R.id.progressBar, 15, 15, false);
                    refreshLock = false;
                    appWidgetManager.updateAppWidget(new ComponentName(context, TokenWidget.class), remoteViews);

                }
            };

            if (codeNumbers != 0) {
                Toast.makeText(context, "已更新，剩余容量: " + codeList.size(), Toast.LENGTH_SHORT).show();
                remoteViews.setTextViewText(R.id.appwidget_text, codeList.get(0).substring(0, 7));
                codeList.remove(0);
                SharedPreferences.Editor editor = context.getSharedPreferences("codeList", 0).edit();
                editor.putInt("codeNumbers", codeList.size());
                for (int i = 0; i < codeList.size(); i++) {
                    editor.putString("item_" + i, codeList.get(i));
                }
                editor.commit();
                mCountDownTimer.start();
            } else {
                remoteViews.setTextViewText(R.id.appwidget_text, "没有数据");
                Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
            }


            appWidgetManager.updateAppWidget(new ComponentName(context, TokenWidget.class), remoteViews);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.token_widget);
            Intent intentClick = new Intent();
            intentClick.setClass(context, TokenWidget.class);
            intentClick.setAction(CLICK_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}