package fitple.duksung.ac.kr.fitple2;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntityBuilder;


import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 2016-04-12.
 */
public class JsonParser {

    static InputStream is = null;

    static List<Cookie> cookies;
    static JSONObject jObj = null;
    static String json = "";
    static DefaultHttpClient httpClient = new DefaultHttpClient();

    // constructor
    public JsonParser() {

    }

    public JSONObject getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            updateCookie();
            //  DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            cookies = httpClient.getCookieStore().getCookies();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                // json = sb.toString().substring(0, sb.toString().length()-1);
            }
            is.close();
            json = sb.toString();
            Log.e("jsonString: ", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
    public JSONObject getJSONFromUrl(String url,ArrayList<NameValuePair> params) {

        // Making HTTP request
        try {
            updateCookie();

            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            cookies = httpClient.getCookieStore().getCookies();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                // json = sb.toString().substring(0, sb.toString().length()-1);
            }
            is.close();
            json = sb.toString();
            Log.e("jsonString: ", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject getJSONFromUrl(String url, MultipartEntityBuilder builder) {

        // Making HTTP request
        try {
            updateCookie();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(builder.build());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            cookies = httpClient.getCookieStore().getCookies();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                // json = sb.toString().substring(0, sb.toString().length()-1);
            }
            is.close();
            json = sb.toString();
            Log.e("jsonString: ", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public static void updateCookie() {
        CookieStore cookieStore = ((DefaultHttpClient) httpClient).getCookieStore();
        List<Cookie> cookieList = cookieStore.getCookies();
        if (cookieList.size() ==0) {
            System.out.println("\tNone"); //$NON-NLS-1$
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                Log.e("cookie", cookies.get(i).toString()); //$NON-NLS-1$
                BasicClientCookie cookie = new BasicClientCookie(cookies.get(i).getName(), cookies.get(i).getValue());
                cookie.setDomain(cookies.get(i).getDomain());
                cookie.setPath(cookies.get(i).getPath());
                cookieStore.addCookie(cookie);
            }


        }
    }


}
