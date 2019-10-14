package com.example.android.popularmovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieContainer {
    @SerializedName("results")
    private List<Movie>mMovies = new ArrayList<>();

    public MovieContainer(List<Movie> movies) {
        mMovies = movies;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
    }
}
