package adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.faustus.mixins.build2.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;

import database.GenerateLiquors;
import model.Liquor;

/**
 * Created by flux on 5/26/15.
 */
public class RecyclerStaggeredAdapter extends RecyclerView.Adapter<RecyclerStaggeredAdapter.ViewHolder>
{
    private static final int ACTION_BUTTON_ONE = 1;
    private static final int ACTION_BUTTON_TWO = 2;
    private static final int ACTION_BUTTON_THREE = 3;
    public static FloatingActionMenu Currentmenu = null;
    private static Activity context;
    private static ArrayList<FloatingActionMenu> menus = new ArrayList<>();
    ArrayList<Liquor> LiquorItems;
    private DisplayMetrics windowMetrics;


    public RecyclerStaggeredAdapter(Activity activity, ArrayList<Liquor> liquorItems)
    {
        LiquorItems = liquorItems;
        context = activity;
        windowMetrics = context.getResources().getDisplayMetrics();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //windowMetrics = parent.getResources().getDisplayMetrics();
        //context = parent.getContext();
        // Log.i("onCreateViewHolder", "OnCreateViewHolder");
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_items, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        if (LiquorItems.get(position).getTileType() == GenerateLiquors.SMALL_TILE) // small tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(false);
        } else if (LiquorItems.get(position).getTileType() == GenerateLiquors.BIG_TILE)// big tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, windowMetrics.heightPixels, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(true);
        } else //long tile view
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
        holder.img.setTag(LiquorItems.get(position));
    }

    @Override
    public int getItemCount()
    {
        return LiquorItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public void onViewRecycled(ViewHolder holder)
    {
        super.onViewRecycled(holder);
        if (Currentmenu != null)
            Currentmenu.close(true);
    }

    public ArrayList<Liquor> getLiquorItems()
    {
        return LiquorItems;
    }

    public void LoadMore(int page)
    {
        //GenerateLiquors.setLastTileType(LiquorItems.get(LiquorItems.size()-1).getTileType());
        GenerateLiquors.LoadDrinks(page, LiquorItems);

    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView img;
        TextView txtview;
        CardView Tile;
        //ActionButton fabButton;

        public ViewHolder(View itemView)
        {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageLiquor);
            txtview = (TextView) itemView.findViewById(R.id.textLiquor);
            Tile = (CardView) itemView.findViewById(R.id.card_view);
            //fabButton = (ActionButton) itemView.findViewById(R.id.fab_button);


            Log.i("onCreateViewHolder", "OnCreateViewHolder");
            SubActionButton.Builder builder = new SubActionButton.Builder(context);
            builder.setTheme(SubActionButton.THEME_DARK);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(10, 10, 10, 10); //button content margin
            //builder.setLayoutParams(params);
            FloatingActionButton.LayoutParams params2 = new FloatingActionButton.LayoutParams(30, 30);// Button size
            params2.setMargins(10, 10, 10, 10);
            //builder.setLayoutParams(params2);
            /*ImageView image1 = new ImageView(context);
            ImageView image2 = new ImageView(context);
            ImageView image3 = new ImageView(context);*/
            ActionButton ab1 = new ActionButton(context);
            ActionButton ab2 = new ActionButton(context);
            ActionButton ab3 = new ActionButton(context);

            ab1.setButtonColor(Color.parseColor("#2196f3"));
            ab2.setButtonColor(Color.parseColor("#2196f3"));
            ab3.setButtonColor(Color.parseColor("#2196f3"));
            ab1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_camera));
            ab2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_chat));
            ab3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_video));
            ab1.setImageSize(20);
            ab1.setLayoutParams(params2);
            ab2.setLayoutParams(params2);
            ab3.setLayoutParams(params2);


            ab1.setTag(ACTION_BUTTON_ONE);
            ab1.setOnClickListener(this);
            ab2.setTag(ACTION_BUTTON_TWO);
            ab2.setOnClickListener(this);
            ab3.setTag(ACTION_BUTTON_THREE);
            ab3.setOnClickListener(this);


            /*ab1.setSize(30);
            ab2.setSize(30);o
            ab3.setSize(30);*/

            /*image1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_camera));
            image2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_chat));
            image3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_video));*/

            FloatingActionMenu itemMenu = new FloatingActionMenu.Builder(context)
                    .setStartAngle(-20)
                    .setEndAngle(-155)
                    .setRadius(60)
                    /*.addSubActionView(builder.setContentView(image1,params).build())
                    .addSubActionView(builder.setContentView(image2,params).build())
                    .addSubActionView(builder.setContentView(image3, params).build())*/
                    .addSubActionView(ab1, 60, 60)
                    .addSubActionView(ab2, 60, 60)
                    .addSubActionView(ab3, 60, 60)
                    .attachTo(img)
                    .setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener()
                    {
                        @Override
                        public void onMenuOpened(FloatingActionMenu floatingActionMenu)
                        {

                            for (FloatingActionMenu Iteratemenu : menus)
                            {
                                //if (Iteratemenu.isOpen())
                                Iteratemenu.close(true);
                            }

                            Currentmenu = floatingActionMenu;


                        }

                        @Override
                        public void onMenuClosed(FloatingActionMenu floatingActionMenu)
                        {
                            Currentmenu = null;
                        }
                    })
                    .build();
            menus.add(itemMenu);
        }


        @Override
        public void onClick(View v)
        {
            switch (((Integer) v.getTag()))
            {
                case ACTION_BUTTON_ONE:
                    Toast.makeText(context, "This is Button one", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_BUTTON_TWO:
                    Toast.makeText(context, "This is Button two", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_BUTTON_THREE:
                    Toast.makeText(context, "This is Button three", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}
