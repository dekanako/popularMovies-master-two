package com.example.android.popularmovies.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import androidx.recyclerview.widget.RecyclerView;

import timber.log.Timber;

public class NetworkingUtil
{
    private static final String BASE_PHOTO_URL = "http://image.tmdb.org/t/p/";



    private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    private static final String TAG = NetworkingUtil.class.getName() ;
    public static final String BACKDROP_IMAGE_W1280 = "w1280";
    public static final String POSTER_IMAGE_W500 = "w500";
    /**
     * @param movieImagePath append the movie path to the url
     * @param quality select the quality of the photo
     * @return the url which has been built by those parameters
     */
    public static URL buildPhotoURL(String movieImagePath,String quality)
    {
        Uri movieUri = Uri.parse(BASE_PHOTO_URL)
                .buildUpon()
                .appendEncodedPath(quality)
                .appendEncodedPath(movieImagePath)
                .build();
        try
        {
            return new URL(movieUri.toString());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri createYoutubeLink(String youtubeTrailerKey)
    {
        return Uri.parse(YOUTUBE_URL.concat(youtubeTrailerKey));
    }


}
