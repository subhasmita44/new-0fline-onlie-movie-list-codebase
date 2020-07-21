package com.example.mymovieapplication.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymovieapplication.R;
import com.example.mymovieapplication.model.Search;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private boolean isLoading;

    private Context context;
    public List<Search> movieList,filterList;
    CustomFilter filter;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public MovieAdapter(RecyclerView recyclerView, Context context, List<Search> movieList) {
        this.context = context;
        this.movieList = movieList;
        filterList=movieList;
        //filter=movieList;
        Log.v("TAG","adapter");
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.each_row, parent, false));
          //  return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));

        }
        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            Search contact = movieList.get(position);
            MovieViewHolder userViewHolder = (MovieViewHolder) holder;
            userViewHolder.title.setText("Title : "+contact.getTitle());
            userViewHolder.type.setText("Type : "+contact.getType());
            userViewHolder.imbd.setText("imbdID : "+contact.getImdbID());
            userViewHolder.year.setText("Year : "+contact.getYear());

            Glide.with(context).load(contact.getPoster()).into(((MovieViewHolder) holder).img);
            //  movieVH.textView.setText(movie.getTitle());
            // userViewHolder.email.setText(contact.getYear());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void getAllDatas(List<Search> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }



    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title,type,year,poster,imbd;
        public ImageView img;
        ItemClickListener itemClickListener;

        public MovieViewHolder(@NonNull View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            type = (TextView) view.findViewById(R.id.type);

            year = (TextView) view.findViewById(R.id.year);
            imbd=(TextView) view.findViewById(R.id.imbd);
            img = (ImageView) view.findViewById(R.id.img);
           // itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }

    }
    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter( filterList,this);
        }
        return filter;
    }

    //  In this adapter, declare two constants that is delegate for two item type of RecyclerView:

    //  Provide an OnLoadMoreListener variable and set an "add method":
    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
}
