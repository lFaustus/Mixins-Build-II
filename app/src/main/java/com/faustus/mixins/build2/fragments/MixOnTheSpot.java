package com.faustus.mixins.build2.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.faustus.mixins.build2.Bottle;
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
        args.putString(CreateLiquor.FRAGMENT_TAG, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.liquor_image_select).setVisibility(View.GONE);
        ((Button)getView().findViewById(R.id.button_drinks)).setText("Mix");
    }

    @Override
    public void onClick(View v)
    {
        //Implement some functions
        switch(v.getId())
        {
            default:
                //PopUpDialog(view,parent,position);
                View dialogView = super.PopupDialoglayoutCreator();
                super.mTextView = (TextView)v;
                super.mEditText.setHint("Liquor Name Here");
                AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(getActivity())
                        .setView(dialogView)
                        .setPositiveButton("OK", (dialog, which) ->
                        {
                           super. mTextView.setText(mEditText.getText());
                            SharedPreferences.Editor editor = super.sp.edit();
                            Bottle b = (Bottle)v.getTag();
                            editor.putString(b.name(),((TextView)v).getText().toString());
                            editor.commit();
                           super.mCurrentBottleSettings.put(b,((TextView)v).getText().toString());
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setCancelable(true);
                mAlertBuilder.create().show();
                break;

        }
    }
}
