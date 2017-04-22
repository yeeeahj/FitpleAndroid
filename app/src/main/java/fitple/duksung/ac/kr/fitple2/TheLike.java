package fitple.duksung.ac.kr.fitple2;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by YEJIN on 2016-09-13.
 */
public class TheLike extends AsyncTask<String,String,String> implements RequestCode,ResponseCode {

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
        String url = "http://203.252.219.238:8080/Fitple/Servlet/";

/*        switch () {
            nameValueArr.add(new BasicNameValuePair("like_st", params[0]));
            nameValueArr.add(new BasicNameValuePair("feed_seq", params[1]));
            url += LIKE_URL;

        }*/



        JsonParser jParser = new JsonParser();
        JSONObject jObject = jParser.getJSONFromUrl(url,nameValueArr);

        jObject.toString();

        return   jObject.toString();

    }



}