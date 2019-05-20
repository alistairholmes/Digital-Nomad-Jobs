package io.github.alistairholmes.digitalnomadjobs.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import javax.inject.Inject;

import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    @Inject
    JobRepository jobRepository;

    private List<FavoriteJob> favoriteJobs = null;
    private CompositeDisposable compositeDisposable;
    private Context mContext;

    public FavoriteRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        //mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
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
        int priceChangeDrawableId;

        // Data checks OK //
//        Timber.e("{ Data: %s, %s, %s, %s, %s }", position,
//                data.get(position).getStockSymbol() != null ? data.get(position).getStockSymbol(): "---",
//                data.get(position).getStockPrice() != null ? data.get(position).getStockPrice(): "---",
//                data.get(position).getChangeInPercent() != null ? data.get(position).getChangeInPercent(): "---",
//                data.get(position).IsUp());
        // Data checks OK //

        // Bind data to remoteViews
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
//        remoteViews.setTextViewText(R.id.widget_stock_symbol,
//                data.get(position).getStockSymbol() != null ? data.get(position).getStockSymbol(): "---");
//        remoteViews.setTextViewText(R.id.widget_bid_price,
//                data.get(position).getStockPrice() != null ? data.get(position).getStockPrice(): "---");
//        remoteViews.setTextViewText(R.id.widget_percent_change,
//                data.get(position).getChangeInPercent() != null ? data.get(position).getChangeInPercent(): "---");
//
//
//        if (data.get(position).IsUp()) {
//            priceChangeDrawableId = R.drawable.percent_change_pill_green;
//        } else {
//            priceChangeDrawableId = R.drawable.percent_change_pill_red;
//        }
//
//        remoteViews.setInt(R.id.widget_percent_change, "setBackgroundResource", priceChangeDrawableId);

                /*
                    Now handle the unique part for each element, calling 'setOnClickFillInIntent' to
                    fill in pendingTemplate appropriately.
                 */
        Bundle extras = new Bundle();
        //extras.putInt(Constants.SELECTED_STOCK_POSITION, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

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
