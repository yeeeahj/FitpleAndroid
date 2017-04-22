package fitple.duksung.ac.kr.fitple2;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class Home_list_Adapter extends RecyclerView.Adapter<Home_list_Adapter.ViewHolder> {
    private static ClickListener clickListener;
    private int group_count;

    public ArrayList<String> group_seq_List;
    public ArrayList<String> groupname_List;
    public ArrayList<String> statistics_List;

    //  PieView pieView ;

    public Home_list_Adapter() {
        group_seq_List = new ArrayList<String>();
        groupname_List = new ArrayList<String>();
        statistics_List = new ArrayList<String>();
        TheTask task = new TheTask();
        task.execute();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int group_seq =0;
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.home_item, viewGroup, false);

        return new ViewHolder(itemView, viewType);
    }
    @Override
    public void onBindViewHolder(Home_list_Adapter.ViewHolder holder, int position) {
        int group_seq =0;
        ViewGroup viewGroup;


        PieView pieView = (PieView) holder.itemView.findViewById(R.id.pie_view);
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
        int percent =Integer.parseInt(statistics_List.get(position));
        pieHelperArrayList.add(0, new PieHelper(percent));
        pieHelperArrayList.add(1, new PieHelper(100-percent));
        pieView.setDate(pieHelperArrayList);
        pieView.showPercentLabel(true); //optional
        //   TextView home_percent = (TextView) holder.itemView.findViewById(R.id.home_percent);


        //     home_percent.setX(pieView.getX()+pieView.getWidth() / 2);
        //     home_percent.setY(pieView.getY() + pieView.getHeight() / 2);
        //    Log.v("position",pieView.getX() +","+pieView.getY());
        //     home_percent.setText(statistics_List.get(position));
        group_seq = Integer.parseInt(group_seq_List.get(position));
        TextView home_groupname = (TextView) holder.itemView.findViewById(R.id.home_groupname);
        home_groupname.setText(groupname_List.get(position));


    }
    @Override
    public int getItemCount() {
        return group_count;
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
        // ViewHolder
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        Home_list_Adapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);

    }
    public String getGroupSeq(int position){return group_seq_List.get(position);}
    public String getGroupName(int position){return groupname_List.get(position);}

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

            String url = "http://203.252.219.238:8080/Fitple/Servlet/GroupListServlet";
            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url);


            try {

                responseCode = jObject.get("responseCode") + "";
                if (responseCode.equals("7")) {
                    //나중에 전역변수에 저장

                    JSONArray group_array = jObject.getJSONArray("group");
                    group_count = group_array.length();
                    Log.e("array count", group_count + "");
                    for (int i = 0; i < group_count; i++) {
                        JSONObject group = group_array.getJSONObject(i);
                        group_seq_List.add(i, (String) group.get("group_seq"));
                        groupname_List.add(i, (String) group.get("groupname"));
                        statistics_List.add(i, (String) group.get("statistics"));

                    }
                } else {
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }finally {

            }
            return responseCode;
        }


    }
}
