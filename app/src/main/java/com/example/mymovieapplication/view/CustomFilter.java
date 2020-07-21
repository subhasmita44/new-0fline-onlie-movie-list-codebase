package com.example.mymovieapplication.view;

import android.widget.Filter;

import com.example.mymovieapplication.model.Search;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {
    MovieAdapter adapter;
    List<Search> filterList;
    public CustomFilter(List<Search> filterList, MovieAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;
    }
    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            List<Search> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.movieList= (ArrayList<Search>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}

/**
 * Created by Atif on 11/3/2018.
 * devofandroid.blogspot.com
 */


