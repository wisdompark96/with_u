package lecture.mobile.final_project.ma01_20150995;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import lecture.mobile.final_project.ma01_20150995.dto.FestDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class DateFragment extends Fragment {


    ArrayList<FestDTO> result_fest;
    ArrayList<RoadDTO> result_road;
    FestiAdapter festiAdapter;
    RoadAdapter roadAdapter;
    public String search = "강원도";
    public int mon = 1;
    String query;
    boolean flag = false;


    public DateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = getResources().getString(R.string.url);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_date,null);
        final EditText editText = (EditText)view.findViewById(R.id.roadEd);
        Button search_btn = (Button) view.findViewById(R.id.search);
        Button road = (Button)view.findViewById(R.id.chRoad);
        Button fest = (Button)view.findViewById(R.id.chFesti);
        result_road = new ArrayList<RoadDTO>();
        result_fest = new ArrayList<FestDTO>();
        roadAdapter = new RoadAdapter(getActivity(),R.layout.raod_adapter_activity,result_road);
        festiAdapter = new FestiAdapter(getActivity(),R.layout.fest_adapter_activity,result_fest);
        final ListView listView = (ListView)view.findViewById(R.id.roadList);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ShowInfo.class);
                intent.putExtra("flag",flag);
                if(!flag)
                    intent.putExtra("data",result_road.get(position));
                else
                    intent.putExtra("data",result_fest.get(position));

                startActivity(intent);

            }
        };
        listView.setOnItemClickListener(itemClickListener);

        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                Toast.makeText(getActivity(),"걷기 좋은 길을 검색할께요!",Toast.LENGTH_SHORT).show();
            }
        });

        fest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                Toast.makeText(getActivity(),"전국의 축제를 검색할께요!",Toast.LENGTH_SHORT).show();
            }
        });


        search_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!flag) {
                    search = editText.getText().toString();
                    if(search == "")
                        search = "강원도";
                    listView.setAdapter(roadAdapter);
                    new RoadAsync().execute();
                }
                else{
                    search = editText.getText().toString();
                    if(search == "")
                       search = "강원도";
                    listView.setAdapter(festiAdapter);
                    new FestiAsync().execute();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    public class FestiAsync extends AsyncTask<String,Integer,String>{

        public final static String TAG = "RoadAsync";
        public final static int TIME_OUT = 10000;

        ProgressDialog progressDialog;
        String address;

        @Override
        protected String doInBackground(String... params) {

            StringBuilder urlBuilder;
            StringBuffer resultBuilder = new StringBuffer();
            try{
                urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/cltur-fstvl-std");
                urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=oqXsvnv9%2BwGiparBG6SObmvwo837JhjNlybDSRUSivkjiOl%2BiIUfKxi1LKx80mr14%2FuAkM3mbq%2BC4%2BCW%2FPCE%2BQ%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("s_page","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
                urlBuilder.append("&" + URLEncoder.encode("s_list","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*페이지 크기*/
                urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*XML/JSON 여부*/
                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if(conn != null){
                    conn.setConnectTimeout(TIME_OUT);
                    conn.setUseCaches(false);
                    conn.setReadTimeout(60000);
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        Log.e(TAG,"connected");
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        for(String line = br.readLine(); line != null; line = br.readLine()){
                            resultBuilder.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }

            }catch (MalformedURLException e){
                e.printStackTrace();

            }catch (Exception e){

            }


            return resultBuilder.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"wait","Downloading...");
        }

        @Override
        protected void onPostExecute(String s) {


            ArrayList<FestDTO> fest  = new ArrayList<FestDTO>();
            try {
                    JSONArray jsonArray = new JSONArray(s);

                for(int i = 0; i < jsonArray.length(); i++){
                    FestDTO festDTO = new FestDTO();
                   JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String startAdd = jsonObject.getString("축제시작일자");

                    boolean find = false;
                    if(startAdd.indexOf("2017") != -1) {
                            find = true;
                    }

                        Log.e(TAG,String.valueOf(find));


                    String Road = jsonObject.getString("소재지도로명주소");
                    String Add = jsonObject.getString("소재지지번주소");
                    String str;
                    if(Road .equals("null"))
                        str = Add;
                    else
                       str = Road;
                    boolean f = false;

                    if(str.indexOf(search) != -1){
                        f = true;
                    }

                    if(find && f){
                        Log.e(TAG,jsonObject.getString("축제명"));

                            festDTO.setStartDay(jsonObject.getString("축제시작일자"));
                            festDTO.setTitle(jsonObject.getString("축제명"));
                            festDTO.setEndDay(jsonObject.getString("축제종료일자"));
                            festDTO.setAboutFes(jsonObject.getString("축제내용"));
                            festDTO.setUrl(jsonObject.getString("홈페이지주소"));
                            festDTO.setAdd(jsonObject.getString("소재지지번주소"));
                            festDTO.setRoadAdd(jsonObject.getString("소재지도로명주소"));
                            fest.add(festDTO);

                    }
                    else continue;
                }
            }catch (JSONException e){
                Toast.makeText(getActivity(),"Error!",Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }finally {
                result_fest = fest;
                festiAdapter.clear();
                festiAdapter.setList(result_fest);
                festiAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

        }
    }
    public class RoadAsync extends AsyncTask<String,Integer,String>{

        public final static String TAG = "RoadAsync";
        public final static int TIME_OUT = 10000;

        ProgressDialog progressDialog;
        String address;

        @Override
        protected String doInBackground(String... params) {

            StringBuilder urlBuilder;
            StringBuffer resultBuilder = new StringBuffer();
            try{
                urlBuilder = new StringBuilder(query);
                urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=oqXsvnv9%2BwGiparBG6SObmvwo837JhjNlybDSRUSivkjiOl%2BiIUfKxi1LKx80mr14%2FuAkM3mbq%2BC4%2BCW%2FPCE%2BQ%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("s_page","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
                urlBuilder.append("&" + URLEncoder.encode("s_list","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*페이지 크기*/
                urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*XML/JSON 여부*/
                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if(conn != null){
                    conn.setConnectTimeout(TIME_OUT);
                    conn.setReadTimeout(60000);
                    conn.setUseCaches(false);
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        Log.e(TAG,"connected");
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        for(String line = br.readLine(); line != null; line = br.readLine()){
                            resultBuilder.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }

            }catch (MalformedURLException e){
                e.printStackTrace();

            }catch (Exception e){

            }


            return resultBuilder.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"wait","Downloading...");
        }

        @Override
        protected void onPostExecute(String s) {

            Log.e(TAG,"뭐지"+search);
            ArrayList<RoadDTO> road = new ArrayList<RoadDTO>();
            try {
                JSONArray jsonArray = new JSONArray(s);
                Log.e(TAG,String.valueOf(jsonArray.length()));

                for(int i = 0; i < jsonArray.length(); i++){
                    RoadDTO r = new RoadDTO();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String startRoad = jsonObject.getString("시작지점도로명주소");
                    String startAdd = jsonObject.getString("시작지점소재지지번주소");
                    Log.e(TAG,startAdd+startRoad);
                    String str;
                    if(startRoad .equals("null"))
                        str = startAdd;
                    else
                       str = startRoad;
                    boolean find = false;

                    if(str.indexOf(search)!= -1){
                            find = true;
                    }

                    Log.e(TAG,String.valueOf(find));

                    if(!find )
                        continue;
                    else{
                        r.setStartRoad(startRoad);
                        r.setStartAd(startAdd);
                        r.set_id(jsonObject.getInt("_id"));
                        r.setEndAd(jsonObject.getString("종료지점소재지지번주소"));
                        r.setEndRoad(jsonObject.getString("종료지점소재지도로명주소"));
                        r.setAboutRoad(jsonObject.getString("경로정보"));
                        r.setIntro(jsonObject.getString("길소개"));
                        r.setTitle(jsonObject.getString("길명"));
                        r.setTotalLength(jsonObject.getInt("총길이"));
                        r.setTotalTime(jsonObject.getString("총소요시간"));
                        r.setStart(jsonObject.getString("시작지점명"));
                        r.setEnd(jsonObject.getString("종료지점명"));
                        Log.e(TAG,r.getTitle());
                        road.add(r);
//                        Log.e(TAG,resultList.get(i).getTitle());
                    }
                }

                Log.e(TAG, String.valueOf(road.size()));



            }catch (JSONException e){
                Toast.makeText(getActivity(),"Error!",Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                result_road = road;
                roadAdapter.clear();
                roadAdapter.setList(result_road);
                roadAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

        }
    }


}
