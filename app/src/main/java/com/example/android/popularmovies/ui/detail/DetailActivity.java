package com.example.android.popularmovies.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.android.popularmovies.Fragments.OverViewFragment;
import com.example.android.popularmovies.Fragments.ReviewFragment.ReviewFragment;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Util.NetworkingUtil;
import com.example.android.popularmovies.data.Movie;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity
{


    private Movie mMovie;

    private ImageView mBackgroundImage;
    private ImageView playButtonImageView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private DetailActivityViewModel mViewModel;
    private MyPagerAdapter adapterViewPager;
    private boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //initializing the views
        initViews();

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        setClickableOfPlayButton();

        //check if there is a passed intent
        if (getIntent().hasExtra(Intent.EXTRA_INTENT))
        {
            mMovie = getIntent().getParcelableExtra(Intent.EXTRA_INTENT);
            Glide.with(this).load(NetworkingUtil.buildPhotoURL(mMovie.getCoverImage(),NetworkingUtil.BACKDROP_IMAGE_W1280))
                    .placeholder(R.drawable.place_holder)
                    .into(mBackgroundImage);

        }


        DetailActivityViewModelFactory factory = new DetailActivityViewModelFactory(getApplication(),mMovie.getDbMovieId());

        mViewModel = ViewModelProviders.of(this,factory).get(DetailActivityViewModel.class);

        mViewModel.getDetailedMovie().observe(this,(movie)->{
            mMovie.setVideos(movie.getVideos());
        });

        mViewModel.getMovieInDatabase().observe(this,(movie)->{
            Timber.d("OBSERVE");
            if (movie != null){
                isFav = true;
                invalidateOptionsMenu();
            }else {
                isFav = false;
                invalidateOptionsMenu();
            }
        });
    }

    private void setClickableOfPlayButton() {
        playButtonImageView.setOnClickListener(v -> {
            if (mMovie.getVideos().getArrayOfTrailers().length>0)
            {
                showTrailersAlertDialog(v);
            }
            else
            {
                Toast.makeText(getBaseContext(),"This movie doesn't has a trailer",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        mBackgroundImage = findViewById(R.id.background_image_id);
        playButtonImageView = findViewById(R.id.play_trailer_youtube_button);
        mViewPager = findViewById(R.id.movies_view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
    }

    private void showTrailersAlertDialog(View v)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(v.getContext(),R.style.MyAlertDialogTheme);

        builderSingle.setTitle("Trailers");

        builderSingle.setItems(mMovie.getTrailersNameArray(), (dialog, which) -> {
            Uri uri = NetworkingUtil.createYoutubeLink(mMovie.getVideos().getArrayOfTrailers()[which].getYoutubeTrailerKey());
            Timber.d(uri.toString());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            if (intent.resolveActivity(getPackageManager()) != null)
            {
                startActivity(intent);
            }
        });
        builderSingle.setNegativeButton("cancel",null);
        builderSingle.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        final MenuItem menuItem = menu.getItem(0);


        if (isFav)
        {
            Timber.d("FAV");
            menuItem.setIcon(R.drawable.ic_favorite);
        }
        else
        {
            Timber.d("NOT FAV");
            menuItem.setIcon(R.drawable.ic_not_favourite);
        }
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        View view = findViewById(R.id.detail_act);

        final Snackbar snackbar =  Snackbar.make(view,R.string.message,Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.primaryColor))
                .setAction(R.string.undo, v -> mViewModel.insertMovie(mMovie));

        switch (item.getItemId())
        {
            case R.id.favourite_id:
                    if (snackbar.isShown())
                    {
                        snackbar.dismiss();
                    }

                    if (!isFav){
                        mViewModel.insertMovie(mMovie);
                    }
                    else
                    {
                        mViewModel.removeFromFavourites(mMovie);
                        snackbar.show();
                    }
                    invalidateOptionsMenu();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter
    {
        private int NUM_ITEMS = 2;

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
        @NonNull
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
