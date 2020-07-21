package com.example.mymovieapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetails {
    @SerializedName("Response")
    @Expose
    private String Response;
    @SerializedName("Search")
    @Expose
    private List<Search> Search=null;
    @SerializedName("totalResults")
    @Expose
    private String totalResults;

    public MovieDetails(String response, List<Search>  search, String totalResults) {
        Response = response;
        Search = search;
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return Response;
    }


    public List<Search> getSearch() {
        return Search;
    }

    public String getTotalResults() {
        return totalResults;
    }
}
