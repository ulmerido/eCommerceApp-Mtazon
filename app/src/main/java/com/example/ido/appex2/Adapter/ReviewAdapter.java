package com.example.ido.appex2.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.Review;
import com.example.ido.appex2.entities.User;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>
{
    private final String           TAG = "ReviewAdapter";
    private List<Review>           m_ReviewList;
    private User                   m_User;
    private LayoutInflater         m_inflater;


    public ReviewAdapter(List<Review> i_Review, User i_User)
    {
        this.m_ReviewList = i_Review;
        this.m_User = i_User;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        Log.e(TAG, "onCreateViewHolder() >>");

        m_inflater = LayoutInflater.from(parent.getContext());
        View itemView = m_inflater.inflate(R.layout.item_card_userreview, parent, false);

        Log.e(TAG, "onCreateViewHolder() <<");
        return new ReviewHolder(parent.getContext(), itemView);
    }



    @Override
    public void onBindViewHolder(final ReviewHolder holder, int position)
    {
        Log.e(TAG,"onBindViewHolder() >> " + position);
        final Review review = m_ReviewList.get(position);
        holder.populate(review);

        Log.e(TAG,"onBindViewHolder() << "+ position);
    }

    @Override
    public int getItemCount()
    {
        return m_ReviewList.size();
    }


    public class ReviewHolder extends RecyclerView.ViewHolder
    {

        public TextView m_ReviewBody;

        public TextView m_ReviewHeader;


        public TextView m_ReviewDate;

        public TextView m_ReviewRating;

        public TextView m_ReviewUserName;

        public ImageView m_ReviewStarImage;

        public ImageView m_ReviewUserImage;

        public CardView m_cvViewBook;

        public Context m_Context;

        public ReviewHolder(Context i_Context, View itemView)
        {
            super(itemView);

            m_ReviewBody = itemView.findViewById(R.id.UserReview_body);
            m_ReviewHeader = itemView.findViewById(R.id.UserReview_header);
            m_ReviewDate = itemView.findViewById(R.id.UserReview_Date);
            m_ReviewRating = itemView.findViewById(R.id.UserReview_rating_tv);
            m_ReviewUserName = itemView.findViewById(R.id.UserReview_user_name);
            m_ReviewStarImage = itemView.findViewById(R.id.UserReview_ratingstar_iv);
            m_ReviewUserImage = itemView.findViewById(R.id.UserReview_user_image);
            m_cvViewBook = itemView.findViewById(R.id.item_card_UserReview);

            this.m_Context = i_Context;

        }


        public void populate(Review i_Review)
        {
            Log.e(TAG,"populate() >>  "+ i_Review.getUserEmail());

            if(m_ReviewBody == null)
            {
                Log.e(TAG,"Nulllllllllllllll() >>  "+ i_Review.getUserEmail());

            }
            m_ReviewBody.setText(i_Review.getReviewBody());
            m_ReviewHeader.setText(i_Review.getReviewHedaer());

            m_ReviewDate.setText(i_Review.getM_Date());
            m_ReviewRating.setText(Double.toString( i_Review.getRating()));

            m_ReviewUserName.setText(i_Review.getUserEmail());



            Log.e(TAG, "updateProfilePicInTheActivityView() >>");

            Log.e(TAG, "updateProfilePicInTheActivityView() <<");

            Log.e(TAG,"populate() << ");


        }
        public TextView getReviewBody()
        {
            return m_ReviewBody;
        }

        public void setReviewBody(TextView m_ReviewBody)
        {
            this.m_ReviewBody = m_ReviewBody;
        }

        public TextView getReviewHeader()
        {
            return m_ReviewHeader;
        }

        public void setReviewHeader(TextView m_ReviewHeader)
        {
            this.m_ReviewHeader = m_ReviewHeader;
        }

        public TextView getReviewDate()
        {
            return m_ReviewDate;
        }

        public void setReviewDate(TextView m_ReviewDate)
        {
            this.m_ReviewDate = m_ReviewDate;
        }

        public TextView getReviewRating()
        {
            return m_ReviewRating;
        }

        public void setReviewRating(TextView m_ReviewRating)
        {
            this.m_ReviewRating = m_ReviewRating;
        }

        public TextView getReviewUserName()
        {
            return m_ReviewUserName;
        }

        public void setReviewUserName(TextView m_ReviewUserName)
        {
            this.m_ReviewUserName = m_ReviewUserName;
        }

        public ImageView getReviewStarImage()
        {
            return m_ReviewStarImage;
        }

        public void setReviewStarImage(ImageView m_ReviewStarImage)
        {
            this.m_ReviewStarImage = m_ReviewStarImage;
        }

        public ImageView getReviewUserImage()
        {
            return m_ReviewUserImage;
        }

        public void setReviewUserImage(ImageView m_ReviewUserImage)
        {
            this.m_ReviewUserImage = m_ReviewUserImage;
        }

        public CardView getCvViewBook()
        {
            return m_cvViewBook;
        }

        public void setCvViewBook(CardView m_cvViewBook)
        {
            this.m_cvViewBook = m_cvViewBook;
        }

        public Context getContext()
        {
            return m_Context;
        }

        public void setContext(Context m_Context)
        {
            this.m_Context = m_Context;
        }



    }
}
