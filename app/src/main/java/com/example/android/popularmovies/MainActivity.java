package com.example.android.popularmovies;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.Model.Movie;
import com.example.android.popularmovies.Model.MovieContainer;
import com.example.android.popularmovies.Room.AppDBRoom;
import com.example.android.popularmovies.Util.JsonUtil;
import com.example.android.popularmovies.Util.NetworkingUtil;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<List<Movie>>

{
    public static final int POPULAR_LOADER_ID = 123;
    public static final int TOP_RATED_LOADER_ID = 352;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovies;
    private String mSelectedQuery;
    private TextView mOopsView;
    private ProgressBar mProgressBar;
    private static final String TAG = MainActivity.class.getName();

    private AppDBRoom mAppDBRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTimberLogging();

        mOopsView = findViewById(R.id.ops_id);
        mRecyclerView = findViewById(R.id.recycle_view_id);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
            {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 2;
                int spacing = 1;//spacing between views in grid

                if (position >= 0)
                {
                    int column = position % spanCount; // item column

                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount)
                    { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                }
                else
                {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });
        mRecyclerView.setHasFixedSize(true);

    }


    private void initQueries(int p)
    {

        Bundle bundle = new Bundle();
        if (getString(p).equals(getString(R.string.popular)))
        {
            bundle.putString(Intent.EXTRA_INTENT,NetworkingUtil.buildURLForListOfPopularMovies(1).toString());
            chooseLoaderAndStartLoading(POPULAR_LOADER_ID,bundle);
        }
        else if (getString(p).equals(getString(R.string.top_rated)))
        {
            bundle.putString(Intent.EXTRA_INTENT,NetworkingUtil.buildURLForListOfTopRatedMovies(1).toString());
            chooseLoaderAndStartLoading(TOP_RATED_LOADER_ID,bundle);
        }
        else if (getString(p).equals(getString(R.string.favourites)))
        {
            mAppDBRoom = AppDBRoom.getInstance(this);
            mAppDBRoom.dao().getAllMovie().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(List<Movie> movies)
                {
                    //We add this block of checking because when the observer is triggered the result will be set on the favourite whenever the selected query was
                    if (!QueryPreferences.getStoredTypeOfQuery(getBaseContext()).equals(getString(R.string.popular)))
                    {
                        if (!QueryPreferences.getStoredTypeOfQuery(getBaseContext()).equals(getString(R.string.top_rated)))
                        {
                            showTheReceivedList(movies);
                        }

                    }

                }
            });

        }


    }
    private void chooseLoaderAndStartLoading(int passedLoaderKey, Bundle args)
    {
        if (isInternetConnection())
        {
            getSupportLoaderManager().initLoader(passedLoaderKey,args,this);
        }

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        TextView textView = (TextView)view;
        if ( textView.getText().equals(getResources().getString(R.string.popular)))
        {
            queryBasedOnWhat(R.string.popular);
        }
        else if (textView.getText().equals(getResources().getString(R.string.top_rated)))
        {
            queryBasedOnWhat(R.string.top_rated);
        }
        else if (textView.getText().equals(getString(R.string.favourites)))
        {
            queryBasedOnWhat(R.string.favourites);
        }
    }

    private void queryBasedOnWhat(int p)
    {
        QueryPreferences.setStoredTypeOfQuery(this, getResources().getString(p));
        initQueries(p);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        QueryPreferences.getStoredTypeOfQuery(this);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable final Bundle args)
    {

        return new AsyncTaskLoader<List<Movie>>(this)
        {
            private List<Movie> mResult;

            @Override
            protected void onStartLoading()
            {
                if (mResult != null)
                {
                    deliverResult(mResult);
                }
                else
                {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable List<Movie> data) {
                mResult = data;
                super.deliverResult(data);
            }


            String urls;
            @Nullable
            @Override
            public List<Movie> loadInBackground()
            {
                String jsonOutput = null;
                try
                {

                    if (args!= null)
                    {

                        urls = args.getString(Intent.EXTRA_INTENT,null);
                        Timber.d("URL DEKAN AKO TEST   " + urls);
                        jsonOutput = NetworkingUtil.getResponseFromHttpUrlUsingScanner(new URL(urls));
                        return JsonUtil.extractMovieList(jsonOutput);
                    }
                    else
                    {
                        mAppDBRoom = AppDBRoom.getInstance(getBaseContext());
                        return null;

                    }


                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data)
    {
        if (data == null)
        {
            showTheReceivedList(data);
        }else
        {
            showTheReceivedList(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        Spinner spinner = (Spinner) menu.findItem(R.id.spinner).getActionView(); // find the spinner
        if (PreferenceManager.getDefaultSharedPreferences(this).contains("QUERY"))
        {
           mSelectedQuery =  QueryPreferences.getStoredTypeOfQuery(this);
        }
        else
        {
            //set the query sort type to popular because its the first time we launch the app
            mSelectedQuery = getString(R.string.popular);
        }
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.query_array, R.layout.spinner_item); //  create the adapter from a StringArray
        spinner.setAdapter(mSpinnerAdapter); // set the adapter
        spinner.setOnItemSelectedListener(this); // (optional) reference to a OnItemSelectedListener, that you can use to perform actions based on user selection

        if (mSelectedQuery.equals(getResources().getString(R.string.popular)))
        {
            spinner.setSelection(0);
        }
        else if(mSelectedQuery.equals(getResources().getString(R.string.top_rated)))
        {
            spinner.setSelection(1);
        }
        else if(mSelectedQuery.equals(getResources().getString(R.string.favourites)))
        {
            spinner.setSelection(2);
        }

        return true;
    }


    private void showTheReceivedList(List<Movie> movies)
    {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mMovies = new ArrayList<>();
        mMovies.addAll(movies);
        mMovieAdapter = new MovieAdapter(mMovies,getBaseContext());
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    public boolean isInternetConnection()
    {

        ConnectivityManager connectivityManager =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null )
        {
            if (!mSelectedQuery.equals(getString(R.string.favourites)))
            {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mOopsView.setVisibility(View.VISIBLE);
            }

            return false;
        }
        else
        {

            mOopsView.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);

            return true;
        }
    }

    private void initTimberLogging() {
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }

}
