package fitple.duksung.ac.kr.fitple2;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;

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
import java.security.acl.Group;
import java.util.ArrayList;

import fitple.duksung.ac.kr.fitple2.EventListener.makeFeedClickListener;

public class FeedActivity extends Activity {

    private RecyclerView recyclerView;
    private Feed_list_Adapter r_Adapter;
    private int group_seq;
    private String group_name;
    public static ImageView title ;
    ImageButton makeFeedBtn;
    private Bitmap bmImg;
    public static ArrayList<FeedInfo> feedList = new ArrayList<FeedInfo>();;
    public  JSONObject group;
    public  GroupInfo groupinfo;
    String ImgUrl;
    Bitmap mPlaceHolderBitmap = null;
    boolean flag = false;
    makeFeedClickListener makeFeedClidkListener;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_feed);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);

        Intent intent = getIntent();
        group_seq = intent.getExtras().getInt("group_seq");
        group_name = intent.getExtras().getString("group_name");
        recyclerView = (RecyclerView) findViewById(R.id.feedRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        r_Adapter=  new Feed_list_Adapter(getApplicationContext());
        recyclerView.setAdapter(r_Adapter);
        TheTask datatask = new TheTask();
        datatask.execute();

        TextView titleText = (TextView) findViewById(R.id.title);
        makeFeedBtn =(ImageButton) findViewById(R.id.makeFeedBtn);

        titleText.setText(group_name);

        title = (ImageView)findViewById(R.id.feed_group_img);

        final BitmapLoadTask task = new BitmapLoadTask( title);
        final AsyncDrawable asyncDrawable =
                new AsyncDrawable( title.getResources(), mPlaceHolderBitmap, task);
        title.setImageDrawable(asyncDrawable); // imageView에 AsyncDrawable을 묶어줌
        while(flag==false){}
        task.execute( groupinfo.group_image);
        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getApplicationContext(),recyclerView, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(v.getId() == R.id.feed_like){//좋아요클릭
                    Log.v("click","여기라네1 좋아요 ㄱ");
                }else if(v.getId() == -1){// view 클릭
                    Log.v("click","여기라네1"+"댓글 ㄱ"+position);

                    Intent intent = new Intent(v.getContext(), CommentActivity.class);


                    intent.putExtra("feed_seq",FeedActivity.feedList.get(position).getFeed_seq());
                    intent.putExtra("member_seq",FeedActivity.feedList.get(position).getMember_seq());
                    intent.putExtra("nickname",FeedActivity.feedList.get(position).getNickname());
                    intent.putExtra("content",FeedActivity.feedList.get(position).getContent());
                    intent.putExtra("date",FeedActivity.feedList.get(position).getDate());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }


        }));

        makeFeedBtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               Intent intent = new Intent(v.getContext(), FeedAddActivity.class);
                                               intent.putExtra("group_seq",group_seq);
                                               startActivity(intent);
                                           }
                                       }

        );






    }
    public static BitmapLoadTask getBitmapLoadTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) { //drawable 이 null일 경우 false 반환
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapLoadTask();
            }
        }
        return null;
    }

    public class BitmapLoadTask extends AsyncTask<String, Integer,Bitmap> {

        private int feedId;


        private final WeakReference<ImageView> imageViewReference;

        public BitmapLoadTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
                Log.e("image", String.valueOf(urls));
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
                final BitmapLoadTask bitmapLoadTask =
                        getBitmapLoadTask(imageView);
                if (this == bitmapLoadTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }

        }

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
    public void fabMenuClicked(View v) {
        Intent intent = new Intent(FeedActivity.this, StatisticTest.class);
        //  Log.e("intent", r_Adapter,getItem);
        intent.putExtra("group_seq",group_seq);
        startActivity(intent);
    }

    class TheTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);
            // update textview here
            Log.e("responseCode", responseCode);

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

            nameValueArr.add(new BasicNameValuePair("group_seq",String.valueOf(group_seq)));
            String url = "http://203.252.219.238:8080/Fitple/Servlet/FeedServlet";
            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url,nameValueArr);


            try {

                responseCode = String.valueOf(jObject.get("responseCode")+"");
                if(responseCode.equals("9")) {
                    //나중에 전역변수에 저장
                    //   JSONObject feed = jObject.getJSONObject("feed");
                    JSONArray feed_array = jObject.getJSONArray("feed");
                    group = jObject.getJSONObject("group");
                    int feed_count=feed_array.length();

                    Log.e("feed count",feed_count+"");
                    r_Adapter.setFeed_count(feed_count);
                    for(int i=0;i<feed_count;i++) {
                        JSONObject feed = feed_array.getJSONObject(i);
                        boolean likest ;
                        if(Integer.parseInt((String) feed.get("like_st"))==1){
                            likest = true;
                        }else{
                            likest = false;
                        }
                        FeedInfo feeditem = new FeedInfo(Integer.parseInt((String) feed.get("feed_seq")),Integer.parseInt((String) feed.get("member_seq")),(String) feed.get("date"),(String) feed.get("nickname"),
                                (String) feed.get("image"), (String) feed.get("content"),Integer.parseInt((String) feed.get("like_num")), likest,Integer.parseInt((String) feed.get("comment_num")));
                        feedList.add(i,feeditem);


                        groupinfo = new GroupInfo (Integer.parseInt(group.getString("group_seq")),group.getString("groupname"),group.getString("group_image"));


                        flag =true;
                    }
                }else{
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseCode;
        }


    }

}
