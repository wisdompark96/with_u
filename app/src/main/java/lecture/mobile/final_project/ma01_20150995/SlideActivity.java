package lecture.mobile.final_project.ma01_20150995;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

public class SlideActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CHOOSE = 100;
    ImageView imageView;

    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info_partner);

        Intent intent = getIntent();
        final String iName = intent.getStringExtra("name");
        final String iphone = intent.getStringExtra("phone");
        final String iUri = intent.getStringExtra("uri");
        final String birth = intent.getStringExtra("birth");

        Log.e("Slide",iName);
        Log.e("Slide",iUri);

        final TextView pName = (TextView)findViewById(R.id.part_regis_name);
        final TextView pBirth = (TextView)findViewById(R.id.part_regis_birth);
        final TextView pPhone = (TextView)findViewById(R.id.part_regis_phone);
        imageView = (ImageView)findViewById(R.id.profile_image) ;

        Button button = (Button)findViewById(R.id.regis_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDBHelper helper = new UserDBHelper(SlideActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values2 = new ContentValues();
                values2.put("name",pName.getText().toString());
                values2.put("birth",pBirth.getText().toString());
                values2.put("phone","0"+Integer.parseInt(pPhone.getText().toString()));
                values2.put("image",uri.toString());
                db.insert(helper.TABLE_NAME,null,values2);

                ContentValues values = new ContentValues();
                values.put("name",iName);
                values.put("birth",birth);
                values.put("phone","0"+Integer.parseInt(iphone));
                values.put("image",iUri);
                db.insert(helper.TABLE_NAME,null,values);
                helper.close();
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picker.from(SlideActivity.this)
                        .count(1)
                        .enableCamera(true)
                        .setEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.regis_part_image_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlideActivity.this,RegisterInfo.class);
                intent.putExtra("name",iName);
                intent.putExtra("birth",birth);
                intent.putExtra("phone",iphone);
                intent.putExtra("uri",iUri);
                startActivity(intent);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);
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
}
