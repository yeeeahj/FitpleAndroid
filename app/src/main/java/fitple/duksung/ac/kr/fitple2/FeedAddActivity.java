package fitple.duksung.ac.kr.fitple2;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FeedAddActivity extends Activity implements RequestCode,ResponseCode {
    ImageButton feedAdd_image;
    EditText feedAdd_content;
    Button feedAdd_okay;
    byte[] imageBytes = null;
    Uri mImageCaptureUri;
    DataOutputStream dataStream = null;
    String pictureFileName = null;
    String CRLF = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****mgd*****";
    int group_seq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_feed_add);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);
        File f;


        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText("인증하기");


        Intent intent = getIntent();
        group_seq = intent.getExtras().getInt("group_seq");

        feedAdd_content = (EditText) findViewById(R.id.feedAdd_contentText);
        feedAdd_image = (ImageButton) findViewById(R.id.feedAdd_imageButton);
        feedAdd_okay = (Button) findViewById(R.id.feedAdd_okay);

        feedAdd_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());
                alt_bld.setCancelable(false)
                        .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CROP_FROM_CAMERA);
                            }
                        }).setNegativeButton("앨범", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_FROM_ALBUM);
                        //dialog.cancel();
                    }
                });
                AlertDialog alert = alt_bld.create();
                // Title for AlertDialog
                alert.setTitle("이미지 불러오기");

                alert.show();

            }
        });
        feedAdd_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedAdd_image == null) {

                } else if (feedAdd_content == null) {

                } else {
                    Log.v("통신", feedAdd_content.getText().toString() + mImageCaptureUri);
                    TheTask task = new TheTask();
                    task.execute("FeedAdd", group_seq+"",feedAdd_content.getText().toString(), mImageCaptureUri + "");


                }
            }
        });

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        Log.e("resultCode,requestCode", resultCode + "" + requestCode);
        if (resultCode != RESULT_OK) {
            Log.e("resultCode", resultCode + "");
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {

                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    feedAdd_image.setImageBitmap(photo);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    imageBytes = baos.toByteArray();

                }

                // 임시 파일 삭제
                //   f = new File(mImageCaptureUri.getPath());
            /*    if(f.exists())
                {
                    f.delete();
                }
*/
                break;
            }

            case PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA: {
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

    class TheTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String responseCode) {
            // TODO Auto-generated method stub
            super.onPostExecute(responseCode);
            // update textview here
            Log.e("responseCode", responseCode);
            if (responseCode.equals(String.valueOf(ResponseCode.FEED_ADD_SUCCESS))) {
                Log.e("responseCode", "15");
                Intent intent = new Intent(FeedAddActivity.this, HomeActivity.class);
                intent.putExtra("group_seq",group_seq);
                startActivity(intent);
                finish();
            } else if (responseCode.equals(String.valueOf(ResponseCode.FEED_ADD_FAIL))) {
                Log.e("responseCode", "16 안됨");
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
            String responseCode = null;
            HttpResponse httpRes = null;
            InputStream inputStream = null;
            HttpURLConnection conn=null;


            ArrayList<NameValuePair> nameValueArr = new ArrayList<NameValuePair>();
            Log.e("params", params[0] + params[1] + params[2]+params[3]);
            String url = "http://203.252.219.238:8080/Fitple/Servlet/FeedAddTestServlet";


            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(Uri.parse(params[3]), projection, null, null, null);
            startManagingCursor(cursor);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            File uploadFile = new File(cursor.getString(columnIndex));


            try {
                Log.e("params", "존재>?");
                FileInputStream fileInputStream = new FileInputStream(uploadFile);
                URL connectURL = new URL(url);
               conn = (HttpURLConnection) connectURL.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                Log.d("Cookie", "Cookie=" + MyInfo.member_seq);
                conn.setRequestProperty("Cookie", MyInfo.member_seq + "");
                conn.setRequestProperty("User-Agent", "myGeodiary-V1");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.connect();

                dataStream = new DataOutputStream(conn.getOutputStream());
                Log.e("KOREAN", URLEncoder.encode(params[2], "EUC-KR"));
                writeFormField("group_seq", params[1]);
                writeFormField("content", params[2]);
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
            } catch (MalformedURLException mue) {
                // Log.e(Tag, "error: " + mue.getMessage(), mue);
                System.out.println("GeoPictureUploader.uploadPicture: Malformed URL: " + mue.getMessage());
                //  return ReturnCode.http400;
            } catch (IOException ioe) {
                // Log.e(Tag, "error: " + ioe.getMessage(), ioe);
                System.out.println("GeoPictureUploader.uploadPicture: IOE: " + ioe.getMessage());
                // return ReturnCode.http500;
            } catch (Exception e) {
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
            Log.v("http성공?", httpRes + "");
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
            dataStream.writeBytes(fieldValue);
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
                    + fieldValue.getBytes("euc-kr")
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
}

