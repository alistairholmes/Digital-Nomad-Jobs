package io.github.alistairholmes.digitalnomadjobs.ui.favorite;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.github.alistairholmes.digitalnomadjobs.di.Injectable;
import io.github.alistairholmes.digitalnomadjobs.ui.adapter.FavoriteAdapter;
import io.github.alistairholmes.digitalnomadjobs.ui.viewholder.FavoriteViewHolder;
import io.github.alistairholmes.digitalnomadjobs.utils.JobListItemDecoration;
import io.github.alistairholmes.digitalnomadjobs.utils.ViewUtil;

public class FavoriteFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.favorite_container)
    ConstraintLayout constraintLayout;
    @BindView(R.id.recycler_favorites_list)
    RecyclerView favoriteRecyclerView;
    @BindView(R.id.item_favorite_state)
    View noFavorites;

    private FavoriteViewModel favoriteViewModel;
    private FavoriteAdapter.OnFavoriteJobClickListener onFavoriteJobClickListener;
    private FavoriteAdapter favoriteAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Unbinder unbinder;
    private Paint paint = new Paint();

    static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    void setOnFavoriteJobClickListener(Activity activity) {
        AndroidSupportInjection.inject(this);
        onFavoriteJobClickListener = (FavoriteAdapter.OnFavoriteJobClickListener) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(FavoriteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        favoriteRecyclerView.setLayoutManager(linearLayoutManager);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.job_item_spacing_small);
        favoriteRecyclerView.addItemDecoration(new JobListItemDecoration(smallPadding));
        favoriteRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(favoriteRecyclerView);

        favoriteAdapter = new FavoriteAdapter(getActivity(), onFavoriteJobClickListener);
        favoriteViewModel.favoriteJobLiveData.observe(this, favoriteJobs -> {
            if (favoriteJobs != null && favoriteJobs.size() > 0) {
                noFavorites.setVisibility(View.GONE);
                favoriteAdapter.submitList(favoriteJobs);
            } else {
                favoriteRecyclerView.setVisibility(View.GONE);
                noFavorites.setVisibility(View.VISIBLE);
            }
        });
        favoriteRecyclerView.setAdapter(favoriteAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFavoriteJobClickListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private final ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper
            .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (viewHolder instanceof FavoriteViewHolder) {

                final int position = viewHolder.getAdapterPosition();
                final FavoriteJob favoriteJob = favoriteAdapter.removeItem(position);

                favoriteViewModel.removeFavorite(favoriteJob);
                favoriteAdapter.notifyItemRemoved(position);
                favoriteAdapter.notifyDataSetChanged();
                //favoriteMoviesAdapter.notifyItemRangeChanged(position, favMoviesList.size());

                // Show SnackBar with Movie name and Undo option
                Snackbar snackbar = Snackbar.make(constraintLayout,
                        favoriteJob.getPosition() + " is removed from Favorites!",
                        Snackbar.LENGTH_LONG);

                snackbar.setAction("UNDO", view -> {
                    // Undo is selected, restore the deleted item
                    favoriteViewModel.addFavorite(favoriteJob);
                    favoriteAdapter.restoreItem(favoriteJob, position);
                    if (favoriteAdapter.getItemCount() != 0) {
                        favoriteRecyclerView.setVisibility(View.VISIBLE);
                        noFavorites.setVisibility(View.GONE);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;
                paint.setAntiAlias(true);
                paint.setColor(Color.parseColor("#388E3C"));

                if (dX > 0) {
                    RectF background = new RectF((float) itemView.getLeft(),
                            (float) itemView.getTop(), dX, (float) itemView.getBottom());
                    RectF iconDest = new RectF((float) itemView.getLeft() + width,
                            (float) itemView.getTop() + width,
                            (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                    c.drawRect(background, paint);
                    c.drawBitmap(ViewUtil.getBitmap(getActivity().getDrawable(R.drawable.ic_delete)), null, iconDest, paint);
                } else {
                    RectF background = new RectF((float) itemView.getRight() + dX,
                            (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    RectF iconDest = new RectF((float) itemView.getRight() - 2 * width,
                            (float) itemView.getTop() + width, (float) itemView.getRight() - width,
                            (float) itemView.getBottom() - width);
                    c.drawRect(background, paint);
                    c.drawBitmap(ViewUtil.getBitmap(getActivity().getDrawable(R.drawable.ic_delete)), null, iconDest, paint);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


}
