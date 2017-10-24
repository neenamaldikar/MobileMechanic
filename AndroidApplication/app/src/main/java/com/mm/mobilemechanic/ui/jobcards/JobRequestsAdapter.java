package com.mm.mobilemechanic.ui.jobcards;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mm.mobilemechanic.R;
import com.mm.mobilemechanic.user.Job;

import java.util.List;

/**
 * Created by ndw6152 on 10/13/2017.
 *
 */

public class JobRequestsAdapter extends RecyclerView.Adapter<JobRequestsAdapter.JobCardsViewHolder> {

    private List<Job> mJobsList;

    public JobRequestsAdapter(List<Job> jobsList) {
        mJobsList = jobsList;
    }

    @Override
    public JobCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_job_request_card, parent, false);
        return new JobCardsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobCardsViewHolder holder, int position) {
        Job job = mJobsList.get(position);
        holder.jobSummary.setText(job.getSummary());
        holder.numberOfQuotes.setText(5 + "");  // TODO get actual number of quotes
        holder.currentStatus.setText(job.getStatus().toString());
    }

    @Override
    public int getItemCount() {
        return mJobsList.size();
    }

    class JobCardsViewHolder extends RecyclerView.ViewHolder {
        View hexColor;
        TextView jobSummary;
        TextView numberOfQuotes;
        TextView currentStatus;
        CardView cv;

        public JobCardsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            hexColor = (View) itemView.findViewById(R.id.view_hex_color);
            jobSummary = (TextView) itemView.findViewById(R.id.textView_job_summary);
            numberOfQuotes = (TextView) itemView.findViewById(R.id.textView_number_of_quotes);
            currentStatus = (TextView) itemView.findViewById(R.id.textView_status);
        }
    }
}
