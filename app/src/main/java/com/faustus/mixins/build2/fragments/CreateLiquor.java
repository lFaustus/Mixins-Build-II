package com.faustus.mixins.build2.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faustus.mixins.build2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateLiquor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLiquor extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAGMENT_TAG = "CreateLiquor";

    // TODO: Rename and change types of parameters
    private String mParam1;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment CreateLiquor.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateLiquor newInstance(String param1) {
        CreateLiquor fragment = new CreateLiquor();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TAG, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateLiquor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(FRAGMENT_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.mix_drinks_ui,container,false);
    }


}
