package com.example.android.popularmovies;




import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieContainer;
import com.example.android.popularmovies.data.ReviewContainer;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    String BASE_MOVIE_URL ="https://api.themoviedb.org/3/movie/";

    String POPULAR_PATH = "popular";
    String TOP_RATED_PATH = "top_rated";

    String ENG_LANG_RESULT = "en-US";
    String API_KEY = "90429cbb0771760ab50be543df397f62";

    String VIDEO_APPEND = "videos";
    String REVIEWS_PATH =  "reviews";

    @GET("{search_type}")
    Call<MovieContainer> getMovies(@Path("search_type")String searchType,
                                   @Query("api_key")String apiKey,
                                   @Query("page") String page,
                                   @Query("language")String language);


    @GET("{movie_id}")
    Call<Movie> getMovieById(@Path("movie_id")int movieId,
                            @Query("api_key")String apiKey,
                            @Query("language")String language,
                            @Query("append_to_response") String videos);

    @GET("{movie_id}/"+REVIEWS_PATH)
    Call<ReviewContainer> getReviewsOfAMovie(@Path("movie_id")int movieId,
                                             @Query("api_key")String apiKey,
                                             @Query("language")String language);

}
