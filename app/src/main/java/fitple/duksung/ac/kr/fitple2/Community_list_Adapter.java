package fitple.duksung.ac.kr.fitple2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by USER on 2016-05-12.
 */
public class Community_list_Adapter extends BaseAdapter {
    private Context cContext;
    private int type;
    private int group_count;
    String imgUrl =null;

    TextView commu_groupname;
    ImageView commu_group_image;
    CommunityHolder holder=null;
    Bitmap bmImg;

    public ArrayList<String> group_seq_List;
    private ArrayList<String> groupname_List;
    private ArrayList<String> group_info_List;
    private ArrayList<String> group_image_List;

    public Community_list_Adapter(Context c, int t) {
        cContext = c;
        type = t;

        group_seq_List = new ArrayList<String>();
        groupname_List = new ArrayList<String>();
        group_info_List = new ArrayList<String>();
        group_image_List = new ArrayList<String>();
        Log.e("eeee", "일을 하고 있니?");
        TheTask task = new TheTask();
        task.execute();
    }

    @Override
    public int getCount() {
        if(type ==1)
             return group_count;
        else
            return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        Log.v("position","position");
        final Context context = parent.getContext();

        Bitmap mPlaceHolderBitmap = null;


            try {
                if(convertView ==null){
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.community_item, parent, false);




//            imgUrl= group_image_List.get(position);

                    commu_groupname= (TextView)convertView.findViewById(R.id.community_groupname);
                    commu_group_image= (ImageView)convertView.findViewById(R.id.community_groupimage);

                    DisplayMetrics dm = context.getResources().getDisplayMetrics();
                    int width = dm.widthPixels;
                    int height = dm.heightPixels;
                    Log.e("contextsize", width + "+" + height);



                    ViewGroup.LayoutParams params = convertView.getLayoutParams();
                    params.width = (width-150)/2;
                    params.height = (height)/4;

                    convertView.setLayoutParams(params);
                    if(type==1 && position ==0) {
                        commu_groupname.setText("하루에 물 2L 마시기 ");
                    }else if(type==1 && position ==2){
                        commu_groupname.setText("줄넘기 매일 500개!!");
                    }else{
                        commu_groupname.setText(groupname_List.get(position));
                    }
                    //각 항목마다 화면크기 맞춰서 크기 정해주기

                    holder = new CommunityHolder();
                    holder.commu_group_name = commu_groupname;
                    holder.commu_group_image = commu_group_image;
                    convertView.setTag(holder);
                }else {
                    holder = (CommunityHolder) convertView.getTag();
                    commu_groupname = holder.commu_group_name;
                    commu_group_image=holder.commu_group_image;
                }

                    if (cancelPotentialWork(pos, commu_group_image)) {
                        final BitmapLoadTask task = new BitmapLoadTask(commu_group_image);
                        final AsyncDrawable asyncDrawable =
                                new AsyncDrawable(commu_group_image.getResources(), mPlaceHolderBitmap, task);
                        commu_group_image.setImageDrawable(asyncDrawable); // imageView에 AsyncDrawable을 묶어줌
                        if(type==1 && position ==0) {
                            commu_group_image.setImageResource(R.drawable.water);
                        }else if(type==1 && position ==2){
                            commu_group_image.setImageResource(R.drawable.run3);
                        }else if(type==1 && position ==3){
                            commu_group_image.setImageResource(R.drawable.plank);
                        }else{
                            task.execute(group_image_List.get(position));
                        }
                   }


            }
            catch (Exception e){
                Log.e("sleep","sleep중");
                new Thread() {
                    public void run() {
                        try {
                            // Do some work here
                            sleep(1000);
                            Log.e("sleep","sleep중");
                        } catch (Exception e) {
                        }
                        // start next intent
                        new Thread() {
                            public void run() {
                                // Dismiss the Dialog

                            }
                        }.start();
                    }
                }.start();
            }



        return convertView;
    }

    private class CommunityHolder {
        public int groupId;
        TextView commu_group_name;
        ImageView commu_group_image;
    }

    class TheTask extends AsyncTask<String, String, String> {

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
            String responseCode = null;
            String url;
            if (type == 1) {
               url = "http://203.252.219.238:8080/Fitple/Servlet/HotCommunityServlet";
            }else{
                url = "http://203.252.219.238:8080/Fitple/Servlet/MainCommunityServlet";
            }

            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url);


            try {

                responseCode = jObject.get("responseCode") + "";
                Log.e("responseCode", responseCode);
                if (responseCode.equals("31")) {
                    JSONArray group_array;
                    if (type == 1) {
                        group_array = jObject.getJSONArray("Group");
                        Log.e("??","두번째 호출");
                    }else{
                        group_array = jObject.getJSONArray("Main_group");
                        Log.e("??","첫번째 호출");
                    }

                    group_count = group_array.length();
                    Log.e("array count", group_count + "");
                    for (int i = 0; i < group_count; i++) {

                        JSONObject group = group_array.getJSONObject(i);
                        group_seq_List.add(i, (String) group.get("group_seq"));
                        groupname_List.add(i, (String) group.get("groupname"));
                        group_info_List.add(i, (String) group.get("group_info"));
                        group_image_List.add(i, (String) group.get("group_image"));


                    }
                } else{}

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
                final BitmapLoadTask bitmapLoadTask =
                        getBitmapLoadTask(imageView);
                if (this == bitmapLoadTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
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



