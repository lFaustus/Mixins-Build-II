package com.faustus.mixins.build2.fragments;


import android.os.Bundle;
import android.app.Fragment;
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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateLiquor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLiquor extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAGMENT_TAG = "CreateLiquor";
    private String mParam1;
    private String[] mListViewLabels;
    private ListView mListView;
    private Map<String,CircularSeekBar> mCircularSeekBarMap = Collections.synchronizedMap(new HashMap<String, CircularSeekBar>());


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
        View v = inflater.inflate(R.layout.mix_drinks_ui,container,false);
        mListView = (ListView)v.findViewById(R.id.liquor_listview_infos);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListViewLabels = getActivity().getResources().getStringArray(R.array.list_items);
        mListView.setAdapter(new ListViewAdapter());
    }

    private class ListViewAdapter extends ArrayAdapter<String>
    {
        ListViewAdapter()
        {
            super(getActivity(),R.layout.listview_items,R.id.listview_label_listitem,mListViewLabels);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ListViewHolder mListViewHolder = null;
            if(view == null)
            {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.listview_items,parent,false);
                mListViewHolder = new ListViewHolder(view);
                view.setTag(mListViewHolder);
            }
            else
            {
                mListViewHolder = (ListViewHolder)view.getTag();
            }
            mListViewHolder.mLabel.setText(getItem(position));

            return view;
        }

        private class ListViewHolder
        {
            TextView mLabel;
            ListViewHolder(View v)
            {
                mLabel  = (TextView)v.findViewById(R.id.listview_label_listitem);
            }
        }

    }
}
