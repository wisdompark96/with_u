package lecture.mobile.final_project.ma01_20150995;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.frescoimageviewer.*;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import lecture.mobile.final_project.ma01_20150995.dto.DiaryDTO;


public class DiaryShow extends AppCompatActivity {

    int _id;
    DiaryDTO diaryDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Fresco.initialize(this);
        setContentView(R.layout.activity_diary_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        TextView title = (TextView)findViewById(R.id.diary_show_title);
        TextView date = (TextView)findViewById(R.id.diary_show_date);
        TextView loc = (TextView)findViewById(R.id.diary_show_loc);
        TextView memo = (TextView)findViewById(R.id.diary_show_memo);
        SimpleDraweeView image = (SimpleDraweeView) findViewById(R.id.diary_show_image);

        Intent intent = getIntent();

        diaryDTO = (DiaryDTO)intent.getSerializableExtra("data");

        _id = diaryDTO.get_id();

        title.setText(diaryDTO.getTitle());
        date.setText(diaryDTO.getDate()+1);
        loc.setText(diaryDTO.getAddress());
        memo.setText(diaryDTO.getMemo());

        Bitmap orgImage = null;
        if(diaryDTO.getImage().equals("null")) {
            image.getLayoutParams().height = 0;
            image.setImageBitmap(null);
            image.requestLayout();
        }
        else{
            orgImage = BitmapFactory.decodeFile(diaryDTO.getImage());

            Cursor c = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                    null, "_data = '" + diaryDTO.getImage() + "'", null, null );

            c.moveToNext();

            int id = c.getInt( c.getColumnIndex( "_id" ) );

            Uri uri = ContentUris.withAppendedId( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id );

            image.setImageURI(uri);
        }
        Bitmap imag = orgImage;
        SharePhoto sharePhoto1 = new SharePhoto.Builder()
                .setBitmap(imag)
                .build();


        final ShareContent shareContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto1)
                .build();


        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imag)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_butto);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog(DiaryShow.this);
                shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
            }
        });
        shareButton.setShareContent(content);

    }

    public void onClick(View v){
        switch(v.getId()){
           /* case R.id.diary_show_delete:
                DiaryDBHelper helper = new DiaryDBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                String whereClause = "_id=?";
                String[] whereArgs = new String[]{String.valueOf(_id)};
                db.delete(helper.TABLE_NAME,whereClause,whereArgs);
                helper.close();
                finish();
                break;
            case R.id.diary_edit:
                Intent intent = new Intent(this,DiaryEditActivity.class);
                intent.putExtra("data",diaryDTO);
                startActivity(intent);*/
            case R.id.diary_show_cancel:
                finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }


    public void onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_bar:
                DiaryDBHelper helper = new DiaryDBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                String whereClause = "_id=?";
                String[] whereArgs = new String[]{String.valueOf(_id)};
                db.delete(helper.TABLE_NAME,whereClause,whereArgs);
                helper.close();
                finish();
                break;
            case R.id.edit_bar:
                Intent intent = new Intent(this,DiaryEditActivity.class);
                intent.putExtra("data",diaryDTO);
                startActivity(intent);

        }

    }
}
