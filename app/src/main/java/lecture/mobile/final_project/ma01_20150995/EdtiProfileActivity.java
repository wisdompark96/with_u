package lecture.mobile.final_project.ma01_20150995;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class EdtiProfileActivity extends AppCompatActivity {

    private String name;
    private String birth;
    private int phone;
    private String u;
    public static final int REQUEST_CODE_CHOOSE = 100;
    private final static int PERMISSION_REQ_CODE = 100;
    ImageView imageView;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Intent intent = getIntent();
        final int _id = intent.getIntExtra("_id",1);

        UserDBHelper helper = new UserDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+UserDBHelper.TABLE_NAME+" where _id = "+_id+" ;",null);
        if(c.getCount() != 0) {
            c.moveToNext();
            u = c.getString(1);
            name = c.getString(2);
            birth = c.getString(3);
            phone = c.getInt(4);
            helper.close();
            c.close();
        }

        final TextView mName = (TextView)findViewById(R.id.regis_name);
        final TextView mBirth = (TextView)findViewById(R.id.regis_birth);
        final TextView mPhone = (TextView)findViewById(R.id.regis_phone);
        ImageButton imageButton = (ImageButton)findViewById(R.id.regis_img_btn);
        imageView = (ImageView)findViewById(R.id.my_profile_image) ;

        mName.setText(name);
        mBirth.setText(birth);
        mPhone.setText(String.valueOf(phone));

        if(u != null) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Cursor cursor = getContentResolver().query(Uri.parse(u), null, null, null, null);

            cursor.moveToNext();

            String path = cursor.getString(cursor.getColumnIndex("_data"));

            cursor.close();

            Bitmap orgImage = BitmapFactory.decodeFile(path, options);
            imageView.setImageBitmap(orgImage);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EdtiProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EdtiProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                        ActivityCompat.requestPermissions(EdtiProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
                    else
                        ActivityCompat.requestPermissions(EdtiProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
                    return;
                }
                Picker.from(EdtiProfileActivity.this)
                        .count(1)
                        .enableCamera(true)
                        .setEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UserDBHelper helper = new UserDBHelper(EdtiProfileActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values2 = new ContentValues();
                values2.put("name",mName.getText().toString());
                values2.put("birth",mBirth.getText().toString());
                values2.put("phone",Integer.parseInt(mPhone.getText().toString()));
                if(uri == null){
                    uri = Uri.parse(u);
                }
                values2.put("image",uri.toString());
                String whereClause = "_id=?";
                String[] whereArgs = new String[]{String.valueOf(_id)};
                db.update(helper.TABLE_NAME, values2, whereClause,whereArgs);
                helper.close();
                finish();
            }
        });


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
}