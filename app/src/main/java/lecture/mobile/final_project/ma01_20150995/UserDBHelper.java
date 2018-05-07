package lecture.mobile.final_project.ma01_20150995;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2017-11-30.
 */

public class UserDBHelper extends SQLiteOpenHelper{

    private final static String DB_NAME = "user_db";
    public final static String TABLE_NAME = "user_table";

    public UserDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" ( _id integer primary key autoincrement,image text, name text, birth text,phone integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
