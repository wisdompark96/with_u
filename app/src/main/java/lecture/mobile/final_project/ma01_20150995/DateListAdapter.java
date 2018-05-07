package lecture.mobile.final_project.ma01_20150995;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import lecture.mobile.final_project.ma01_20150995.dto.DateListDTO;

/**
 * Created by user on 2017-12-15.
 */

public class DateListAdapter extends CursorAdapter {

    private int layout;
    private ArrayList<DateListDTO> myDatalist;
    private LayoutInflater layoutInflater;
    Cursor cursor;
    NotificationManager notificationManager;
    Intent intent;

    public DateListAdapter(Context context, Cursor c){
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor = c;

    }

    @Override
    public void bindView(View convertView, final Context context, final Cursor cursor) {

        TextView title = (TextView)convertView.findViewById(R.id.date_adapter_title);
        TextView add = (TextView)convertView.findViewById(R.id.date_adapter_add);
        TextView time = (TextView)convertView.findViewById(R.id.date_adapter_time);

        final DateListDBHelper helper = new DateListDBHelper(context);

        final int _id = cursor.getInt(0);
        title.setText(cursor.getString(1));
        add.setText((cursor.getString(2)));
        time.setText(cursor.getInt(3)+"년 "+(cursor.getInt(4)+1)+"월 "+cursor.getInt(5)+"일 ");
        final Switch aSwitch= (Switch)convertView.findViewById(R.id.alarm_switch);
        boolean f;
        if(cursor.getInt(8) > 0)
            f = true;
        else f = false;
        aSwitch.setChecked(f);
        aSwitch.setFocusable(false);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                intent = new Intent(context, BroadCast.class);
                intent.putExtra("title",cursor.getString(1));
                intent.putExtra("_id", _id);

                PendingIntent result = PendingIntent.getBroadcast(context,cursor.getInt(0), intent,PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();

                Log.e("DateList",String.valueOf(calendar.get(calendar.MONTH)));
                int year = cursor.getInt(3);
                Log.e("DataList",String.valueOf(year));
                int month = cursor.getInt(4);
                int day = cursor.getInt(5)-1;
                int hour = cursor.getInt(6);
                int min = cursor.getInt(7);

                Log.e("DateList",year+","+month+","+day+","+hour+","+min);


                Log.e("DateList",calendar.get(calendar.YEAR)+".");
                calendar.set(year,month,day,hour,min);
                Log.e("DateList",calendar.get(Calendar.YEAR)+".");

                DateListDBHelper helper1 = new DateListDBHelper(context);
                SQLiteDatabase db2 = helper.getReadableDatabase();

                if(aSwitch.isChecked()){
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.execSQL("update "+helper.TABLE_NAME+" set flag = 1 where _id = "+_id+";");
                    Log.e("DateList",_id+","+cursor.getString(1));
                    alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),result);
                    aSwitch.setChecked(true);

//                    Toast.makeText(context,calendar.get(calendar.YEAR)+","+calendar.get(Calendar.MONTH)+","+calendar.get(calendar.DAY_OF_MONTH)+","+calendar.get(calendar.HOUR_OF_DAY),Toast.LENGTH_SHORT).show();

                    Toast.makeText(context,"알람이 설정되었습니다.",Toast.LENGTH_SHORT).show();
                    SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
                    Date timeInDate = new Date(calendar.getTimeInMillis());
                    String timeInFormat = sm.format(timeInDate);
                    Toast.makeText(context,timeInFormat+".",Toast.LENGTH_SHORT).show();

                }
                else{
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.execSQL("update "+helper.TABLE_NAME+" set flag = 0 where _id = "+_id+";");
                    aSwitch.setChecked(false);

                    alarmManager.cancel(result);

                }

                helper.close();
            }
        });


    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View list = layoutInflater.inflate(R.layout.date_list_adapter_activity,parent,false);
        return list;
    }
}
