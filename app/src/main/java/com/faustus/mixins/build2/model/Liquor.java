package com.faustus.mixins.build2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.faustus.mixins.build2.Bottle;
import com.faustus.mixins.build2.fragments.MainMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by flux on 5/26/15.
 */
public class Liquor implements Parcelable
{
    private int Liquor_Id;
    /*private String Liquor_Name;
    private String Liquor_Picture_URL;
    private String Liquor_Description;
    private String DateAdded;
    private List<String> Ingredients;*/

    public final static String JSONDB_LIQUOR_NAME = "Name";
    public final static String JSONDB_LIQUOR_PIC_URL = "Image";
    public final static String JSONDB_LIQUOR_DESCRIPTION = "Description";
    public final static String JSONDB_LIQUOR_DATE_ADDED = "DateAdded";
    private JSONObject JSONLiquor;



    public Liquor(JSONObject JSONLiquors)
    {
        this.JSONLiquor = JSONLiquors;
    }


    private Liquor(Parcel parcel)
    {
        Liquor_Id = parcel.readInt();

        try
        {
            JSONLiquor = new JSONObject(parcel.readString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public static final Creator<Liquor> CREATOR = new Creator<Liquor>()
    {
        @Override
        public Liquor createFromParcel(Parcel in)
        {
            return new Liquor(in);
        }

        @Override
        public Liquor[] newArray(int size)
        {
            return new Liquor[size];
        }
    };

    public int getLiquorId()
    {
        return Liquor_Id;
    }

    public void setLiquorId(int liquor_Id)
    {
        Liquor_Id = liquor_Id;
    }

    public String getDateAdded() throws JSONException
    {
        return this.JSONLiquor.getString(JSONDB_LIQUOR_DATE_ADDED);
    }

    public ArrayList<String> getIngredients()
    {
        ArrayList<String> Ingredients  = new ArrayList<>();
        for(Bottle b:MainMenu.getBottles())
        {

            try
            {
                Ingredients.add(this.JSONLiquor.getString(b.name()));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return Ingredients;
    }

    public String getLiquorPictureURI() throws JSONException
    {
       return this.JSONLiquor.getString(JSONDB_LIQUOR_PIC_URL);
    }

    public String getLiquorName() throws JSONException
    {
        return this.JSONLiquor.getString(JSONDB_LIQUOR_NAME);
    }

    public String  getLiquorDescription() throws JSONException
    {
        return this.JSONLiquor.getString(JSONDB_LIQUOR_DESCRIPTION);
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.getLiquorId());
        dest.writeString(this.JSONLiquor.toString());
    }
}
