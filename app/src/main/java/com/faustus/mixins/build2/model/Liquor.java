package com.faustus.mixins.build2.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flux on 5/26/15.
 */
public class Liquor implements Parcelable
{


    private int Liquor_Id;
    private String Liquor_Name;
    private String Liquor_Picture_URL;
    private String Liquor_Description;
    private int TileType;
    private String TileColor;
    private final String JSONDB_LIQUOR_NAME = "liquor_name";
    private final String JSONDB_LIQUOR_PIC_URL = "liquor_url";
    private final String JSONDB_LIQUOR_DESCRIPTION = "liquor_description";
    private JSONObject JSONLiquor;

    public Liquor(String liquor_Name)
    {
        this.Liquor_Name = liquor_Name;
    }

    public Liquor(String liquor_Name,int tileType,String tileColor)
    {
        this. Liquor_Name = liquor_Name;
        this.TileType = tileType;
        this.TileColor = tileColor;
    }

    public Liquor()
    {

    }

    public Liquor(JSONObject JSONLiquor)
    {
        this.JSONLiquor = JSONLiquor;
        try
        {
            this.Liquor_Name = JSONLiquor.getString(JSONDB_LIQUOR_NAME);
            this.Liquor_Description = JSONLiquor.getString(JSONDB_LIQUOR_DESCRIPTION);
            this.Liquor_Picture_URL = JSONLiquor.getString(JSONDB_LIQUOR_PIC_URL);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }

    }


    private Liquor(Parcel parcel)
    {
       /* try
        {
            this.JSONLiquor = new JSONObject(parcel.readString());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }*/
        this.Liquor_Name = parcel.readString();
        this.Liquor_Description = parcel.readString();
        this.Liquor_Picture_URL = parcel.readString();
        this.TileType = parcel.readInt();
        this.TileColor = parcel.readString();
    }

    public static Parcelable.Creator<Liquor> Creator = new Creator<Liquor>()
    {
        @Override
        public Liquor createFromParcel(Parcel source)
        {
            return new Liquor(source);
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

    public String getLiquorName()
    {
        return Liquor_Name;
    }

    public void setLiquorName(String liquor_Name)
    {
        Liquor_Name = liquor_Name;
    }

    public String getLiquorPictureURL()
    {
        return Liquor_Picture_URL;
    }

    public void setLiquorPictureURL(String liquor_Picture_URL)
    {
        Liquor_Picture_URL = liquor_Picture_URL;
    }

    public String getLiquorDescription()
    {
        return Liquor_Description;
    }

    public void setLiquorDescription(String liquor_Description)
    {
        Liquor_Description = liquor_Description;
    }

    public String getTileColor()
    {
        return TileColor;
    }

    public void setTileColor(String tileColor)
    {
        TileColor = tileColor;
    }

    public void setTileType(int tileType)
    {
        TileType = tileType;
    }

    public int getTileType()
    {
        return TileType;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
       // dest.writeString(JSONLiquor.toString());
        dest.writeString(Liquor_Name);
        dest.writeString(Liquor_Description);
        dest.writeString(Liquor_Picture_URL);
        dest.writeInt(TileType);
        dest.writeString(TileColor);
    }

}
