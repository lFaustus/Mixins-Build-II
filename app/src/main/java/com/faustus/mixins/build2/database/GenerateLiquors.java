package com.faustus.mixins.build2.database;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.faustus.mixins.build2.model.Liquor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by flux on 6/1/15.
 */
public class GenerateLiquors
{
    private static int mCurrentTileType = -1;
    public static final int SMALL_TILE = 0;
    public static final int LONG_TILE = 1;
    public static final int BIG_TILE = 2;
    private static ArrayList<Liquor> liquorlist = null;
    private static boolean BigTile = false;
    private static int mExpectedTileType = -1;
    private static String[] materialPalette;
    private static int counter = 0;
    private static Activity mActivity;
    private static DB mDB;
    public static ArrayList<Liquor> generateDrinks(int number,ArrayList<Liquor> list)
    {

        //LoadDrinks(number,list);
        return liquorlist;
    }
    public static ArrayList<Liquor> generateDrinks(Activity activity)
    {
        mActivity = activity;
        LoadDrinks();
        return liquorlist;
    }


    public static ArrayList<Liquor> generateDrinks(ArrayList<Liquor> mTempArray)
    {
            ArrayList<Liquor> mArray = mTempArray;

            return mArray;
    }

    public static void LoadDrinks()
    {
        //if(liquorlist == null)
        //{
            fetchDrinksFromDB();
        //}

        RandomTileSize(liquorlist.size());

    }


    private static void RandomTileSize(int size)
    {

        for(;counter<size;counter++)
        {
            generateSize();
        }

    }

    private static void generateSize()
    {
        if(mCurrentTileType == BIG_TILE || mExpectedTileType == -1 )
        {
            do
            {
                mExpectedTileType = randomTile(3);
            } while (mCurrentTileType == mExpectedTileType);
        }

        switch(mExpectedTileType)
        {
            case BIG_TILE:
                mCurrentTileType = BIG_TILE;
                BigTile = false;
                //liquorlist.add(new Liquor("Liquor Number: " + counter, mCurrentTileType,randomTileColor()));
                liquorlist.get(counter).setTileColor(randomTileColor());
                liquorlist.get(counter).setTileType(mCurrentTileType);
                break;

            case SMALL_TILE:
                if(mCurrentTileType == LONG_TILE)
                    mExpectedTileType = SMALL_TILE;
                else if(mCurrentTileType == SMALL_TILE)
                    BigTile = true;
                else
                    mExpectedTileType = LONG_TILE;

                mCurrentTileType = SMALL_TILE;
                //liquorlist.add(new Liquor("Liquor Number: " + counter, mCurrentTileType,randomTileColor()));
                liquorlist.get(counter).setTileColor(randomTileColor());
                liquorlist.get(counter).setTileType(mCurrentTileType);
                if(BigTile)
                    mExpectedTileType = BIG_TILE;
                break;

            case LONG_TILE:
                if(mCurrentTileType == SMALL_TILE)
                    BigTile = true;

                mExpectedTileType = SMALL_TILE;
                mCurrentTileType = LONG_TILE;
                //liquorlist.add(new Liquor("Liquor Number: " + counter, mCurrentTileType,randomTileColor()));
                liquorlist.get(counter).setTileColor(randomTileColor());
                liquorlist.get(counter).setTileType(mCurrentTileType);
                break;
        }
    }




    private static void fetchDrinksFromDB()
    {
        mDB = new DB(mActivity);
        if(liquorlist == null)
            liquorlist = new ArrayList<Liquor>();
        Liquor mTempLiquor;
        Cursor cursor = mDB.selectTen();
        JSONObject mJsonObject;
        while(cursor.moveToNext())
        {
            try
            {
                mJsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(mDB.getDBColumns()[1])));
                mTempLiquor = new Liquor(mJsonObject);
                mTempLiquor.setLiquorId(cursor.getInt(cursor.getColumnIndex(mDB.getDBColumns()[0])));
                liquorlist.add(mTempLiquor);
                Log.i("liquorlist",liquorlist.size()+"");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        cursor.close();
    }



    /*public static void LoadMoreDrinks(int number,ArrayList<Liquor> liquors)
    {
       tileGenerator(number,liquors);

    }*/

    public static void setMaterialPalette(java.lang.String[] materialPalettes)
    {
        materialPalette = materialPalettes;
    }


    private static int randomTile(int number)
    {
        Random rand = new Random();
        int random = rand.nextInt(number);
        Log.i("random", random + "");
        return random;
    }

    private static String randomTileColor()
    {
        Random rand = new Random();
        return materialPalette[rand.nextInt(materialPalette.length)];

    }

}
