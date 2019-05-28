package io.github.alistairholmes.digitalnomadjobs.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.TaskStackBuilder;

import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.ui.jobdetail.DetailActivity;
import io.github.alistairholmes.digitalnomadjobs.ui.jobs.JobActivity;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteJobsAppWidget extends AppWidgetProvider {

    public static final String FAVORITE_DATA_UPDATED
            = "io.github.alistairholmes.digitalnomadjobs.widget.FAVORITE_DATA_UPDATED";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            // This will take us to RemoteViewsFactory onDataSetChanged method
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // TODO: Come back here for any issues
        for (int appWidgetId : appWidgetIds) {
            // RemoteViews describe view hierarchy in another process
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list);

            Intent intent = new Intent(context, JobActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_header, pendingIntent);

            Intent clickIntentTemplate = new Intent(context, DetailActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

            // Set up the collection. This takes us to 'onCreate' in RemoteViewsFactory in the
            // FavoriteJobWidgetService, and then through the RemoteViewsFactory life-cycle events
            remoteViews.setRemoteAdapter(R.id.widget_list,
                    new Intent(context, FavoriteJobWidgetService.class));

            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            remoteViews.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Finished setting up remoteViews except looking up and binding data (background thread operation)
            // Do that in DetailWidgetRemoteViewsService. We can even do it here with observables.
            // This invariably calls the 'onDataSetChanged' method in RemoteViewsFactory

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

