package com.faustus.mixins.build2;

/**
 * Created by flux on 6/19/15.
 */
public enum Fragments {
    CREATELIQUOR("CreateLiquor"),
    MIXONTHESPOT("MixOnTheSpot");
    private String TAG;
     Fragments(String t)
    {
        TAG = t;
    }
    public String getTAG()
    {
        return TAG;
    }
}
