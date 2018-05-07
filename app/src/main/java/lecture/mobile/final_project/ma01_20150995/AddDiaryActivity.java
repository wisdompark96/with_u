package lecture.mobile.final_project.ma01_20150995;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddDiaryActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private final static int PERMISSION_REQ_CODE = 100;
    private final static int LOC_PERMISSION_REQ_CODE = 200;


    Bitmap photo;
    private Uri mlmageCaptureUri;

    private String absoultePath = "null";

    private String USER_BG_PATH = "";

    SimpleDraweeView imageView;
    EditText t;
    EditText loc;
    EditText memo;
    TextView date;

    LocationManager locationManager;
    Geocoder geocoder;
    DiaryDBHelper helper;
    DatePickerDialog.OnDateSetListener picker;
    DatePickerDialog datePickerDialog;
    Uri uri;

    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_add_diary);
        imageView = (SimpleDraweeView) findViewById(R.id.diary_add_imageView);

        imageButton = (ImageButton)findViewById(R.id.diary_add_save);
        imageButton.setEnabled(false);

        t = (EditText)findViewById(R.id.diary_add_title);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        BitmapDrawable d = (BitmapDrawable)((ImageView)findViewById(R.id.diary_add_image)).getDrawable();
        photo = d.getBitmap();
        geocoder = new Geocoder(this);
        helper = new DiaryDBHelper(this);
        date = (TextView)findViewById(R.id.diary_add_date);
        memo = (EditText)findViewById(R.id.diary_add_memo);
        final Calendar c = Calendar.getInstance();
        loc = (EditText)findViewById(R.id.diary_add_loc);
        date.setText(c.get(Calendar.YEAR)+"년"+(c.get(Calendar.MONTH)+1)+"월"+c.get(Calendar.DAY_OF_MONTH)+"일");
        picker= new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                date.setText(c.get(Calendar.YEAR)+"년"+(c.get(Calendar.MONTH)+1)+"월"+c.get(Calendar.DAY_OF_MONTH)+"일");
            }
        };
        datePickerDialog = new DatePickerDialog(this,picker,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.diary_add_image:
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
            case R.id.diary_add_loc_img:

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOC_PERMISSION_REQ_CODE);
                    return;
                }
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(AddDiaryActivity.this,"위치를 확인 할수 없습니다!",Toast.LENGTH_SHORT).show();
                    break;
                }
                Log.e("AddDiary","위치");

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,locationListener);

                break;
            case R.id.diary_add_cal:
                datePickerDialog.show();
                break;
            case R.id.diary_add_save:
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("title",t.getText().toString());
                values.put("image",absoultePath);
                values.put("memo",memo.getText().toString());
                values.put("address",loc.getText().toString());
                values.put("date",date.getText().toString());
                db.insert(helper.TABLE_NAME,null,values);
                helper.close();
            case R.id.diary_add_cancel:
                finish();


        }
    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            try {
                List<Address> list = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if(list.size() >0 && list != null ){
                    loc.setText(list.get(0).getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

            Toast.makeText(AddDiaryActivity.this, "위치 제공자를 켜주세요!", Toast.LENGTH_SHORT).show();

        }
    };

    private File createImageFile() throws IOException {
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
        /**/


//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_FROM_ALBUM);
        ImageSelectorActivity.start(AddDiaryActivity.this,1,ImageSelectorActivity.MODE_SINGLE,false,true,false);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission was granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission was denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            case LOC_PERMISSION_REQ_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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

                imageButton.setEnabled(true);

                ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                Log.e("Add",images.get(0));
                absoultePath = images.get(0);

                Cursor c = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                        null, "_data = '" + absoultePath + "'", null, null );

                c.moveToNext();

                int id = c.getInt( c.getColumnIndex( "_id" ) );

                Uri uri = ContentUris.withAppendedId( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id );

                imageView.setImageURI(uri);

                Log.e("Register",imageView.getRotation()+"");
                break;

            case PICK_FROM_CAMERA:

                imageButton.setEnabled(true);
                imageView.setImageURI(mlmageCaptureUri);

                break;


        }

    }

    /*private void setPic(){
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(absoultePath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW,photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(absoultePath, bmOptions);
        photo = bitmap;
        imageView.setImageBitmap(bitmap);
    }*/
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },PERMISSION_REQ_CODE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                          final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission }, PERMISSION_REQ_CODE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}

