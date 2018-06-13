package io.github.alistairholmes.digitalnomadjobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private List<Job> jobs;

    private static final String LOG_TAG = JobAdapter.class.getName();



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // Provide a reference to the views for each data item
    // Provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobTitle;
        public TextView companyName;
        public TextView datePosted;
        public View layout;


        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            jobTitle = (TextView) layout.findViewById(R.id.textView_job_title);
            companyName = (TextView) layout.findViewById(R.id.textView_company_name);
            datePosted = (TextView) layout.findViewById(R.id.textView_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    // Pass in a the Job constructor
    public JobAdapter(List<Job> job, OnItemClickListener listener) {
        jobs=job.subList(1, job.size());
        this.mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.job_item_row, parent,false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get element from the dataset at this position
        // Replace the contents of the view with that element
        Job currentJob = jobs.get(position);

        String mJobTitle = currentJob.getJobTitle();
        holder.jobTitle.setText(mJobTitle);

        String mCompanyName = currentJob.getCompanyName();
        holder.companyName.setText(mCompanyName);

        Context context = holder.datePosted.getContext();

        Date mDatePosted = currentJob.getDatePosted();
        String dateFormat = formatDayMonth(context, mDatePosted);
        holder.datePosted.getContext();
        holder.datePosted.setText(dateFormat);
    }

    @Nullable
    public static String formatDayMonth(@NonNull Context context, @Nullable Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(
                context.getString(R.string.format_date),
                Locale.US);
        return sdf.format(date);
    }

    public int getItemCount() {
        return jobs.size();
    }
}
