package com.faustus.mixins.build2.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.faustus.mixins.build2.Bottle;
import com.faustus.mixins.build2.Fragments;
import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.adapters.RecyclerStaggeredAdapter;
import com.faustus.mixins.build2.circularseekbar.CircularSeekBar;
import com.faustus.mixins.build2.database.DB;
import com.faustus.mixins.build2.filechooser.FileChooser;
import com.faustus.mixins.build2.model.Liquor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateLiquor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLiquor extends Fragment implements View.OnClickListener,ListView.OnItemClickListener {

            // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected static final String FRAGMENT_TAG = "CreateLiquor";
    private final static String BOTTLE_VOLUME = "VOLUME";
    private String mImageLocation;
    private String mParam1;
    private String[] mListViewLabels;
    private ListView mListView;
    private Map<Bottle, TextView> mTextViewSeekBarValue = Collections.synchronizedMap(new HashMap<>());
    private Map<String, String> mOrder = Collections.synchronizedMap(new LinkedHashMap<>());
    protected Map<Bottle,String> mCurrentBottleSettings = Collections.synchronizedMap(new LinkedHashMap<Bottle, String>());
    private int mCounter = 0;
    protected Bottle[] mBottle;
    private ImageView imgView;
    private DB mDB;
    private JSONArray mJSONArrayLiquorOrder;
    protected EditText mEditText;
    protected TextView mTextView;
    protected  SharedPreferences sp;



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
        if(!mParam1.equals(Fragments.MIXONTHESPOT.getTAG()))
            mListView = (ListView) v.findViewById(R.id.liquor_listview_infos);
        imgView = (ImageView)v.findViewById(R.id.liquor_image);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!mParam1.equals(Fragments.MIXONTHESPOT.getTAG())) {

            mListViewLabels = getActivity().getResources().getStringArray(R.array.list_items);
            mListView.setAdapter(new ListViewAdapter());
            mListView.setOnItemClickListener(this);
            mDB = new DB(getActivity());
        }
            mBottle = MainMenu.getBottles();
            sp = MainMenu.getSharedPreferences();
            mCurrentBottleSettings = MainMenu.getCurrentBottleSettings();
            //ViewGroup vg = (ViewGroup) getView().findViewWithTag(VIEWGROUP_CSEEKBAR_TAG);
           ViewGroup vg = (ViewGroup)getView().getRootView();
        initializeViews(vg);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
            if(resultCode == Activity.RESULT_OK)
            {
                mImageLocation = data.getStringExtra("Image");
                try
                {
                    imgView.setImageBitmap(BitmapFactory.decodeFile(new URI(mImageLocation).getPath()));
                }
                catch (URISyntaxException e)
                {
                    e.printStackTrace();
                    imgView.setImageBitmap(null);
                }
            }
    }

    private void initializeViews(ViewGroup vg) {
        try {
            for (int i = 0; i < vg.getChildCount(); i++) {

                if (vg.getChildAt(i) instanceof ViewGroup) {
                    initializeViews((ViewGroup) vg.getChildAt(i));
                }
                else {
                    if(vg.getChildAt(i) instanceof com.software.shell.fab.ActionButton || vg.getChildAt(i) instanceof Button)
                    {
                        vg.getChildAt(i).setOnClickListener(this);
                    }

                   else  if (vg.getChildAt(i) instanceof CircularSeekBar) {
                        vg.getChildAt(i).setTag(mBottle[mCounter]);
                        ((CircularSeekBar) vg.getChildAt(i)).setOnSeekBarChangeListener(new SeekBarListener());

                    }
                   else  if (vg.getChildAt(i) instanceof TextView) {

                        if (vg.getChildAt(i).getTag() != null) {
                            mTextViewSeekBarValue.put(mBottle[mCounter], (TextView) vg.getChildAt(i));

                        }
                        else
                        {
                            vg.getChildAt(i).setOnClickListener(this);
                            vg.getChildAt(i).setTag(mBottle[mCounter]);
                            ((TextView) vg.getChildAt(i)).setText(mCurrentBottleSettings.get(mBottle[mCounter]));
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
       // Log.i("CLICKED", "CLICKED");
                switch(v.getId())
                {
                    case R.id.liquor_image_select:
                        Intent imgChooser = new Intent(getActivity(), FileChooser.class);
                        startActivityForResult(imgChooser,1);
                        break;

                    case R.id.button_drinks:
                        v.setEnabled(false);
                        JSONObject mJSONObjectLiquor = new JSONObject();

                        try
                        {
                            for(int i=0;i<mListView.getAdapter().getCount();i++)
                            {
                                mJSONObjectLiquor.put((String) mListView.getAdapter().getItem(i), String.valueOf(((TextView) mListView.getChildAt(i)
                                        .findViewById(R.id.listview_value_listitem))
                                        .getText()));
                            }
                            mJSONObjectLiquor.put(Liquor.JSONDB_LIQUOR_PIC_URL, mImageLocation);
                            for(Map.Entry<String,String> map: mOrder.entrySet())
                            {

                                if(!map.getKey().contains(BOTTLE_VOLUME))
                                    mJSONObjectLiquor.put(map.getKey(),mCurrentBottleSettings.get(Bottle.valueOf(map.getKey())));
                                else
                                    mJSONObjectLiquor.put(map.getKey(), map.getValue());
                            }
                            mJSONObjectLiquor.put("Order", mJSONArrayLiquorOrder);
                            String mDate = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(new Date());
                            mJSONObjectLiquor.put(Liquor.JSONDB_LIQUOR_DATE_ADDED,mDate);
                            mDB.insert(mJSONObjectLiquor);
                            RecyclerStaggeredAdapter.mGenerateLiquors.LoadMore();
                        }catch (JSONException exp)
                        {
                            Toast.makeText(getActivity(),"Opps Something went wrong",Toast.LENGTH_SHORT).show();
                            exp.printStackTrace();
                        }
                        finally
                        {
                            v.setEnabled(true);
                        }
                        break;

                    default:
                        //PopUpDialog(view,parent,position);
                        View dialogView = PopupDialoglayoutCreator();
                        mTextView = (TextView)v;
                        mEditText.setHint("Liquor Name Here");
                        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(getActivity())
                                .setView(dialogView)
                                .setPositiveButton("OK", (dialog, which) ->
                                {
                                    mTextView.setText(mEditText.getText());
                                    SharedPreferences.Editor editor = sp.edit();
                                    Bottle b = (Bottle)v.getTag();
                                    editor.putString(b.name(),((TextView)v).getText().toString());
                                    editor.commit();
                                    mCurrentBottleSettings.put(b,((TextView)v).getText().toString());
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                                .setCancelable(true);
                        mAlertBuilder.create().show();
                        break;
                }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Log.i("itemClick", parent.getItemAtPosition(position).toString());
        /*View mView = LayoutInflater.from(getActivity()).inflate(R.layout.liquor_dialog,null);
        final TextView mTextView = (TextView)view.findViewById(R.id.listview_value_listitem);
        final EditText mEditText =(EditText)mView.findViewById(R.id.liquor_dialog_editText);
        mEditText.setHint(parent.getItemAtPosition(position).toString() + " Here");
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(getActivity())
                            .setView(mView)
                            .setPositiveButton("OK", (dialog, which) -> mTextView.setText(mEditText.getText()))
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .setCancelable(true);
                            mAlertBuilder.create().show();*/
        //PopUpDialog(view,parent,position);
        View dialogView = PopupDialoglayoutCreator();
        mTextView = (TextView) view.findViewById(R.id.listview_value_listitem);
        mEditText.setHint(parent.getItemAtPosition(position).toString() + " Here");
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> mTextView.setText(mEditText.getText()))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(true);
        mAlertBuilder.create().show();
    }


    public View PopupDialoglayoutCreator()
    {
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.liquor_dialog,null);
        mEditText = (EditText)mView.findViewById(R.id.liquor_dialog_editText);
        return mView;
    }

    /*
    *Created a sub class that implements seekbar change listener instead of implementing the listener in main class
    * to prevent a textview change value bug wherein user tries to move the thumb of multiple seekbars
    * this class allows the seekbar to change their corresponding textview value even the user tries to move the thumb
    * of multiple seekbars
     */
    private class SeekBarListener implements  CircularSeekBar.OnCircularSeekBarChangeListener
    {
        private TextView mSeekBarTextViewValue;
        SeekBarListener(){}

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
           // Log.i("Order",mOrder.values()+"");
            mJSONArrayLiquorOrder = new JSONArray(mOrder.values());

        }

        @Override
        public void onStartTrackingTouch(CircularSeekBar seekBar) {
            mSeekBarTextViewValue = mTextViewSeekBarValue.get(seekBar.getTag());
        }
    }



    private class ListViewAdapter extends ArrayAdapter<String> {
        ListViewAdapter() {
            super(getActivity(), R.layout.listview_items, R.id.listview_label_listitem, mListViewLabels);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ListViewHolder mListViewHolder;
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
