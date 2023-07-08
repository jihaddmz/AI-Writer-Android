package com.appsfourlife.draftogo

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.appsfourlife.draftogo.helpers.Helpers


class MyWidgetProvider : AppWidgetProvider() {

    companion object {
        const val LAUNCH_TEMPLATE_ACTION = "com.appsfourlife.draftogo.widget.LAUNCH_TEMPLATE_ACTION"
        const val WIDGET_ITEM_POSITION = "com.appsfourlife.draftogo.widget.WIDGET_ITEM_POSITION"
        private val listOfWidgetsScreen: MutableList<String> = mutableListOf(App.getTextFromString(R.string.chat), App.getTextFromString(R.string.art))

    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onReceive(context: Context?, intent: Intent) {
        val mgr = AppWidgetManager.getInstance(context)
        if (intent.action == LAUNCH_TEMPLATE_ACTION) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            val viewIndex = intent.getIntExtra(WIDGET_ITEM_POSITION, 0)
            Helpers.logD("view index ${viewIndex}")
            val actionIntent  = Intent(context, MainActivity::class.java)
            actionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            actionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            actionIntent.putExtra("widgetScreenClicked", listOfWidgetsScreen[viewIndex])
            context?.startActivity(actionIntent)
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // update each of the widgets with the remote adapter
        for (i in appWidgetIds.indices) {

            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i])
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val rv = RemoteViews(context.packageName, R.layout.widget_layout)
            rv.setRemoteAdapter(R.id.stack_view, intent)
            rv.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, MyWidgetProvider::class.java)
            toastIntent.action = LAUNCH_TEMPLATE_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i])
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val toastPendingIntent = PendingIntent.getBroadcast(
                context, 0, toastIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}