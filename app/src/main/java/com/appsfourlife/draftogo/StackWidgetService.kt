package com.appsfourlife.draftogo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelFavoriteTemplate


class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return StackRemoteViewsFactory(applicationContext, intent)
    }
}

class StackRemoteViewsFactory(applicationContext: Context, intent: Intent?) :
    RemoteViewsService.RemoteViewsFactory {

    private var listOfTemplates: MutableList<ModelFavoriteTemplate> = App.databaseApp.daoApp.getAllFavoriteTemplates() as MutableList<ModelFavoriteTemplate>
    private val mContext = applicationContext

    override fun onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

    }

    override fun onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        listOfTemplates.clear()
    }

    override fun getCount(): Int {
        return listOfTemplates.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setTextViewText(R.id.widget_item, listOfTemplates[position].query)
        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        val extras = Bundle()
        extras.putInt(MyWidgetProvider.WIDGET_ITEM_POSITION, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent)
        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.
//        try {
//            println("Loading view $position")
//            Thread.sleep(500)
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
        // Return the remote views object.
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        listOfTemplates = App.databaseApp.daoApp.getAllFavoriteTemplates() as MutableList<ModelFavoriteTemplate>
//        listOfTemplates = App.databaseApp.daoApp.getAllFavoriteTemplates()
//        getCount()
//        onCreate()
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }
}