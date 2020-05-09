package com.example.trucktracking;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="truck.tracking";
    public static final String TABLE_NAME="tracking";
    public static final String COL_1="phone";
    public static final String COL_2="lat";
    public static final String COL_3="lon";
    public static final String COL_4="date";
    public static final String COL_5="distance";
    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + "(phone INTEGER , lat decimal(10,10),lon decimal(10,10),date datetime,distance decimal(10,10) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public boolean insertData(String ph,String lat,String lon,String dat,String distance ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,ph);
        contentValues.put(COL_2,lat);
        contentValues.put(COL_3,lon);
        contentValues.put(COL_4,dat);
        contentValues.put(COL_5,distance);
       long result= db.insert(TABLE_NAME,null,contentValues);
       if(result ==-1)
        return false;
       else return true;
    }
    public double getDistance(String lat,String lon){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select lat,lon,distance from "+TABLE_NAME +" where date(date)= DATE('now') order by date desc limit 1",null);
        if(res.moveToNext()) {
            Double dlat = Double.parseDouble(lat) - Double.parseDouble(res.getString(0));
            Double dlon = Double.parseDouble(lat) - Double.parseDouble(res.getString(1));
            double a = Math.pow(Math.sin(dlat / 2), 2)
                    + Math.cos(Double.parseDouble(lat)) * Math.cos(Double.parseDouble(res.getString(1)))
                    * Math.pow(Math.sin(dlon / 2), 2);

            double c = 2 * Math.asin(Math.sqrt(a));

            // Radius of earth in kilometers. Use 3956
            // for miles
            double r = 6371;

            // calculate the result
            double cdist = (c * r)*1000;
            double distance =  cdist;
          //  Double.parseDouble(res.getString(2)) +

            return distance;
        }
        return 0;
    }

}
