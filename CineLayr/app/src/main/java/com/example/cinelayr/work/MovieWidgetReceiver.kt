package com.example.cinelayr.work

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import com.example.cinelayr.di.KoinInitializer
import com.example.cinelayr.widget.MovieWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MovieWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        // Ensure Koin started before any widget content loads
        // Synchronous initialization to avoid null injection or Koin crash
        KoinInitializer.initSync(context)

        //  Now safe to trigger first immediate update on widget creation
        CoroutineScope(Dispatchers.Default).launch {
            MovieWidget().updateAll(context)
        }

        // Schedule periodic updates when widget is first placed
        WidgetUpdateWorker.schedulePeriodicUpdates(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Cancel updates when all widgets are removed
        WidgetUpdateWorker.cancelUpdates(context)
    }
}