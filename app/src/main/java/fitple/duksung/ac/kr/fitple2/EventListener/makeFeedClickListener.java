package fitple.duksung.ac.kr.fitple2.EventListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by YEJIN on 2016-10-26.
 */

public class makeFeedClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());
        alt_bld.setCancelable(false)
                .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       Log.v("dialog","camera");
                    }
                }).setNegativeButton("앨범", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
           //     startActivityForResult(intent,PICK_FROM_ALBUM);

                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog

        // Icon for AlertDialog
        //alert.setIcon(R.drawable.icon);
        alert.show();

    }
}
