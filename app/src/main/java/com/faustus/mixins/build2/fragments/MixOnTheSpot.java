package com.faustus.mixins.build2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faustus.mixins.build2.R;

/**
 * Created by flux on 6/27/15.
 */
public class MixOnTheSpot extends CreateLiquor{

    public MixOnTheSpot() {
    }

    public static MixOnTheSpot newInstance(String param)
    {
        MixOnTheSpot fragment = new MixOnTheSpot();
        Bundle args = new Bundle();
        args.putString(CreateLiquor.FRAGMENT_TAG,param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.liquor_image_select).setVisibility(View.GONE);
    }

}
