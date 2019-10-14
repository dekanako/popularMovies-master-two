package com.example.android.popularmovies;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences
{
    private static final String QUERY_KEY = "QUERY";
    public static void setStoredTypeOfQuery(Context context,String passsedValue)
    {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(QUERY_KEY,passsedValue).apply();
    }
    public static String getStoredTypeOfQuery(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(QUERY_KEY,context.getResources().getString(R.string.popular));
    }
}
