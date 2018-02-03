package com.mm.mobilemechanic.job;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mm.mobilemechanic.JobAddImagesActivity;
import com.mm.mobilemechanic.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ndw6152 on 10/13/2017.
 */

public class JobAddImageAdapter extends RecyclerView.Adapter<JobAddImageAdapter.JobCardsViewHolder> {

    private List<Uri> mImageList;
    private Activity mActivity;
    int mPosition = 0;


    public JobAddImageAdapter(Activity context, List<Uri> imageList) {
        mImageList = imageList;
        mActivity = context;
    }


    @Override
    public JobCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_job_image_card, parent, false);
        return new JobCardsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JobCardsViewHolder holder, int position) {
        mPosition = position;
        holder.imageView_job_image.setOnClickListener(new View.OnClickListener() {  // TODO open summary and job req info
            @Override
            public void onClick(View v) {


                if (mActivity instanceof JobAddImagesActivity) {
                    ((JobAddImagesActivity) mActivity).recyclerViewListClicked(v, mPosition);
                }


            }
        });

        holder.imageView_job_image_delete.setVisibility(View.GONE);

        holder.imageView_job_image_delete.setOnClickListener(new View.OnClickListener() {  // TODO open summary and job req info
            @Override
            public void onClick(View v) {


                if (mActivity instanceof JobAddImagesActivity) {
                    ((JobAddImagesActivity) mActivity).deleteImage(mPosition);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    class JobCardsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_job_image, imageView_job_image_delete;
        CardView cardView;


        public JobCardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imageView_job_image = (ImageView) itemView.findViewById(R.id.imageView_job_image);

            imageView_job_image_delete = (ImageView) itemView.findViewById(R.id.imageView_job_image_delete);
            imageView_job_image_delete.setVisibility(View.GONE);

        }
    }
}
