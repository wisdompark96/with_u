package lecture.mobile.final_project.ma01_20150995;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2017-11-30.
 */

public class DateListDBHelper extends SQLiteOpenHelper{

    private final static String DB_NAME = "date_db";
    public final static String TABLE_NAME = "date_table";

    public DateListDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" ( _id integer primary key autoincrement, title text, address text, year integer, month integer, day_of_month integer, hours integer, minit integer,  flag integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
