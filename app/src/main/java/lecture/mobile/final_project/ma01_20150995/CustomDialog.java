package lecture.mobile.final_project.ma01_20150995;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class CustomDialog extends DialogFragment {

    private String name;
    private String birth;
    private int phone;
    private String uri;
    private int _id;
    Context context;
    private Fragment fragment;

    public  static CustomDialog newInstance(String name, String birth, String uri, int phone, int _id){
        Bundle args = new Bundle();
        args.putString("name",name);
        args.putString("birth",birth);
        args.putString("uri",uri);
        args.putInt("phone",phone);
        args.putInt("_id",_id);

        CustomDialog fragment = new CustomDialog();
        fragment.setArguments(args);
        return fragment;

    }




    /*public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_custom_dialog, container, false);

        Bundle args = getArguments();

        String value = args.getString("key");


        TextView Edname = (TextView)view.findViewById(R.id.dialog_name);
        TextView Edbirth = (TextView)view.findViewById(R.id.dialog_birth);
        TextView EdPhone = (TextView)view.findViewById(R.id.dialog_phone);
        Button button = (Button)view.findViewById(R.id.dialog_submit);
        ImageView imageView = (ImageView)view.findViewById(R.id.my_profile_image);

        Edname.setText(name);
        Edbirth.setText(birth);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Cursor c = context.getContentResolver().query(Uri.parse(uri), null, null, null, null);

        c.moveToNext();

        String path = c.getString(c.getColumnIndex("_data"));

        c.close();

        Bitmap orgImage = BitmapFactory.decodeFile(path, options);

        imageView.setImageBitmap(orgImage);
        imageView.setRotation(90);
        EdPhone.setText(String.valueOf(phone));





        /*

         * DialogFragment를 종료시키려면? 물론 다이얼로그 바깥쪽을 터치하면 되지만

         * 종료하기 버튼으로도 종료시킬 수 있어야겠죠?

         */

        // 먼저 부모 프래그먼트를 받아옵니다.

        //findFragmentByTag안의 문자열 값은 Fragment1.java에서 있던 문자열과 같아야합니다.

        //dialog.show(getActivity().getSupportFragmentManager(),"t*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            name = getArguments().getString("name");
            birth = getArguments().getString("birth");
            phone = getArguments().getInt("phone");
            _id = getArguments().getInt("_id");
            uri = getArguments().getString("uri");
        }




/*        if (mLeftClickListener != null && mRightClickListener != null) {

            mLeftButton.setOnClickListener(mLeftClickListener);

            mRightButton.setOnClickListener(mRightClickListener);

        } else if (mLeftClickListener != null

                && mRightClickListener == null) {

            mLeftButton.setOnClickListener(mLeftClickListener);

        } else {



        }*/

    }
    @Override
     public void onResume() {
        super.onResume();

        int dialogWidth = ActionBar.LayoutParams.WRAP_CONTENT;
        int dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view1 = getActivity().getLayoutInflater().inflate(R.layout.activity_custom_dialog,null);

        TextView Edname = (TextView)view1.findViewById(R.id.dialog_name);
        TextView Edbirth = (TextView)view1.findViewById(R.id.dialog_birth);
        TextView EdPhone = (TextView)view1.findViewById(R.id.dialog_phone);
        Button button = (Button)view1.findViewById(R.id.dialog_submit);
        ImageView imageView = (ImageView)view1.findViewById(R.id.my_profile_image);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(),EdtiProfileActivity.class);
               intent.putExtra("_id",_id);
               startActivity(intent);
           }
       });

        Edname.setText(name);
        Edbirth.setText(birth);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Cursor c = getActivity().getContentResolver().query(Uri.parse(uri), null, null, null, null);

        c.moveToNext();

        String path = c.getString(c.getColumnIndex("_data"));

        c.close();

        Bitmap orgImage = BitmapFactory.decodeFile(path, options);

        imageView.setImageBitmap(orgImage);
        EdPhone.setText("0"+String.valueOf(phone));

        builder.setView(view1);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }


}
