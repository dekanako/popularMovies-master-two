package com.example.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

public class Videos {
    @SerializedName("results")
    private Trailer[] mArrayOfTrailers;

    public Trailer[] getArrayOfTrailers() {
        return mArrayOfTrailers;
    }

    public void setArrayOfTrailers(Trailer[] arrayOfTrailers) {
        mArrayOfTrailers = arrayOfTrailers;
    }
}
