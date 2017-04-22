package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class JoinActivity extends Activity implements ResponseCode{

    private EditText sign_email;
    private EditText sign_passwd;
    private EditText sign_nickname;
    private TextView sign_nickcheck;
    private RadioGroup sign_gender;
    private RadioButton sign_woman;
    private RadioButton sign_man;
    private CheckBox sign_check1;
    private CheckBox sign_check2;
    private ImageButton sign_up;
    private ImageButton sign_nickBtn;

    private boolean nickState;
    private int genderState; //남 1 여2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_join);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);



        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText("회원가입");



        sign_email = (EditText) findViewById(R.id.sign_email);
        sign_passwd = (EditText) findViewById(R.id.sign_passwd);
        sign_nickname = (EditText) findViewById(R.id.sign_nickname);
        sign_nickcheck = (TextView) findViewById(R.id.sign_nickcheck);
        sign_gender = (RadioGroup) findViewById(R.id.sign_gender);
        sign_woman = (RadioButton) findViewById(R.id.sign_woman);
        sign_man = (RadioButton) findViewById(R.id.sign_man);

        sign_up = (ImageButton) findViewById(R.id.sign_up);
        sign_nickBtn = (ImageButton) findViewById(R.id.sign_nickBtn);

        View.OnClickListener NickCheckListener = null;
        View.OnClickListener RadioListener = null;
        View.OnClickListener CheckBoxListener = null;

        sign_nickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("click","닉네임 체크");
                String nickname = sign_nickname.getText().toString();
                TheTask task = new TheTask();
                task.execute("0",nickname);

            }
        });


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("click","눌려?");
                if (sign_email.getText() == null) {
                    Log.v("click","어디야1");
                    Toast.makeText(getApplicationContext(), "e-mail을 입력해 주세요", Toast.LENGTH_LONG);
                } else if (sign_passwd.getText() == null) {
                    Log.v("click","어디야2");
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_LONG);
                } else if (!nickState) {
                    Log.v("click","어디야3");
                    Toast.makeText(getApplicationContext(), "닉네임 중복확인 해주세요 ", Toast.LENGTH_LONG);
                } else {
                    Log.v("click","어디야24");
                    if (sign_woman.isChecked()) {
                        genderState = 2;
                    } else if (sign_man.isChecked()) {
                        genderState = 1;
                    }
                    TheTask task = new TheTask();
                    task.execute("1",sign_nickname.getText().toString(),sign_email.getText().toString(),sign_passwd.getText().toString(),genderState+"");

                }


            }
        });


/*        RadioListener  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.sign_man){
                    if(sign_man.isChecked()) {
                        sign_man.setButtonDrawable(R.drawable.button1_on);
                    }else {
                        sign_man.setButtonDrawable(R.drawable.button1_off);
                    }
                }else if(v.getId()==R.id.sign_woman){
                    if(sign_woman.isChecked()) {
                        sign_woman.setButtonDrawable(R.drawable.button1_on);
                    }else {
                        sign_woman.setButtonDrawable(R.drawable.button1_off);
                    }
                }

            }
        };
        CheckBoxListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.sign_check1){
                    if(sign_check1.isChecked()) {
                        sign_check1.setButtonDrawable(R.drawable.button2_on);
                    }else {
                        sign_check1.setButtonDrawable(R.drawable.button2_off);
                    }
                }else if(v.getId()==R.id.sign_check2){
                    if(sign_check2.isChecked()) {
                        sign_check2.setButtonDrawable(R.drawable.button2_on);
                    }else {
                        sign_check2.setButtonDrawable(R.drawable.button2_off);
                    }
                }
            }
        };*/


    }

    class TheTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);
            // update textview here
            Log.e("responseCode", responseCode);
            if(responseCode.equals(String.valueOf(ResponseCode.JOIN_NICKNAMECHECK_SUCCESS))){
                nickState=true;
            }else if(responseCode.equals(String.valueOf(ResponseCode.JOIN_NICKNAMECHECK_FAIL))){

                nickState=false;
                Toast.makeText(getApplicationContext(), "사용할 수 없는 닉네임 입니다.", Toast.LENGTH_LONG);
            }else if(responseCode.equals(String.valueOf(ResponseCode.JOIN_SUCCESS))){
                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG);
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);

            }else if(responseCode.equals(String.valueOf(ResponseCode.JOIN_FAIL))){
                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG);
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
                Log.e("paramssss", params[0] );
            String url=null;
            if(params[0].equals("0")) { //0이면 닉네임 체크
                nameValueArr.add(new BasicNameValuePair("nickname", params[1]));

                 url = "http://203.252.219.238:8080/Fitple/Servlet/NickCheckServlet";
            }else{ // 그외 회원가입
                nameValueArr.add(new BasicNameValuePair("email", params[1]));
                nameValueArr.add(new BasicNameValuePair("nickname", params[2]));
                nameValueArr.add(new BasicNameValuePair("password", params[3]));
                nameValueArr.add(new BasicNameValuePair("gender", params[4]));


                url = "http://203.252.219.238:8080/Fitple/Servlet/JoinServlet";
            }
                JsonParser jParser = new JsonParser();
                JSONObject jObject = jParser.getJSONFromUrl(url, nameValueArr);


            try {
                responseCode = jObject.get("responseCode")+"";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseCode;
        }




    }
}
