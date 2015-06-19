package com.faustus.mixins.build2.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.faustus.mixins.build2.Bottle;
import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.circularseekbar.CircularSeekBar;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MixOnTheSpot#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MixOnTheSpot extends Fragment implements View.OnClickListener,CircularSeekBar.OnCircularSeekBarChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "MixOnTheSpot";
    private final String BOTTLE_VOLUME = "VOLUME";
    private final String VIEWGROUP_CSEEKBAR_TAG = "viewgroup_circularseekbar";
    private Map<Bottle, TextView> mTextViewSeekBarValue = Collections.synchronizedMap(new HashMap<Bottle, TextView>());
    private Map<String, String> mOrder = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    private int mCounter = 0;
    private Bottle[] mBottle;
    private TextView mSeekBarTextViewValue;
    // TODO: Rename and change types of parameters
    private String mParam1;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment MixOnTheSpot.
     */
    // TODO: Rename and change types and number of parameters
    public static MixOnTheSpot newInstance(String param1) {
        MixOnTheSpot fragment = new MixOnTheSpot();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MixOnTheSpot() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mix_drinks_ui,container,false);
        v.findViewById(R.id.liquor_listview_infos).setVisibility(View.GONE);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewGroup vg = (ViewGroup) getView().findViewWithTag(VIEWGROUP_CSEEKBAR_TAG);
        mBottle = Bottle.values();
        initializeCircularSeekBar(vg);
    }

    private void initializeCircularSeekBar(ViewGroup vg) {
        try {
            for (int i = 0; i < vg.getChildCount(); i++) {

                if (vg.getChildAt(i) instanceof ViewGroup) {
                    initializeCircularSeekBar((ViewGroup) vg.getChildAt(i));
                }
                else {
                    if (vg.getChildAt(i) instanceof CircularSeekBar) {
                        vg.getChildAt(i).setTag(mBottle[mCounter]);
                        ((CircularSeekBar) vg.getChildAt(i)).setOnSeekBarChangeListener(this);

                    }
                    if (vg.getChildAt(i) instanceof TextView) {
                        vg.getChildAt(i).setOnClickListener(this);
                        if (vg.getChildAt(i).getTag() != null) {
                            mTextViewSeekBarValue.put(mBottle[mCounter], (TextView) vg.getChildAt(i));
                            mCounter++;
                        }

                    }
                }

            }
        } catch (NullPointerException exp) {
            Log.e("Null", "ViewGroup is Null");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Log.i("CLICKED", "CLICKED");
    }

    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
        mSeekBarTextViewValue.setText(progress + "ml");
    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {

        Bottle bottle = (Bottle) seekBar.getTag();
        if (mOrder.get(bottle.name()) == null && seekBar.getProgress() != 0) {
            mOrder.put(bottle.name(), String.valueOf(bottle.getBottleValue()));
            mOrder.put(bottle.name() + BOTTLE_VOLUME, String.valueOf(seekBar.getProgress()));
        }
        else if (mOrder.get(bottle.name()) != null && seekBar.getProgress() >= 0) {
            if (seekBar.getProgress() == 0) {
                mOrder.remove(bottle.name());
                mOrder.remove(bottle.name() + BOTTLE_VOLUME);
            }
            else {
                mOrder.put(bottle.name() + BOTTLE_VOLUME, String.valueOf(seekBar.getProgress()));
            }
        }
        Log.i("mOrder values", mOrder.values()+"");
    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {
        mSeekBarTextViewValue = mTextViewSeekBarValue.get(seekBar.getTag());
    }
}
