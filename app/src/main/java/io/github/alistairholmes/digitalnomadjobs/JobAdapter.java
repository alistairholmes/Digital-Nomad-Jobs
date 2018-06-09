package io.github.alistairholmes.digitalnomadjobs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private List<Job> jobs;

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
        jobs = job;
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

        String mDatePosted = currentJob.getDatePosted();
        holder.datePosted.setText(mDatePosted);


    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}
