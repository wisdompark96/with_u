package lecture.mobile.final_project.ma01_20150995;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import lecture.mobile.final_project.ma01_20150995.dto.DateListDTO;

public class AddDateList extends AppCompatActivity {

    Geocoder geocoder;
    GoogleMap googlemap;
    MarkerOptions markerOptions;
    DatePickerDialog.OnDateSetListener picker;
    DatePickerDialog datePickerDialog;
    TimePickerDialog.OnTimeSetListener timePicker;
    TimePickerDialog timePickerDialog;
    EditText title;
    EditText address;
    TextView date_time;
    TextView date_day;
    DateListDTO dateListDTO;
    Marker marker;
    int year;
    int month;
    int day_of_month;
    int hours;
    int minute;
    DateListDBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date_list);
        geocoder = new Geocoder(this);
        title = (EditText)findViewById(R.id.data_list_title);
        address = (EditText)findViewById(R.id.date_list_search);
        date_time = (TextView)findViewById(R.id.date_time);
        date_day = (TextView)findViewById(R.id.date_day);
        helper = new DateListDBHelper(this);

        dateListDTO = new DateListDTO();

        final Calendar c = Calendar.getInstance();

        date_time.setText(c.get(c.HOUR_OF_DAY)+" 시 "+c.get(c.MINUTE)+" 분 ");
        date_day.setText(c.get(c.YEAR)+" 년 "+(c.get(c.MONTH)+1)+" 월 "+c.get(c.DAY_OF_MONTH)+" 일 ");
        dateListDTO.setYear(c.get(c.YEAR));
        dateListDTO.setMonth(c.get(c.MONTH));
        dateListDTO.setDay_of_month(c.get(c.DAY_OF_MONTH));
        dateListDTO.setHours(c.get(c.HOUR_OF_DAY));
        dateListDTO.setMinit(c.get(c.MINUTE));
         picker= new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                dateListDTO.setYear(c.get(c.YEAR));
                dateListDTO.setMonth(c.get(c.MONTH));
                dateListDTO.setDay_of_month(c.get(c.DAY_OF_MONTH));
                date_day.setText(c.get(c.YEAR)+" 년 "+(c.get(c.MONTH)+1)+" 월 "+c.get(c.DAY_OF_MONTH)+" 일 ");
                timePickerDialog.show();
            }
        };
        timePicker = new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                c.set(Calendar.MINUTE, minute);

                dateListDTO.setHours(c.get(c.HOUR_OF_DAY));
                dateListDTO.setMinit(c.get(c.MINUTE));
                date_time.setText(c.get(c.HOUR_OF_DAY)+" 시 "+c.get(c.MINUTE)+" 분 ");
            }
        };

        datePickerDialog = new DatePickerDialog(this,picker,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        timePickerDialog = new TimePickerDialog(this,timePicker,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false);

        Intent resultIntent = getIntent();
        String intent_title = resultIntent.getStringExtra("title");
        String intent_place = resultIntent.getStringExtra("place");
        title.setText(intent_title);
        address.setText(intent_place);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.google_map_list);
        mapFragment.getMapAsync(onMapReadyCallback);

        markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.red_marker));
    }
    OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googlemap = googleMap;
            LatLng current = null;
            if(address.getText().toString().length() == 0)
                current = new LatLng(37.495647,126.772745);
            else{
                try {
                    List<Address>list = geocoder.getFromLocationName(address.getText().toString(),1);
                    if(list != null && list.size() > 0)
                        current = new LatLng(list.get(0).getLatitude(),list.get(0).getLongitude());
                    else
                        current = new LatLng(37.495647,126.772745);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,17));
            markerOptions.position(current);
            marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();
        }
    };
    public void onClick(View v){
        switch (v.getId()){
            case R.id.date_list_search_btn:
                LatLng position = null;
                try {
                    List<Address> list = geocoder.getFromLocationName(address.getText().toString(),1);
                    if(list != null && list.size() >0)
                        position = new LatLng(list.get(0).getLatitude(),list.get(0).getLongitude());
                    else {
                        position = new LatLng(37.495647, 126.772745);
                        Toast.makeText(this,"검색결과가 없습니다!",Toast.LENGTH_SHORT).show();
                    }
                    googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,17));
                    marker.setPosition(position);
                    marker.showInfoWindow();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.date_picker:
                datePickerDialog.show();
                break;

            case R.id.date_list_save_btn:
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();
//                if(title.getText().toString().length() == 0) {
//                    Toast.makeText(this, "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();
//                    break;
//                }
                row.put("title", title.getText().toString());
                row.put("year", dateListDTO.getYear());
                row.put("month",dateListDTO.getMonth());
                row.put("day_of_month",dateListDTO.getDay_of_month());
                row.put("address", address.getText().toString());
                row.put("minit", dateListDTO.getMinit());
                row.put("hours", dateListDTO.getHours());
                row.put("flag",0);
                db.insert(DateListDBHelper.TABLE_NAME, null, row);

                helper.close();
                finish();
                break;
            case R.id.date_list_cancel_btn:
                finish();

        }

    }
}
