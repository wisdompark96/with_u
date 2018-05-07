package lecture.mobile.final_project.ma01_20150995;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by user on 2017-11-24.
 */

public class RoadAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<RoadDTO> list;
    public RoadAdapter(Context context, int resource, ArrayList<RoadDTO> list){

        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setList(ArrayList<RoadDTO> list) {
        this.list = list;
    }

    public void clear() {
        this.list = new ArrayList<RoadDTO>();

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RoadDTO getItem(int position) {
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

        TextView title = (TextView)convertView.findViewById(R.id.road_title);
        TextView start = (TextView)convertView.findViewById(R.id.start_road);

        title.setText(list.get(position).getTitle());
        start.setText(list.get(position).getStart());

        return convertView;
    }
}
