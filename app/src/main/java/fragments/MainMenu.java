package fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.faustus.mixins.build2.R;

import java.util.ArrayList;

import adapters.EndlessStaggeredRecyclerOnScrollListener;
import adapters.RecyclerStaggeredAdapter;
import database.GenerateLiquors;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMenu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenu extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAGMENT_TAG = "MainMenu";
    private final String LIQUORS_TAG = "LIQUORS";

    // TODO: Rename and change types of parameters
    private String mParam1;
   // private String mParam2;
    private RecyclerView recyclerStaggeredView;
    private StaggeredGridLayoutManager stgv;
    private RecyclerStaggeredAdapter recyclerAdapter;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment MainMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenu newInstance(String param1) {
        MainMenu fragment = new MainMenu();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TAG, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(FRAGMENT_TAG);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.staggeredgridview,container,false);
        recyclerStaggeredView = (RecyclerView)view.findViewById(R.id.staggeredgridview);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState == null)
        {
            GenerateLiquors.setMaterialPalette(getActivity().getResources().getStringArray(R.array.material_palette));
            recyclerAdapter = new RecyclerStaggeredAdapter(getActivity(), GenerateLiquors.generateDrinks(11));
            Toast.makeText(getActivity(),"First Time Loading list", Toast.LENGTH_SHORT).show();
        }
        else
        {
            recyclerAdapter = new RecyclerStaggeredAdapter(getActivity(),savedInstanceState.getParcelableArrayList(LIQUORS_TAG));
        }
        recyclerStaggeredView.setAdapter(recyclerAdapter);
        stgv = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL);
        stgv.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerStaggeredView.setLayoutManager(stgv);
        recyclerStaggeredView.addOnScrollListener(new EndlessStaggeredRecyclerOnScrollListener(stgv)
        {
            @Override
            public void OnLoadMore(int page)
            {
                recyclerAdapter.LoadMore(page);
                recyclerAdapter.notifyItemInserted(page);

            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIQUORS_TAG,recyclerAdapter.getLiquorItems());

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}