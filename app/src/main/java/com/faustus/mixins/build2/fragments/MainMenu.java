package com.faustus.mixins.build2.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.faustus.mixins.build2.ExtendedStaggeredGridLayoutManager;
import com.faustus.mixins.build2.Fragments;
import com.faustus.mixins.build2.MainActivity;
import com.faustus.mixins.build2.OnFragmentChangeListener;
import com.faustus.mixins.build2.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.faustus.mixins.build2.adapters.EndlessStaggeredRecyclerOnScrollListener;
import com.faustus.mixins.build2.adapters.RecyclerStaggeredAdapter;
import com.faustus.mixins.build2.database.GenerateLiquors;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMenu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenu extends Fragment implements View.OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAGMENT_TAG = "MainMenu";
    private final String LIQUORS_TAG = "LIQUORS";
    // TODO: Rename and change types of parameters
    private String mParam1;
    // private String mParam2;
    private RecyclerView recyclerStaggeredView;
    private ExtendedStaggeredGridLayoutManager stgv;
    private RecyclerStaggeredAdapter recyclerAdapter;

    private OnFragmentChangeListener mListener;

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
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TAG, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(FRAGMENT_TAG);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.staggeredgridview, container, false);
        recyclerStaggeredView = (RecyclerView) view.findViewById(R.id.staggeredgridview);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initializeFloatingMenuAndButtons();
        if (savedInstanceState == null)
        {
            GenerateLiquors.setMaterialPalette(getActivity().getResources().getStringArray(R.array.material_palette));
            recyclerAdapter = new RecyclerStaggeredAdapter(getActivity(), GenerateLiquors.generateDrinks(11));
            Toast.makeText(getActivity(), "First Time Loading list", Toast.LENGTH_SHORT).show();
        } else
        {
            recyclerAdapter = new RecyclerStaggeredAdapter(getActivity(), (ArrayList) savedInstanceState.getParcelableArrayList(LIQUORS_TAG));
        }
        recyclerStaggeredView.setAdapter(recyclerAdapter);
        stgv = new ExtendedStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        stgv.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerStaggeredView.setLayoutManager(stgv);
        recyclerStaggeredView.addOnScrollListener(new EndlessStaggeredRecyclerOnScrollListener(stgv)
        {
            @Override
            public void OnScrolled()
            {
                if (RecyclerStaggeredAdapter.Currentmenu != null)
                {
                    RecyclerStaggeredAdapter.Currentmenu.updateItemPositions();
                }
            }

            @Override
            public void OnLoadMore(int page)
            {
                recyclerAdapter.LoadMore(page);
                recyclerAdapter.notifyItemInserted(page);
            }
        });


    }

    private void initializeFloatingMenuAndButtons()
    {
        ViewGroup vg =(ViewGroup) getView().findViewWithTag("floatingactionsmenu");
        Log.i("ChildCount", vg.getChildCount() + "");
        for(int i = 0; i<vg.getChildCount(); i++)
        {
            try
            {
                if (vg.getChildAt(i) instanceof FloatingActionButton && vg.getChildAt(i).getTag().equals("fab"))
                {
                    vg.getChildAt(i).setOnClickListener(this);
                }
            }catch(NullPointerException exp)
            {
                Log.i("FABIndex Exception","Tag not set at Button Index "+i);
            }
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIQUORS_TAG, recyclerAdapter.getLiquorItems());

    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override //Onclick Listener for Floating Side Buttons
    public void onClick(View v)
    {
        if(mListener!=null)
            switch (v.getId())
            {
                case R.id.floating_side_button_1:
                    mListener.OnFragmentChange(Fragments.CREATELIQUOR);
                    break;
                case R.id.floating_side_button_2:
                    mListener.OnFragmentChange(Fragments.MIXONTHESPOT);
                    break;
                case R.id.floating_side_button_3:
                    Toast.makeText(getActivity(), "This is button 3", Toast.LENGTH_SHORT).show();
                    break;
            }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other com.faustus.mixins.build2.fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
