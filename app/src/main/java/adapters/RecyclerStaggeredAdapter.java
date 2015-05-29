package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.TypedValue;
import com.faustus.mixins.build2.R;
import android.util.Log;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Liquor;

/**
 * Created by flux on 5/26/15.
 */
public class RecyclerStaggeredAdapter extends RecyclerView.Adapter<RecyclerStaggeredAdapter.ViewHolder>
{
    List<Liquor> LiquorItems = Collections.emptyList();
    private DisplayMetrics windowMetrics;
    private Context context;
    String[] materialPalette;
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

    public RecyclerStaggeredAdapter(Activity activity,List<Liquor> liquorItems)
    {
        LiquorItems = liquorItems;
        context = activity;
        windowMetrics = context.getResources().getDisplayMetrics();
        materialPalette = context.getResources().getStringArray(R.array.material_palette);
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

        if(LiquorItems.get(position).getTileType() == Liquor.SMALL_TILE) // small tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(false);
        }
        else if(LiquorItems.get(position).getTileType() == Liquor.BIG_TILE)// big tile view
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

        holder.txtview.setText(LiquorItems.get(position).getLiquor_Name());
        holder.Tile.setCardBackgroundColor(Color.parseColor(materialPalette[rand.nextInt(materialPalette.length)]));
    }

    @Override
    public int getItemCount()
    {
        return LiquorItems.size();
    }




}
