package ru.protasov_dev.letmecodeinterviewtask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Utils {
    public static void savePicture(Context context, String imageName, Bitmap bitmap)
            throws Exception {
        OutputStream outputStream = null;
        File folderToSave = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/LMC/");
        folderToSave.mkdir();
        File file = new File(folderToSave, imageName + ".jpg");
        outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(folderToSave);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
