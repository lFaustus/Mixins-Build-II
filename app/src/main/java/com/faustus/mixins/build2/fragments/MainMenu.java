package com.faustus.mixins.build2.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.faustus.mixins.build2.Bottle;
import com.faustus.mixins.build2.Fragments;
import com.faustus.mixins.build2.OnFragmentChangeListener;
import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.adapters.EndlessStaggeredRecyclerOnScrollListener;
import com.faustus.mixins.build2.adapters.RecyclerStaggeredAdapter;
import com.faustus.mixins.build2.animation.PopupFloatingActionButtonAnimation;
import com.faustus.mixins.build2.database.DB;
import com.faustus.mixins.build2.database.GenerateLiquors;
import com.faustus.mixins.build2.model.CardInformation;
import com.faustus.mixins.build2.model.Liquor;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.software.shell.fab.ActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

//import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * * Use the {@link MainMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenu extends Fragment implements View.OnClickListener
{
    private static final String FRAGMENT_TAG;
    private static final String LIQUORS_TAG;
    private String mParam1;
    private RecyclerView recyclerStaggeredView;
    private StaggeredGridLayoutManager stgv;
    private RecyclerStaggeredAdapter recyclerAdapter;
    private ActionButton mFabButton;
    private PopupFloatingActionButtonAnimation mFabAnimation;
    private OnFragmentChangeListener mListener;
    private ArrayList<CardInformation> mCardInformation;
    private static Map<Bottle,String> mCurrentBottleSettings = Collections.synchronizedMap(new LinkedHashMap<Bottle, String>());
   // private static boolean mAlreadyLoaded = false;
    //private static ViewGroup.LayoutParams mCopyParams;
    private static  Bottle[] mBottle;
    private DB mDB;
    protected static SharedPreferences sp;

    static
    {
        LIQUORS_TAG = "LIQUORS";
        FRAGMENT_TAG = "MainMenu";
    }
    public MainMenu()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MainMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenu newInstance(String param1)
    {
        MainMenu fragment = new MainMenu();
        Bundle   args     = new Bundle();
        args.putString(FRAGMENT_TAG, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            this.mParam1 = getArguments().getString(FRAGMENT_TAG);
            Log.e("OnCreate","OnCreate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.staggeredgridview, container, false);
        this.recyclerStaggeredView = (RecyclerView) view.findViewById(R.id.staggeredgridview);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initializeFloatingMenuAndButtons();
        sp = getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.shared_preference_name), Context.MODE_PRIVATE);
        sp.edit().apply();
        mBottle = Bottle.values();
        checkCurrentBottle();
        mDB = new DB(getActivity());
        this.stgv = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        this.stgv.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

                if(savedInstanceState == null)
                {
                    mCardInformation = getArguments().getParcelableArrayList(LIQUORS_TAG);
                    if(mCardInformation == null)
                    {
                        mCardInformation = new ArrayList<>();
                        getArguments().putParcelableArrayList(LIQUORS_TAG,mCardInformation);
                    }
                    //assert mCardInformation != null;
                    //Log.e("mCardInfo",mCardInformation.size()+"");
                }
                else
                    mCardInformation =  savedInstanceState.getParcelableArrayList(LIQUORS_TAG);
               // if(recyclerAdapter == null)
               //{
                    recyclerAdapter = new RecyclerStaggeredAdapter(getActivity(), mCardInformation)
                    {
                        @Override
                        public void OnRemoveItem(int position)
                        {
                            stgv.removeViewAt(position);
                            mDB.delete(((Liquor) this.getmCardInformation().get(position).getmObjectArray()[0]).getLiquorId());
                            this.getmCardInformation().remove(position);
                            for (CardInformation card : this.getmCardInformation())
                            {

                                if (position < card.getCardPosition())
                                {
                                    card.setCardPosition(card.getCardPosition() - 1);
                                }
                            }
                            this.notifyItemRemoved(position);
                            GenerateLiquors.counter -= 1;

                        }
                    };

                    Toast.makeText(getActivity(), "Second Time Loading list", Toast.LENGTH_SHORT).show();
               // }
            //}


        recyclerStaggeredView.setAdapter(recyclerAdapter);
        recyclerStaggeredView.setLayoutManager(stgv);
        recyclerStaggeredView.addOnScrollListener(new EndlessStaggeredRecyclerOnScrollListener(stgv,getActivity())
        {

            @Override
            public void OnScrolled(int FirstVisibleItem, int VisibleItemCount, int TotalItemCount,int LastTotalItemCount)
            {
              /*Log.i("OnLoadMore ","LastCount: " +LastTotalItemCount+"");;
                if(LastTotalItemCount<=TotalItemCount && LastTotalItemCount != 0)
                    recyclerStaggeredView.smoothScrollToPosition(TotalItemCount);*/
                if (RecyclerStaggeredAdapter.Currentmenu != null)
                {
                    RecyclerStaggeredAdapter.Currentmenu.updateItemPositions();
                }

            }

            @Override
            public void OnLoadMore(int page)
            {
              new LoadMoreTask(page).execute();
            }

            @Override
            public void OnScrollStateChanged(int previous_state, int newState)
            {
                //Log.e("RecyclerView",recyclerView.getCh);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    mFabAnimation.setAnimationDuration(400)
                            .setTranslationX(0)
                            .setInterpolator(new OvershootInterpolator(0.9F))
                            .setStartDelay(10);
                }
                //  mFabButton.moveUp(50F);
                else if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    mFabAnimation.setAnimationDuration(400)
                            .setTranslationX(-100)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setStartDelay(10);
                }

                if (previous_state == EndlessStaggeredRecyclerOnScrollListener.PREVIOUS_SCROLL_STATE_DEFAULT ||
                        newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    mFabAnimation.startAnimation();
                    Log.i("OnScrollStateChanged", newState + "");

                }
            }
        });


    }

    private void initializeFloatingMenuAndButtons()
    {
        ViewGroup vg = (ViewGroup) getView().findViewWithTag("floatingactionsmenu");
        this.mFabAnimation = new PopupFloatingActionButtonAnimation(vg);
        //Log.i("ChildCount", vg.getChildCount() + "");
        for (int i = 0; i < vg.getChildCount(); i++)
        {
            if (vg.getChildAt(i).getTag() != null)
                if (vg.getChildAt(i) instanceof FloatingActionButton && vg.getChildAt(i).getTag().equals("fab"))
                {
                    vg.getChildAt(i).setOnClickListener(this);
                }

        }

    }

    private void checkCurrentBottle()
    {
        for(Bottle b: mBottle)
        {
            mCurrentBottleSettings.put(b,sp.getString(b.name(),getActivity().getResources().getString(R.string.liquor_label_default_value)));
        }
    }

    public static Map<Bottle, String> getCurrentBottleSettings()
    {
        return mCurrentBottleSettings;
    }

    public static SharedPreferences getSharedPreferences()
    {
        return sp;
    }

    public static Bottle[] getBottles()
    {
        return mBottle;
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
       // outState.putParcelableArrayList(LIQUORS_TAG,mCardInformation);
       outState.putParcelableArrayList(LIQUORS_TAG, recyclerAdapter.getmCardInformation());

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            this.mListener = (OnFragmentChangeListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        this.mListener = null;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        //recyclerAdapter = null;
    }

    @Override //Onclick Listener for Floating Side Buttons
    public void onClick(View v)
    {
        if (mListener != null)
            switch (v.getId())
            {

                case R.id.floating_side_button_1:
                    this.mListener.OnFragmentChange(Fragments.CREATELIQUOR);
                    break;
                case R.id.floating_side_button_2:
                    this.mListener.OnFragmentChange(Fragments.MIXONTHESPOT);
                    break;
                case R.id.floating_side_button_3:
                    //View viewTemp,viewTemp2;
                    WeakReference<Liquor> mLiquor;
                    WeakReference<TextView> mTextViewRibbon;
                    WeakReference<CardView> mCardView;
                    WeakReference<ImageView> mImageView;

                    WeakReference<RecyclerStaggeredAdapter.checkDateThread> mCheckDateThread = new WeakReference<RecyclerStaggeredAdapter.checkDateThread>(recyclerAdapter.getCheckDateThread());
                    recyclerAdapter.setModify(!recyclerAdapter.isModify());


                    for(int i = 0; i < recyclerStaggeredView.getChildCount(); i++)
                    {

                        mCardView = new WeakReference<CardView>((CardView)recyclerStaggeredView.getChildAt(i).findViewById(R.id.card_view));
                        mLiquor = new WeakReference<Liquor>((Liquor)((CardInformation) mCardView.get().getTag()).getmObjectArray()[0]);
                        mTextViewRibbon = new WeakReference<TextView>((TextView)recyclerStaggeredView.getChildAt(i).findViewById(R.id.edit_mode_tag));
                        mImageView = new WeakReference<ImageView>((ImageView)recyclerStaggeredView.getChildAt(i).findViewById(R.id.imageLiquor));
                        mCheckDateThread.get().getHandler().post(recyclerAdapter.new checkDateRunnable(mLiquor.get(),mTextViewRibbon.get(),mImageView.get(),mCardView.get()));
                    }
                    break;
            }
    }

    class LoadMoreTask extends AsyncTask<Void,Void,Void>
    {
        int page;

        public LoadMoreTask(int page)
        {
            this.page = page;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            recyclerAdapter.LoadMore();
            recyclerAdapter.notifyItemInserted(page);
            super.onPostExecute(aVoid);
        }

    }
}
