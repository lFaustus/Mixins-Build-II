package com.faustus.mixins.build2.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.database.GenerateLiquors;
import com.faustus.mixins.build2.model.Liquor;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.software.shell.fab.ActionButton;
import java.util.ArrayList;

/**
 * Created by flux on 5/26/15.
 */
public class RecyclerStaggeredAdapter extends RecyclerView.Adapter<RecyclerStaggeredAdapter.ViewHolder> {

    public static FloatingActionMenu Currentmenu = null;
    private static Activity context;
    private static ArrayList<FloatingActionMenu> menus = new ArrayList<>();
    ArrayList<Liquor> LiquorItems;
    private DisplayMetrics windowMetrics;


    public RecyclerStaggeredAdapter(Activity activity, ArrayList<Liquor> liquorItems) {
        LiquorItems = liquorItems;
        context = activity;
        windowMetrics = context.getResources().getDisplayMetrics();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Log.i("onCreateViewHolder", "OnCreateViewHolder");
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_items, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (LiquorItems.get(position).getTileType() == GenerateLiquors.SMALL_TILE) // small tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(false);
        }
        else if (LiquorItems.get(position).getTileType() == GenerateLiquors.BIG_TILE)// big tile view
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
        //holder.Tile.setCardBackgroundColor(Color.parseColor(LiquorItems.get(position).getTileColor()));
        holder.Tile_label.setBackgroundColor(Color.parseColor(LiquorItems.get(position).getTileColor()));
        holder.img.setTag(LiquorItems.get(position));
    }

    @Override
    public int getItemCount() {
        return LiquorItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (Currentmenu != null)
            Currentmenu.close(true);
    }

    public ArrayList<Liquor> getLiquorItems() {
        return LiquorItems;
    }

    public void LoadMore(int page) {
        GenerateLiquors.LoadDrinks(page, LiquorItems);

    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView txtview;
        CardView Tile;
        FrameLayout Tile_label;
        //ActionButton fabButton;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageLiquor);
            txtview = (TextView) itemView.findViewById(R.id.textLiquor);
            Tile = (CardView) itemView.findViewById(R.id.card_view);
            Tile_label = (FrameLayout) itemView.findViewById(R.id.tile_label);
            //fabButton = (ActionButton) itemView.findViewById(R.id.fab_button);


            //Log.i("onCreateViewHolder", "OnCreateViewHolder");
            // SubActionButton.Builder builder = new SubActionButton.Builder(context);
            //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            //params.setMargins(10, 10, 10, 10); //button content margin
            //builder.setLayoutParams(params);//useless
            FloatingActionButton.LayoutParams buttonparams = new FloatingActionButton.LayoutParams(30, 30);// Button size
            buttonparams.setMargins(10, 10, 10, 10);
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
            ab1.setLayoutParams(buttonparams);
            ab2.setLayoutParams(buttonparams);
            ab3.setLayoutParams(buttonparams);


            ab1.setTag(R.id.ACTION_BUTTON_ONE, R.id.ACTION_BUTTON_ONE);
            ab1.setId(R.id.ACTION_BUTTON_ONE);
            ab1.setOnClickListener(this);

            ab2.setOnClickListener(this);
            ab2.setId(R.id.ACTION_BUTTON_TWO);
            ab3.setTag(R.id.ACTION_BUTTON_THREE, R.id.ACTION_BUTTON_THREE);
            ab3.setId(R.id.ACTION_BUTTON_THREE);
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
                    .setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
                        @Override
                        public void onMenuOpened(FloatingActionMenu floatingActionMenu) {

                            for (FloatingActionMenu Iteratemenu : menus) {
                                //if (Iteratemenu.isOpen())
                                Iteratemenu.close(true);
                            }

                            Currentmenu = floatingActionMenu;
                            floatingActionMenu.getSubActionItems().get(1).view.setTag(img.getTag());

                        }

                        @Override
                        public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                            Currentmenu = null;
                        }
                    })
                    .build();
            menus.add(itemMenu);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ACTION_BUTTON_ONE:
                    Toast.makeText(context, "This is Button one", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.ACTION_BUTTON_TWO:
                    View view = context.getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setView(view);
                    ((TextView) view.findViewById(R.id.liquor_name)).setText(((Liquor) v.getTag()).getLiquorName());
                    view.findViewById(R.id.left_border).setBackgroundColor(Color.parseColor(((Liquor) v.getTag()).getTileColor()));
                    dialog.create().show();
                    break;

                case R.id.ACTION_BUTTON_THREE:
                    Toast.makeText(context, "This is Button three", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}
