package com.mm.mobilemechanic.job;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mm.mobilemechanic.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ndw6152 on 10/13/2017.
 *
 */

public class JobRequestsAdapter extends RecyclerView.Adapter<JobRequestsAdapter.JobCardsViewHolder> {

    private List<Job> mJobsList;
    private Activity mActivity;

    public JobRequestsAdapter(Activity context, List<Job> jobsList) {
        mJobsList = jobsList;
        mActivity = context;
    }


    public void showJobInformation(Activity activity, Job job) {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_main_job_dialog);
        dialog.setTitle("Job Information");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textView_dialog_job_summary);
        text.setText(job.getSummary());
        TextView text1 = (TextView) dialog.findViewById(R.id.textView_dialog_job_description);
        text1.setText("Description: \n" + job.getDescription());
        TextView text2 = (TextView) dialog.findViewById(R.id.textView_dialog_onSiteDiagnostic);
        text2.setText("On Site Diagnostic = " + job.getJobOptions().isOnSiteDiagnostic());
        TextView text3 = (TextView) dialog.findViewById(R.id.textView_dialog_carInWorkingCondition);
        text3.setText("Car in working condition = " + job.getJobOptions().isCarInWorkingCondition());
        TextView text4 = (TextView) dialog.findViewById(R.id.textView_dialog_repairCanBeDoneOnSite);
        text4.setText("Repair can be done on-site = " + job.getJobOptions().isRepairCanBeDoneOnSite());
        TextView text5 = (TextView) dialog.findViewById(R.id.textView_dialog_carPickUpDropOff);
        text5.setText("Car pick up and drop off = " + job.getJobOptions().isCarPickUpAndDropOff());

        TextView text6 = (TextView) dialog.findViewById(R.id.textView_dialog_parkingAvailable);
        text6.setText("Parking available on-site = " + job.getJobOptions().isParkingAvailable());

        dialog.show();
    }

    @Override
    public JobCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_job_request_card, parent, false);
        return new JobCardsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobCardsViewHolder holder, int position) {
        final Job job = mJobsList.get(position);
        holder.jobSummary.setText(job.getSummary());
        holder.numberOfQuotes.setText(5 + "");  // TODO get actual number of quotes
        holder.currentStatus.setText(job.getStatus().toString());

        holder.cardView.setOnClickListener(new View.OnClickListener() {  // TODO open summary and job req info
            @Override
            public void onClick(View v) {
                //implement onClick
                System.out.println("Clicked " + job.getSummary());
                showJobInformation(mActivity, job);
            }
        });
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
        CardView cardView;



        public JobCardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            hexColor = (View) itemView.findViewById(R.id.view_hex_color);
            jobSummary = (TextView) itemView.findViewById(R.id.textView_job_summary);
            numberOfQuotes = (TextView) itemView.findViewById(R.id.textView_number_of_quotes);
            currentStatus = (TextView) itemView.findViewById(R.id.textView_status);
        }
    }
}
