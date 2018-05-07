package lecture.mobile.final_project.ma01_20150995;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import lecture.mobile.final_project.ma01_20150995.dto.DateListDTO;

/**
 * Created by user on 2017-12-20.
 */

public class CalendarFragment extends android.support.v4.app.Fragment {
    private MaterialCalendarView mcv;
    ArrayList<DateListDTO> list;
    DateListDBHelper helper;
    Cursor c;
    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_custom_calendar,null);
        mcv = (MaterialCalendarView)view.findViewById(R.id.calendarView);
        ListView listView = (ListView)view.findViewById(R.id.calendar_list_view);
        final CalendarAdapter adapter = new CalendarAdapter(getActivity(),null);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),UpdateDateList.class);
                intent.putExtra("data",id);
                startActivity(intent);
            }
        });

        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(1996, 1, 1))
                .setMaximumDate(CalendarDay.from(2022, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy 년 MM 월");
        final DateFormatTitleFormatter dateFormatTitleFormatter = new DateFormatTitleFormatter(simpleDateFormat);
        mcv.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                CharSequence charSequence = dateFormatTitleFormatter.format(day);
                return charSequence;
            }
        });




        helper = new DateListDBHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("select * from "+helper.TABLE_NAME+" ;",null);
        list = new ArrayList<DateListDTO>();
        while(cursor.moveToNext()){
            DateListDTO dto = new DateListDTO();
            dto.setTitle(cursor.getString(1));
            dto.setAddress(cursor.getString(2));
            dto.setYear(cursor.getInt(3));
            dto.setMonth(cursor.getInt(4));
            dto.setDay_of_month(cursor.getInt(5));
            list.add(dto);
        }
        cursor.close();

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                SQLiteDatabase db = helper.getReadableDatabase();
                c = db.rawQuery("select * from "+DateListDBHelper.TABLE_NAME+" where year = "+date.getYear()+" and month = "+date.getMonth()+" and day_of_month = "+date.getDay()+";",null);
                adapter.changeCursor(c);

            }
        });



        ArrayList<CalendarDay> calendarDays = new ArrayList<CalendarDay>();
        for(int i = 0; i < list.size(); i++){
            DotSpan dotSpan= new DotSpan();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,list.get(i).getYear());
            calendar.set(Calendar.MONTH,list.get(i).getMonth());
            calendar.set(Calendar.DAY_OF_MONTH,list.get(i).getDay_of_month());
            CalendarDay calendarDay = CalendarDay.from(calendar);
            calendarDays.add(calendarDay);
        }

        EventDecorator eventDecorator = new EventDecorator(Color.RED,calendarDays);
        mcv.addDecorator(eventDecorator);



        return view;
    }

    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(c != null) c.close();
        if(helper != null)helper.close();
    }
}
