package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.android.popularmovies.Util.NetworkingUtil;
import com.example.android.popularmovies.Util.StringCheck;
import com.example.android.popularmovies.data.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Movie> mMovies;
    private Context mContext;
    private String TAG = MovieAdapter.class.getName();

    public MovieAdapter(List<Movie> movies,Context context)
    {
        this.mMovies = movies;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {

        if (holder instanceof MovieViewHolder)
        {
            bindTo(position,(MovieViewHolder) holder);
        }

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mMovieTitleView;
        private ImageView mPosterView;
        private RatingBar mRatingBarView;
        public MovieViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mMovieTitleView = itemView.findViewById(R.id.movie_title_id);
            mPosterView = itemView.findViewById(R.id.poster_view_id);
            mRatingBarView = itemView.findViewById(R.id.rating_bar_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(mContext,DetailActivity.class);
                    Movie movie = mMovies.get(getAdapterPosition());
                    intent.putExtra(Intent.EXTRA_INTENT,movie);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }

    }
    public void bindTo(int position,MovieViewHolder holder)
    {
        String filmTitle = StringCheck.stringFixer(mMovies.get(position).getFilmTitle());
        holder.mMovieTitleView.setText(filmTitle);
        holder.mRatingBarView.setRating((float) mMovies.get(position).getRating()/2);
        Glide.with(mContext).load(NetworkingUtil.buildPhotoURL(mMovies.get(position).getImageLink(),NetworkingUtil.POSTER_IMAGE_W500)).into(holder.mPosterView);
    }

    public void clearAdapter(){
        mMovies.clear();
        notifyDataSetChanged();
    }

}
