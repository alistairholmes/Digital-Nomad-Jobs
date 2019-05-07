package io.github.alistairholmes.digitalnomadjobs.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.ui.viewholder.JobViewHolder;
import io.github.alistairholmes.digitalnomadjobs.utils.GlideApp;

import static io.github.alistairholmes.digitalnomadjobs.utils.ViewUtil.formatDayMonth;

public class JobAdapter extends RecyclerView.Adapter<JobViewHolder> {

    private OnJobClickListener onJobClickListener;
    private List<Job> jobs;
    private Context context;

    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    public JobAdapter(Context context, List<Job> jobs, OnJobClickListener onJobClickListener) {
        this.jobs = jobs;
        this.onJobClickListener = onJobClickListener;
        this.context = context;
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
        Job currentJob = jobs.get(position);

        jobViewHolder.jobTitle.setText(currentJob.getPosition());
        jobViewHolder.companyName.setText(currentJob.getCompany());
        String dateFormat = formatDayMonth(context, currentJob.getDate());
        jobViewHolder.datePosted.setText(dateFormat);

        if (!TextUtils.isEmpty(currentJob.getCompany_logo())) {
            GlideApp.with(context)
                    .load(currentJob.getCompany_logo())
                    //.placeholder(R.drawable.ic_launcher_foreground)
                    //.fitCenter()
                    .into(jobViewHolder.companyLogo);
        } else if (!TextUtils.isEmpty(currentJob.getCompany())){
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(currentJob.getCompany().substring(0, 1).toUpperCase(), color);
            jobViewHolder.companyLogo.setImageDrawable(drawable);
        }

        jobViewHolder.itemView.setOnClickListener(view -> {
            if (onJobClickListener != null) {
                onJobClickListener.onJobClick(currentJob);
            }
        });
    }

    public int getItemCount() {
        return (jobs != null) ? jobs.size() : 0;
    }

}
