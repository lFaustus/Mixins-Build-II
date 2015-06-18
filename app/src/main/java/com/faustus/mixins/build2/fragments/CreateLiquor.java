package com.faustus.mixins.build2.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.circularseekbar.CircularSeekBar;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateLiquor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLiquor extends Fragment implements CircularSeekBar.OnCircularSeekBarChangeListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAGMENT_TAG = "CreateLiquor";
    private final String VIEWGROUP_CSEEKBAR_TAG = "viewgroup_circularseekbar";
    private final String BOTTLE_VOLUME = "VOLUME";
    private String mParam1;
    private String[] mListViewLabels;
    private ListView mListView;
    private Map<Bottle, TextView> mTextViewSeekBarValue = Collections.synchronizedMap(new HashMap<Bottle, TextView>());
    private Map<String, String> mOrder = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    private int mCounter = 0;
    private TextView mSeekBarTextViewValue;
    private Bottle[] mBottle;


    public CreateLiquor() {
        // Required empty public constructor
    }

    public static CreateLiquor newInstance(String param1) {
        CreateLiquor fragment = new CreateLiquor();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TAG, param1);
        fragment.setArguments(args);
        return fragment;
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
        View v = inflater.inflate(R.layout.mix_drinks_ui, container, false);
        mListView = (ListView) v.findViewById(R.id.liquor_listview_infos);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListViewLabels = getActivity().getResources().getStringArray(R.array.list_items);
        mListView.setAdapter(new ListViewAdapter());
        mBottle = Bottle.values();
        ViewGroup vg = (ViewGroup) getView().findViewWithTag(VIEWGROUP_CSEEKBAR_TAG);
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
        //Log.i("mOrder values", mOrder.values()+"");
    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {
        mSeekBarTextViewValue = mTextViewSeekBarValue.get(seekBar.getTag());
    }

    @Override
    public void onClick(View v) {
        Log.i("CLICKED", "CLICKED");
    }

    private enum Bottle {
        BOTTLE1(1),
        BOTTLE4(4),
        BOTTLE2(2),
        BOTTLE5(5),
        BOTTLE3(3),
        BOTTLE6(6);

        private final int mBottleValue;

        Bottle(int val) {
            mBottleValue = val;
        }

        public int getBottleValue() {
            return mBottleValue;
        }

    }

    private class ListViewAdapter extends ArrayAdapter<String> {
        ListViewAdapter() {
            super(getActivity(), R.layout.listview_items, R.id.listview_label_listitem, mListViewLabels);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ListViewHolder mListViewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.listview_items, parent, false);
                mListViewHolder = new ListViewHolder(view);
                view.setTag(mListViewHolder);
            }
            else {
                mListViewHolder = (ListViewHolder) view.getTag();
            }
            mListViewHolder.mLabel.setText(getItem(position));

            return view;
        }

        private class ListViewHolder {
            TextView mLabel;

            ListViewHolder(View v) {
                mLabel = (TextView) v.findViewById(R.id.listview_label_listitem);
            }
        }

    }
}
