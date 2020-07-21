package com.example.mymovieapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.mymovieapplication.R;
import com.example.mymovieapplication.api.Api;
import com.example.mymovieapplication.model.MovieDetails;
import com.example.mymovieapplication.model.Search;
import com.example.mymovieapplication.model.repository.Repository;
import com.example.mymovieapplication.viewmodel.MovieViewModal;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MovieViewModal movieViewModal;
    private TextInputEditText keywordEditText;
    private Button searchButton;
    private List<Search> getMovieList;
    private List<Search> getMovieList1;
    private List<Search> getMovieList2;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private Repository repository;
    private BroadcastReceiver MyReceiver = null;
    boolean isLoading = false;
    private static final String DATA_URL = "http://www.omdbapi.com";
    String apikey="5d81e1ce";
    String page="1";
    String type="movie";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        MyReceiver = new MyReceiver();
        broadcastIntent();
        keywordEditText =findViewById(R.id.moviename);

        searchButton = findViewById(R.id.movie_search);
        repository = new Repository(getApplication());
        getMovieList = new ArrayList<>();
        getMovieList1 = new ArrayList<>();
        getMovieList2 = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);


        movieViewModal = new ViewModelProvider(this).get(MovieViewModal.class);
      //  movieAdapter = new MovieAdapter(MainActivity.this, getMovieList);


        movieViewModal.getGetAllMovie().observe(this, new Observer<List<Search>>() {
            @Override
            public void onChanged(List<Search>  movieDetails) {
              //  getMovieList.addAll(movieDetails);
            if (GlobalData.status.equals("Mobile data enabled") || GlobalData.status.equals("Wifi enabled")) {
                    // Its Available...
                    getMovieList.addAll(getMovieList1);
                    Log.v("main","isAvailable");
                } else if(GlobalData.status.equals("No internet is available")) {
                    // Not Available...
                    getMovieList.addAll(movieDetails);
                    Log.v("main","notAvailable");
                }else
               // getMovieList2.addAll(movieDetails);

                movieAdapter.getAllDatas(getMovieList);
                movieAdapter.notifyDataSetChanged();

                Log.d("main", "onChanged: " + movieDetails.size());
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(recyclerView,MainActivity.this, getMovieList);
        recyclerView.setAdapter(movieAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getMovieList.size()>0) {
                    getMovieList.clear();


                    movieAdapter.notifyDataSetChanged();


                    validEditText();
                }else{
                    //movieAdapter.notifyDataSetChanged();
                    validEditText();


                }



            }
        });
       // initScrollListener();
    }

    private void initScrollListener() {
        movieAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (getMovieList.size() <= 10) {
                    getMovieList.add(null);
                    movieAdapter.notifyItemChanged(getMovieList.size() - 1);
                  movieAdapter.getAllDatas(getMovieList);
//                    movieAdapter.notifyDataSetChanged();

                   // movieAdapter.notifyItemInserted(getMovieList.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getMovieList.remove(getMovieList.size() - 1);
                            movieAdapter.notifyItemRemoved(getMovieList.size());

                            //Generating more data
                            int index = getMovieList.size();
                            int end = getMovieList1.size();

                            // Search contact = new Search();

                            getMovieList.addAll(getMovieList1);

                            movieAdapter.notifyDataSetChanged();
                            movieAdapter.setLoaded();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(MainActivity.this, "Loading data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void loadMore() {
        getMovieList.add(null);
        movieAdapter.notifyItemInserted(getMovieList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMovieList.remove(getMovieList.size() - 1);
                int scrollPosition = getMovieList.size();
                movieAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    getMovieList.addAll(getMovieList1);
                    currentSize++;
                }

                movieAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }
    private void validEditText() {
        String keyword = keywordEditText.getEditableText().toString();
        int length = keywordEditText.length();

        if(!TextUtils.isEmpty(keyword) && length>=3) {
            if (GlobalData.status.equals("Mobile data enabled") || GlobalData.status.equals("Wifi enabled")) {
                // Its Available...
                makeRequest(keyword);
            } else if(GlobalData.status.equals("No internet is available")) {
                addData(keyword);
                // Not Available...
            }


            //

            try {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }


        }else {
            Toast.makeText(MainActivity.this, "Enter Movie Name", Toast.LENGTH_LONG).show();
        }
    }

    private void addData(String keyword) {
        for(int i=0;i<getMovieList.size();i++){
            if(getMovieList.get(i).getTitle().contains(keyword)){

            }else{

            }
        }
    }

    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }
    public void makeRequest(String keyword) {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(DATA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api=retrofit.create(Api.class);
        getMovieList.clear();
        getMovieList1.clear();
        getMovieList2.clear();
        Call<MovieDetails> call=api.getMovieList(type,apikey,page,keyword);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if(response.isSuccessful()) {
                    repository.insert(response.body().getSearch());
                    getMovieList1.addAll(response.body().getSearch());
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.d("main", "onFailure: "+t.getMessage());
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                movieAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        return true;
    }
}