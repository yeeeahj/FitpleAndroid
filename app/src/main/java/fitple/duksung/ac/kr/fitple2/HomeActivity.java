package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class HomeActivity extends Activity {
    private RecyclerView recyclerView;
    private Home_list_Adapter r_Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        FloatingActionsMenu home_menu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        recyclerView = (RecyclerView) findViewById(R.id.HomeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        r_Adapter = new Home_list_Adapter();
        recyclerView.setAdapter(r_Adapter);
        //click event
        r_Adapter.setOnItemClickListener(new Home_list_Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(HomeActivity.this, FeedActivity.class);
                Log.d("intent", "" + Integer.parseInt(r_Adapter.getGroupSeq(position)));
                intent.putExtra("group_seq", Integer.parseInt(r_Adapter.getGroupSeq(position)));
                intent.putExtra("group_name",r_Adapter.getGroupName(position));

                startActivity(intent);
                r_Adapter.group_seq_List.clear();
                r_Adapter.groupname_List.clear();
                r_Adapter.statistics_List.clear();
                finish();
            }
        });

    }

    public void fabMenuClicked(View v) {
        final FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) v;
    }
    public void fab_community(View v) {
        Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
        startActivity(intent);
    }
    public void fab_new(View v) {
        Intent intent = new Intent(HomeActivity.this, GroupAddActivity.class);
        startActivity(intent);
    }
}

