package lecture.mobile.final_project.ma01_20150995;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CalendarAdapter extends CursorAdapter{
    Cursor cursor;
    LayoutInflater inflater;

    public CalendarAdapter(Context context, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View list = inflater.inflate(R.layout.activity_calendar_adapter,parent,false);
        return list;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView title = (TextView)view.findViewById(R.id.calendar_title);
        TextView add = (TextView)view.findViewById(R.id.calendar_add);
        title.setText(cursor.getString(1));
        add.setText(cursor.getString(2));
    }
}
