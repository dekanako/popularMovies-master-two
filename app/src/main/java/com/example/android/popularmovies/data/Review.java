package com.example.android.popularmovies.data;

public class Review
{
    private String mTitle;
    private String mReviewContent;

    public Review(String mTitle, String mReviewContent) {
        this.mTitle = mTitle;
        this.mReviewContent = mReviewContent;
    }

    public Review() {

    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmReviewContent() {
        return mReviewContent;
    }

    public void setmReviewContent(String mReviewContent) {
        this.mReviewContent = mReviewContent;
    }
}
