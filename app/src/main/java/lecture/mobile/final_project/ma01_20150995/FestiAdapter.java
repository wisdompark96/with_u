package lecture.mobile.final_project.ma01_20150995;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lecture.mobile.final_project.ma01_20150995.dto.FestDTO;

/**
 * Created by user on 2017-11-24.
 */

public class FestiAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<FestDTO> list;
    public FestiAdapter(Context context, int resource, ArrayList<FestDTO> list){

        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setList(ArrayList<FestDTO> list) {
        this.list = list;
    }

    public void clear() {
        this.list = new ArrayList<FestDTO>();

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FestDTO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(layout, parent, false);

        }

        TextView title = (TextView)convertView.findViewById(R.id.fest_title);
        TextView start = (TextView)convertView.findViewById(R.id.start_end);
        TextView place = (TextView)convertView.findViewById(R.id.fest_place);

        title.setText(list.get(position).getTitle());
        start.setText(list.get(position).getStartDay()+" - "+list.get(position).getEndDay());
        place.setText(list.get(position).getPlace());

        return convertView;
    }
}
