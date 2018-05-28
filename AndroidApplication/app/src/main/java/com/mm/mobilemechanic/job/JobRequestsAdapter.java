package com.mm.mobilemechanic.job;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mm.mobilemechanic.MainActivity;
import com.mm.mobilemechanic.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ndw6152 on 10/13/2017.
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
        dialog.setContentView(R.layout.dialog_display_job);
        dialog.setTitle("Job Information");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textView_dialog_job_summary);
        text.setText(job.getSummary());
        TextView text1 = (TextView) dialog.findViewById(R.id.textView_dialog_job_description);
        text1.setText("Description: \n" + job.getDescription());
        TextView text2 = (TextView) dialog.findViewById(R.id.textView_dialog_onSiteDiagnostic);
        text2.setText("On-site diagnostic = " + job.getJobOptions().isOnSiteDiagnostic());
        TextView text3 = (TextView) dialog.findViewById(R.id.textView_dialog_carInWorkingCondition);
        text3.setText("Car in working condition = " + job.getJobOptions().isCarInWorkingCondition());
        TextView text4 = (TextView) dialog.findViewById(R.id.textView_dialog_repairCanBeDoneOnSite);
        text4.setText("Repair can be done on-site = " + job.getJobOptions().isRepairCanBeDoneOnSite());
        TextView text5 = (TextView) dialog.findViewById(R.id.textView_dialog_carPickUpDropOff);
        text5.setText("Car pick up and drop off = " + job.getJobOptions().isCarPickUpAndDropOff());

        TextView text6 = (TextView) dialog.findViewById(R.id.textView_dialog_parkingAvailable);
        text6.setText("Parking available on-site = " + job.getJobOptions().isParkingAvailable());

        ImageView imageView = (ImageView) dialog.findViewById(R.id.imageview_dialog_jobimage);

        if (job.getImages().length != 0) {
            if (mActivity instanceof MainActivity) {
                ((MainActivity) mActivity).downloadImage(job.getJob_id(), job.getImages()[0], imageView);
            }
        }
        dialog.show();


    }


    @Override
    public JobCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_job_request_card, parent, false);
        return new JobCardsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobCardsViewHolder holder, final int position) {
        final Job job = mJobsList.get(position);
        holder.jobSummary.setText(job.getSummary());
        holder.numberOfQuotes.setText(0 + "");  // TODO get actual number of quotes
        holder.currentStatus.setText(job.getStatus().toString());

        holder.cardView.setOnClickListener(new View.OnClickListener() {  // TODO open summary and job req info
            @Override
            public void onClick(View v) {
                //implement onClick
                System.out.println("Clicked " + job.getSummary());
                showJobInformation(mActivity, job);
            }
        });
        holder.imageButton.setFocusable(true);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            // TODO open summary and job req info
            @Override
            public void onClick(View v) {
                showPopup(v, job, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJobsList.size();
    }

    class JobCardsViewHolder extends RecyclerView.ViewHolder {
        ImageView hexColor;
        TextView jobSummary;
        TextView numberOfQuotes;
        TextView currentStatus;
        CardView cardView;
        ImageButton imageButton;

        public JobCardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            hexColor = (ImageView) itemView.findViewById(R.id.view_hex_color);
            jobSummary = (TextView) itemView.findViewById(R.id.textView_job_summary);
            numberOfQuotes = (TextView) itemView.findViewById(R.id.textView_number_of_quotes);
            currentStatus = (TextView) itemView.findViewById(R.id.textView_status);
            imageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
        }
    }


    public void showPopup(final View view, final Job job, final int position) {

        final PopupMenu popup = new PopupMenu(mActivity, view);
        MenuInflater inflater = popup.getMenuInflater();


        inflater.inflate(R.menu.activity_main_card_actions, popup.getMenu());
        if (mActivity instanceof MainActivity) {

            if (((MainActivity) mActivity).isMechanic && job.getStatus() == JobStatus.SUBMITTED) { // if is mechanic show the send quote option
                popup.getMenu().getItem(0).setVisible(false); // delete job
                popup.getMenu().getItem(1).setVisible(false); // edit job
                popup.getMenu().getItem(2).setVisible(true); // send quote
            } else {
                popup.getMenu().getItem(0).setVisible(true);
                popup.getMenu().getItem(1).setVisible(true);
                popup.getMenu().getItem(2).setVisible(false);
            }
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_card_edit:
                        if (mActivity instanceof MainActivity) {
                            ((MainActivity) mActivity).editJobOnClick(view, job);
                        }

                        return true;
                    case R.id.menu_card_delete:
                        if (mActivity instanceof MainActivity) {
                            ((MainActivity) mActivity).deleteJob(job.getJob_id(), position);
                        }
                        return true;

                    case R.id.menu_card_sendquote:
                        if (mActivity instanceof MainActivity) {
                            ((MainActivity) mActivity).submitJobQuote();
                        }
                        return true;


                    default:
                        return false;
                }
            }
        });
        popup.show();
    }


}

