package com.example.android.popularmovies.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.QueryPreferences;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Util.AppUtil;


import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.di.MoviesApplication;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener

{
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovies;
    private String mSelectedQuery;
    private TextView mOopsView;
    private ProgressBar mProgressBar;

    private MainActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTimberLogging();

        ((MoviesApplication)getApplication()).getAppComponent().injectActivity(this);

        queryIfThereIsConnection();

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

    private void queryIfThereIsConnection() {
        if (AppUtil.isConnectedToNetwork(this)){
            mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
            mViewModel.getListOfMovies().observe(this,(this::showTheReceivedList));
        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("there is no connection please connect and try again")
                    .setTitle("Problems while connecting")
                    .setPositiveButton("ok", (dialog, which) -> queryIfThereIsConnection());
            alert.show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        Timber.d("On Item SELECTED");
        TextView textView = (TextView)view;
        if (textView.getText().equals(QueryPreferences.getStoredTypeOfQuery(this))){
             return;
        }

        if ( textView.getText().equals(getResources().getString(R.string.popular)))
        {
            setQueryType(R.string.popular);
            reloadData();
        }
        else if (textView.getText().equals(getResources().getString(R.string.top_rated)))
        {
            setQueryType(R.string.top_rated);
            reloadData();
        }
        else if (textView.getText().equals(getString(R.string.favourites)))
        {
            setQueryType(R.string.favourites);
            reloadData();
        }
    }

    private void reloadData() {
        mMovieAdapter.clearAdapter();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void setQueryType(int p)
    {
        QueryPreferences.setStoredTypeOfQuery(this,getString(p));
        mViewModel.refreshForNewData();
        mViewModel.getListOfMovies().observe(this,this::showTheReceivedList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        QueryPreferences.getStoredTypeOfQuery(this);
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
            //refreshForNewData the query sort type to popular because its the first time we launch the app
            mSelectedQuery = getString(R.string.popular);
        }
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.query_array, R.layout.spinner_item); //  create the adapter from a StringArray
        spinner.setAdapter(mSpinnerAdapter); // refreshForNewData the adapter
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

        if (movies!= null){
            mMovies.addAll(movies);
        }else {
            mOopsView.setVisibility(View.VISIBLE);
        }


        mMovieAdapter = new MovieAdapter(mMovies,getBaseContext());
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    private void initTimberLogging() {
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }


}
