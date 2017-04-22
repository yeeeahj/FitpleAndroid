package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;


public class StatisticActivity extends Activity {

    private ArrayList<String>   total_nickname_List;
    private ArrayList<String>   total_count_List;
    private ArrayList<String>   total_image_List;

    private ArrayList<String>   parti_nickname_List;
    private ArrayList<String>   parti_image_List;

    private ArrayList<String>   unparti_nickname_List;
    private ArrayList<String>   unparti_image_List;

    private ArrayList<String>   date_List;

    private int totalMember;
    private int partiMember;
    private int statistics;

    private ViewPager mPager;
    private TextView test;
    private TextView test2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        mPager = (ViewPager)findViewById(R.id.statistic_Pager);
        mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));

        total_nickname_List = new ArrayList<String>();
        total_count_List = new ArrayList<String>();
        total_image_List = new ArrayList<String>();
        parti_nickname_List = new ArrayList<String>();
        parti_image_List = new ArrayList<String>();
        unparti_nickname_List = new ArrayList<String>();
        unparti_image_List = new ArrayList<String>();
        date_List = new ArrayList<String>();
    }

    private class PagerAdapterClass extends PagerAdapter {

        private LayoutInflater mInflater;

        public PagerAdapterClass(Context c){
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(View pager, int position) {



            View v = null;
            View v1 = mInflater.inflate(R.layout.statistic_one, null);
            PieView pieView = (PieView)v1.findViewById(R.id.pie_view);
            ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
            int percent =statistics;
            pieHelperArrayList.add(0, new PieHelper(percent));
            pieHelperArrayList.add(1, new PieHelper(100-percent));
            pieView.setDate(pieHelperArrayList);
            //    pieView.selectedPie(2); //optional
            //   pieView.setOnPieClickListener(listener); //optional
            pieView.showPercentLabel(true); //optional

            // TextView에 현재 position의 문자열 추가
      //      TextView home_percent = (TextView) v1.findViewById(R.id.home_percent);


            //home_percent.setX(pieView.getX()+pieView.getWidth() / 2);
           // home_percent.setY(pieView.getY() + pieView.getHeight() / 2);
            //   Log.v("position", home_pie.getX() + home_pie.getWidth() / 2 + "," + home_pie.getY() + home_pie.getHeight() / 2);
            Log.v("position",pieView.getX() +","+pieView.getY());

/*
            TabHost tab_host = (TabHost) v1.findViewById(R.id.tabhost);
            tab_host.setup();

            TabHost.TabSpec ts1 = tab_host.newTabSpec("tab1");
            ts1.setIndicator("tab1");
            ts1.setContent(R.id.testtttt);
            tab_host.addTab(ts1);

            TabHost.TabSpec ts2 = tab_host.newTabSpec("tab2");
            ts2.setIndicator("tab2");
            ts2.setContent(R.id.idddddd);
            tab_host.addTab(ts2);

            tab_host.setCurrentTab(0);
*/

            View v2 = mInflater.inflate(R.layout.statistic_two, null);
            View v3 = mInflater.inflate(R.layout.statistic_three, null);

            TheTask task = new TheTask();
            task.execute();

            test2 = (TextView)v2.findViewById(R.id.test2);
            test2.setText("테스트 2");

            if(position==0){
                v = v1;
            //    test = (TextView)v.findViewById(R.id.test);

              //  v.findViewById(R.id.btn_click).setOnClickListener(mPagerListener);
            }
            else if(position==1){
                v = v2;
                test2 = (TextView)v.findViewById(R.id.test2);
                test2.setText("테스트 2");
               // v.findViewById(R.id.iv_two);
               // v.findViewById(R.id.btn_click_2).setOnClickListener(mPagerListener);
            }else{
                v = v3;
               // v.findViewById(R.id.iv_three);
               // v.findViewById(R.id.btn_click_3).setOnClickListener(mPagerListener);
            }

            ((ViewPager)pager).addView(v, 0);

            return v;
        }

        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager)pager).removeView((View)view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
        @Override public Parcelable saveState() { return null; }
        @Override public void startUpdate(View arg0) {}
        @Override public void finishUpdate(View arg0) {}
    }


    class TheTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);




        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            InputStream is = null;
        }

        @Override
        protected String doInBackground(String... params) {
            String responseCode=null;
            ArrayList<NameValuePair> nameValueArr = new ArrayList<NameValuePair>();
            //    Log.e("params ssssssssssssssssssss", params[0] + params[1] + params[2]);
            String url="http://203.252.219.238:8080/Fitple/test.jsp";

            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url, nameValueArr);
            try {
                JSONArray totalAuth_array = jObject.getJSONArray("totalAuth");
                int totalAuth_length = totalAuth_array.length();
                for(int i =0 ; i<totalAuth_length;i++){
                    JSONObject totalAuth = totalAuth_array.getJSONObject(i);
                    total_nickname_List.add(i, (String)totalAuth.get("nickname"));
                    total_count_List.add(i, (String)totalAuth.get("count"));
                    total_image_List.add(i, null);
                   // total_image_List.add(i, (String)totalAuth.get("image"));
                }

                 totalMember = jObject.getInt("total_member");
                 partiMember = jObject.getInt("participant");
                 statistics = jObject.getInt("statistics");

                JSONArray parti_info_array = jObject.getJSONArray("participant_info");
                int parti_info_length = parti_info_array.length();
                for(int i =0 ; i<parti_info_length;i++){
                    JSONObject parti_info= parti_info_array.getJSONObject(i);
                    parti_nickname_List.add(i, (String) parti_info.get("nickname"));
                    parti_image_List.add(i, null);
                  //  parti_image_List.add(i, (String) parti_info.get("image"));
                }

                JSONArray unparti_info_array = jObject.getJSONArray("unparticipant_info");
                int unparti_info_length = unparti_info_array.length();
                for(int i =0 ; i<unparti_info_length;i++){
                    JSONObject unparti_info= unparti_info_array.getJSONObject(i);
                    unparti_nickname_List.add(i, (String) unparti_info.get("nickname"));
                    unparti_image_List.add(i, null);
                   // unparti_image_List.add(i, (String) unparti_info.get("image"));
                }

                JSONArray totalDate_array = jObject.getJSONArray("totalDate");
                int totalDate_length = totalDate_array.length();
                for(int i =0 ; i<totalDate_length;i++){
                    JSONObject totalDate= totalDate_array.getJSONObject(i);
                    date_List.add(i, (String) totalDate.get("date"));

                }


                responseCode = jObject.get("responseCode")+"50";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseCode;
        }




    }
}
