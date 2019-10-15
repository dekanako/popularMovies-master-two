package com.example.android.popularmovies.Util;



import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil
{
    public static final String AUTHOR = "author";
    private static final String ID = "id";
    private static final String RATE = "vote_average";
    private static final String TITLE = "title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String BACKGROUND_IMAGE = "backdrop_path";
    private static final String RELEASE_DATE = "release_date";

    private static final String TAG = JsonUtil.class.getName();

    public static final String VIDEOS = "videos";
    public static final String RESULTS = "results";
    public static final String TRAILERS_KEY = "key";
    public static final String TRAILER_NAME = "name";
    public static final String CONTENT = "content";

    public static  List<Movie> extractMovieList(String json)
    {

        List<Movie> movie = new ArrayList<>();
        try
        {
            JSONObject baseJsonObject = new JSONObject(json);
            JSONArray array = baseJsonObject.getJSONArray("results");

            for (int x = 0;x<array.length();x++)
            {
                JSONObject jsonObject = array.getJSONObject(x);
                int movieIDExtracted = jsonObject.getInt(ID);

                double movieRateExtracted = jsonObject.getDouble(RATE);

                String movieTitleExtracted = jsonObject.getString(TITLE);

                String posterPathExtracted = jsonObject.getString(POSTER_PATH);

                String overView = jsonObject.getString(OVERVIEW);

                String coverImage = jsonObject.getString(BACKGROUND_IMAGE);

                String date = jsonObject.getString(RELEASE_DATE);

                movie.add(new Movie(posterPathExtracted,movieTitleExtracted,movieRateExtracted,movieIDExtracted,coverImage,overView,date));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return movie;
    }

    public static String extractTrailerPathAndAddTheTrailersToTheMovieObject(String json, Movie movie)
    {
        try
        {
            JSONObject object = new JSONObject(json);
            JSONObject videosExtractedJSON = object.getJSONObject(VIDEOS);
            JSONArray trailersArray = videosExtractedJSON.getJSONArray(RESULTS);
            //check if the trailer has 5 or less trailers to know how to set the trailers
            Trailer trailersArrayExtracted[];
            //if it was less than 5 trailers we crate the array corresponding to the size of the json
            if (trailersArray.length() < 5)
            {

                 trailersArrayExtracted = new Trailer[trailersArray.length()];
            }
            //else if it was more than 5 we compulsry make it 5
            else
            {
                trailersArrayExtracted = new Trailer[5];
            }


            for (int x = 0;x<trailersArray.length();x++)
            {
                Trailer trailer = new Trailer();
                trailer.setTrailerTitle(trailersArray.getJSONObject(x).getString(TRAILER_NAME));
                trailer.setYoutubeTrailerKey(trailersArray.getJSONObject(x).getString(TRAILERS_KEY));
                trailersArrayExtracted[x]= trailer;
                if (x == 4)
                {
                    //to set the limit for 5 trailers for those movies which have more than 5 trailers
                    break;
                }

            }
            movie.setTrailersArray(trailersArrayExtracted);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Review> extractMovieReviews(String json)
    {
        List<Review> reviews = new ArrayList<>();
        try
        {
            JSONObject object = new JSONObject(json);
            JSONArray reviewsArray = object.getJSONArray(RESULTS);
            for (int x = 0;x<reviewsArray.length();x++)
            {
                JSONObject reviewObject = reviewsArray.getJSONObject(x);
                Review review = new Review();
                review.setmTitle(reviewObject.getString(AUTHOR));
                review.setmReviewContent(reviewObject.getString(CONTENT));
                reviews.add(review);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return reviews;
    }
}
