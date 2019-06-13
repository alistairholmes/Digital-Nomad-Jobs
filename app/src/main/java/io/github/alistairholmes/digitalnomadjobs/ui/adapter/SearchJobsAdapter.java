package io.github.alistairholmes.digitalnomadjobs.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.ui.search.SearchActivity;
import io.github.alistairholmes.digitalnomadjobs.utils.GlideApp;

// Adapter for Search results.
public class SearchJobsAdapter extends RecyclerView.Adapter<SearchJobsAdapter.SearchViewHolder> {

    private List<Job> searchJobsList;
    private final SearchActivity mParentActivity;
    private final Context context;

    public SearchJobsAdapter(SearchActivity parentActivity, Context context) {
        mParentActivity = parentActivity;
        this.context = context;
    }

    void setSearchJobsList(List<Job> searchJobsList) {
        this.searchJobsList = searchJobsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SearchViewHolder(inflater.inflate(R.layout.item_job, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bindTo(position, mParentActivity);
    }

    @Override
    public int getItemCount() {
        return searchJobsList == null ? 0 : searchJobsList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_logo)
        ImageView movieImage;
        @BindView(R.id.text_job_title)
        TextView mt;
        @BindView(R.id.text_date)
        TextView md;
        @BindView(R.id.text_company_name)

        Job m;

        private SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindTo(final int position, SearchActivity mParentActivity) {
            this.m = searchJobsList.get(position);

            // Use RoundedCornered ImageView
            if (!TextUtils.isEmpty(m.getCompany_logo())) {
                GlideApp.with(context)
                        .load(m.getCompany_logo())
                        //.placeholder(R.drawable.ic_launcher_foreground)
                        .into(movieImage);
            } else if (!TextUtils.isEmpty(m.getCompany())) {
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getRandomColor();
                TextDrawable drawable = TextDrawable
                        .builder()
                        .buildRoundRect(m.getCompany().substring(0, 1).toUpperCase(), color, 50);
                movieImage.setImageDrawable(drawable);
            }

            /*jobViewHolder.favoriteButton.isFavorite(currentJob.isFavorite());
            jobViewHolder.favoriteButton.setOnClickListener(view -> {
                jobViewHolder.favoriteButton.toggleWishlisted();
                onJobClickListener.onFavoredClicked(currentJob, !currentJob.isFavorite(), position);
            });*/
        }

    }
}