package com.example.android.popularmovies.ui.review;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.popularmovies.R;

import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.ui.ViewModelFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import timber.log.Timber;

public class ReviewFragment extends Fragment
{

    private static final String MOVIE_REVIEW_BUNDLE_KEY = "movie_review_bundle";
    private int mMovieID;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private ProgressBar mProgressBar;
    private TextView mOopsView;

    public static Fragment newInstance(int movieId)
    {
        Bundle args = new Bundle();
        args.putInt(MOVIE_REVIEW_BUNDLE_KEY,movieId);
        ReviewFragment fragment = new ReviewFragment();
        fragment.setRetainInstance(true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mMovieID = getArguments().getInt(MOVIE_REVIEW_BUNDLE_KEY);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View  view = inflater.inflate(R.layout.review_fragment,container,false);
        mRecyclerView = view.findViewById(R.id.reviews_recycler_view_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mProgressBar = view.findViewById(R.id.progressBar2);
        mOopsView = view.findViewById(R.id.network_text_view);

        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication(),mMovieID );
        ReviewViewModel viewModel = ViewModelProviders.of(this,factory).get(ReviewViewModel.class);

        if (isInternetConnection())
        {
            viewModel.getReviewsLiveData().observe(this, reviews -> {
                Timber.d(reviews.getReviews().get(0)+"");
                showListOfReviews(reviews.getReviews());
            });
        }

        return view;
    }

    private void showListOfReviews(List<Review> reviews) {
        mReviewAdapter = new ReviewAdapter(reviews);
        mRecyclerView.setAdapter(mReviewAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public  boolean isInternetConnection()
    {

        ConnectivityManager connectivityManager =  (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null )
        {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mOopsView.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            mOopsView.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            return true;
        }
    }
}

