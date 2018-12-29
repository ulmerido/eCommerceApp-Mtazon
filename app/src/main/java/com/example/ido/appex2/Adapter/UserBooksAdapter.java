package com.example.ido.appex2.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ido.appex2.Activities.Interface_OnClickAudioBookCard;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.AudioBook;
import com.example.ido.appex2.entities.Review;
import com.example.ido.appex2.entities.User;

import java.util.List;

public class UserBooksAdapter extends RecyclerView.Adapter<UserBooksAdapter.UserBookHolder>
{
    private final String           TAG = "UserBooksAdapter";
    private List<AudioBookWithKey> mBookList;
    private User                   m_User;
    private LayoutInflater         m_inflater;
    public Interface_OnClickAudioBookCard m_AudioBookCardClick;


    public UserBooksAdapter(List<AudioBookWithKey> i_Book, User i_User, Interface_OnClickAudioBookCard i_AudioBookCardClick)
    {
        Log.e(TAG, "UserBooksAdapter() <<");
        this.mBookList = i_Book;
        this.m_User = i_User;
        this.m_AudioBookCardClick = i_AudioBookCardClick;
        Log.e(TAG, "UserBooksAdapter() >>");

    }

    @Override
    public UserBooksAdapter.UserBookHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        Log.e(TAG, "onCreateViewHolder() >>");

        m_inflater = LayoutInflater.from(parent.getContext());
        View itemView = m_inflater.inflate(R.layout.item_user_book, parent, false);

        Log.e(TAG, "onCreateViewHolder() <<");
        return new UserBookHolder(parent.getContext(), itemView);
    }


    @Override
    public void onBindViewHolder(final UserBooksAdapter.UserBookHolder holder, int position)
    {
        Log.e(TAG,"onBindViewHolder() >> " + position);
        final AudioBookWithKey bookWithKeyy = mBookList.get(position);
        AudioBook book = mBookList.get(position).getAudioBook();
        holder.populate(book);

        Log.e(TAG,"onBindViewHolder() << "+ position);

        holder.m_cvViewBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                final AudioBookWithKey bookWithKey = bookWithKeyy;
                Log.e(TAG,"onBindViewHolder()After >> " + bookWithKey.getAudioBook().getName());
                m_AudioBookCardClick.onAudioBookCardClick(bookWithKey);
            }
        });

        Log.e(TAG,"onBindViewHolder() >> ");

    }

    @Override
    public int getItemCount()
    {
        Log.e(TAG, "onBindViewHolder() <<");
        return mBookList.size();
    }


    public class UserBookHolder extends RecyclerView.ViewHolder
    {
        public TextView  mRatingReview;
        public ImageView mItemBookImage;
        public TextView  mUserItemBookName;
        public TextView  mUserItemAuthorName;
        public TextView  mUserItemGenrekName;
        public TextView   mUserReviesCount;
        public ImageView  mRatingStar;
        public Button     mPlaybtn;
        public CardView   m_cvViewBook;
        public Context    m_Context;

        public UserBookHolder(Context i_Context, View itemView)
        {

            super(itemView);
            Log.e(TAG, "UserBookHolder() >> ");
            mRatingReview     = itemView.findViewById(R.id.user_rating_review_tv);
            mItemBookImage    = itemView.findViewById(R.id.user_item_book_image);
            mUserItemBookName = itemView.findViewById(R.id.user_item_book_name);
            mUserItemAuthorName = itemView.findViewById(R.id.user_item_book_auther);
            mUserItemGenrekName = itemView.findViewById(R.id.user_item_book_genre);
            mUserReviesCount    = itemView.findViewById(R.id.user_item_book_review_count);
            mRatingStar= itemView.findViewById(R.id.user_ratingstar_iv);
            m_cvViewBook = itemView.findViewById(R.id.item_card_view_book);
            this.m_Context = i_Context;
            Log.e(TAG, "UserBookHolder() << ");
        }

        public void populate(AudioBook i_books)
        {
            Double rating;
            Log.e(TAG, "populate() >> ");
            itemView.setTag(i_books);
            mUserItemBookName.setText(i_books.getName());
            mUserItemGenrekName.setText(i_books.getGenre());
            mUserItemAuthorName.setText(i_books.getAuthor());
            mUserReviesCount.setText("(" + Integer.toString(i_books.getReviewsCount()) + ")");
            rating = i_books.getRating();
            mRatingReview.setText("[" + String.format("%.2f", rating)+ "]");


            Log.e(TAG, "updateProfilePicInTheActivityView() >>");
            Glide.with(this.getM_Context())
                    .load(i_books.getThumbImage())
                    .thumbnail(Glide.with(this.getM_Context()).load(R.drawable.sppiner_loading))
                    .fallback(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .into(mItemBookImage);
            Log.e(TAG, "updateProfilePicInTheActivityView() <<");

        }

        public TextView getmRatingReview()
        {
            return mRatingReview;
        }

        public ImageView getmItemBookImage()
        {
            return mItemBookImage;
        }

        public TextView getmUserItemBookName()
        {
            return mUserItemBookName;
        }

        public TextView getmUserItemAuthorName()
        {
            return mUserItemAuthorName;
        }

        public TextView getmUserItemGenrekName()
        {
            return mUserItemGenrekName;
        }
        
        public TextView getmUserReviesCount()
        {
            return mUserReviesCount;
        }

        public ImageView getmRatingStar()
        {
            return mRatingStar;
        }

        public Button getmPlaybtn()
        {
            return mPlaybtn;
        }

        public CardView getM_cvViewBook()
        {
            return m_cvViewBook;
        }

        public Context getM_Context()
        {
            return m_Context;
        }


    }
}

