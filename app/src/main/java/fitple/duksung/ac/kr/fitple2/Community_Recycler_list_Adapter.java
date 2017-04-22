package fitple.duksung.ac.kr.fitple2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by YEJIN on 2016-09-13.
 */
public class Community_Recycler_list_Adapter extends RecyclerView.Adapter<Community_Recycler_list_Adapter.ViewHolder> implements ResponseCode,RequestCode{

    ArrayList<CommunityData> mDataset= new ArrayList<CommunityData>();

    private int type;
    Bitmap bmImg;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;

        public TextView mTextView;


        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.community_groupimage);

            mTextView = (TextView)view.findViewById(R.id.community_groupname);
        }
    }
    public Community_Recycler_list_Adapter(  int type) {

        this.type = type;
        TheTask mainTask = new TheTask();
        mainTask.execute(type);
        Log.d("tofinderror ",this.type+"");
    }



    @Override
    public Community_Recycler_list_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.community_item, viewGroup, false);


        // set the view's size, margins, paddings and layout parameters
       // return new ViewHolder(itemView, viewType);

        DisplayMetrics dm = viewGroup.getContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Log.e("contextsize", width + "+" + height);



        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.width = (width-180)/2;
        params.height = (height-60)/4;

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Community_Recycler_list_Adapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).text);

      //  holder.mImageView.setImageResource(mDataset.get(position).img);

        Bitmap mPlaceHolderBitmap = null;

       if (cancelPotentialWork(position, holder.mImageView)) {
            final BitmapLoadTask task = new BitmapLoadTask(holder.mImageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(holder.mImageView.getResources(), mPlaceHolderBitmap, task);
           holder.mImageView.setImageDrawable(asyncDrawable); // imageView에 AsyncDrawable을 묶어줌
            task.execute(String.valueOf(mDataset.get(position).img));
        }

    }

    @Override
    public int getItemCount() {
        if(type ==1) {

            return 2;
        }
        else {
           return mDataset.size();
        }
    }
    private class BitmapLoadTask extends AsyncTask<String, Integer,Bitmap> {

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


    class TheTask extends AsyncTask<Integer, String, String> {
        ArrayList<CommunityData> data ;

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);
            // update textview here
            Log.e("responseCode", responseCode);

        }

        @Override
        protected String doInBackground(Integer... integers) {
            int responseCode=0;
            String url;
            if (integers[0] == 1) {
                url = "http://203.252.219.238:8080/Fitple/Servlet/HotCommunityServlet";
            }else{
                url = "http://203.252.219.238:8080/Fitple/Servlet/MainCommunityServlet";
            }

            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url);


            try {

                responseCode = (int)jObject.get("responseCode") ;
                Log.d("responseCode", responseCode+"");
                if (responseCode==COMMUNITY_SUCCESS) {
                    JSONArray group_array;
                    if (integers[0] == 1) {
                        group_array = jObject.getJSONArray("Group");
                        Log.e("??","두번째 호출");

                    }else{
                        group_array = jObject.getJSONArray("Main_group");
                        Log.e("??","첫번째 호출");

                    }

                    int  group_count = group_array.length();
                    Log.e("array count", group_count + "");

                    for (int i = 0; i < group_count; i++) {

                        JSONObject group = group_array.getJSONObject(i);
                        mDataset.add(new CommunityData(Integer.parseInt((String) group.get("group_seq")),(String)group.get("groupname"),(String) group.get("group_image")));

                    }
                } else{}

            } catch (JSONException e) {
                e.printStackTrace();

            }
            return responseCode+"";
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            InputStream is = null;
        }

    }
}

class CommunityData{

    public String text;

    public String img;
    public int group_seq;

    public CommunityData(int group_seq ,String text, String img){

        this.group_seq = group_seq;
        this.text = text;

        this.img = img;


    }

}


