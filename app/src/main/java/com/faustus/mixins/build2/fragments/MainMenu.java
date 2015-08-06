package com.faustus.mixins.build2.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.faustus.mixins.build2.ExtendedStaggeredGridLayoutManager;
import com.faustus.mixins.build2.Fragments;
import com.faustus.mixins.build2.OnFragmentChangeListener;
import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.adapters.EndlessStaggeredRecyclerOnScrollListener;
import com.faustus.mixins.build2.adapters.RecyclerStaggeredAdapter;
import com.faustus.mixins.build2.animation.PopupFloatingActionButtonAnimation;
import com.faustus.mixins.build2.model.Liquor;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;

//import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * * Use the {@link MainMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenu extends Fragment implements View.OnClickListener
{
    private static final String FRAGMENT_TAG = "MainMenu";
    private final String LIQUORS_TAG = "LIQUORS";
    private String mParam1;
    private RecyclerView recyclerStaggeredView;
    private ExtendedStaggeredGridLayoutManager stgv;
    private RecyclerStaggeredAdapter recyclerAdapter;
    private ActionButton mFabButton;
    private PopupFloatingActionButtonAnimation mFabAnimation;
    private OnFragmentChangeListener mListener;
    private boolean mStartAnimation = false;
    private static boolean mAlreadyLoaded = false;

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
        this.stgv = new ExtendedStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        this.stgv.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        if (savedInstanceState == null && !mAlreadyLoaded)
        {
            this.recyclerAdapter = new RecyclerStaggeredAdapter(getActivity(),new ArrayList<Liquor>())
            {
                @Override
                public void OnRemoveItem(int position)
                {
                    stgv.removeViewAt(position);
                    this.getLiquorItems().remove(position);
                    this.notifyDataSetChanged();
                }
            };
            Toast.makeText(getActivity(), "First Time Loading list", Toast.LENGTH_SHORT).show();
            mAlreadyLoaded  = true;
        }
        else if(savedInstanceState !=null)
        {
            this.recyclerAdapter = new RecyclerStaggeredAdapter(getActivity(),(ArrayList) savedInstanceState.getParcelableArrayList(LIQUORS_TAG))
            {
                @Override
                public void OnRemoveItem(int position)
                {
                    stgv.removeViewAt(position);
                    this.getLiquorItems().remove(position);
                    this.notifyDataSetChanged();
                }
            };
            Toast.makeText(getActivity(), "Second Time Loading list", Toast.LENGTH_SHORT).show();
        }

        this.recyclerStaggeredView.setAdapter(recyclerAdapter);
        this.recyclerStaggeredView.setLayoutManager(stgv);
        this.recyclerStaggeredView.addOnScrollListener(new EndlessStaggeredRecyclerOnScrollListener(stgv)
        {

            @Override
            public void OnScrolled(int FirstVisibleItem, int VisibleItemCount, int TotalItemCount,int LastTotalItemCount)
            {
//                Log.i("OnLoadMore ","LastCount: " +LastTotalItemCount+"");;
              /*  if(LastTotalItemCount<=TotalItemCount && LastTotalItemCount != 0)
                    recyclerStaggeredView.smoothScrollToPosition(TotalItemCount);*/
                if (RecyclerStaggeredAdapter.Currentmenu != null)
                {
                    RecyclerStaggeredAdapter.Currentmenu.updateItemPositions();
                }

            }

            @Override
            public void OnLoadMore(int page)
            {
                //Log.i("OnLoadMore ","Load MOre");
                recyclerAdapter.LoadMore();
                recyclerAdapter.notifyItemInserted(page);
            }

            @Override
            public void OnScrollStateChanged(int previous_state, int newState)
            {

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
                        previous_state == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    mStartAnimation = true;
                }

                if (mStartAnimation)
                {
                    mFabAnimation.startAnimation();
                    Log.i("OnScrollStateChanged", newState + "");
                    mStartAnimation = false;
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




    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
       outState.putParcelableArrayList(LIQUORS_TAG, recyclerAdapter.getLiquorItems());

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
                    View viewTemp;
                    this.recyclerAdapter.setModify(!this.recyclerAdapter.isModify());
                    for (int i = 0; i < recyclerStaggeredView.getChildCount(); i++)
                    {
                        viewTemp = recyclerStaggeredView.getChildAt(i).findViewById(R.id.edit_mode_tag);
                        if(((TextView)viewTemp).getText().equals("Edit Mode"))
                        {
                            ((TextView) viewTemp).setText("New");
                            viewTemp.setBackgroundColor(Color.parseColor("#B2FF5252"));
                        }
                        else
                        {
                            ((TextView) viewTemp).setText("Edit Mode");
                            viewTemp.setBackgroundResource(R.color.green_material_semi_transparent);
                        }
                      //  viewTemp.setVisibility(viewTemp.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                        viewTemp = recyclerStaggeredView.getChildAt(i).findViewById(R.id.imageLiquor);
                        if(!this.recyclerAdapter.isModify())
                            viewTemp.setClickable(false);
                        else
                            viewTemp.setClickable(true);
                        viewTemp = recyclerStaggeredView.getChildAt(i).findViewById(R.id.card_view);
                        if(this.recyclerAdapter.isModify())
                            viewTemp.setClickable(false);
                        else
                            viewTemp.setClickable(true);

                    }
                    break;
            }
    }
}
