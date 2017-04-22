package fitple.duksung.ac.kr.fitple2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Community_Recycle_Activity extends AppCompatActivity implements ResponseCode,RequestCode {
    private RecyclerView main_recyclerView;
    private Community_Recycler_list_Adapter main_Adapter;


    private RecyclerView hot_recyclerView;
    private Community_Recycler_list_Adapter hot_Adapter;
/*    ArrayList<CommunityData> hot_Data= new ArrayList<CommunityData>();
    ArrayList<CommunityData> main_Data= new ArrayList<CommunityData>();*/
  //  private int group_seq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community__recycle_);
  /*      TheTask hotTask = new TheTask();
        hotTask.execute(1);*/
        hot_recyclerView = (RecyclerView) findViewById(R.id.hot_RecyclerView);
        LinearLayoutManager hot_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        hot_recyclerView.setLayoutManager(hot_layoutManager);

/*        TheTask mainTask = new TheTask();
        mainTask.execute(2);*/
        main_recyclerView = (RecyclerView) findViewById(R.id.main_RecyclerView);
        LinearLayoutManager main_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        main_recyclerView.setLayoutManager(main_layoutManager);

        main_Adapter = new Community_Recycler_list_Adapter(2);
        main_recyclerView.setAdapter(main_Adapter);

        hot_Adapter = new Community_Recycler_list_Adapter(1);
        hot_recyclerView.setAdapter(hot_Adapter);

    }

}
