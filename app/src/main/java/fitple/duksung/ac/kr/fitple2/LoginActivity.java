package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button facebookLogin ;
    private Button login;
    private Button join;
    private Button help;
    private EditText edit_id;
    private EditText edit_pass;
    private CheckBox auto_Login;
    private Boolean loginChecked;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        edit_id = (EditText)findViewById(R.id.edit_id);
        edit_pass= (EditText)findViewById(R.id.edit_password);
        login=(Button)findViewById(R.id.btn_login);
        help =(Button)findViewById(R.id.help);
        join=(Button)findViewById(R.id.join);
        auto_Login=(CheckBox)findViewById(R.id.autoLogin);
        facebookLogin=(Button)findViewById(R.id.facebookLogin);

        login.setOnClickListener(this);
        //Jogin Button Event
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 정보가 서버에 잇으면 로그인 성공
                //정보가 서버에 없으면 페이스 북에서 데이터 받아와 토큰 해체
                Intent intent = new Intent(LoginActivity.this, FacebookTest.class);
                startActivity(intent);
            }
        });
        //AutoLogin CheckBox Event
        auto_Login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    loginChecked = true;
                }else{
                    loginChecked =false;
                    editor.clear();
                    editor.commit();
                }
            }
        });

        //AutoLogin
        prefs = getSharedPreferences("AutoLogin", MODE_PRIVATE);
        Log.d("autologin","잇니없니");
        if (prefs.getBoolean("autoLogin", true)) {
            edit_id.setText(prefs.getString("email", ""));
            edit_pass.setText(prefs.getString("pw", ""));
            auto_Login.setChecked(true);
            login.performClick();

        }





    }

    @Override
    public void onClick(View v) {
        String loginCode = "1";
        String id = edit_id.getText().toString();
        String pass = edit_pass.getText().toString();
        TheTask task = new TheTask();
        task.execute(loginCode, id, pass);

    }


    class TheTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);
            // update textview here
            Log.e("responseCode", responseCode);
            if(responseCode.equals("3")){
                Log.e("responseCode","3");
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }else if(responseCode.equals("4")){
                Log.e("responseCode","4");
                //얼러으로 안됨 알리기
            }


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
            nameValueArr.add(new BasicNameValuePair("loginCode", params[0]));
            nameValueArr.add(new BasicNameValuePair("email", params[1]));
            nameValueArr.add(new BasicNameValuePair("password", params[2]));
            String url = "http://203.252.219.238:8080/Fitple/Servlet/LoginServlet";
            JsonParser jParser = new JsonParser();
            JSONObject jObject = jParser.getJSONFromUrl(url, nameValueArr);


            try {

                responseCode = jObject.get("responseCode")+"";
                if(responseCode.equals("3")) {
                    //나중에 전역변수에 저장
                    JSONObject myinfo = jObject.getJSONObject("myInfo");
                    String member_seq = (String) myinfo.get("member_seq");
                    String nickname = (String) myinfo.get("nickname");
                    String email = (String) myinfo.get("email");
                    String gender = (String) myinfo.get("gender");
                    String passwd = (String) myinfo.get("passwd");
                    Log.e("eee",member_seq+nickname+email+gender);
                    MyInfo.member_seq = Integer.parseInt(member_seq);
                    if(loginChecked) {
                        prefs = getSharedPreferences("AutoLogin", MODE_PRIVATE);
                        editor = prefs.edit();
                        editor.putString("email", email);
                        editor.putString("pw", passwd);
                        editor.putBoolean("autoLogin", true);
                        editor.commit();
                        Log.d("autologin","prefs저장");
                    }

                }else{
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseCode;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}

