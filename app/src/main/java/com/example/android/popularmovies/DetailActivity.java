package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.Fragments.OverViewFragment;
import com.example.android.popularmovies.Fragments.ReviewFragment.ReviewFragment;
import com.example.android.popularmovies.Model.Movie;
import com.example.android.popularmovies.Room.AppDBRoom;
import com.example.android.popularmovies.Room.AppExecutors;
import com.example.android.popularmovies.Util.JsonUtil;
import com.example.android.popularmovies.Util.NetworkingUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<String>
{

    private static final String TAG = DetailActivity.class.getName();
    private static final String TRAILER_KEY = "trailer_key";
    private static final int LOADER_ID = 125;
    private Movie mMovie;

    private ImageView mBackgroundImage;
    private ImageView playButtonImageView;
    private AppDBRoom mAppDBRoom;
    private MyPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAppDBRoom = AppDBRoom.getInstance(getApplicationContext());

        //initializing the views
        mBackgroundImage = findViewById(R.id.background_image_id);
        playButtonImageView = findViewById(R.id.play_trailer_youtube_button);

        ViewPager vpPager = findViewById(R.id.movies_view_pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vpPager);

        playButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mMovie.getTrailersArray().length>0)
                {

                    showTrailersAlertDialog(v);

                }
                else
                {
                    Toast.makeText(getBaseContext(),"This movie doesn't has a trailer",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //check if there is a passed intent
        if (getIntent().hasExtra(Intent.EXTRA_INTENT))
        {

            mMovie = getIntent().getParcelableExtra(Intent.EXTRA_INTENT);
            Bundle args = new Bundle();
            args.putInt(TRAILER_KEY,mMovie.getDbMovieId());
            getSupportLoaderManager().initLoader(LOADER_ID,args,this).forceLoad();


            Glide.with(this).load(NetworkingUtil.buildPhotoURL(mMovie.getCoverImage(),NetworkingUtil.BACKDROP_IMAGE_W1280))
                    .placeholder(R.drawable.place_holder)
                    .into(mBackgroundImage);










        }
    }

    private void showTrailersAlertDialog(View v)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(v.getContext(),R.style.MyAlertDialogTheme);

        builderSingle.setTitle("Trailers");

        builderSingle.setItems(mMovie.getTrailersNameArray(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = NetworkingUtil.createYoutubeLink(mMovie.getTrailersArray()[which].getYoutubeTrailerKey());
                Log.d(TAG,uri.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                if (intent.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(intent);
                }
            }
        });
        builderSingle.setNegativeButton("cancel",null);
        builderSingle.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.detail_activity_menu,menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        final MenuItem menuItem = menu.getItem(0);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run()
            {
                final Movie movie = mAppDBRoom.dao().getMovie(mMovie.getDbMovieId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movie == null)
                        {
                            menuItem.setIcon(R.drawable.ic_not_favourite);
                        }
                        else
                        {
                            menuItem.setIcon(R.drawable.ic_favorite);
                        }
                    }
                });
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        View view = findViewById(R.id.detail_act);

        final Snackbar snackbar =  Snackbar.make(view,R.string.message,Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.primaryColor))
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run()
                            {
                                mAppDBRoom.dao().insertMovie(mMovie);
                            }
                        });

                        invalidateOptionsMenu();
                    }
                });

        switch (item.getItemId())
        {
            case R.id.favourite_id:
                    if (snackbar.isShown())
                    {
                        snackbar.dismiss();
                    }
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run()
                        {
                            try
                            {
                                mAppDBRoom.dao().insertMovie(mMovie);
                            }
                            catch (SQLiteConstraintException e)
                            {
                                mAppDBRoom.dao().deleteMovieFromFavourites(mMovie);
                                snackbar.show();
                            }
                        }
                    });
                    invalidateOptionsMenu();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args)
    {
        int movieId = 0;

        if (args.containsKey(TRAILER_KEY))
        {

            movieId = args.getInt(TRAILER_KEY);
        }
        final int finalMovieId = movieId;
        return new AsyncTaskLoader<String>(this)
        {
            @Nullable
            @Override
            public String loadInBackground()
            {   String output = null;

                URL url = NetworkingUtil.buildURLForOneMovieWithTrailers(finalMovieId);
                Log.d(TAG,url.toString());
                try
                {
                   output = NetworkingUtil.getResponseFromHttpUrlUsingScanner(url);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return output;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data)
    {
        if (data ==null)
        {
            playButtonImageView.setClickable(false);
            return;
        }
        JsonUtil.extractTrailerPathAndAddTheTrailersToTheMovieObject(data,mMovie);
        playButtonImageView.setClickable(true);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader)
    {

    }
    public class MyPagerAdapter extends FragmentPagerAdapter
    {
        private   int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount()
        {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position)
        {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return OverViewFragment.newInstance(mMovie);
                case 1:
                    return ReviewFragment.newInstance(mMovie.getDbMovieId());
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "Overview";
                case 1:
                    return "Reviews";
                default:
                    return "";
            }

        }

    }
}
