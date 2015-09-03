package com.faustus.mixins.build2.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.faustus.mixins.build2.loader.ImageLoader;

/**
 * Created by flux on 6/1/15.
 */
public abstract class EndlessStaggeredRecyclerOnScrollListener extends RecyclerView.OnScrollListener
{
    private final int visibleThreshold = 5;
    private StaggeredGridLayoutManager mStaggeredgrid;
    private int firstVisibleItem, VisibleItemCount, totalItemCount, current_page = 1;
    private static int previousTotal;
    public static final int PREVIOUS_SCROLL_STATE_DEFAULT = -1;
    private int previous_scroll_state = PREVIOUS_SCROLL_STATE_DEFAULT;
    private boolean isLoading = false;
    private ImageLoader mImageLoader;

    public EndlessStaggeredRecyclerOnScrollListener(StaggeredGridLayoutManager staggeredgrid,Activity activity)
    {
        mStaggeredgrid = staggeredgrid;
        mImageLoader = new ImageLoader(activity);
        previousTotal = mStaggeredgrid.getItemCount();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);
        firstVisibleItem = mStaggeredgrid.findFirstVisibleItemPositions(null)[0];
        VisibleItemCount = recyclerView.getChildCount();
        totalItemCount = mStaggeredgrid.getItemCount();
        current_page = totalItemCount;
        OnScrolled(firstVisibleItem,VisibleItemCount,totalItemCount,previousTotal);

        /*Log.i("ItemCount", "ItemCount " + totalItemCount);
        Log.i("Childcount", "childcount: " + VisibleItemCount);
        Log.i("FirstPosition", "FirstVisibleItemPosition: " + firstVisibleItem);*/


        /*if ((totalItemCount - VisibleItemCount) <= (firstVisibleItem + visibleThreshold))
        {
            current_page++;
            OnLoadMore(current_page);
        }*/
        if(isLoading)
        {

            if(totalItemCount > previousTotal)
            {
                previousTotal = totalItemCount + 1;
                isLoading = false;
            }

        }

        if(!isLoading &&(firstVisibleItem + VisibleItemCount) >= totalItemCount)
        {
            current_page++;
            Log.e("Current Page",current_page+"");
            isLoading = true;
            OnLoadMore(current_page);
        }
        /*if((VisibleItemCount + firstVisibleItem) >= totalItemCount)
        {
            OnLoadMore(current_page);
            current_page ++;
        }*/


    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
       OnScrollStateChanged(previous_scroll_state,newState);
        previous_scroll_state = newState;


        switch(newState)
        {
            case RecyclerView.SCROLL_STATE_IDLE:
             /*   for(int i =mStaggeredgrid.findFirstVisibleItemPositions(null)[0];i<=mStaggeredgrid.findLastVisibleItemPositions(null)[0];i++)
                {
                    Log.e("visibleitemlength", mStaggeredgrid.findFirstVisibleItemPositions(null).length + "");
                    Log.e("visibleitempositions", i + "");
                    RecyclerStaggeredAdapter.ViewHolder vh = (RecyclerStaggeredAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    CardInformation mCard = (CardInformation)vh.Tile.getTag();
                    Liquor mLiquor = (Liquor)mCard.getmObjectArray()[0];
                    try
                    {
                        mImageLoader.DisplayImage(mLiquor.getLiquorPictureURI(),vh.img,mLiquor.getLiquorName());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }*/
                break;

        }
    }


    public abstract void OnLoadMore(int page);

    public abstract void OnScrolled(int FirstVisibleItem, int VisibleItemCount, int TotalItemCount, int LastTotalItemCount);

    public abstract void OnScrollStateChanged(int previous_state,int newState);

}
