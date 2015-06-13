package com.faustus.mixins.build2.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by flux on 6/1/15.
 */
public abstract class EndlessStaggeredRecyclerOnScrollListener extends RecyclerView.OnScrollListener
{
    private final int visibleThreshold = 5;
    private StaggeredGridLayoutManager mStaggeredgrid;
    private int firstVisibleItem, VisibleItemCount, totalItemCount, current_page = 1;

    public EndlessStaggeredRecyclerOnScrollListener(StaggeredGridLayoutManager staggeredgrid)
    {
        mStaggeredgrid = staggeredgrid;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);
        OnScrolled();
        firstVisibleItem = mStaggeredgrid.findFirstVisibleItemPositions(null)[0];
        VisibleItemCount = recyclerView.getChildCount();
        totalItemCount = mStaggeredgrid.getItemCount();
        current_page = totalItemCount;
        Log.i("ItemCount", "ItemCount " + totalItemCount);
        Log.i("Childcount", "childcount: " + VisibleItemCount);
        Log.i("FirstPosition", "FirstVisibleItemPosition: " + firstVisibleItem);


        if ((totalItemCount - VisibleItemCount) <= (firstVisibleItem + visibleThreshold))
        {
            current_page++;
            OnLoadMore(current_page);
        }
        /*if((VisibleItemCount + firstVisibleItem) >= totalItemCount)
        {
            OnLoadMore(current_page);
            current_page ++;
        }*/


    }

    public abstract void OnLoadMore(int page);

    public abstract void OnScrolled();
}
