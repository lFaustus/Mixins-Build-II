package com.faustus.mixins.build2.adapters;

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
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.database.GenerateLiquors;
import com.faustus.mixins.build2.loader.ImageLoader;
import com.faustus.mixins.build2.model.CardInformation;
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
    private final ArrayList<FloatingActionMenu> menus = new ArrayList<>();
    private ArrayList<CardInformation> mCardInformation;
    private DisplayMetrics windowMetrics;
    private static boolean isModify = false;
    //static RecyclerView mRecyclerView;
    public static GenerateLiquors mGenerateLiquors;
    private int mDateDiff;
   // private BitmapManager mBitmapManager;
    private ImageLoader mImageLoader;
    //private DB mDB;


    public RecyclerStaggeredAdapter(Activity activity,ArrayList<CardInformation>cardInformations) {
        mCardInformation = cardInformations;
        context = activity;
       // mBitmapManager = new BitmapManager(activity);
        mImageLoader = new ImageLoader(activity);
        //if(mCardInformation == null)
          //  mCardInformation =  new ArrayList<>();
        Log.e("CardSize", mCardInformation.size() + "");
        mGenerateLiquors = new GenerateLiquors(context, mCardInformation);
        windowMetrics = context.getResources().getDisplayMetrics();
        //mDB = new DB(activity);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_items, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
         Liquor mLiquor =  (Liquor) mCardInformation.get(position).getmObjectArray()[0];
        if (mCardInformation.get(position).getTileType() == GenerateLiquors.SMALL_TILE) // small tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(false);
        }
        else if (mCardInformation.get(position).getTileType() == GenerateLiquors.BIG_TILE)// big tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, windowMetrics.heightPixels, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(true);
        }
        else //long tile view
        {
            ViewGroup.LayoutParams layoutparams = holder.Tile.getLayoutParams();
            layoutparams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, windowMetrics);
            layoutparams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, windowMetrics);
            holder.Tile.setLayoutParams(layoutparams);
            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) holder.Tile.getLayoutParams());
            layoutParams1.setFullSpan(false);
        }

        holder.txtview.setText(mLiquor.getLiquorName());
        holder.Tile_label.setBackgroundColor(Color.parseColor(mCardInformation.get(position).getTileColor()));
        mCardInformation.get(position).setCardPosition(position);
        holder.Tile.setTag(mCardInformation.get(position));

       //holder.setPosition(position);
        //holder.img.setImageBitmap(BitmapFactory.decodeFile(mCardInformation.get(position).getLiquorPictureURL()));
       //mBitmapManager.LoadImage(holder.img,mCardInformation.get(position).getLiquorPictureURL());
      // holder.txtview.setTag(position);
       /* if(isIdle)
        {
            try
            {
                SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date mTempDate = mSdf.parse(mSdf.format(new Date()));
                Date mTempDate2 = mSdf.parse(mCardInformation.get(position).getDateAdded());
                long mDateDiff1 = ((mTempDate2.getTime() - mTempDate.getTime()) / (1000 * 60 * 60 * 24));
                Log.i("DATEDIFF", mDateDiff1 + "");
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return mCardInformation.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
       // mRecyclerView = recyclerView;

    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        CardInformation mCard = (CardInformation)holder.Tile.getTag();
        Liquor mLiquor = (Liquor)mCard.getmObjectArray()[0];
        mImageLoader.DisplayImage(mLiquor.getLiquorPictureURL(), holder.img, mLiquor.getLiquorName());
        holder.txtviewModeIndicator.setVisibility(View.INVISIBLE);
        if(isModify)
        {
            holder.txtviewModeIndicator.setVisibility(View.VISIBLE);
            holder.txtviewModeIndicator.setText("Edit Mode");
            holder.txtviewModeIndicator.setBackgroundResource(R.color.green_material_semi_transparent);
            holder.img.setClickable(true);
            holder.Tile.setClickable(false);
            if(mDateDiff != 30)
                mCard.setRibbonLabel("New");
        }



        if(mDateDiff != 30 && !isModify)
        {
            //((ViewGroup) holder.txtviewModeIndicator.getParent()).setBackgroundColor(Color.RED);
            holder.txtviewModeIndicator.setText("New");
            holder.txtviewModeIndicator.setBackgroundResource(R.color.red_material_semi_transparent);
            holder.txtviewModeIndicator.setVisibility(View.VISIBLE);
            mCard.setRibbonLabel(holder.txtviewModeIndicator.getText().toString());

        }

        if(!isModify && holder.img.isClickable())
        {
            holder.img.setClickable(false);
            holder.Tile.setClickable(true);
            //mCard.setCardAttribute(holder.txtviewModeIndicator.getLayoutParams());
        }

        if(holder.txtviewModeIndicator.getVisibility() == View.VISIBLE)
        {
  //          if(!holder.txtviewModeIndicator.getText().toString().contains("Edit"))
//                mCard.setRibbonLabel(holder.txtviewModeIndicator.getText().toString());
            mCard.setRibbonColorResource(R.color.red_material_semi_transparent);
        }




    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder)
    {
        super.onViewDetachedFromWindow(holder);
        holder.img.setImageBitmap(null);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.img.setImageBitmap(null);
        if (Currentmenu != null)
            Currentmenu.close(true);
    }

    public ArrayList<CardInformation> getmCardInformation() {
        return mCardInformation;
    }

    public void LoadMore() {
        mGenerateLiquors.LoadMore();
    }

    public void setmCardInformation(ArrayList<CardInformation> mCardInformation)
    {
        this.mCardInformation = mCardInformation;
    }

    public void setModify(boolean Modify)
    {
        isModify = Modify;
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
       // int position;

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


            FloatingActionMenu itemMenu = new FloatingActionMenu.Builder(context)
                    .setStartAngle(0)
                    .setEndAngle(-180)
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
            menus.add(itemMenu);
        }

    /* public void setPosition(int position)
     {
         this.position = position;
     }*/



        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ACTION_BUTTON_ONE:
                    //OnRemoveItem();
                   /* if(Tile.getTag() != null)
                    {
                        mDB.delete(((Liquor) ((CardInformation) Tile.getTag()).getmObjectArray()[0]).getLiquorName());
                        OnRemoveItem(((Liquor) ((CardInformation) Tile.getTag()).getmObjectArray()[0]).getLiquorId());
                    }*/

                    OnRemoveItem(((CardInformation) Tile.getTag()).getCardPosition());
                    for (FloatingActionMenu Iteratemenu : menus) {
                        if (Iteratemenu.isOpen())
                            Iteratemenu.close(true);
                    }
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
                        mDialogView = context.getLayoutInflater().inflate(R.layout.liquor_information_dialog_big, null);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setView(mDialogView);
                        dialog.setCancelable(true);
                        dialog.setOnCancelListener(this);
                        ((TextView) mDialogView.findViewById(R.id.liquor_name)).setText(((Liquor)((CardInformation) v.getTag()).getmObjectArray()[0]).getLiquorName());
                        mImageLoader.DisplayImage(((Liquor)((CardInformation) v.getTag()).getmObjectArray()[0]).getLiquorPictureURL(), ((ImageView) mDialogView.findViewById(R.id.info_picture)), ((Liquor)((CardInformation) v.getTag()).getmObjectArray()[0]).getLiquorName());
                        mDialogView.findViewById(R.id.left_border).setBackgroundColor(Color.parseColor(((CardInformation) v.getTag()).getTileColor()));
                        AlertDialog al = dialog.show();

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(al.getWindow().getAttributes());
                        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, windowMetrics);
                       // lp.gravity = Gravity.CENTER;
                       al.getWindow().setAttributes(lp);
                    }

                    break;
            }
        }

     @Override
     public void onCancel(DialogInterface dialog)
     {
            mDialogView = null;
            dialog.dismiss();
     }

     @Override
     public void onMenuOpened(FloatingActionMenu floatingActionMenu)
     {
         for (FloatingActionMenu Iteratemenu : menus) {
             if (Iteratemenu.isOpen())
                Iteratemenu.close(true);
         }

        /* if(Currentmenu !=null)
             Currentmenu.close(true);*/
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
