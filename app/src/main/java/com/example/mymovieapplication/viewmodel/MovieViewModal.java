package com.example.mymovieapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymovieapplication.model.MovieDetails;
import com.example.mymovieapplication.model.Search;
import com.example.mymovieapplication.model.repository.Repository;

import java.util.List;

public class MovieViewModal extends AndroidViewModel {
        private Repository repository;
        public LiveData<List<Search>> getAllMovie;

    public MovieViewModal(@NonNull Application application) {
            super(application);
            repository=new Repository(application);
            getAllMovie=repository.getAllPosts();
        }

        public void insert(List<Search> movies){
            repository.insert(movies);
        }

        public LiveData<List<Search>> getGetAllMovie()
        {
            return getAllMovie;
        }
}
