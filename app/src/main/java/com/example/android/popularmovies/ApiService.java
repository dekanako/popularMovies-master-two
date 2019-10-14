package com.example.android.popularmovies;


import com.example.android.popularmovies.Model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_MOVIE_URL ="https://api.themoviedb.org/3/movie";

    String POPULAR_PATH = "popular";
    String TOP_RATED_PATH = "top_rated";

    String ENG_LANG_RESULT = "en-US";

    @GET("{search_type}")
    Call<List<Movie>> getMovies(@Path("search_type")String searchType,
                                @Query("api_key")String apiKey,
                                @Query("page") String page,
                                @Query("language")String language);







}
