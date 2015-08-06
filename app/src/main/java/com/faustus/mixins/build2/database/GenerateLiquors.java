package com.faustus.mixins.build2.database;

import android.app.Activity;
import android.util.Log;

import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.model.Liquor;

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
    private  ArrayList<Liquor> liquorlist;
    private static boolean BigTile = false;
    private static int mExpectedTileType = -1;
    private static String[] materialPalette;
    private static int counter = 0;
    private DB mDB;
    private static int mDBOffset = 0;

    public GenerateLiquors(Activity activity,ArrayList<Liquor> liquorlist)
   {
       mDB = new DB(activity);
       this.liquorlist = liquorlist;
       materialPalette = activity.getResources().getStringArray(R.array.material_palette);
       if(this.liquorlist.size() == 0)
            LoadMore();

   }
    public void LoadMore()
    {
        mDBOffset = liquorlist.size();
        mDB.select(mDBOffset, this.liquorlist);
        RandomTileSize(this.liquorlist);

    }

    private void RandomTileSize(ArrayList<Liquor>liquorlist)
    {

        for(;counter<liquorlist.size();counter++)
        {
            generateSize(liquorlist);
        }

    }

    private void generateSize(ArrayList<Liquor> liquorlist)
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

    public void setMaterialPalette(java.lang.String[] materialPalettes)
    {
        materialPalette = materialPalettes;
    }


    private int randomTile(int number)
    {
        Random rand = new Random();
        int random = rand.nextInt(number);
        Log.i("random", random + "");
        return random;
    }

    private String randomTileColor()
    {
        Random rand = new Random();
        return materialPalette[rand.nextInt(materialPalette.length)];

    }

}
