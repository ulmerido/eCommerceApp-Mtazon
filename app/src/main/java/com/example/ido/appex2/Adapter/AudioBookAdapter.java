package com.example.ido.appex2.Adapter;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.AudioBook;
import com.example.ido.appex2.entities.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.Iterator;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioBookAdapter extends RecyclerView.Adapter<AudioBookAdapter.AudioBookHolder>
{
    private final String           TAG = "AudioBookAdapter";
    private List<AudioBookWithKey> m_BooksList;
    private User                   m_User;
    private LayoutInflater         m_inflater;

    public AudioBookAdapter(List<AudioBookWithKey> i_Books, User i_User)
    {
        this.m_BooksList = i_Books;
        this.m_User = i_User;
    }

    @Override
    public AudioBookHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        Log.e(TAG, "onCreateViewHolder() >>");

        m_inflater = LayoutInflater.from(parent.getContext());
        View itemView = m_inflater.inflate(R.layout.list_audiobook_item, parent, false);

        Log.e(TAG, "onCreateViewHolder() <<");
        return new AudioBookHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(AudioBookHolder holder, int position)
    {

        Log.e(TAG,"onBindViewHolder() >> " + position);

        AudioBook book = m_BooksList.get(position).getAudioBook();
        String songKey = m_BooksList.get(position).getKey();
        //holder.getRating().setRating(book.getRating());
        holder.populate(book);



        //holder.getRating().setRating((float)(book.getRating() / book.getReviewsCount()));

        //Check if the user already purchased the song if set the text to Play
        //If not to BUY $X
        if(m_User!=null)
        {
            Iterator i = m_User.getMyAudioBooks().iterator();
            while (i.hasNext())
            {
                if (i.next().equals(songKey))
                {
                    holder.getPrice().setTextColor(0xffaabb);
                    break;
                }
            }
        }

        Log.e(TAG,"onBindViewHolder() << "+ position);
    }

    @Override
    public int getItemCount()
    {
        return m_BooksList.size();
    }


    public class AudioBookHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.item_book_genre)
        public TextView m_tvGenre;

        @BindView(R.id.item_book_image)
        public ImageView m_ivImage;

        @BindView(R.id.item_book_name)
        public TextView m_tvName;

        @BindView(R.id.item_book_price)
        public TextView m_tvPrice;

        @BindView(R.id.rating_review_tv)
        public TextView m_tvRating;

        @BindView(R.id.item_book_review_count)
        public TextView m_tvReviewCount;

        @BindView(R.id.item_book_auther)
        public TextView m_tvAuther;

        @BindView(R.id.item_card_view_book)
        public CardView m_cvViewBook;

        public Context m_Context;

        public AudioBookHolder(Context i_Context, View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.m_Context = i_Context;


            m_cvViewBook.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Context context = view.getContext();
                    //Intent intent = new Intent(context, SongDetailsActivity.class);
                    //intent.putExtra("song", selectedSong);
                    //intent.putExtra("key", selectedSongKey);
                    //intent.putExtra("user",user);
                    //context.startActivity(intent);
                }
            });
        }



        public void populate(AudioBook i_audiobook)
        {
            Log.e(TAG,"populate() >> ");
            itemView.setTag(i_audiobook);
            m_tvName.setText(i_audiobook.getName());
            m_tvGenre.setText(i_audiobook.getGenre());
            m_tvAuther.setText(i_audiobook.getAuthor());
            m_tvReviewCount.setText("(" + Integer.toString(i_audiobook.getReviewsCount()) + ")");
            m_tvPrice.setText(Integer.toString(i_audiobook.getPrice()) +"$");
           




            Log.e(TAG, "updateProfilePicInTheActivityView() >>");
            Glide.with(this.getContext())
                    .load(i_audiobook.getThumbImage())
                    .thumbnail(Glide.with(this.getContext()).load(R.drawable.sppiner_loading))
                    .fallback(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .into(m_ivImage);
            Log.e(TAG, "updateProfilePicInTheActivityView() <<");



//            Picasso.with(this.getContext())
//                    .load(i_audiobook.getThumbImage())
//                    .into(m_ivImage, new Callback()
//                    {
//                        @Override
//                        public void onSuccess()
//                        {
//                            // put a progress bar
//                            // visibility on
//                        }
//
//                        @Override
//                        public void onError()
//                        {
//
//                        }
//                    });
//            Log.e(TAG,"populate() << ");
            Log.e(TAG,"Hello World "+ i_audiobook.getThumbImage());

        }

        public TextView getGenre()
        {
            return m_tvGenre;
        }

        public ImageView getImage()
        {
            return m_ivImage;
        }

        public TextView getName()
        {
            return m_tvName;
        }

        public TextView getPrice()
        {
            return m_tvPrice;
        }

        public TextView getRating()
        {
            return m_tvRating;
        }

        public TextView getReviewCount()
        {
            return m_tvReviewCount;
        }

        public TextView getAuther()
        {
            return m_tvAuther;
        }

        public CardView getViewBook()
        {
            return m_cvViewBook;
        }

        public Context getContext()
        {
            return m_Context;
        }


    }
}
