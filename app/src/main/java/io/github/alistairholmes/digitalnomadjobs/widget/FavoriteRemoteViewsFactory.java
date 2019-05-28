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

import com.bumptech.glide.Glide;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static io.github.alistairholmes.digitalnomadjobs.utils.AppConstants.SELECTED_JOB_POSITION;

public class FavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private JobRepository jobRepository;
    private List<FavoriteJob> favoriteJobs;
    private CompositeDisposable compositeDisposable;
    private Context mContext;
    private final int mAppWidgetId;

    FavoriteRemoteViewsFactory(Context context, Intent intent, JobRepository jobRepository) {
        mContext = context;
        mAppWidgetId = intent
                .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.jobRepository = jobRepository;
    }

    @Override
    public void onCreate() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();

        favoriteJobs = jobRepository.getFavoriteListForWidget().blockingLast();
        Timber.e(String.valueOf(favoriteJobs.size()));

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
            Bitmap bitmap = Glide.with(mContext.getApplicationContext())
                    .asBitmap()
                    .placeholder(R.color.textPrimary)
                    .load(favoriteJobs.get(position).getCompany_logo())
                    .submit(512, 512)
                    .get();
            remoteViews.setImageViewBitmap(R.id.widget_company_logo, bitmap);
        } catch (Exception e) {
            e.toString();
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
        return new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
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
