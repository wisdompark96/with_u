package lecture.mobile.final_project.ma01_20150995;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import lecture.mobile.final_project.ma01_20150995.dto.DateListDTO;

/**
 * Created by user on 2017-12-01.
 */

public class FragmentDateList extends Fragment {

    ListView listview;
    ArrayList<DateListDTO> arrayList;
    DateListDBHelper helper;
    DateListAdapter adapter;
    Cursor cursor;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_date_list,null);

        ImageButton button = (ImageButton)view.findViewById(R.id.add_date_list_img_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddDateList.class);
                startActivity(intent);
            }
        });
        adapter = new DateListAdapter(getActivity(),null);

        listview = (ListView)view.findViewById(R.id.date_list_view);
        listview.setClickable(true);
        listview.setFocusable(false);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),UpdateDateList.class);
                intent.putExtra("data",id);
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("delete from " + DateListDBHelper.TABLE_NAME + " where _id = " + id + ";");
                        cursor = db.rawQuery("select * from "+DateListDBHelper.TABLE_NAME+";", null);
                        adapter.changeCursor(cursor);
                    }
                });
                builder.show();

                return true;
            }
        });

        listview.setAdapter(adapter);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        helper = new DateListDBHelper(getActivity());

        SQLiteDatabase db = helper.getReadableDatabase();

        cursor= db.rawQuery("select * from date_table;", null);
        adapter.changeCursor(cursor);

        helper.close();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
    }
}
