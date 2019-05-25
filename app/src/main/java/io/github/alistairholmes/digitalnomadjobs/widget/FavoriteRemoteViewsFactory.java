package io.github.alistairholmes.digitalnomadjobs.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import javax.inject.Inject;

import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;
import io.github.alistairholmes.digitalnomadjobs.utils.GlideApp;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static io.github.alistairholmes.digitalnomadjobs.utils.AppConstants.SELECTED_JOB_POSITION;

public class FavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    @Inject
    JobRepository jobRepository;

    private List<FavoriteJob> favoriteJobs = null;
    private CompositeDisposable compositeDisposable;
    private Context mContext;
    private final int mAppWidgetId;

    public FavoriteRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();

        compositeDisposable.add(jobRepository.getFavoriteListForWidget()
                .observeOn(Schedulers.io())
                .subscribe(favoriteJobs1 -> favoriteJobs = favoriteJobs1,
                        throwable -> Timber.e("Widget Data loading Failed!: %s", throwable.toString()),
                        () -> Timber.i("Widget Data Loading Completed!")));

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        favoriteJobs = null;
        compositeDisposable.clear();
    }

    @Override
    public int getCount() {
        return favoriteJobs == null ? 0 : favoriteJobs.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION || favoriteJobs == null || favoriteJobs.get(position) == null)
            return null;

        Timber.e("{ Data: %s, %s, %s, %s }", position, favoriteJobs.get(position).getId(),
                favoriteJobs.get(position).getPosition() != null ? favoriteJobs.get(position).getPosition() : "---",
                favoriteJobs.get(position).getCompany() != null ? favoriteJobs.get(position).getCompany() : "---");

        // Bind data to remoteViews
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        remoteViews.setTextViewText(R.id.widget_position, favoriteJobs.get(position).getPosition());
        remoteViews.setTextViewText(R.id.widget_company, favoriteJobs.get(position).getCompany());

        try {
            // TODO: Suspicious code
            System.out.println("Loading view " + position);
            AppWidgetTarget appWidgetTarget =
                    new AppWidgetTarget(mContext, R.id.widget_company_logo, remoteViews, mAppWidgetId) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                            super.onResourceReady(resource, transition); // Comment
                            //views.setImageViewBitmap(R.id.icon_from_currency, resource);
                            //appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
                        }
                    };

            GlideApp
                    .with(mContext.getApplicationContext())
                    .asBitmap()
                    .load(favoriteJobs.get(position).getCompany_logo())
                    .into(appWidgetTarget);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Bundle extras = new Bundle();
        extras.putInt(SELECTED_JOB_POSITION, position);
        Intent intent = new Intent();
        intent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_list_item, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        //return new RemoteViews(getPackageName(), R.layout.widget_list_item);
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
