package com.example.mymovieapplication.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.example.mymovieapplication.model.Search;

import java.util.List;


@Database(entities = {Search.class},version = 3)
public  abstract class MovieDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "MyMovieApp";

    public abstract MovieDao movieDao();

    public static volatile MovieDatabase INSTANCE = null;

    public static MovieDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, MovieDatabase.class, DATABASE_NAME)
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    public static Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsyn(INSTANCE);

        }
    };

    static  class  PopulateDbAsyn extends AsyncTask<Void,Void,Void>{
        private MovieDao movieDao;
        public PopulateDbAsyn(MovieDatabase postDatabase)
        {
            movieDao=postDatabase.movieDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            movieDao.deleteAll();

            return null;
        }
    }

}
