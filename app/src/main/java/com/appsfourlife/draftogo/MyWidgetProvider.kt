package com.appsfourlife.draftogo

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews


class MyWidgetProvider : AppWidgetProvider() {

    companion object {
        const val LAUNCH_TEMPLATE_ACTION = "com.appsfourlife.draftogo.widget.LAUNCH_TEMPLATE_ACTION"
        const val WIDGET_ITEM_POSITION = "com.appsfourlife.draftogo.widget.WIDGET_ITEM_POSITION"
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
//            HelperUI.showToast(msg = App.databaseApp.daoApp.getAllFavoriteTemplates()[viewIndex].query)
            val actionIntent  = Intent(context, MainActivity::class.java)
            actionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            actionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            actionIntent.putExtra("templateClickedQuery", App.databaseApp.daoApp.getAllFavoriteTemplates()[viewIndex].query)
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
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}