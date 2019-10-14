package com.example.android.popularmovies.Fragments.ReviewFragment;

import android.app.Notification;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.Model.Review;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Util.JsonUtil;
import com.example.android.popularmovies.Util.NetworkingUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        if (isInternetConnection())
        {
            new ReviewsAsyncTask().execute(mMovieID);
        }

        return view;
    }
    private class ReviewsAsyncTask extends AsyncTask<Integer,Void, List<Review>>
    {

        @Override
        protected void onPreExecute()
        {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Review> doInBackground(Integer... integers)
        {
            URL url = NetworkingUtil.buildReviewURL(integers[0]);
            String jsonOutput = null;
            try
            {
                jsonOutput = NetworkingUtil.getResponseFromHttpUrlUsingScanner(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return JsonUtil.extractMovieReviews(jsonOutput);
        }

        @Override
        protected void onPostExecute(List<Review> reviews)
        {
            mReviewAdapter = new ReviewAdapter(reviews);
            mRecyclerView.setAdapter(mReviewAdapter);
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }

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

