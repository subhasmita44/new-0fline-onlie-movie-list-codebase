package com.example.mymovieapplication.model.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mymovieapplication.model.MovieDetails;
import com.example.mymovieapplication.model.Search;
import com.example.mymovieapplication.model.database.MovieDao;
import com.example.mymovieapplication.model.database.MovieDatabase;

import java.util.List;

public class Repository {
    public MovieDao movieDao;
    public LiveData<List<Search>> getAllMovie;
    private MovieDatabase database;


    public Repository(Application application){
        database=MovieDatabase.getInstance(application);
        movieDao=database.movieDao();
        getAllMovie=movieDao.getMovieList();

    }

    public void insert(List<Search> movies){

        new InsertAsyncTask(movieDao).execute(movies);
    }

    public LiveData<List<Search>> getAllPosts(){
        return getAllMovie;
    }

    private static class InsertAsyncTask extends AsyncTask<List<Search>,Void,Void>{
        private MovieDao movieDao;

        public InsertAsyncTask(MovieDao movieDao)
        {
            this.movieDao=movieDao;
        }
        @Override
        protected Void doInBackground(List<Search>... lists) {
            //movieDao.deleteAll();
            movieDao .insert(lists[0]);
            return null;
        }
    }


}
