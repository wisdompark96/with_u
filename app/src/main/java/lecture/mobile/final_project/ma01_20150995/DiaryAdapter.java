package lecture.mobile.final_project.ma01_20150995;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import lecture.mobile.final_project.ma01_20150995.dto.DiaryDTO;

/**
 * Created by user on 2017-12-18.
 */

public class DiaryAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<DiaryDTO> myDatalist;
    private LayoutInflater layoutInflater;

    public DiaryAdapter(Context context, int layout, ArrayList<DiaryDTO> myDatalist){
        this.context = context;
        this.layout = layout;
        this.myDatalist = myDatalist;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return myDatalist.size();
    }

    @Override
    public Object getItem(int position) {
        return myDatalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myDatalist.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if(convertView == null){
            convertView = layoutInflater.inflate(layout, parent, false);
        }

        TextView date = (TextView)convertView.findViewById(R.id.diary_list_date);
        ImageView image = (ImageView)convertView.findViewById(R.id.diary_list_image);
        TextView title = (TextView)convertView.findViewById(R.id.diary_list_title);

      if(myDatalist.get(pos).getImage().equals("null"))
          image.setImageBitmap(null);
        else{
          BitmapFactory.Options options = new BitmapFactory.Options();
          options.inSampleSize = 4;

          Bitmap orgImage = BitmapFactory.decodeFile(myDatalist.get(pos).getImage(),options);

          image.setImageBitmap(orgImage);
      }

        title.setText(myDatalist.get(pos).getTitle());
        date.setText(myDatalist.get(pos).getDate());
        return convertView;
    }
}
