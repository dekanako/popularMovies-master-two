package com.example.android.popularmovies.ui.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Movie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OverViewFragment extends Fragment
{

    private static final String MOVIE_BUNDLE_KEY = "movie_bundle_key";
    public static final String TAG = OverViewFragment.class.getName();
    private Movie mPassedMovie;
    private TextView mTitleView;
    private TextView mRateView;
    private TextView mYearView;
    private TextView mOverViewView;

    public static Fragment newInstance(Movie movie)
    {
        OverViewFragment overViewFragment = new OverViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_BUNDLE_KEY,movie);
        overViewFragment.setArguments(args);
        return overViewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPassedMovie = getArguments().getParcelable(MOVIE_BUNDLE_KEY);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.over_view_fragment,container,false);
        mTitleView = view.findViewById(R.id.movie_title_id);
        mOverViewView = view.findViewById(R.id.overview_id4);
        mYearView = view.findViewById(R.id.date_id);
        mRateView = view.findViewById(R.id.rateing_id);

        mTitleView.setText(mPassedMovie.getFilmTitle());
        mOverViewView.setText(mPassedMovie.getOverView());
        mYearView.setText(mPassedMovie.getDate());
        mRateView.setText(mPassedMovie.getRating()+"/10");



        return view;
    }
}
