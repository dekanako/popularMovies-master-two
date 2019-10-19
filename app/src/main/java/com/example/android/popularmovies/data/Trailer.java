package com.example.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

public class Trailer
{
    @SerializedName("key")
    private String youtubeTrailerKey;

    @SerializedName("name")
    private String trailerTitle;

    public String getYoutubeTrailerKey()
    {
        return youtubeTrailerKey;
    }

    public void setYoutubeTrailerKey(String youtubeTrailerKey)
    {
        this.youtubeTrailerKey = youtubeTrailerKey;
    }

    public String getTrailerTitle()
    {
        return trailerTitle;
    }

    public void setTrailerTitle(String trailerTitle)
    {
        this.trailerTitle = trailerTitle;
    }
}
