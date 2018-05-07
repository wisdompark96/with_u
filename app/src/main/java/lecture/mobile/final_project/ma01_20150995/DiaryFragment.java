package lecture.mobile.final_project.ma01_20150995;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import lecture.mobile.final_project.ma01_20150995.dto.DiaryDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {


    ListView listView;
    public DiaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary,null);
        // Inflate the layout for this fragment
        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddDiaryActivity.class);
                startActivity(intent);
            }
        });
        listView = (ListView)view.findViewById(R.id.diary_listView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        DiaryDBHelper helper = new DiaryDBHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+helper.TABLE_NAME+";",null);
        final ArrayList<DiaryDTO> list = new ArrayList<DiaryDTO>();
        while(cursor.moveToNext()){
            DiaryDTO diaryDTO = new DiaryDTO();
            diaryDTO.set_id(cursor.getInt(0));
            diaryDTO.setImage(cursor.getString(1));
            diaryDTO.setTitle(cursor.getString(2));
            diaryDTO.setAddress(cursor.getString(3));
            diaryDTO.setDate(cursor.getString(4));
            diaryDTO.setMemo(cursor.getString(5));
            list.add(diaryDTO);
        }

        DiaryAdapter adapter = new DiaryAdapter(getActivity(),R.layout.listview_activity,list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DiaryShow.class);
                intent.putExtra("data",list.get(position));
                startActivity(intent);
            }
        });
    }

}
