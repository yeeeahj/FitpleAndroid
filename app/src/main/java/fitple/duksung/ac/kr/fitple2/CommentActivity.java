package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentActivity extends Activity implements ResponseCode, RequestCode {
    private int feed_seq;
    private int member_seq;
    private String content;
    private String nickname;
    private String date;
    public static ArrayList<CommentInfo> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Comment_list_Adpater r_Adapter;
    private CommentActivity r_Activity;
    Button addComment_btn;
    EditText addComment_text;
    TheTask task;
    ImageView profile_Img;
    TextView nickname_Text;
    TextView date_Text;
    TextView content_Text;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //title
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_feed);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);

        setContentView(R.layout.activity_comment);

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText("댓글");

        Intent intent = getIntent();
        feed_seq = intent.getExtras().getInt("feed_seq");
        member_seq = intent.getExtras().getInt("member_seq");
        content = intent.getExtras().getString("content");
        nickname = intent.getExtras().getString("nickname");
        date = intent.getExtras().getString("date");
        //통신
        task = new TheTask();
        task.execute("show", String.valueOf(feed_seq));
        //게시글 보여주기

        nickname_Text = (TextView)findViewById(R.id.comment_feednickname);
        content_Text =(TextView)findViewById(R.id.comment_feedcontent);
        date_Text =(TextView)findViewById(R.id.comment_feeddate);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm EEE");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date_Text.setText( format.format(format2.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        nickname_Text.setText(nickname);
        content_Text.setText(content);




        recyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        r_Adapter = new Comment_list_Adpater();
        r_Activity = this;
        recyclerView.setAdapter(r_Adapter);
        addComment_btn = (Button) findViewById(R.id.addComment_btn);
        addComment_text = (EditText) findViewById(R.id.addComment_text);

        addComment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel(true);
                TheTask writetask = new TheTask();
                writetask.execute("upload", String.valueOf(feed_seq), String.valueOf(addComment_text.getText()));

                TheTask refreshTask = new TheTask();
                refreshTask.execute("show", String.valueOf(feed_seq));
                r_Adapter.viewRefresh();


            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

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
            ArrayList<NameValuePair> nameValueArr = new ArrayList<NameValuePair>();
            String url = "";
            nameValueArr.add(new BasicNameValuePair("feed_seq", params[1]));
            if (params[0].equals("show")) {
                url = "http://203.252.219.238:8080/Fitple/Servlet/CommentListServlet";
            } else if (params[0].equals("upload")) {
                nameValueArr.add(new BasicNameValuePair("comment", params[2]));
                url = "http://203.252.219.238:8080/Fitple/Servlet/CommentWriteServlet";
            }
            //  nameValueArr.add(new BasicNameValuePair("feed_seq",String.valueOf(feed_seq)));
            // String url = "http://203.252.219.238:8080/Fitple/Servlet/CommentListServlet";
            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url, nameValueArr);
            try {

                responseCode = String.valueOf(jObject.get("responseCode"));
                if (responseCode.equals(String.valueOf(ResponseCode.COMMENTLIST_SUCCESS))) {
                    JSONArray comment_array = jObject.getJSONArray("comment");
                    int comment_count = comment_array.length();
                    Log.e(" comment_count", comment_count + "");
                    commentList.clear();
                    for (int i = 0; i < comment_count; i++) {
                        JSONObject comment = comment_array.getJSONObject(i);

                        CommentInfo commentItem = new CommentInfo(Integer.parseInt(String.valueOf(comment.get("member_seq"))), String.valueOf(comment.get("nickname")), String.valueOf(comment.get("image"))
                                , Integer.parseInt(String.valueOf(comment.get("comment_seq"))), String.valueOf(comment.get("date")), String.valueOf(comment.get("comment")));
                        commentList.add(i, commentItem);
                    }
                } else if (responseCode.equals(String.valueOf(ResponseCode.COMMENTWRITE_SUCCESS))) {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseCode;
        }


    }

}
