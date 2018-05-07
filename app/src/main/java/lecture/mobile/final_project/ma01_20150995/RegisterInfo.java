package lecture.mobile.final_project.ma01_20150995;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

public class RegisterInfo extends AppCompatActivity {

    BackPressClose backPressClose ;
    public static final int REQUEST_CODE_CHOOSE = 100;
    private final static int PERMISSION_REQ_CODE = 100;
    ImageView imageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

        final TextView mName = (TextView)findViewById(R.id.regis_name);
        final TextView mBirth = (TextView)findViewById(R.id.regis_birth);
        final TextView mPhone = (TextView)findViewById(R.id.regis_phone);
        ImageButton imageButton = (ImageButton)findViewById(R.id.regis_img_btn);
        imageView = (ImageView)findViewById(R.id.my_profile_image) ;

        Intent intent = getIntent();
        final String iName = intent.getStringExtra("name");
        final String iphone = intent.getStringExtra("phone");
        final String iUri = intent.getStringExtra("uri");
        final String birth = intent.getStringExtra("birth");
        mName.setText(iName);
        mBirth.setText(birth);
        mPhone.setText(iphone);

        if(iUri != null) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Cursor cursor = getContentResolver().query(Uri.parse(iUri), null, null, null, null);

            cursor.moveToNext();

            String path = cursor.getString(cursor.getColumnIndex("_data"));

            cursor.close();

            Bitmap orgImage = BitmapFactory.decodeFile(path, options);
            imageView.setImageBitmap(orgImage);
            imageView.setRotation(90);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                        ActivityCompat.requestPermissions(RegisterInfo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
                    else
                        ActivityCompat.requestPermissions(RegisterInfo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
                    return;
                }
                Picker.from(RegisterInfo.this)
                        .count(1)
                        .enableCamera(true)
                        .setEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterInfo.this,SlideActivity.class);
                intent.putExtra("name",mName.getText().toString());
                intent.putExtra("birth",mBirth.getText().toString());
                intent.putExtra("phone",mPhone.getText().toString());
                if(uri == null){
                    if(iUri != null)
                        uri = Uri.parse(iUri);
                    else
                        uri = Uri.parse("android.resource://dongduk.mobile.exam.afinal/"+R.drawable.ic_camera);
                }
                intent.putExtra("uri",uri.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
                finish();
            }
        });


        backPressClose = new BackPressClose(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                uri = u;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                Cursor cursor = getContentResolver().query(u, null, null, null, null );

                cursor.moveToNext();

                String path = cursor.getString( cursor.getColumnIndex( "_data" ) );

                cursor.close();

                Bitmap orgImage = BitmapFactory.decodeFile(path,options);

                imageView.setImageBitmap(orgImage);
                Log.e("Register",imageView.getRotation()+"");
            }
        }
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

    @Override
    public void onBackPressed() {
        backPressClose.onBackPressed();
    }
}
