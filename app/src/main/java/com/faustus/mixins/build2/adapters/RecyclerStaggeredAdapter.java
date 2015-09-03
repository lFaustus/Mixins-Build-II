package com.faustus.mixins.build2.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.os.Looper;
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
import com.faustus.mixins.build2.fragments.MainMenu;
import com.faustus.mixins.build2.loader.ImageLoaderEX;
import com.faustus.mixins.build2.model.CardInformation;
import com.faustus.mixins.build2.model.Liquor;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.software.shell.fab.ActionButton;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by flux on 5/26/15.
 */
public abstract class RecyclerStaggeredAdapter extends RecyclerView.Adapter<RecyclerStaggeredAdapter.ViewHolder> {

    public static FloatingActionMenu Currentmenu = null;
    private View mDialogView = null;
    private Activity context;
    //private final ArrayList<FloatingActionMenu> menus = new ArrayList<>();
    private ArrayList<CardInformation> mCardInformation;
    private DisplayMetrics windowMetrics;
    private static boolean isModify = false;
    public static GenerateLiquors mGenerateLiquors;
    private long mDateDiff;
    private static ImageLoaderEX mImageLoader;
    private checkDateThread mCheckDateThread;

    //private DB mDB;

    public RecyclerStaggeredAdapter(Activity activity,ArrayList<CardInformation>cardInformations) {
        mCardInformation = cardInformations;
        context = activity;
        if(mImageLoader == null)
        mImageLoader = new ImageLoaderEX(activity);
        //Log.e("CardSize", mCardInformation.size() + "");
        mGenerateLiquors = new GenerateLiquors(context, mCardInformation);
        windowMetrics = context.getResources().getDisplayMetrics();
        mCheckDateThread = new checkDateThread();
        mCheckDateThread.setPriority(Thread.NORM_PRIORITY-1);
        if(mCheckDateThread.getState() == Thread.State.NEW)
            mCheckDateThread.start();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_items, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
         Liquor mLiquor =  (Liquor) mCardInformation.get(position).getmObjectArray()[0];
        try
        {
            mImageLoader.DisplayImage(mLiquor.getLiquorPictureURI(),mLiquor.getLiquorName(),holder.img);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
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

        try
        {
            holder.txtview.setText(mLiquor.getLiquorName());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        holder.Tile_label.setBackgroundColor(Color.parseColor(mCardInformation.get(position).getTileColor()));
        mCardInformation.get(position).setCardPosition(position);
        holder.Tile.setTag(mCardInformation.get(position));
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
        mCheckDateThread.mHandler.post(new checkDateRunnable(mLiquor, holder.img, holder.txtviewModeIndicator, holder.Tile));
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder)
    {
        if(Currentmenu != null)
            if (Currentmenu.isOpen())
            {
                Currentmenu.close(false);
                Currentmenu = null;
            }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.img.setImageBitmap(null);
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
        return isModify;
    }

    public checkDateThread getCheckDateThread()
    {
        return mCheckDateThread;
    }

    public final class checkDateThread extends Thread
    {
        Handler mHandler;
        public checkDateThread(Runnable runnable)
        {
            super(runnable);
        }

        public checkDateThread()
        {
            super();
        }

        public Handler getHandler()
        {
            return mHandler;
        }

        @Override
        public void run()
        {
            Looper.prepare();
            mHandler = new Handler();
            Looper.loop();

        }
    }

   public final class checkDateRunnable implements Runnable
    {
        Liquor mLiquor;
        ImageView mImageView;
        TextView mTextViewRibbon;
        CardView mCardView;
        public checkDateRunnable(Liquor liquor, View... views)
        {
            mLiquor = liquor;
            for(View v: views)
            {
                if(v instanceof ImageView)
                    mImageView = (ImageView) v;
                else if (v instanceof TextView)
                    mTextViewRibbon = (TextView) v;
                else if(v instanceof  CardView)
                    mCardView = (CardView) v;

            }
        }

        @Override
        public void run()
        {
                for (String s : mLiquor.getIngredients())
                    if (!MainMenu.getCurrentBottleSettings().containsValue(s))
                    {
                        ColorMatrix cm = new ColorMatrix();
                        cm.setSaturation(0);
                        mImageView.post(() ->
                        {
                            mImageView.setColorFilter(new ColorMatrixColorFilter(cm));
                            mImageView.invalidate();
                        });
                        break;
                    }

            try
            {
                SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date mTempDate = mSdf.parse(mSdf.format(new Date()));
                Date mTempDate2 = mSdf.parse(mLiquor.getDateAdded());
                //long mDateDiff1 = ((mTempDate2.getTime() - mTempDate.getTime()) / (1000 * 60 * 60 * 24));
                RecyclerStaggeredAdapter.this.mDateDiff = ((mTempDate2.getTime() - mTempDate.getTime()) / (1000 * 60 * 60 * 24));
                if(RecyclerStaggeredAdapter.this.isModify())
                {
                    mTextViewRibbon.post(() -> {
                        mTextViewRibbon.setVisibility(View.VISIBLE);
                        mTextViewRibbon.setText("Edit Mode");
                        mTextViewRibbon.setBackgroundResource(R.color.green_material_semi_transparent);
                        mTextViewRibbon.setClickable(true);
                        mTextViewRibbon.setClickable(false);
                        mTextViewRibbon.invalidate();
                    });
                    mImageView.setClickable(true);
                    mCardView.setClickable(false);
                }
                if(!RecyclerStaggeredAdapter.this.isModify())
                {
                    mTextViewRibbon.post(() ->
                    {
                        mTextViewRibbon.setText("New");
                        mTextViewRibbon.setBackgroundResource(R.color.red_material_semi_transparent);
                        mTextViewRibbon.setVisibility(View.VISIBLE);
                        if(RecyclerStaggeredAdapter.this.mDateDiff >= 30)
                        {
                            mTextViewRibbon.setVisibility(View.INVISIBLE);
                        }
                        mTextViewRibbon.invalidate();
                    });

                    if(Currentmenu != null)
                        if(Currentmenu.isOpen())
                        {
                            context.runOnUiThread(() ->
                                    {
                                        Currentmenu.close(false);
                                        RecyclerStaggeredAdapter.Currentmenu = null;
                                    });
                        }
                    mImageView.setClickable(false);
                    mCardView.setClickable(true);
                }
                //Log.e("ismodify",RecyclerStaggeredAdapter.this.isModify()+"");
                Log.i("DATEDIFF", RecyclerStaggeredAdapter.this.mDateDiff + "");
            }catch(ParseException | JSONException exp)
            {
                exp.printStackTrace();
            }


        }
    }

 class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,DialogInterface.OnCancelListener,FloatingActionMenu.MenuStateChangeListener {
        ImageView img;
        TextView txtview,txtviewModeIndicator;
        CardView Tile;
        FrameLayout Tile_label;

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
            //menus.add(itemMenu);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ACTION_BUTTON_ONE:
                    OnRemoveItem(((CardInformation) Tile.getTag()).getCardPosition());
                    if(Currentmenu != null)
                        if (Currentmenu.isOpen())
                        {
                            Currentmenu.close(false);
                            Currentmenu = null;
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
                        try
                        {
                            ((TextView) mDialogView.findViewById(R.id.liquor_name)).setText(((Liquor)((CardInformation) v.getTag()).getmObjectArray()[0]).getLiquorName());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        try
                        {
                            mImageLoader.DisplayImage(((Liquor)((CardInformation) v.getTag()).getmObjectArray()[0]).getLiquorPictureURI(), ((Liquor)((CardInformation) v.getTag()).getmObjectArray()[0]).getLiquorName(),((ImageView) mDialogView.findViewById(R.id.info_picture)));
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        mDialogView.findViewById(R.id.left_border).setBackgroundColor(Color.parseColor(((CardInformation) v.getTag()).getTileColor()));
                        AlertDialog al = dialog.show();

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(al.getWindow().getAttributes());
                        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, windowMetrics);
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
         if(Currentmenu != null)
            if(Currentmenu.isOpen())
            {
                Currentmenu.close(false);
            }

         //if animation is enable, it closes all possible opened menu which
         //was failed to close maybe due to opening another menu
         //while the animation and removal of view(s) was still on-going
         /*for (FloatingActionMenu Iteratemenu : menus) {
             if (Iteratemenu.isOpen())
             {
                 Iteratemenu.close(true);
             }
         }*/
         Currentmenu = floatingActionMenu;
         floatingActionMenu.getSubActionItems().get(0).view.setTag(txtview.getTag());
     }

     @Override
     public void onMenuClosed(FloatingActionMenu floatingActionMenu)
     {
        // if(Currentmenu == floatingActionMenu)
            Currentmenu = null;
     }
 }
    public abstract void OnRemoveItem(int position);
}
