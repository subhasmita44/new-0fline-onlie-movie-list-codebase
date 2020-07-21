package com.example.mymovieapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName ="moviedb",indices = @Index(value = {"imdbID"},unique = true))
public class Search {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movieId")
    private int movieId;
    @SerializedName("imdbID")
    @ColumnInfo(name = "imdbID")
    private String imdbID;
    @SerializedName("Poster")
    @ColumnInfo(name = "Poster")
    private String Poster;

    @SerializedName("Title")
    @ColumnInfo(name = "Title")
    private String Title;

    @SerializedName("Type")
    @ColumnInfo(name = "Type")
    private String Type;

    @SerializedName("Year")
    @ColumnInfo(name = "Year")
    private String Year;
    public Search(){

    }
    public Search(String response, String poster, String title, String type, String year) {
        imdbID = response;
        Poster = poster;
        Title = title;
        Type = type;
        Year = year;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getPoster() {
        return Poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getTitle() {
        return Title;
    }

    public String getType() {
        return Type;
    }

    public String getYear() {
        return Year;
    }

    public int getMovieId() {
        return movieId;
    }

    @Override
    public String toString() {
        return "Search{" +
                "movieId=" + movieId +
                ", imdbID='" + imdbID + '\'' +
                ", Poster='" + Poster + '\'' +
                ", Title='" + Title + '\'' +
                ", Type='" + Type + '\'' +
                ", Year='" + Year + '\'' +
                '}';
    }
}
