package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie")
public class Movie implements Parcelable
{
    public static final String TAG = Movie.class.getName();

    @SerializedName("poster_path")
    private String imageLink;

    @SerializedName("title")
    private String filmTitle;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("id")
    @PrimaryKey
    private int dbMovieId;

    @SerializedName("backdrop_path")
    private String coverImage;

    @SerializedName("overview")
    private String overView;

    @SerializedName("release_date")
    private String date;

    @Ignore
    private Trailer[] trailersArray;

    public Movie(String imageLink, String filmTitle, double rating, int dbMovieId, String coverImage, String overView, String date)
    {
        this.imageLink = imageLink;
        this.filmTitle = filmTitle;
        this.rating = rating;
        this.dbMovieId = dbMovieId;
        this.coverImage = coverImage;
        this.overView = overView;
        this.date = date;
    }
    @Ignore
    private Movie(Parcel p)
    {

        imageLink = p.readString();
        filmTitle = p.readString();
        rating = p.readDouble();
        dbMovieId = p.readInt();

        coverImage = p.readString();
        overView = p.readString();
        date = p.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(imageLink);
        dest.writeString(filmTitle);
        dest.writeDouble(rating);
        dest.writeInt(dbMovieId);

        dest.writeString(coverImage);
        dest.writeString(overView);
        dest.writeString(date);
    }
    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public int getDbMovieId() {
        return dbMovieId;
    }

    public void setDbMovieId(int dbMovieId) {
        this.dbMovieId = dbMovieId;
    }

    public String getImageLink()
    {
        return imageLink;
    }

    public void setImageLink(String imageLink)
    {
        this.imageLink = imageLink;
    }

    public String getFilmTitle()
    {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle)
    {
        this.filmTitle = filmTitle;
    }

    public double getRating()
    {
        return rating;
    }
    /**
     * @param rating used to rate with the rating bar and we divide it by 2
     *
    **/
    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    @NonNull
    @Override
    public String toString()
    {
        return imageLink + " " + filmTitle + " " + getRating() + " " +dbMovieId + "  " + getImageLink() + " " + overView ;
    }

    public Trailer[] getTrailersArray() {
        return trailersArray;
    }

    public void setTrailersArray(Trailer[] trailersArray)
    {
        this.trailersArray = trailersArray;
    }

    public static Creator<Movie> getCREATOR()
    {
        return CREATOR;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    public String[] getTrailersNameArray()
    {
        String []trailersNameArray = new String[trailersArray.length];
        for (int i = 0;i<trailersArray.length;i++)
        {
            trailersNameArray[i] = trailersArray[i].getTrailerTitle();

        }
        return trailersNameArray;
    }

}
