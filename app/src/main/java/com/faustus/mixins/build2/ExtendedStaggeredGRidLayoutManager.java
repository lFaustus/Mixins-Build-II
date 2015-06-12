package com.faustus.mixins.build2;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
/**
 * Created by flux on 6/4/15.
 */
public class ExtendedStaggeredGRidLayoutManager extends StaggeredGridLayoutManager
{

    /**
     * Creates a StaggeredGridLayoutManager with given parameters.
     *
     * @param spanCount   If orientation is vertical, spanCount is number of columns. If
     *                    orientation is horizontal, spanCount is number of rows.
     * @param orientation {@link #VERTICAL} or {@link #HORIZONTAL}
     */
    public ExtendedStaggeredGRidLayoutManager(int spanCount, int orientation)
    {
        super(spanCount, orientation);
    }

    @Override
    public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount)
    {
        super.onItemsAdded(recyclerView, positionStart, itemCount);
       // Log.i("position",positionStart+"");
       // Log.i("itemCount",itemCount+"");
    }

   
}
