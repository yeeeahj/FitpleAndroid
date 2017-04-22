/*
package fitple.duksung.ac.kr.fitple2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;

*/
/**
 * Created by YEJIN on 2016-10-26.
 *//*


public class getImage {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM=1;
    private static final int CROP_FROM_CAMERA = 2;
    ImageButton picktureBtn;
    Uri mImageCaptureUri;
    byte[] imageBytes=null;
    public getImage()


    protected void getImageData(int requestCode, int resultCode, Intent data){


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
                    picktureBtn.setImageBitmap(photo);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    imageBytes = baos.toByteArray();

                }

                // 임시 파일 삭제
                //f = new File(mImageCaptureUri.getPath());
            */
/*    if(f.exists())
                {
                    f.delete();
                }
*//*

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
                intent.setDataAndType(mImageCaptureUri, "image*/
/*");

                intent.putExtra("outputX", 90);
                intent.putExtra("outputY", 90);
                intent.putExtra("aspectX", 2);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
               // startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
        }



    }
}
*/
