package com.faustus.mixins.build2.database;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import com.faustus.mixins.build2.model.Liquor;

/**
 * Created by flux on 6/1/15.
 */
public class GenerateLiquors
{
    private static int mLastTileType = -1;
    public static final int SMALL_TILE = 0;
    public static final int LONG_TILE = 1;
    public static final int BIG_TILE = 2;
    private static ArrayList<Liquor> liquorlist = null;
    private static boolean BigTile = false;
    private static int temp = -1;
    private static String[] materialPalette;

    public static ArrayList<Liquor> generateDrinks(int number)
    {

        LoadDrinks(number,liquorlist);
        return liquorlist;
    }

    /*public static void LoadMoreDrinks(int number,ArrayList<Liquor> liquors)
    {
       tileGenerator(number,liquors);

    }*/

    public static void setMaterialPalette(java.lang.String[] materialPalettes)
    {
        materialPalette = materialPalettes;
    }

    public static void LoadDrinks(int number,ArrayList<Liquor> liquors )
    {

        int i;

        if(liquors == null)
        {
            liquors = new ArrayList<>();
            liquorlist = liquors;
            i = 0;


        }
        else
            i = liquors.size();

        for(; i<number; i++)
        {

            if (mLastTileType == -1 || temp == BIG_TILE)
            {
                do
                {
                    temp = randomTile(3);

                } while ((mLastTileType == temp && i!=0) || mLastTileType == temp);
                mLastTileType = temp;

            }



            if(mLastTileType == SMALL_TILE)
            {
                if(BigTile)
                {
                    liquors.add(new Liquor("Liquor Number: " + i, mLastTileType,randomTileColor()));
                    mLastTileType = BIG_TILE;
                    //temp = mLastTileType;
                }
                else
                {
                    if (temp == LONG_TILE)
                    {
                        temp = mLastTileType;
                        mLastTileType = SMALL_TILE;
                        liquors.add(new Liquor("Liquor Number: " + i, mLastTileType,randomTileColor()));
                        BigTile = true;
                    } else
                    {
                            liquors.add(new Liquor("Liquor Number: " + i, mLastTileType,randomTileColor()));
                            temp = SMALL_TILE;
                            mLastTileType = LONG_TILE;

                    }


                }
            }
            else
            {
                if (mLastTileType == LONG_TILE)
                {
                    if (temp == SMALL_TILE)
                    {
                        liquors.add(new Liquor("Liquor Number: " + i, mLastTileType,randomTileColor()));
                        mLastTileType = SMALL_TILE;
                        BigTile = true;
                    } else
                    {
                        liquors.add(new Liquor("Liquor Number: " + i, mLastTileType,randomTileColor()));
                        temp = LONG_TILE;
                        mLastTileType = SMALL_TILE;

                    }
                }
                else
                {
                    temp = mLastTileType;
                    liquors.add(new Liquor("Liquor Number: " + i, mLastTileType,randomTileColor()));
                    BigTile = false;
                   /* do
                    {
                        temp = randomTile(2);

                    } while (mLastTileType == temp);*/
                    //mLastTileType = temp;
                }
            }

        }
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
