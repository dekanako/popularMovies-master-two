package com.example.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewContainer {

    @SerializedName("results")
    private List<Review> mReviews;

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }
}
