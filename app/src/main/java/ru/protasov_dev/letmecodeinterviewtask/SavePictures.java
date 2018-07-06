package ru.protasov_dev.letmecodeinterviewtask;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SavePictures {
    private View view;
    private Activity activity;
    private String imageName;
    private ImageView imageView;
    private File folderToSave;
    private Context context;

    public SavePictures(Context context,
                        View view,
                        Activity activity,
                        String imageName,
                        ImageView imageView) {
        this.context = context;
        this.view = view;
        this.activity = activity;
        this.imageName = imageName;
        this.imageView = imageView;
        save();
    }

    private void save() {
        OutputStream outputStream = null;
        folderToSave = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/LMC/");

        try {
            folderToSave.mkdir();
            File file = new File(folderToSave, imageName + ".jpg");
            outputStream = new FileOutputStream(file);
            try {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                outputStream.flush();
                outputStream.close();

                galleryAddPic();
//                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                            file.getAbsolutePath(), imageName + ".jpg", imageName);

                Snackbar.make(view, context.getString(R.string.image_saved), Snackbar.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(view, context.getString(R.string.file_not_available), Snackbar.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Snackbar.make(view, context.getString(R.string.no_write_access), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(folderToSave);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
