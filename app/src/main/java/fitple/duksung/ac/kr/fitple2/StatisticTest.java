

package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;



import android.content.Intent;
import android.graphics.drawable.ColorDrawable;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StatisticTest extends FragmentActivity {
    private int group_seq;
    private ViewPager s_ViewPager;
    private ViewPagerAdapter v_Adapter;
    Fragment cur_fragment = new Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_test);
        Intent intent = getIntent();
        group_seq = intent.getExtras().getInt("group_seq");
        //    m_Adapter =new StatisticTestAdapter(group_seq);

        // Xml에서 추가한 ListView 연결
        s_ViewPager = (ViewPager) findViewById(R.id.statistics_viewpager);

        // ListView에 어댑터 연결
        s_ViewPager.setAdapter(new adapter(getSupportFragmentManager()));
    }

    private class adapter extends FragmentPagerAdapter {
        public adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < 0 || 3 <= position)
                return null;
            switch (position) {
                case 0:
                    cur_fragment = new page_1();
                    break;
                case 1:
                    cur_fragment = new page_2();
                    break;
                case 2:
                    cur_fragment = new page_3();
                    break;
            }
            return cur_fragment;
        }


        @Override
        public int getCount() {
            return 3;
        }
    }

    class page_1 extends  android.support.v4.app.Fragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.statistic_one,container,false);
            LinearLayout background=(LinearLayout)linearLayout.findViewById(R.id.viewpager_one);
    //        TextView page_num=(TextView)linearLayout.findViewById(R.id.page_num);
      //      page_num.setText(String.valueOf(1));
      //      background.setBackground(new ColorDrawable(0xff6dc6d2));
            return linearLayout;
        }
    }
    class page_2 extends android.support.v4.app.Fragment  {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.statistic_two,container,false);
            LinearLayout background=(LinearLayout)linearLayout.findViewById(R.id.viewpager_two);
         //   TextView page_num=(TextView)linearLayout.findViewById(R.id.page_num);
        //    page_num.setText(String.valueOf(1));
       //     background.setBackground(new ColorDrawable(0xff6dc6d2));
            return linearLayout;
        }
    }
    class page_3 extends android.support.v4.app.Fragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.statistic_three,container,false);
            LinearLayout background=(LinearLayout)linearLayout.findViewById(R.id.viewpager_three);
        //    TextView page_num=(TextView)linearLayout.findViewById(R.id.page_num);
        ///    page_num.setText(String.valueOf(1));
        //    background.setBackground(new ColorDrawable(0xff6dc6d2));
            return linearLayout;
        }
    }



}


