package model;

import android.util.Log;

import org.apache.http.cookie.SM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by flux on 5/26/15.
 */
public class Liquor
{
    private String Liquor_Name;
    private int TileType;
    public static final int SMALL_TILE = 0;
    public static final int LONG_TILE = 1;
    public static final int BIG_TILE = 2;


    public Liquor(String liquor_Name)
    {
        this.Liquor_Name = liquor_Name;
    }

    public Liquor(String liquor_Name,int tileType)
    {
        this. Liquor_Name = liquor_Name;
        this.TileType = tileType;

    }

    public Liquor()
    {

    }

    public String getLiquor_Name()
    {
        return Liquor_Name;
    }

    public void setLiquor_Name(String liquor_Name)
    {
        Liquor_Name = liquor_Name;
    }

    public int getTileType()
    {
        return TileType;
    }

    public void setTileType(int tileType)
    {
        TileType = tileType;
    }

    public static List<Liquor> getAllLiquors(int number)
    {
        int previousTileType = -1,count =0;
        boolean bigFlag = false;
        int random = -1;

        List<Liquor> liquors = new ArrayList<>();
        for(int i=0 ; i<number ; i++)
        {
            if(previousTileType == -1 && random == -1)
            {
                do
                {
                    Random rand = new Random();
                    random = rand.nextInt(3);

                }while(random == BIG_TILE && i!=0); //Big tile type can be added as the first tile type @ tile index 0

            }

            if(random == LONG_TILE)
            {
                liquors.add(new Liquor("Liquor Number: " + i, random));
                if(previousTileType == -1)
                {
                    previousTileType = random;
                    random = SMALL_TILE;
                }
                else
                {
                    //if(previousTileType == SMALL_TILE)
                    //{
                        previousTileType = random;
                        random = SMALL_TILE;
                        bigFlag = true;
                    //}
                }


            }
            else if(random == SMALL_TILE)
            {
                liquors.add(new Liquor("Liquor Number: " + i, random));

                if(previousTileType == -1)
                {
                    previousTileType = random;
                    random = LONG_TILE;
                }
                else
                {
                    if(previousTileType == LONG_TILE)
                    {
                        previousTileType = random;
                        if(bigFlag == true)
                        {
                            random = BIG_TILE;
                        }


                    }
                    else
                    {
                        //if(previousTileType == SMALL_TILE)
                        //{
                            //bigFlag = true;
                            random = BIG_TILE;
                        //}
                            previousTileType = random;

                    }
                }
            }
            else
            {
                liquors.add(new Liquor("Liquor Number: " + i, random));
                bigFlag = false;
                previousTileType = -1;
                random = -1;

            }

        }
     return liquors;
    }

}
