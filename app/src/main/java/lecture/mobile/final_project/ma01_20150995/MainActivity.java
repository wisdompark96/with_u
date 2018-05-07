package lecture.mobile.final_project.ma01_20150995;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener{

    private HomeFragment homeFragment;
    private DiaryFragment diaryFragment;
    private DateFragment dateFragment;
    private CalendarFragment calendarFragment;

    private final static int PERMISSION_REQ_CODE = 100;
    UserDBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        helper = new UserDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+UserDBHelper.TABLE_NAME+";",null);

        if(cursor.getCount() == 0){
            Intent intent = new Intent(this, RegisterInfo.class);
            startActivity(intent);
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this,
                    new String[] {  Manifest.permission.SEND_SMS}, PERMISSION_REQ_CODE);
            return;
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        homeFragment = new HomeFragment();
        diaryFragment = new DiaryFragment();
        dateFragment = new DateFragment();
        calendarFragment = new CalendarFragment();


        initFragment();

        final BottomBar bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch(tabId) {
                    case R.id.home :
                        transaction.replace(R.id.contentContainer, homeFragment).commit();
                        bottomBar.setTabTitleTextAppearance(R.style.CustomTitleTextAppearance);
                        break;
                    case R.id.diary:
                        transaction.replace(R.id.contentContainer, diaryFragment).commit();
                        bottomBar.setTabTitleTextAppearance(R.style.CustomTitleTextAppearance);
                        break;
                    case R.id.date:
                        transaction.replace(R.id.contentContainer, dateFragment).commit();
                        bottomBar.setTabTitleTextAppearance(R.style.CustomTitleTextAppearance);
                        break;
                    case R.id.calendar:
                        transaction.replace(R.id.contentContainer, calendarFragment).commit();
                        bottomBar.setTabTitleTextAppearance(R.style.CustomTitleTextAppearance);
                        break;
                }

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission was granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission was denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }






    public void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
