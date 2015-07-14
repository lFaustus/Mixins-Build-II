package com.faustus.mixins.build2.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.faustus.mixins.build2.database.GenerateLiquors;
import com.faustus.mixins.build2.model.Liquor;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.software.shell.fab.ActionButton;
import java.util.ArrayList;

/**
 * Created by flux on 5/26/15.
 */
public abstract class RecyclerStaggeredAdapter extends RecyclerView.Adapter<RecyclerStaggeredAdapter.ViewHolder> {

    public static FloatingActionMenu Currentmenu = null;
    private View mDialogView = null;
    private Activity context;
    private ArrayList<FloatingActionMenu> menus = new ArrayList<>();
    ArrayList<Liquor> LiquorItems;
    private DisplayMetrics windowMetrics;
    private boolean isModify = false;
    static RecyclerView mRecyclerView;

    public RecyclerStaggeredAdapter(Activity activity, ArrayList<Liquor> liquorItems) {
        LiquorItems = liquorItems;
        context = activity;
        windowMetrics = context.getResources().getDisplayMetrics();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         Log.i("onCreateViewHolder", "OnCreateViewHolder");
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
        holder.Tile.setTag(LiquorItems.get(position));
        holder.txtview.setTag(position);


    }

    @Override
    public int getItemCount() {
        return LiquorItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(isModify)
        {
            holder.txtviewModeIndicator.setVisibility(View.VISIBLE);
            holder.img.setClickable(true);
            holder.Tile.setClickable(false);
        }
        else if(!isModify && holder.txtviewModeIndicator.getVisibility() == View.VISIBLE )
        {
            holder.txtviewModeIndicator.setVisibility(View.INVISIBLE);
        }

        if(!isModify && holder.img.isClickable())
        {
            holder.img.setClickable(false);
            holder.Tile.setClickable(true);
        }
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

    public void setModify(boolean isModify)
    {
        this.isModify = isModify;
    }

    public boolean isModify()
    {
        return this.isModify;
    }


 class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,DialogInterface.OnCancelListener,FloatingActionMenu.MenuStateChangeListener {
        ImageView img;
        TextView txtview,txtviewModeIndicator;
        CardView Tile;
        FrameLayout Tile_label;

        //ActionButton fabButton;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageLiquor);
            txtview = (TextView) itemView.findViewById(R.id.textLiquor);
            Tile = (CardView) itemView.findViewById(R.id.card_view);
            Tile_label = (FrameLayout) itemView.findViewById(R.id.tile_label);
            txtviewModeIndicator = (TextView)itemView.findViewById(R.id.edit_mode_tag);

            Tile.setOnClickListener(this);
            Tile.setClickable(true);

            FloatingActionButton.LayoutParams buttonparams = new FloatingActionButton.LayoutParams(30, 30);// Button size
            buttonparams.setMargins(10, 10, 10, 10);

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
                    .setStateChangeListener(this)
                    .build();
            //menus.add(itemMenu);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ACTION_BUTTON_ONE:
                    OnRemoveItem((int)v.getTag());
                    break;

                case R.id.ACTION_BUTTON_TWO:
                    Toast.makeText(context, "This is Button two", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.ACTION_BUTTON_THREE:
                    Toast.makeText(context, "This is Button three", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.card_view:
                    if(mDialogView == null)
                    {
                        mDialogView = context.getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setView(mDialogView);
                        dialog.setOnCancelListener(this);
                        ((TextView) mDialogView.findViewById(R.id.liquor_name)).setText(((Liquor) v.getTag()).getLiquorName());
                        mDialogView.findViewById(R.id.left_border).setBackgroundColor(Color.parseColor(((Liquor) v.getTag()).getTileColor()));
                        dialog.create().show();
                    }

                    break;
            }
        }

     @Override
     public void onCancel(DialogInterface dialog)
     {
            mDialogView = null;
     }

     @Override
     public void onMenuOpened(FloatingActionMenu floatingActionMenu)
     {
        /* for (FloatingActionMenu Iteratemenu : menus) {
             //if (Iteratemenu.isOpen())
             Iteratemenu.close(true);
         }*/

         if(Currentmenu !=null)
             Currentmenu.close(true);
         Currentmenu = floatingActionMenu;

         // floatingActionMenu.getSubActionItems().get(1).view.setTag(img.getTag());
         floatingActionMenu.getSubActionItems().get(0).view.setTag(txtview.getTag());
     }

     @Override
     public void onMenuClosed(FloatingActionMenu floatingActionMenu)
     {
         if(Currentmenu == floatingActionMenu)
            Currentmenu = null;
     }
 }

    public abstract void OnRemoveItem(int position);
}
