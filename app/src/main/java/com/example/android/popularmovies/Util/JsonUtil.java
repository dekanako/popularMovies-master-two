package com.example.android.popularmovies.Util;



import com.example.android.popularmovies.data.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil
{
    public static final String AUTHOR = "author";
    public static final String RESULTS = "results";
    public static final String CONTENT = "content";

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
