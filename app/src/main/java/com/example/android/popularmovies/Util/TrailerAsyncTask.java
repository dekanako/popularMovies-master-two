package com.example.android.popularmovies.Util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class TrailerAsyncTask extends AsyncTaskLoader<String>
{

    public TrailerAsyncTask(@NonNull Context context)
    {
        super(context);

    }

    @Nullable
    @Override
    public String loadInBackground()
    {
        return null;
    }
}
