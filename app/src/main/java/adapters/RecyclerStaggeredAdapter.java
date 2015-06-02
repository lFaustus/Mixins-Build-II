package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.faustus.mixins.build2.R;
import java.util.ArrayList;
import java.util.Random;

import database.GenerateLiquors;

import model.Liquor;

/**
 * Created by flux on 5/26/15.
 */
public class RecyclerStaggeredAdapter extends RecyclerView.Adapter<RecyclerStaggeredAdapter.ViewHolder>
{
    ArrayList<Liquor> LiquorItems;
    private DisplayMetrics windowMetrics;
    private Context context;

    Random rand;




    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView txtview;
        CardView Tile;

        public ViewHolder(View itemView)
        {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageLiquor);
            txtview = (TextView) itemView.findViewById(R.id.textLiquor);
            Tile = (CardView)itemView.findViewById(R.id.card_view);
        }

    }

    public RecyclerStaggeredAdapter(Activity activity,ArrayList<Liquor> liquorItems)
    {
        LiquorItems = liquorItems;
        context = activity;
        windowMetrics = context.getResources().getDisplayMetrics();
        rand = new Random();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //windowMetrics = parent.getResources().getDisplayMetrics();
        //context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_items, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        if(LiquorItems.get(position).getTileType() == GenerateLiquors.SMALL_TILE) // small tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(false);
        }
        else if(LiquorItems.get(position).getTileType() == GenerateLiquors.BIG_TILE)// big tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, windowMetrics.heightPixels, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(true);
        }
        else //long tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(false);
        }

        holder.txtview.setText(LiquorItems.get(position).getLiquorName());
        holder.Tile.setCardBackgroundColor(Color.parseColor(LiquorItems.get(position).getTileColor()));
    }

    @Override
    public int getItemCount()
    {
        return LiquorItems.size();
    }




    public ArrayList<Liquor> getLiquorItems()
    {
        return LiquorItems;
    }

    public void LoadMore(int page)
    {
           //GenerateLiquors.setLastTileType(LiquorItems.get(LiquorItems.size()-1).getTileType());
           GenerateLiquors.LoadDrinks(page,LiquorItems);

    }





}
