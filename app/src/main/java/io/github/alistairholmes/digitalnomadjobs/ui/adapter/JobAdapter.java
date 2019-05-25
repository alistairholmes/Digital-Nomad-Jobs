package io.github.alistairholmes.digitalnomadjobs.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.ui.viewholder.JobViewHolder;
import io.github.alistairholmes.digitalnomadjobs.utils.GlideApp;
import timber.log.Timber;

import static io.github.alistairholmes.digitalnomadjobs.utils.ViewUtil.formatDayMonth;

public class JobAdapter extends RecyclerView.Adapter<JobViewHolder> {

    private final AsyncListDiffer<Job> mDiffer = new AsyncListDiffer(this, DIFF_CALLBACK);
    private final OnJobClickListener onJobClickListener;
    private Context context;

    public interface OnJobClickListener {
        void onJobClick(Job job);

        void onFavoredClicked(@NonNull final Job job, boolean isFavorite, int position);
    }

    public JobAdapter(Context context, OnJobClickListener onJobClickListener) {
        this.onJobClickListener = onJobClickListener;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public void submitList(List<Job> list) {
        mDiffer.submitList(list);
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item_row, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder jobViewHolder, int position) {

        final Job currentJob = mDiffer.getCurrentList().get(position);

        jobViewHolder.itemView.setOnClickListener(view -> {
            if (onJobClickListener != null) onJobClickListener.onJobClick(currentJob);
        });

        jobViewHolder.jobTitle.setText(currentJob.getPosition());
        jobViewHolder.companyName.setText(currentJob.getCompany());
        String dateFormat = formatDayMonth(context, currentJob.getDate());
        jobViewHolder.datePosted.setText(dateFormat);

        // TODO 3 Use RoundedCornered ImageView
        if (!TextUtils.isEmpty(currentJob.getCompany_logo())) {
            GlideApp.with(context)
                    .load(currentJob.getCompany_logo())
                    //.placeholder(R.drawable.ic_launcher_foreground)
                    //.circleCrop()
                    .into(jobViewHolder.companyLogo);
        } else if (!TextUtils.isEmpty(currentJob.getCompany())) {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable
                    .builder()
                    .buildRoundRect(currentJob.getCompany().substring(0, 1).toUpperCase(), color, 50);
            //.buildRound(currentJob.getCompany().substring(0, 1).toUpperCase(), color);
            jobViewHolder.companyLogo.setImageDrawable(drawable);
        }

        jobViewHolder.favoriteButton.isFavorite(currentJob.isFavorite());

        jobViewHolder.favoriteButton.setOnClickListener(view -> {
            jobViewHolder.favoriteButton.toggleWishlisted();
            onJobClickListener.onFavoredClicked(currentJob, !currentJob.isFavorite(), position);
        });
    }

    private static final DiffUtil.ItemCallback DIFF_CALLBACK = new DiffUtil.ItemCallback<Job>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull Job oldUser, @NonNull Job newUser) {
            // User properties may have changed if reloaded from the DB, but ID is fixed
            return oldUser.getId() == newUser.getId();
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull Job oldUser, @NonNull Job newUser) {
            // No need to check the equality for all User fields ;
            // just check the equality for fields that change the display of your item.
            return oldUser.getId() == newUser.getId()
                    && oldUser.isFavorite() == newUser.isFavorite();

        }
    };

}
