package io.github.alistairholmes.digitalnomadjobs.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FavoriteJobWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}