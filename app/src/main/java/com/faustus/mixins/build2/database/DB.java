package com.faustus.mixins.build2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.faustus.mixins.build2.R;
import com.faustus.mixins.build2.model.CardInformation;
import com.faustus.mixins.build2.model.Liquor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by flux on 7/17/15.
 */
public class DB
{
   private static  Context context;
   private  DBhelper mDbHelper;

    public DB(Context context)
    {
        DB.context = context;
        mDbHelper = DBhelper.getInstance(context);
    }

    public void insert(Object... params)
    {
        SQLiteDatabase db = mDbHelper.openToWriteDB();
        ContentValues mContentValues = new ContentValues();
        try{
            for (Object param : params)
            {
                if (param instanceof JSONObject)
                {
                    mContentValues.put(DBhelper.mTableColumns[1], param.toString());
                    db.insertOrThrow(DBhelper.mTableName, null, mContentValues);
                    Log.i("SUCCESS","SUCC");
                }
            }
           // db.setTransactionSuccessful();
        }
        catch(Exception exp)
        {
            Toast.makeText(DB.context,"Failed to create liquor", Toast.LENGTH_SHORT).show();
            exp.printStackTrace();
        }
        finally
        {
            mDbHelper.closeDB();
        }


    }

    public void  delete(Object... params)
    {
        SQLiteDatabase db = mDbHelper.openToWriteDB();
        try
        {
            //if(params[0] instanceof  JSONObject)
                //db.delete(DBhelper.mTableName,DBhelper.mTableColumns[1]+" like '%" + params[0].toString() + "%'; ",null);
            db.delete(DBhelper.mTableName,DBhelper.mTableColumns[0] +" =" +params[0].toString() +";" ,null);
           // db.delete(DBhelper.mTableName,DBhelper.mTableColumns[0] ,new String[]{params[0].toString()});

        }
        catch (Exception exp)
        {
            Toast.makeText(DB.context,"Failed to delete liquor", Toast.LENGTH_SHORT).show();
            exp.printStackTrace();
        }finally
        {
            mDbHelper.closeDB();
        }

    }

    public void select(int offset, ArrayList<CardInformation> cardInformations)
    {
        SQLiteDatabase db = mDbHelper.openToReadDB();
        //Cursor cursor = db.query(DBhelper.mTableName, DBhelper.mTableColumns, null, null, null, null, null, "LIMIT 10 OFFSET 10");
        Cursor cursor =  db.rawQuery("Select * from " + DBhelper.mTableName +" LIMIT 10 OFFSET " + offset,null);
        Liquor mTempLiquor;
        CardInformation mTempInfo;
        JSONObject mJsonObject;
        while(cursor.moveToNext())
        {
            try
            {
                mJsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(this.getDBColumns()[1])));
                mTempLiquor = new Liquor(mJsonObject);
                mTempLiquor.setLiquorId(cursor.getInt(cursor.getColumnIndex(this.getDBColumns()[0])));
                Log.i("Liquor ID",mTempLiquor.getLiquorId()+"");
                mTempInfo = new CardInformation(mTempLiquor);
                cardInformations.add(mTempInfo);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        cursor.close();
        mDbHelper.closeDB();
    }

    public String[] getDBColumns()
    {
        return DBhelper.mTableColumns;
    }


    public static class DBhelper extends SQLiteOpenHelper
    {
        private static DBhelper dbhelper;
        private static final String mTableName = context.getResources().getString(R.string.db_table);
        private static final String[] mTableColumns = context.getResources().getStringArray(R.array.db_columns);
        private static final int mDbVersion =context.getResources().getInteger(R.integer.db_version);
        private static final  String mCreateTableSQL ="create table if not exists " +  mTableName
                + " (" +  mTableColumns[0] +  " integer primary key autoincrement, "
                + mTableColumns[1]  + " TEXT );";

        private DBhelper(Context context)
        {
            super(context, mTableName,null, mDbVersion);
        }

        private static synchronized DBhelper getInstance(Context context)
        {
                if(dbhelper == null)
                {
                    dbhelper = new DBhelper(context);
                    Log.i("dbhelper", "dbhelper is null");
                }

                return dbhelper;
        }



        private SQLiteDatabase openToWriteDB()
        {
            return this.getWritableDatabase();
        }

        private SQLiteDatabase openToReadDB()
        {
            return this.getReadableDatabase();
        }


        private void closeDB()
        {
             this.close();
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
                db.execSQL(mCreateTableSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
                db.execSQL("Drop table if exist "+mTableName);
                onCreate(db);
        }
    }

}
