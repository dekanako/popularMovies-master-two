package com.example.android.popularmovies.Fragments.ReviewFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Model.Review;
import com.example.android.popularmovies.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>
{
    private List<Review>mReviews;

    public ReviewAdapter(List<Review> mReviews)
    {
        this.mReviews = mReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position)
    {
        holder.mReviewrTextView.setText(mReviews.get(position).getmTitle());
        holder.mReviewContentView.setText(mReviews.get(position).getmReviewContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mReviewrTextView;
        private TextView mReviewContentView;

        public ReviewViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mReviewrTextView = itemView.findViewById(R.id.review_view_id);
            mReviewContentView = itemView.findViewById(R.id.review_content_id);
        }

    }
}
