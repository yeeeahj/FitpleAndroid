package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CommunityDetailActivity extends Activity implements ResponseCode{

    String title;
    String info;
    String image;
    String num;
    ArrayList member_seq_List;
    ArrayList member_name_List;
    ArrayList member_image_List;
    Bitmap bmImg=null;

    private String group_seq;
    public ImageView CDgroupimage;
    public TextView CDgrouptitle;
    public TextView CDgroupmembernum;
    public TextView CDgroupdetail;
    public TextView CDmembertitle;
    public TextView CDaveragetitle;
    private Button CDokay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_community_detail);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText("Project Info");

        Intent intent = getIntent();
        group_seq =intent.getExtras().getString("group_seq");




        CDgroupimage =(ImageView)findViewById(R.id.CDetail_groupimage);
        CDgrouptitle = (TextView)findViewById(R.id.CDetail_grouptitle);
        CDgroupmembernum = (TextView)findViewById(R.id.CDetail_groupmembernum);
        CDgroupdetail = (TextView)findViewById(R.id.CDtail_groupdetail);



        CDokay = (Button)findViewById(R.id.CDTail_okay);

        CDokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TheTask task = new TheTask();
                task.execute("groupJoin",group_seq);
        //        Toast.makeText(getApplicationContext(), "화이팅");
            }
        });


        member_seq_List = new ArrayList<String>();
        member_name_List = new ArrayList<String>();
        member_image_List = new ArrayList<String>();

        TheTask task = new TheTask();
        task.execute("groupDetail",group_seq);

    }
    private int imageCount(){
        return member_image_List.size();
    }


    private void setView(){
        CDgrouptitle.setText(title);
        CDgroupmembernum.setText(num +"명 ");
        CDgroupdetail.setText(info);


            final BitmapLoadTask task = new BitmapLoadTask(CDgroupimage);
            task.execute(image);

      //  CDgroupimage.setMaxHeight( bmImg.getHeight());
        CDgroupimage.setImageBitmap(bmImg);


    }
    class TheTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);
            // update textview here
            Log.e("responseCode", responseCode);
            setView();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            InputStream is = null;
        }

        @Override
        protected String doInBackground(String... params) {
            String responseCode = null;
            String url="";
            ArrayList<NameValuePair> nameValueArr = new ArrayList<NameValuePair>();


            nameValueArr.add(new BasicNameValuePair("group_seq", params[1]));
            if(params[0].equals("groupDetail")) {
                url = "http://203.252.219.238:8080/Fitple/Servlet/GroupDetailServlet";
            }else if(params[0].equals("groupJoin")){
                url = "http://203.252.219.238:8080/Fitple/Servlet/GroupJoinServlet";
            }

            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url,nameValueArr);


            try {

                responseCode = jObject.get("responseCode") + "";
                Log.e("responseCode", responseCode);
                if (responseCode.equals(String.valueOf(ResponseCode.GROUPDETAIL_SUCCESS))) {
                    JSONArray group_array;
                    JSONArray member_array;
                    group_array = jObject.getJSONArray("Group");
                    int group_count = group_array.length();

                    for (int i = 0; i < group_count; i++) {
                        JSONObject group = group_array.getJSONObject(i);
                        title=group.get("groupname").toString();
                        info= group.get("group_info").toString();
                        image= group.get("group_image").toString();
                        num= group.get("member_num").toString();
                    }

                    member_array = jObject.getJSONArray("Member");
                    int member_count = member_array.length();
                    for (int i = 0; i < member_count; i++) {
                        JSONObject member = member_array.getJSONObject(i);
                        member_seq_List.add(i,member.get("member_seq"));
                        member_name_List.add(i,member.get("nickname"));
                        member_image_List.add(i,member.get("image"));
                    }
                } else if(responseCode.equals(String.valueOf(ResponseCode.GROUPJOIN_SUCCESS))){
                    Log.v("join","success");
                }  else if(responseCode.equals(String.valueOf(ResponseCode.GROUPJOIN_FAIL))){
                    Log.v("join","fail");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseCode;
        }


    }

    private class BitmapLoadTask extends AsyncTask<String, Integer,Bitmap>{

        private int feedId;


        private final WeakReference<ImageView> imageViewReference;

        public BitmapLoadTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);


            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }

        protected void onPostExecute(Bitmap bitmap){
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
            ///    final BitmapLoadTask bitmapLoadTask =
            //            getBitmapLoadTask(imageView);
            //    if (this == bitmapLoadTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
          //      }
            }

        }

    }
    private static BitmapLoadTask getBitmapLoadTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) { //drawable 이 null일 경우 false 반환
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapLoadTask();
            }
        }
        return null;
    }


    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapLoadTask bitmapLoadTask = getBitmapLoadTask(imageView);

        if (bitmapLoadTask != null) {
            final int bitmapData = bitmapLoadTask.feedId;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
                bitmapLoadTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapLoadTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapLoadTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapLoadTask>(bitmapWorkerTask);
        }

        public BitmapLoadTask getBitmapLoadTask() {
            return bitmapWorkerTaskReference.get();
        }
    }


}

