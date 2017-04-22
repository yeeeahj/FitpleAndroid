package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class CommunityActivity extends Activity {
    private Community_list_Adapter main_Adapter = new Community_list_Adapter(this,2);
    private Community_list_Adapter hot_Adapter = new Community_list_Adapter(this,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_community);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText("Community");




        GridView main_gridview = (GridView) findViewById(R.id.MainGridView);
        GridView hot_gridview = (GridView) findViewById(R.id.HotGridView);
        main_gridview.setAdapter(main_Adapter);
        hot_gridview.setAdapter(hot_Adapter);

        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(CommunityActivity.this, CommunityDetailActivity.class);
                Log.e("intent", main_Adapter.group_seq_List.get(position));
                intent.putExtra("group_seq", main_Adapter.group_seq_List.get(position));
                startActivity(intent);

            }
        });
        hot_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CommunityActivity.this, CommunityDetailActivity.class);
                Log.e("intent", hot_Adapter.group_seq_List.get(position));
                intent.putExtra("group_seq",hot_Adapter.group_seq_List.get(position));
                startActivity(intent);
            }
        });
    }

}


