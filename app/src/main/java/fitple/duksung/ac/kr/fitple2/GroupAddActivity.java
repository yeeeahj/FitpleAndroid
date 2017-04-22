package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;


//import org.apache.http.entity.ContentType;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

//import static org.apache.http.entity.ContentType.*;

public class GroupAddActivity extends Activity {

    // String url = "http://203.252.219.238:8080/Fitple/Servlet/GroupAddServlet";
    ImageButton groupAdd_makepicture;
    ImageButton groupAdd_public;
    ImageButton groupAdd_namepublic;
    ImageButton groupAdd_private;
    Button groupAdd_permission;
    Button groupAdd_free;
    Button groupAdd_complete;

    EditText groupAdd_groupname;
    EditText groupAdd_groupinfo;
    int public_state=1;
    int permission_state=2;
    Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM=1;
    private static final int CROP_FROM_CAMERA = 2;
    byte[] imageBytes=null;
    File f;
    DataOutputStream dataStream = null;
    String pictureFileName = null;
    String CRLF = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****mgd*****";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        groupAdd_makepicture=(ImageButton)findViewById(R.id.groupAdd_imageButton);
        groupAdd_public= (ImageButton)findViewById(R.id.groupAdd_public);
        groupAdd_namepublic= (ImageButton)findViewById(R.id.groupAdd_namepublic);
        groupAdd_private= (ImageButton)findViewById(R.id.groupAdd_private);
        groupAdd_permission= (Button)findViewById(R.id.groupAdd_permission);
        groupAdd_free= (Button)findViewById(R.id.groupAdd_free);
        groupAdd_complete= (Button)findViewById(R.id.groupAdd_complete);

        groupAdd_groupname= (EditText)findViewById(R.id.groupAdd_groupname);
        groupAdd_groupinfo=(EditText)findViewById(R.id.groupAdd_groupinfo);



        groupAdd_public.setOnClickListener(publicClickListener);
        groupAdd_namepublic.setOnClickListener(publicClickListener);
        groupAdd_private.setOnClickListener(publicClickListener);

        groupAdd_permission.setOnClickListener(permissionClickListener);
        groupAdd_free.setOnClickListener(permissionClickListener);

        groupAdd_makepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);


            }
        });

        groupAdd_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(public_state==0){
                    // 선택하라고 에러
                }else if(permission_state==0){
                    // 선택하라고 에러
                }else{

                    String groupname = groupAdd_groupname.getText().toString();
                    String groupinfo = groupAdd_groupinfo.getText().toString();
                    Log.e("complete", groupname + groupinfo + public_state + permission_state + mImageCaptureUri);


                    TheTask task = new TheTask();
                    task.execute(groupname , groupinfo , public_state+"" , permission_state+"" ,String.valueOf(mImageCaptureUri));
                }
            }
        });



    }
    View.OnClickListener publicClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(v.getId() ==  R.id.groupAdd_public){
                groupAdd_public.setBackgroundResource(R.drawable.make_open);
                groupAdd_namepublic.setBackgroundResource(R.drawable.make_xhalfopen);
                groupAdd_private.setBackgroundResource(R.drawable.make_xsecret);
                public_state=1;
            }else if(v.getId() ==  R.id.groupAdd_namepublic){
                groupAdd_public.setBackgroundResource(R.drawable.make_xopen);
                groupAdd_namepublic.setBackgroundResource(R.drawable.make_halfopen);
                groupAdd_private.setBackgroundResource(R.drawable.make_xsecret);
                public_state=2;
            }else if(v.getId() ==  R.id.groupAdd_private){
                groupAdd_public.setBackgroundResource(R.drawable.make_xopen);
                groupAdd_namepublic.setBackgroundResource(R.drawable.make_xhalfopen);
                groupAdd_private.setBackgroundResource(R.drawable.make_secret);
                public_state=3;
            }else {}

        }
    };
    View.OnClickListener permissionClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(v.getId() ==  R.id.groupAdd_permission){
                groupAdd_permission.setBackgroundResource(R.drawable.make_on);
                groupAdd_free.setBackgroundResource(R.drawable.make_xoff);

                permission_state=1;
            }else if(v.getId() ==  R.id.groupAdd_free){
                groupAdd_permission.setBackgroundResource(R.drawable.make_xon);
                groupAdd_free.setBackgroundResource(R.drawable.make_off);

                permission_state=2;

            }else {}

        }
    };

    class TheTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);
            // update textview here
            Log.e("responseCode", responseCode);
            if(responseCode.equals("5")){
                Log.e("responseCode","5");
                //     Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                //     startActivity(intent);
            }else if(responseCode.equals("6")){
                Log.e("responseCode","6");
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
            HttpResponse httpRes = null;
            InputStream inputStream=null;
            HttpURLConnection conn=null;


            ArrayList<NameValuePair> nameValueArr = new ArrayList<NameValuePair>();
            Log.e("params", params[0] + params[1] + params[2] + params[3]);
            String url = "http://203.252.219.238:8080/Fitple/Servlet/GroupAddTestServlet";


            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(Uri.parse(params[4]), projection, null, null, null);
            startManagingCursor(cursor);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            File uploadFile = new File(cursor.getString(columnIndex));
/*

            F
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);


            MultipartEntityBuilder meb = MultipartEntityBuilder.create();
            meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            meb.addBinaryBody("group_image",uploadFile,ContentType.DEFAULT_BINARY,cursor.getString(columnIndex));
            meb.addTextBody("groupname", params[0], ContentType.TEXT_PLAIN);
            meb.addTextBody("groupinfo", params[1], ContentType.TEXT_PLAIN);//, ContentType.create("Multipart/related", "UTF-8"));
            meb.addTextBody("public_seq", params[2], ContentType.TEXT_PLAIN);//, ContentType.create("Multipart/related", "UTF-8"));
            meb.addTextBody("permission_seq", params[3], ContentType.TEXT_PLAIN);//, ContentType.create("Multipart/related", "UTF-8"));
            post.setEntity(meb.build());
            
            try {
                httpRes=client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }




*/



                try{
                    Log.e("params", "존재>?");
                    FileInputStream fileInputStream = new FileInputStream(uploadFile);
                    URL connectURL = new URL(url);
                     conn= (HttpURLConnection)connectURL.openConnection();

                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    Log.d("Cookie", "Cookie="+MyInfo.member_seq);
                    conn.setRequestProperty("Cookie",MyInfo.member_seq+"");
                    conn.setRequestProperty("User-Agent", "myGeodiary-V1");
                    conn.setRequestProperty("Connection","Keep-Alive");
                    conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);

                    conn.connect();


                    dataStream = new DataOutputStream(conn.getOutputStream());
                    Log.e("KOREAN", URLEncoder.encode(params[1],"EUC-KR"));
                    writeFormField("groupname", params[0]);
                    writeFormField("group_info",URLEncoder.encode(params[1],"EUC-KR"));
                    writeFormField("public_seq", params[2]);
                    writeFormField("permission_seq", params[3]);
                    writeFormField("member_seq", MyInfo.member_seq+"");
                    writeFileField("photo", pictureFileName, "image/jpg", fileInputStream);

                    // final closing boundary line
                    dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);

                    fileInputStream.close();
                    dataStream.flush();
                    dataStream.close();
                    dataStream = null;

                    responseCode = getResponse(conn);

/*
                    if (response.contains("uploaded successfully"))
                        return ReturnCode.http201;
                    else
                        // for now assume bad name/password
                        return ReturnCode.http401;*/
                }
                catch (MalformedURLException mue) {
                    // Log.e(Tag, "error: " + mue.getMessage(), mue);
                    System.out.println("GeoPictureUploader.uploadPicture: Malformed URL: " + mue.getMessage());
                  //  return ReturnCode.http400;
                }
                catch (IOException ioe) {
                    // Log.e(Tag, "error: " + ioe.getMessage(), ioe);
                    System.out.println("GeoPictureUploader.uploadPicture: IOE: " + ioe.getMessage());
                   // return ReturnCode.http500;
                }
                catch (Exception e) {
                    // Log.e(Tag, "error: " + ioe.getMessage(), ioe);
                    System.out.println("GeoPictureUploader.uploadPicture: unknown: " + e.getMessage());
                   // return ReturnCode.unknown;
                }finally {
                    cursor.close();
                    conn.disconnect();
                }


           /* FileBody fileBody = new FileBody(f); // here is line 221
            ByteArrayBody bab = new ByteArrayBody(imageBytes, "pic.png");

            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();

            multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

*//*            multipartEntity.addTextBody("groupname", params[0], ContentType.create("Multipart/related", "UTF-8"));
            multipartEntity.addTextBody("groupinfo",params[1],ContentType.create("Multipart/related","UTF-8"));
            multipartEntity.addTextBody("public_seq",params[2],ContentType.create("Multipart/related","UTF-8"));
            multipartEntity.addTextBody("permission_seq",params[3],ContentType.create("Multipart/related","UTF-8"));*//*

            multipartEntity.addPart("group_image", bab);




        JsonParser jParser = new JsonParser();
        JSONObject jObject = jParser.getJSONFromUrl(url, multipartEntity);


        try {

            responseCode = jObject.get("responseCode")+"";
            if(responseCode.equals("5")) {
                //나중에 전역변수에 저장
                   *//* JSONObject myinfo = jObject.getJSONObject("myInfo");
                    String member_seq = (String) myinfo.get("member_seq");
                    String nickname = (String) myinfo.get("nickname");
                    String email = (String) myinfo.get("email");
                    String gender = (String) myinfo.get("gender");
                    Log.e("eee",member_seq+nickname+email+gender);*//*
            }else{
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }*/
            Log.v("http성공?",httpRes+"");
        return responseCode;
    }



}
    private void writeFormField(String fieldName, String fieldValue)
    {
        try
        {
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + CRLF);
            dataStream.writeBytes(CRLF);
            dataStream.write(fieldValue.getBytes("EUC-KR"));
            dataStream.writeBytes(CRLF);
        }
        catch(Exception e)
        {
            System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            //Log.e(TAG, "GeoPictureUploader.writeFormField: got: " + e.getMessage());
        }
    }
    private void writeFileField(
            String fieldName,
            String fieldValue,
            String type,
            FileInputStream fis) {
        try {
            // opening boundary line
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\""
                    + fieldName
                    + "\";filename=\""
                    + fieldValue
                    + "\""
                    + CRLF);
            dataStream.writeBytes("Content-Type: " + type + CRLF);
            dataStream.writeBytes(CRLF);

            // create a buffer of maximum size
            int bytesAvailable = fis.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fis.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dataStream.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
            }

            // closing CRLF
            dataStream.writeBytes(CRLF);
        } catch (Exception e) {
            System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            //Log.e(TAG, "GeoPictureUploader.writeFormField: got: " + e.getMessage());
        }
    }
    private String getResponse(HttpURLConnection conn) {
        try {
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            byte[] data = new byte[1024];
            int len = dis.read(data, 0, 1024);

            dis.close();
            int responseCode = conn.getResponseCode();
            Log.v("사진올리기 응답", responseCode + "");
            if (len > 0)
                return new String(data, 0, len);
            else
                return "";
        } catch (Exception e) {
            System.out.println("GeoPictureUploader: biffed it getting HTTPResponse");
            //Log.e(TAG, "GeoPictureUploader: biffed it getting HTTPResponse");
            return "";

        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        Log.e("resultCode,requestCode", resultCode + ""+requestCode);
        if(resultCode != RESULT_OK)
        {
            Log.e("resultCode",resultCode+"");
            return;
        }

        switch(requestCode)
        {
            case CROP_FROM_CAMERA:
            {

                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    groupAdd_makepicture.setImageBitmap(photo);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    imageBytes = baos.toByteArray();

                }

                // 임시 파일 삭제
               f = new File(mImageCaptureUri.getPath());
            /*    if(f.exists())
                {
                    f.delete();
                }
*/
                break;
            }

            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 90);
                intent.putExtra("outputY", 90);
                intent.putExtra("aspectX", 2);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
        }



    }

}
