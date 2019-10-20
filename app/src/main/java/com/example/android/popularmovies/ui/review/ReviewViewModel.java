package com.example.android.popularmovies.ui.review;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.android.popularmovies.data.ReviewContainer;
import com.example.android.popularmovies.data.repository.MoviesRepository;
import com.example.android.popularmovies.di.MoviesApplication;
import javax.inject.Inject;

public class ReviewViewModel extends AndroidViewModel {
    private LiveData<ReviewContainer> mReviewsLiveData;
    @Inject MoviesRepository mRepository;
    public ReviewViewModel(@NonNull Application application,int movieId) {
        super(application);
        ((MoviesApplication)getApplication()).getAppComponent().injectReviewViewModel(this);
        mReviewsLiveData = mRepository.getListOfMovieReview(movieId);
    }

    public LiveData<ReviewContainer> getReviewsLiveData() {
        return mReviewsLiveData;
    }

}
