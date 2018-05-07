package lecture.mobile.final_project.ma01_20150995;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by user on 2017-12-02.
 */

public class FirstFragment extends Fragment {
    private final static int PERMISSION_REQ_CODE = 100;
    TextView dday;
    TextView event;
    SharedPreferences sharedPreferences;
    private CustomDialog customDialog;
    String name;
    String birth;
    int phone;
    String uri;
    String name2;
    String birth2;
    int phone2;
    String uri2;
    FrameLayout layout;
    SimpleDraweeView simpleDraweeView;
    DatePickerDialog.OnDateSetListener picker;
    DatePickerDialog datePickerDialog;
    ImageView my;
    ImageView part;

    public FirstFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Fresco.initialize(getActivity());
        layout = (FrameLayout)inflater.inflate(R.layout.fragment_first,container,false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        simpleDraweeView = (SimpleDraweeView)layout.findViewById(R.id.first_background);
        dday = (TextView)layout.findViewById(R.id.dday);
        event = (TextView)layout.findViewById(R.id.setevent);
        ImageButton imageButton = (ImageButton)layout.findViewById(R.id.phone);

        final Calendar calendar = Calendar.getInstance();
        final TextView first = (TextView)layout.findViewById(R.id.firstDay);

        if(!sharedPreferences.getString("first","null").equals("null") ){
            first.setText("우리는 "+sharedPreferences.getString("first","null")+"일 동안 걷는중..");
        }
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        picker= new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);


                Calendar calen = Calendar.getInstance();

                long t=calen.getTimeInMillis();                 //오늘 날짜를 밀리타임으로 바꿈
                long d=calendar.getTimeInMillis();              //디데이날짜를 밀리타임으로 바꿈
                long r=(d-t)/(24*60*60*1000);                 //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈

                int resultNumber=(int)r;


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("first",String.valueOf(resultNumber*-1));
                editor.commit();


                first.setText("우리는 "+resultNumber*-1+"일 동안 걷는중..");
            }
        };

        datePickerDialog = new DatePickerDialog(getActivity(),picker,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));



        if(!sharedPreferences.getString("title","null").equals("null") &&!sharedPreferences.getString("dday","null").equals("null")) {
            event.setText(sharedPreferences.getString("title", "null"));
            dday.setText(sharedPreferences.getString("dday","null"));
        }

        dday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateListDBHelper helper = new DateListDBHelper(getActivity());
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor= db.rawQuery("select * from date_table;", null);
                final ArrayList<HashMap<String,String>>  arrayList = new ArrayList<HashMap<String,String> >();
                while(cursor.moveToNext()){
                    HashMap<String,String> item = new HashMap<String, String>();
                    String title = cursor.getString(1);
                    int year = cursor.getInt(3);
                    int month = cursor.getInt(4);
                    int day = cursor.getInt(5);
                    item.put("item 1",title);
                    item.put("item 2",year+"-"+month+"-"+day);
                    arrayList.add(item);
                }
                if(arrayList.size() == 0){
                    Toast.makeText(getActivity(),"일정을 추가해주세요!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),AddDateList.class);
                    startActivity(intent);
                }
                else {
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), arrayList, android.R.layout.simple_list_item_2, new String[]{"item 1", "item 2"}, new int[]{android.R.id.text1, android.R.id.text2});
                    DialogPlus dialog = DialogPlus.newDialog(getActivity())
                            .setAdapter(adapter)
                            .setGravity(Gravity.CENTER)
                            .setCancelable(true)
                            .setContentHolder(new ListHolder())
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    item.toString();
                                    String title = arrayList.get(position).get("item 1");
                                    String date = arrayList.get(position).get("item 2");
                                    setDday(title, date);
                                    dialog.dismiss();
                                }
                            })
                            .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    dialog.show();
                }


            }
        });

//        actionBar.set

        UserDBHelper helper = new UserDBHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+UserDBHelper.TABLE_NAME+";",null);
        if(cursor.getCount() != 0) {
            cursor.moveToNext();
            uri = cursor.getString(1);
            name = cursor.getString(2);
            birth = cursor.getString(3);
            phone = cursor.getInt(4);

            Log.e("First", name);
            cursor.moveToNext();
            uri2 = cursor.getString(1);
            name2 = cursor.getString(2);
            birth2 = cursor.getString(3);
            phone2 = cursor.getInt(4);

            helper.close();
        }

        TextView pName = (TextView)layout.findViewById(R.id.partner_name);
        TextView mName = (TextView)layout.findViewById(R.id.my_name);
        pName.setText(name);
        mName.setText(name2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE))
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQ_CODE);
                    else
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQ_CODE);
                    return;
                }
                String tel = "tel:0"+phone;
                startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
            }
        });
        my = (ImageView)layout.findViewById(R.id.home_mine_image);
        part= (ImageView)layout.findViewById(R.id.home_partner_image);
        if(cursor.getCount() != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Cursor c = getActivity().getContentResolver().query(Uri.parse(uri), null, null, null, null);

            c.moveToNext();

            String path = c.getString(c.getColumnIndex("_data"));

            c.close();

            Bitmap orgImage = BitmapFactory.decodeFile(path, options);

            my.setImageBitmap(orgImage);
            Cursor c2 = getActivity().getContentResolver().query(Uri.parse(uri2), null, null, null, null);

            c2.moveToNext();

            String path2 = c2.getString(c2.getColumnIndex("_data"));

            c2.close();

            Bitmap orgImage2 = BitmapFactory.decodeFile(path2, options);


            part.setImageBitmap(orgImage2);

        }
        cursor.close();


//        Window window = customDialog.getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//
//        params.copyFrom(customDialog.getWindow().getAttributes());
//        params.height = 1000;
//        params.width = 1000;
//        customDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//        WindowManager manager = (WindowManager)getActivity(). getSystemService(Activity.WINDOW_SERVICE);
//        int width, height;
//
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
//            width = manager.getDefaultDisplay().getWidth();
//            height = manager.getDefaultDisplay().getHeight();
//        } else {
//            Point point = new Point();
//            manager.getDefaultDisplay().getSize(point);
//            width = point.x;
//            height = point.y;
//        }
//

        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                customDialog = new CustomDialog().newInstance(name,birth,uri,phone,1);
                customDialog.show(getActivity().getFragmentManager(),"tag");
//                Window window = customDialog.getWindow();
//                window.setAttributes(params);

            }
        });
//        WindowManager.LayoutParams param = customDialog2.getWindow().getAttributes();
//
//        param.copyFrom(customDialog2.getWindow().getAttributes());
//        param.width = WindowManager.LayoutParams.MATCH_PARENT;
//
//        param.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//        customDialog2.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog customDialog2 = new CustomDialog().newInstance(name2,birth2,uri2,phone2,2);
                customDialog2.show(getActivity().getFragmentManager(),"tag");
            }
        });
//        setHasOptionsMenu(true);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!sharedPreferences.getString("image","null").equals("null")){
            Uri u =Uri.parse(sharedPreferences.getString("image","null"));
            simpleDraweeView.setImageURI(u);
        }
    }


    public void setDday(String title, String date){

        StringTokenizer str = new StringTokenizer(date,"-");
        int year = Integer.parseInt(str.nextToken());
        int month = Integer.parseInt(str.nextToken());
        int day = Integer.parseInt(str.nextToken());

        Calendar calendar = Calendar.getInstance();
        int tYear = calendar.get(Calendar.YEAR);
        int tMonth = calendar.get(Calendar.MONTH);
        int tDay = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar dCalendar = Calendar.getInstance();
        dCalendar.set(year,month,day);

        long t=calendar.getTimeInMillis();                 //오늘 날짜를 밀리타임으로 바꿈
        long d=dCalendar.getTimeInMillis();              //디데이날짜를 밀리타임으로 바꿈
        long r=(d-t)/(24*60*60*1000);                 //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈

        int resultNumber=(int)r;

        if(resultNumber > 0 )
            dday.setText("D-"+resultNumber);
        else if(resultNumber == 0)
            dday.setText("D-Day");
        else
            dday.setText("D+"+(resultNumber)*-1);
        event.setText(title);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title",event.getText().toString());
        editor.putString("dday",dday.getText().toString());
        editor.commit();

    }
//
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }
//
//
//

//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backg_image:
                Intent intent = new Intent(getActivity(),BackGroundActivity.class);
                startActivity(intent);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /*private View.OnClickListener leftListener = new View.OnClickListener() {

        public void onClick(View v) {

            Toast.makeText(getContext(), "왼쪽버튼 클릭",

                    Toast.LENGTH_SHORT).show();

            customDialog.dismiss();

        }

    };*/



   /* private View.OnClickListener rightListener = new View.OnClickListener() {

        public void onClick(View v) {

            Toast.makeText(getContext(), "오른쪽버튼 클릭",

                    Toast.LENGTH_SHORT).show();

        }

    };*/

    @Override
    public void onDestroyView() {
        recycleBitmap(simpleDraweeView);

        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        recycleBitmap(simpleDraweeView);

        super.onDestroy();
    }

    private static void recycleBitmap(ImageView iv) {
        Drawable d = iv.getDrawable();
        if (d instanceof BitmapDrawable) {
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            b.recycle();
        } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.

        d.setCallback(null);
    }
    //

    @Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch(requestCode) {
        case PERMISSION_REQ_CODE:
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission was granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission was denied!", Toast.LENGTH_SHORT).show();
            }
            break;
    }
}
}
