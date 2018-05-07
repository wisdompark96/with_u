package lecture.mobile.final_project.ma01_20150995;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);

        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
        registerReceiver(new BroadcastReceiver() {

            @Override

            public void onReceive(Context context, Intent intent) {

                switch(getResultCode()){

                    case Activity.RESULT_OK:

                        // 전송 성공

                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();

                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        // 전송 실패

                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();

                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        // 서비스 지역 아님

                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();

                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        // 무선 꺼짐

                        Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();

                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        // PDU 실패

                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();

                        break;

                }

            }

        }, new IntentFilter("SMS_SENT_ACTION"));
        registerReceiver(new BroadcastReceiver() {

            @Override

            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()){

                    case Activity.RESULT_OK:

                        // 도착 완료

                        Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();

                        break;

                    case Activity.RESULT_CANCELED:

                        // 도착 안됨

                        Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show();

                        break;

                }

            }

        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        UserDBHelper helper = new UserDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+UserDBHelper.TABLE_NAME+" where _id = 1;",null);
            cursor.moveToNext();
           int phone = cursor.getInt(4);



        SmsManager mSmsManager = SmsManager.getDefault();

        mSmsManager.sendTextMessage("0"+String.valueOf(phone), null, title+"데이트 하루 전날 입니다!", sentIntent, deliveredIntent);
        Log.e("SMS","전송!");

//        sendSMS("01090034108",title+" 데이트 하루 전날 입니다!");
       /* PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("01090034108",null,title+" 데이트 하루 전날 입니다!",pi,null);*/


    }
}
