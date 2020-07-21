package com.example.mymovieapplication.model.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.mymovieapplication.model.MovieDetails;
import com.example.mymovieapplication.model.Search;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Search> movies);

  @Query("SELECT * FROM moviedb")
  LiveData<List<Search>> getMovieList();

    @Query("DELETE FROM moviedb")
    void deleteAll();
}
