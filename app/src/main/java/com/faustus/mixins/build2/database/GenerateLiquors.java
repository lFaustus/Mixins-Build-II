package com.faustus.mixins.build2.database;

import android.app.Activity;
import android.util.Log;

import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.model.CardInformation;

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
    private  ArrayList<CardInformation> cardInformations;
    private static boolean BigTile = false;
    private static int mExpectedTileType = -1;
    private static String[] materialPalette;
    public static int counter = 0;
    private DB mDB;
    private static int mDBOffset = 0;

    public GenerateLiquors(Activity activity,ArrayList<CardInformation> cardInformations)
   {
       mDB = new DB(activity);
       this.cardInformations = cardInformations;
       materialPalette = activity.getResources().getStringArray(R.array.material_palette);
       if(this.cardInformations.size() == 0)
       {
           counter = 0;
           mCurrentTileType = -1;
           mExpectedTileType = -1;
           LoadMore();
       }

   }
    public void LoadMore()
    {
        mDBOffset = cardInformations.size();
        Log.i("OFFSET SIZE",mDBOffset +"");
        mDB.select(mDBOffset, this.cardInformations);
        RandomTileSize(this.cardInformations);

    }

    private void RandomTileSize(ArrayList<CardInformation>cardInformations)
    {

        for(;counter<cardInformations.size();counter++)
        {
            generateSize(cardInformations);
        }

    }

    private void generateSize(ArrayList<CardInformation> cardInformations)
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
                //cardInformations.add(new Liquor("Liquor Number: " + counter, mCurrentTileType,randomTileColor()));
                cardInformations.get(counter).setTileColor(randomTileColor());
                cardInformations.get(counter).setTileType(mCurrentTileType);
                break;

            case SMALL_TILE:
                if(mCurrentTileType == LONG_TILE)
                    mExpectedTileType = SMALL_TILE;
                else if(mCurrentTileType == SMALL_TILE)
                    BigTile = true;
                else
                    mExpectedTileType = LONG_TILE;

                mCurrentTileType = SMALL_TILE;
                //cardInformations.add(new Liquor("Liquor Number: " + counter, mCurrentTileType,randomTileColor()));
                cardInformations.get(counter).setTileColor(randomTileColor());
                cardInformations.get(counter).setTileType(mCurrentTileType);
                if(BigTile)
                    mExpectedTileType = BIG_TILE;
                break;

            case LONG_TILE:
                if(mCurrentTileType == SMALL_TILE)
                    BigTile = true;

                mExpectedTileType = SMALL_TILE;
                mCurrentTileType = LONG_TILE;
                //cardInformations.add(new Liquor("Liquor Number: " + counter, mCurrentTileType,randomTileColor()));
                cardInformations.get(counter).setTileColor(randomTileColor());
                cardInformations.get(counter).setTileType(mCurrentTileType);
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
