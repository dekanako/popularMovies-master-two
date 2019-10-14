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

public class NetworkingUtil
{
    private static final String BASE_MOVIE_URL ="https://api.themoviedb.org/3/movie";
    private static final String BASE_PHOTO_URL = "http://image.tmdb.org/t/p/";

    private static final String API_KEY = "api_key";
    private static final String API_KEY_VALUE = "90429cbb0771760ab50be543df397f62";



    private static final String POPULAR_PATH ="popular";
    private static final String TOP_RATED ="top_rated";

    private static final String PAGE = "page";


    private static final String LANGUAGE = "language";

    private static final String LANGUAGE_VALUE = "en-US";

    private static final String APPEND_TO_RESPONSE = "append_to_response";

    private static final String APPEND_VALUE = "videos";



    private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    private static final String TAG = NetworkingUtil.class.getName() ;
    public static final String REVIEWS_PATH = "reviews";

    // "https://api.themoviedb.org/3/movie/458156?api_key=90429cbb0771760ab50be543df397f62&language=en-US\n"
   // public static final String Quality_2
    public static URL buildURLForListOfPopularMovies(int page)
    {
        Uri movieUri =Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendEncodedPath(POPULAR_PATH)
                .appendQueryParameter(API_KEY,API_KEY_VALUE)
                .appendQueryParameter(PAGE,String.valueOf(page))
                .appendQueryParameter(LANGUAGE,LANGUAGE_VALUE)
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

    public static URL buildURLForListOfTopRatedMovies(int page)
    {
        Uri movieUri =Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendEncodedPath(TOP_RATED)
                .appendQueryParameter(API_KEY,API_KEY_VALUE)
                .appendQueryParameter(PAGE,String.valueOf(page))
                .appendQueryParameter(LANGUAGE,LANGUAGE_VALUE)
                .build();
        try {
            return new URL(movieUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri.Builder buildURLForOneMovie(int movieID)
    {

        return Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendEncodedPath(String.valueOf(movieID))
                .appendQueryParameter(API_KEY,API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE,LANGUAGE_VALUE);


    }


    public static URL buildURLForOneMovieWithTrailers(int movieID)
    {
        Uri movieUri = buildURLForOneMovie(movieID)
                .appendQueryParameter(APPEND_TO_RESPONSE,APPEND_VALUE)
                .build();
        try
        {
            return new URL(movieUri.toString());
        }
        catch
        (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }



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
        Log.d(TAG,movieUri.toString());
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

    public static URL buildReviewURL(int movieID)
    {
        Uri movieUri = buildURLForOneMovie(movieID)
                .appendEncodedPath(REVIEWS_PATH)
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

    public static String getResponseFromHttpUrlUsingScanner(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {

            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\A");

            if (scanner.hasNext())
            {
                return scanner.next();
            }
            else
            {
                return null;
            }
        }
        finally
        {
            urlConnection.disconnect();
        }
    }
    public static Uri createYoutubeLink(String youtubeTrailerKey)
    {
        return Uri.parse(YOUTUBE_URL.concat(youtubeTrailerKey));
    }
    public static boolean isInternetConnection(RecyclerView recyclerView, TextView textView,Context context)
    {

        ConnectivityManager connectivityManager =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null )
        {
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            return true;
        }
    }

}
