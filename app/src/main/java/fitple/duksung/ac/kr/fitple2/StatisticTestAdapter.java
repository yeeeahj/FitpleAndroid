package fitple.duksung.ac.kr.fitple2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.test.suitebuilder.TestMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by YEJIN on 2016-09-22.
 */
public class StatisticTestAdapter extends BaseAdapter {
    private ArrayList<RankTest> RankTest_List;
    private ArrayList<String> photo_List;
    private ArrayList<String> num_List; private ArrayList<String> name_List;


    public StatisticTestAdapter(int group_seq){
        RankTest_List= new ArrayList<RankTest>();
        photo_List = new ArrayList<String>(); name_List = new ArrayList<String>();
        num_List = new ArrayList<String>();

        TheTask task = new TheTask();
        task.execute(group_seq);
    }
    @Override
    public int getCount() {
        return photo_List.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final int pos = position;
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.itemtest, viewGroup, false);

        TextView rank =(TextView) view.findViewById(R.id.testrank);
        ImageView profile =(ImageView) view.findViewById(R.id.testprofile);
        TextView name =(TextView) view.findViewById(R.id.testname) ;
        TextView num  =(TextView) view.findViewById(R.id.testnum);
        rank.setText(position);
        profile.setImageURI(Uri.parse(photo_List.get(position)));
        name.setText(name_List.get(position));
        num.setText(num_List.get(position));
        return view;
    }
    public class RankTest {
        String rank  ;
        String profile;
        String name;
        String num;
        public RankTest(String num, String profile, String rank){
            this.name = name;
            this.profile= profile;
            this.num = num;
        }
    }
    class TheTask extends AsyncTask<Integer,String,String> {

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);
            // update textview here
//            Log.e("responseCode", responseCode);

        }

        @Override
        protected String doInBackground(Integer... integers) {
            String responseCode=null;
            ArrayList<NameValuePair> nameValueArr = new ArrayList<NameValuePair>();
            Log.d("eeeeeeeeeeeeeeeeeee", String.valueOf(integers[0]));
            nameValueArr.add(new BasicNameValuePair("group_seq",String.valueOf(integers[0])));
            String url = "http://203.252.219.238:8080/Fitple/Servlet/StatisticServlet";
            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url,nameValueArr);


            try {

                responseCode = String.valueOf(jObject.get("total_member"));
                if(responseCode.equals("5")) {
                    //나중에 전역변수에 저장
                    //   JSONObject group = jObject.getJSONObject("Group");
                    JSONArray auth_array = jObject.getJSONArray("totalAuth");


                    for(int i=0;i<auth_array.length();i++) {

                        JSONObject auth = auth_array.getJSONObject(i);
                      //  Log.d("eeee",String.valueOf(auth.get("nickname"))+String.valueOf(auth.get("image"))+String.valueOf(auth.get("count")));
                        name_List.add(i,(String)(auth.get("nickname")));
                      photo_List.add(i,(String)(auth.get("image")));
                        num_List.add(i,(String)((auth.get("count"))));
                  //      RankTest  t1 = new RankTest(String.valueOf(auth.get("nickname")),String.valueOf(auth.get("image")),String.valueOf(auth.get("count")));
                  //     RankTest_List.add(t1);


                        //                 Log.e("eee", group_seq + groupname + statistics );
                    }
                }else{
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseCode;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            InputStream is = null;
        }




    }
}
