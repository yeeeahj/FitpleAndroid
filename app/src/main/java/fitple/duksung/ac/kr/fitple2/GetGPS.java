package fitple.duksung.ac.kr.fitple2;

/**
 * Created by oosl1 on 2016. 6. 23..
 */
import java.io.IOException;
import android.app.Activity;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

public class GetGPS extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getgps);
        String filename = Environment.getExternalStorageDirectory().getPath() + "/20160605_162816.jpg";
        try {
            ExifInterface exif = new ExifInterface(filename);
            showExif(exif);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
        }
    }

    private void showExif(ExifInterface exif) {

        String myAttribute = "[Exif information] \n\n";

       // myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);
       // myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE,exif);       // 위도
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif);  // north or south
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif);     // 경도
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif); // east or west
       // myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH,exif);
       // myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH,exif);
       // myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
       // myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
       // myAttribute += getTagString(ExifInterface.TAG_ORIENTATION,exif);
       // myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE,exif);

    }

    private String getTagString(String tag, ExifInterface exif) {
        return (tag + " : " + exif.getAttribute(tag) + "\n");
    }
}