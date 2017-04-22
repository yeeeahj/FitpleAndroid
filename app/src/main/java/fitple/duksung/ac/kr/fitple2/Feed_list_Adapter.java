package fitple.duksung.ac.kr.fitple2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by YEJIN on 2016-09-09.
 */
public class Feed_list_Adapter extends RecyclerView.Adapter<Feed_list_Adapter.ViewHolder> implements View.OnClickListener{
    private static ClickListener clickListener;
    Context context;

    private int feed_seq;
    private static int feed_count;



    String imgUrl =null;
    Bitmap bmImg;
    int like_num;
    public Feed_list_Adapter(Context context){
        this.context = context;
    }

    public  void setFeed_count(int feed_count){
        this.feed_count= feed_count;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int group_seq =0;
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.feed_item, viewGroup, false);



        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(Feed_list_Adapter.ViewHolder holder, int position) {
        final ViewHolder returnViewHolder = (ViewHolder) holder;
        final int pos = position;



        final int[] like_num = new int[1];
        Bitmap mPlaceHolderBitmap = null;
        // view가 null일 경우 커스텀 레이아웃을 얻어 옴
        imgUrl= FeedActivity.feedList.get(position).getImage();
        holder.like_state=FeedActivity.feedList.get(position).getLikeState();
//            task.execute(imgUrl);
        if(holder.like_state == true) {
            //      holder.feed_like.setBackgroundResource(R.drawable.button_unlike);
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm EEE");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            holder.feed_date.setText( format.format(format2.parse(FeedActivity.feedList.get(position).getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //   holder.feed_date.setText(date_List.get(position));
        holder.feed_comment.setText(FeedActivity.feedList.get(position).getConmment()+"");
        holder.feed_content.setText(FeedActivity.feedList.get(position).getContent());
        holder.feed_seq=FeedActivity.feedList.get(position).getFeed_seq();

        holder.feed_nickname.setText(FeedActivity.feedList.get(position).getNickname());

        // holder.feed_image = feed_image;
        //  holder.feed_profile = feed_profile;
        holder.feed_like_num.setText(FeedActivity.feedList.get(position).getLikeNum()+"");






        if (cancelPotentialWork(pos, holder.feed_image)) {
            final BitmapLoadTask task = new BitmapLoadTask( holder.feed_image);


            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable( holder.feed_image.getResources(), mPlaceHolderBitmap, task);

            holder.feed_image.setImageDrawable(asyncDrawable); // imageView에 AsyncDrawable을 묶어줌

            task.execute(imgUrl);
            //      ((BitmapDrawable) holder.feed_image.getDrawable()).getBitmap().recycle();
        }
        //option menu
        if((FeedActivity.feedList.get(position).getMember_seq()== MyInfo.member_seq)) {
            Log.d("photo",position+"");
            Bitmap image = bmImg;

            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            holder.shareButton.setVisibility(View.VISIBLE);
            holder.shareButton.setShareContent(content);


        }else {
            //안보이게 하기
            holder.shareButton.setVisibility(View.GONE);
        }

        //좋아요
        holder.feed_like.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public int getItemCount() {
        return feed_count;
    }

    @Override
    public void onClick(View v) {

    }


    public  class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageButton feed_like ;
        TextView feed_comment;
        TextView feed_content;
        TextView feed_date;
        TextView feed_nickname;
        TextView feed_like_num;
        ImageView feed_image;
        ImageView feed_profile;
        ShareButton shareButton;
        boolean like_state;
        int feed_seq;

        public android.support.v7.widget.Toolbar mCardViewToolbar;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            feed_like = (ImageButton) itemView.findViewById(R.id.feed_like);
            feed_comment = (TextView) itemView.findViewById(R.id.feed_comment);
            feed_content = (TextView) itemView.findViewById(R.id.feed_content);
            feed_date = (TextView) itemView.findViewById(R.id.feed_date);
            feed_nickname = (TextView) itemView.findViewById(R.id.feed_nickname);
            feed_image = (ImageView) itemView.findViewById(R.id.feed_image);
            feed_profile = (ImageView) itemView.findViewById(R.id.feed_profile);
            feed_like_num = (TextView)itemView.findViewById(R.id.feed_like_num);
            shareButton = (ShareButton)itemView.findViewById(R.id.shareBtn);


            //  mCardViewToolbar = (android.support.v7.widget.Toolbar) itemView.findViewById(R.id.toolbarTEST);
            itemView.setOnClickListener(this);

            feed_like.setOnClickListener(this);

        }




        @Override
        public void onClick(View view) {

            Log.e("intent","인식은되냐2" );

            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("feed_seq",feed_seq);
            intent.putExtra("nickname",feed_nickname.getText());
            intent.putExtra("comment",feed_comment.getText());
            intent.putExtra("date",feed_date.getText());
            //intent.putExtra("date",  feed_image.getBackground());

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        // ViewHolder
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        Feed_list_Adapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);

    }




    public class BitmapLoadTask extends AsyncTask<String, Integer,Bitmap>{

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
