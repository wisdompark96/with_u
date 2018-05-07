package lecture.mobile.final_project.ma01_20150995;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


/**
 * Created by user on 2017-12-17.
 */

public class BroadCast extends BroadcastReceiver {
    Context con;
    @Override
    public void onReceive(Context context, Intent intent) {

       NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        int _id = bundle.getInt("_id");

        Log.e("Broad",_id+",");


        Notification noti = new NotificationCompat.Builder(context)
                .setTicker("데이트 하루전!")
                .setSmallIcon(R.drawable.mark)
                .setContentTitle("데이트 하루전!")
                .setContentText(title)
                .setVibrate(new long[]{200, 500, 200, 500,500, 1000})
                .setAutoCancel(true)
                .build();
        notificationManager.notify(100,noti);

        Intent smsIntent = new Intent(context,SMSReceiveActivity.class);
        smsIntent.putExtra("title",title);
        context.startActivity(smsIntent);

        DateListDBHelper helper = new DateListDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update "+helper.TABLE_NAME+" set flag = 0 where _id = "+_id+";");


    }


}
