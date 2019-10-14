package com.example.android.popularmovies.Util;



public class StringCheck
{
    public static String stringFixer(String x)
    {
        StringBuilder builder = new StringBuilder();
        if (x.length() > 17)
        {
            builder.append(x.substring(0,15));
            builder.append("...");
            return builder.toString();
        }
        return x;
    }
}
