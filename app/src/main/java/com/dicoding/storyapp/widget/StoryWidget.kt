package com.dicoding.storyapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.dicoding.storyapp.R
import com.dicoding.storyapp.view.main.MainActivity

class StoryWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == OPEN_APP_ACTION) {
                val launchIntent = Intent(context, MainActivity::class.java)
                launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(launchIntent)
            }
        }
    }

    companion object {
        private const val OPEN_APP_ACTION = "com.dicoding.picodiploma.OPEN_APP_ACTION"
        const val EXTRA_ITEM = "com.dicoding.picodiploma.EXTRA_ITEM"

        @Suppress("DEPRECATION")
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.story_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val openAppIntent = Intent(context, StoryWidget::class.java).apply {
                action = OPEN_APP_ACTION
            }
            val openAppPendingIntent = PendingIntent.getBroadcast(context, 0, openAppIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else 0
            )
            views.setOnClickPendingIntent(R.id.widget_layout, openAppPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}