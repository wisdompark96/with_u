package lecture.mobile.final_project.ma01_20150995;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.yongchun.library.utils.CropUtil;
import com.yongchun.library.view.ImageCropActivity;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

public class BackGroundActivity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private final static int PERMISSION_REQ_CODE = 100;


    Bitmap photo;
    File f;
    private Uri mlmageCaptureUri;
    private SimpleDraweeView imageView;
    private String absoultePath;

    Drawable d;
    private String USER_BG_PATH = "";

    LinearLayout layout;
    SharedPreferences sharedPreferences;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        View view = View.inflate(this,R.layout.fragment_first,null);

        imageView = (SimpleDraweeView) findViewById(R.id.background_imageView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        button = (ImageButton)findViewById(R.id.save);
        button.setEnabled(false);

        layout = (LinearLayout)view.findViewById(R.id.back);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.image_select:
                DialogInterface.OnClickListener cameraListner = new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListner = new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };

                DialogInterface.OnClickListener cancelListner = new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                new AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영",cameraListner)
                        .setNeutralButton("앨범선택", albumListner)
                        .setNegativeButton("취소", cancelListner)
                        .show();
                break;
            case R.id.save:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("image",mlmageCaptureUri.toString());
                editor.commit();
                finish();
                break;
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        absoultePath = image.getAbsolutePath();
        return image;
    }

    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!= null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                mlmageCaptureUri = FileProvider.getUriForFile(this, "lecture.mobile.final_project.ma01_20150995", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mlmageCaptureUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }

    public void doTakeAlbumAction(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
            return;
        }
        /*Picker.from(this)
                .count(1)
                .enableCamera(true)
                .setEngine(new PicassoEngine())
                .forResult(PICK_FROM_ALBUM);*/
        ImageSelectorActivity.start(BackGroundActivity.this,1,ImageSelectorActivity.MODE_SINGLE,false,true,false);



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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        switch (requestCode){
            case ImageSelectorActivity.REQUEST_IMAGE:
                button.setEnabled(true);

//                List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);

//                for (Uri u : mSelected) {
                absoultePath = images.get(0);
//                    mlmageCaptureUri = u;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;

                   /* Cursor cursor = getContentResolver().query(u, null, null, null, null);

                    cursor.moveToNext();

                    String path = cursor.getString(cursor.getColumnIndex("_data"));

                    cursor.close();*/

                    photo = BitmapFactory.decodeFile(absoultePath, options);

                    f = new File(absoultePath);


                Cursor c = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                        null, "_data = '" + absoultePath+ "'", null, null );

                c.moveToNext();

                int id = c.getInt( c.getColumnIndex( "_id" ) );

                final Uri uri = ContentUris.withAppendedId( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id );
                imageView.setImageURI(uri);
                mlmageCaptureUri = uri;


                break;

            case PICK_FROM_CAMERA:
                button.setEnabled(true);

                imageView.setImageURI(mlmageCaptureUri);

                break;

        }

    }

    @Override
    protected void onDestroy() {
        recycleBitmap(imageView);
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
}
